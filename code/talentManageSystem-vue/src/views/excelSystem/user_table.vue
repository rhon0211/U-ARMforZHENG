<template>
  <!-- 标题和返回按钮 -->
  <h3>
    <SvgIcon
      name="back"
      class="icon-svg"
      style="width: 32px; height: 32px; margin-right: 8px"
      @click="backToMenu"
    />{{ tableName }}
  </h3>
  <div style="padding-bottom: 10px; padding-left: 5px">
    <!-- 编辑按钮 -->
    <el-button
      type="primary"
      @click="editHandle"
      v-if="isAuth(['price_search'])"
      :disabled="editable"
    >修正</el-button>

    <!-- 保存按钮 -->
    <el-button
      type="success"
      @click="saveHandle"
      v-if="isAuth(['price_search'])"
      :disabled="!editable"
    >保存</el-button>

    <!-- 导出按钮 -->
    <el-button
      type="info"
      @click="exportHandle"
      :disabled="editable"
    >出力</el-button>

    &nbsp;&nbsp;&nbsp;&nbsp;
    ○：BP社員&nbsp;&nbsp;
    ●：UCL社員&nbsp;&nbsp;
    グレー：技術者退場
  </div>
  <br />

  <!-- wo -->

  <!-- 表格 -->
  <el-table
    :data="dataList"
    :cell-class-name="getCellClassName"
    style="border: none"
    ref="elTable"
  >
    <!-- 基本信息 -->
    <el-table-column
      prop="name"
      label="技術者氏名"
      width="100"
    ></el-table-column>
    <el-table-column
      prop="customerName"
      label="顧客名"
      width="220"
    ></el-table-column>
    <el-table-column
      prop="projectName"
      label="プロジェクト名"
      width="220"
    ></el-table-column>
    <el-table-column
      prop="principal"
      label="案件責任者（顧客先）"
      width="140"
    ></el-table-column>
    <el-table-column
      prop="principalCompany"
      width="140"
      label="案件責任者（所属）"
    ></el-table-column>
    <el-table-column
      prop="belongCompany"
      width="140"
      label="技術者所属（会社名）"
    ></el-table-column>
    <!-- 月份信息 -->
    <el-table-column
      v-for="(month, index) in months"
      :key="index"
      :label="month"
      width="60"
    >
      <template v-slot="{ row }">
        <span>{{ row.fillArr[index] }}</span>
      </template>
    </el-table-column>

    <!-- 備考 -->
    <el-table-column
      label="備考欄"
      width="300"
      prop="remark"
    >
      <template v-slot="{ row }">
        <el-input
          v-model="row.remark"
          v-if="editable"
        ></el-input>
      </template>
    </el-table-column>
  </el-table>
  <br /><br />

  <!-- 统计表格 -->
  <table class="sub-table">
    <thead>
      <th class="row-head"></th>
      <th
        v-for="(one, index) in months"
        :key="index"
      >{{ one }}</th>
    </thead>
    <tbody>
      <tr>
        <td class="row-head">UCL社員数</td>
        <td
          v-for="one in statisList"
          :key="one.statsId"
        >
          <el-input
            v-model="one.uclMember"
            v-if="editable"
          ></el-input>
          <span v-else>{{ one.uclMember }}人</span>
        </td>
      </tr>
      <tr>
        <td class="row-head">BP社員数</td>
        <td
          v-for="one in statisList"
          :key="one.statsId"
        >
          <el-input
            v-model="one.bpMember"
            v-if="editable"
          ></el-input>
          <span v-else>{{ one.bpMember }}人</span>
        </td>
      </tr>
      <tr>
        <td class="row-head">合計人数</td>
        <td
          v-for="one in statisList"
          :key="one.statsId"
        >
          <el-input
            v-model="one.totalNumber"
            v-if="editable"
          ></el-input>
          <span v-else>{{ one.totalNumber }}人</span>
        </td>
      </tr>
      <tr>
        <td class="row-head">BP・UCL社員比率</td>
        <td
          v-for="one in statisList"
          :key="one.statsId"
        >
          <el-input
            v-model="one.bpUclRate"
            v-if="editable"
          ></el-input>
          <span v-else>{{ Math.round(one.bpUclRate * 100) + "%" }}</span>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script>
import ExcelJS from "exceljs";
import { saveAs } from "file-saver";

export default {
  data() {
    return {
      months: [
        "3月",
        "4月",
        "5月",
        "6月",
        "7月",
        "8月",
        "9月",
        "10月",
        "11月",
        "12月",
        "1月",
        "2月",
      ],
      editable: false, // 0--可以编辑  1--不能编辑
      dataList: [],
      statisList: [],
      statisListFormat: [],
      startTime: "",
      endTime: "",
      developers: [
        { id: 1, name: "John Doe", age: 30, skill: "JavaScript" },
        { id: 2, name: "Jane Smith", age: 25, skill: "Vue.js" },
        // Thêm các lập trình viên khác vào đây
      ],
    };
  },
  computed: {
    tableName() {
      return "稼働状况（" + this.startTime + "~" + this.endTime + "）";
    },
  },
  methods: {
    getCellClassName(cel) {
      if (cel.columnIndex >= 6 && cel.columnIndex <= 17) {
        return this.dataList[cel.rowIndex].classNameArr[cel.columnIndex - 6];
      } else {
        return "";
      }
    },
    init(startTime, endTime) {
      this.startTime = startTime;
      this.endTime = endTime;
      this.getDataList();
      this.getStatisList();
      this.editable = false;
    },
    convertToPercentage(num) {
      return num * 100 + "%";
    },
    // 返回
    backToMenu() {
      this.$emit("backToMenu");
    },
    getDataList() {
      let that = this;
      this.$httpV2(
        "/table/technicianList",
        "GET",
        {
          dateStart: this.startTime,
          dateEnd: this.endTime,
        },
        true,
        function (resp) {
          that.dataList = resp.result;
          that.dataList.forEach((one) => {
            // 筛选三个月份
            var begin = parseInt(one.cBeginMonth.slice(-2));
            var end = parseInt(one.cEndMonth.slice(-2));
            var stop = "";

            // 如果有退場月
            if (one.stopMonth != null && one.stopMonth != "") {
              stop = parseInt(one.stopMonth.slice(-2));
            }

            // 如果是来年的1 2月 转换成13 14月
            if (begin == 1 || begin == 2) begin = begin + 12;
            if (end == 1 || end == 2) end = end + 12;
            if (stop == 1 || stop == 2) stop = stop + 12;

            // one.fillArr是一行的 3月~来年2月 标 ○ ● （空白）
            one.fillArr = new Array(12).fill("");
            // one.classNameArr是一行的 3月~来年2月 要不要标灰，标灰为1
            one.classNameArr = new Array(12).fill("");

            // 如果stop为空
            if (stop == "" || stop == null) {
              // 如果是拿end为退場标准，end当月也在场
              for (var i = begin - 3; i <= end - 3; i++) {
                if (one.belongCompany == "UCL" || one.belongCompany == "ucl") {
                  one.fillArr[i] = "●";
                } else {
                  one.fillArr[i] = "○";
                }
              }
            } else {
              // 如果stop不为空
              for (var i = begin - 3; i < stop - 3; i++) {
                if (one.belongCompany == "UCL") {
                  one.fillArr[i] = "●";
                } else {
                  one.fillArr[i] = "○";
                }
              }
              for (var i = stop - 3; i < 12; i++) {
                one.classNameArr[i] = "grey-grid";
              }
            }
          });
        }
      );
    },
    getStatisList() {
      let that = this;
      that.$httpV2(
        "/table/technicianListStats",
        "GET",
        {
          dateStart: this.startTime,
          dateEnd: this.endTime,
        },
        true,
        function (resp) {
          that.statisList = resp.result;
          that.statisList.forEach((one) => {
            if (one.bpUclRate < 0) {
              one.bpUclRate = "";
            }
          });
        }
      );
    },
    editHandle() {
      this.editable = true;
    },
    saveHandle() {
      let that = this;
      this.editable = false;

      // 保存備考
      let techInfoList = [];
      this.dataList.forEach((one) => {
        techInfoList.push({
          technicianId: one.technicianId,
          remark: one.remark,
        });
      });
      this.$httpV2(
        "/table/technicianList",
        "PUT",
        techInfoList,
        false,
        function (resp) {}
      );

      // 保存数据统计
      this.$httpV2(
        "/table/technicianListStats",
        "PUT",
        that.statisList,
        true,
        function (resp) {}
      );
      this.getDataList();
      this.getStatisList();
    },
    tableCellInfo() {
      const table = this.$refs.elTable;
      const rows = table.$el.querySelectorAll(
        ".el-table__body-wrapper tbody tr"
      );
      let cellInfo = [];
      rows.forEach((row, rowIndex) => {
        const cells = row.querySelectorAll("td");
        cells.forEach((cell, columnIndex) => {
          const cellClasses = Array.from(cell.classList);
          cellInfo.push({
            rowIndex,
            columnIndex,
            cellClasses,
          });
        });
      });
      return cellInfo;
    },
    exportHandle() {
      let that = this;

      const workbook = new ExcelJS.Workbook();
      const worksheet = workbook.addWorksheet(that.tableName);

      // 表头
      const header = [
        "技術者氏名",
        "顧客名",
        "プロジェクト名",
        "案件責任者\n（顧客先）",
        "案件責任者\n（所属）",
        "技術者所属\n（会社名）",
      ];
      that.months.forEach((one) => {
        header.push(one);
      });
      header.push("備考欄");
      worksheet.addRow(header);

      that.dataList.forEach((one, index) => {
        var tmp = [
          one.name,
          one.customerName,
          one.projectName,
          one.principal,
          one.principalCompany,
          one.belongCompany,
        ];
        one.fillArr.forEach((oneGrid) => {
          tmp.push(oneGrid);
        });
        tmp.push(one.remark);
        worksheet.addRow(tmp);
      });

      // 设置宽度
      worksheet.getColumn(1).width = 15;
      worksheet.getColumn(2).width = 25;
      worksheet.getColumn(3).width = 20;
      worksheet.getColumn(4).width = 20;
      worksheet.getColumn(5).width = 20;
      worksheet.getColumn(6).width = 20;
      for (var i = 7; i <= 18; i++) {
        worksheet.getColumn(i).width = 6;
      }
      worksheet.getColumn(19).width = 25;

      // 设置高度
      worksheet.getRow(1).height = 70;
      for (var row = 2; row <= that.dataList.length + 1; row++) {
        worksheet.getRow(row).height = 30;
      }

      // 设置表头背景色
      for (var col = 1; col <= 19; col++) {
        worksheet.getCell(1, col).fill = {
          type: "pattern",
          pattern: "solid",
          fgColor: { argb: "f2dcdb" },
        };
      }
      const classList = that.tableCellInfo();
      // 设置字体、边框
      for (var row = 1; row <= that.dataList.length + 1; row++) {
        for (var col = 1; col <= 19; col++) {
          const cell = worksheet.getCell(row, col);
          cell.font = {
            name: "Yu Gothic", // 字体名称
            size: 11, // 字号
          };
          cell.alignment = {
            horizontal: "center", // 水平居中
            vertical: "middle", // 垂直居中
            wrapText: true, // 启用自动换行
          };
          cell.border = {
            top: { style: "thin", color: { argb: "FF000000" } }, // 上边框
            left: { style: "thin", color: { argb: "FF000000" } }, // 左边框
            bottom: { style: "thin", color: { argb: "FF000000" } }, // 下边框
            right: { style: "thin", color: { argb: "FF000000" } }, // 右边框
          };
        }
      }

      classList.forEach((cell) => {
        if (cell.cellClasses.includes("grey-grid")) {
          worksheet.getCell(cell.rowIndex + 2, cell.columnIndex + 1).fill = {
            type: "pattern",
            pattern: "solid",
            fgColor: { argb: "d9d9d9" }, // 灰色
          };
        }
      });

      // 统计表格
      // 行高和列表头颜色
      for (
        var row = that.dataList.length + 3;
        row <= that.dataList.length + 6;
        row++
      ) {
        worksheet.getRow(row).height = 30;
        worksheet.getCell(row, 6).fill = {
          type: "pattern",
          pattern: "solid",
          fgColor: { argb: "e4dfec" },
        };
      }
      // 列表头文字
      worksheet.getCell(that.dataList.length + 3, 6).value = "UCL社員数";
      worksheet.getCell(that.dataList.length + 4, 6).value = "BP社員数";
      worksheet.getCell(that.dataList.length + 5, 6).value = "合計人数";
      worksheet.getCell(that.dataList.length + 6, 6).value = "BP・UCL社員比率";

      // 数据
      for (var col = 7; col <= 18; col++) {
        worksheet.getCell(that.dataList.length + 3, col).value =
          that.statisList[col - 7].uclMember + "人";
        worksheet.getCell(that.dataList.length + 4, col).value =
          that.statisList[col - 7].bpMember + "人";
        worksheet.getCell(that.dataList.length + 5, col).value =
          that.statisList[col - 7].totalNumber + "人";
        if (that.statisList[col - 7].bpUclRate < 0) {
          that.statisList[col - 7].bpUclRate = 0;
        }
        worksheet.getCell(that.dataList.length + 6, col).value =
          that.statisList[col - 7].bpUclRate * 100 + "%";
      }

      // 样式
      for (
        var row = that.dataList.length + 3;
        row <= that.dataList.length + 6;
        row++
      ) {
        for (var col = 6; col <= 18; col++) {
          const cell = worksheet.getCell(row, col);
          cell.font = {
            name: "Yu Gothic", // 字体名称
            size: 11, // 字号
          };
          cell.alignment = {
            horizontal: "center", // 水平居中
            vertical: "middle", // 垂直居中
            wrapText: true, // 启用自动换行
          };
          cell.border = {
            top: { style: "thin", color: { argb: "FF000000" } }, // 上边框
            left: { style: "thin", color: { argb: "FF000000" } }, // 左边框
            bottom: { style: "thin", color: { argb: "FF000000" } }, // 下边框
            right: { style: "thin", color: { argb: "FF000000" } }, // 右边框
          };
        }
      }
      // 导出
      workbook.xlsx
        .writeBuffer()
        .then((buffer) => {
          const blob = new Blob([buffer], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
          });
          saveAs(blob, that.tableName);
        })
        .catch((error) => {
          console.error(
            "稼働状况画面でエクセルファイル出力の際にエラー発生しました。:",
            error
          );
        });
    },
  },
};
</script>

 <style scoped>
/* 灰色格子 */
.grey-grid {
  background-color: #d9d9d9;
}

/* 标题和图标 */
h3 {
  display: flex;
  align-items: center; /* 垂直对齐 */
}

/* 统计表格边框 */
.sub-table {
  border-collapse: collapse;
  width: 1120px;
}

/* 统计表格长宽 */
.sub-table th,
td {
  border: 1px solid black;
  text-align: center;
  width: 80px;
  height: 35px;
}

/* 行表头 */
.row-head {
  width: 160px;
  font-weight: bold;
}
</style>