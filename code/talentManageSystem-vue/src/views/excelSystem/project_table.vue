<template>
  <!-- 返回图标和标题 -->
  <h3>
    <SvgIcon
      name="back"
      class="icon-svg back-icon"
      style="width: 32px; height: 32px; margin-right: 8px"
      @click="backToMenu"
    />{{ tableName }}
  </h3>
  <div style="padding-bottom: 10px; padding-left: 5px">
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

    &nbsp;&nbsp;&nbsp;&nbsp;グレー：退場
    &nbsp;&nbsp;&nbsp;&nbsp; 青：価格変更
    &nbsp;&nbsp;&nbsp;&nbsp; <el-checkbox
      checked
      disabled
    >：手動変更</el-checkbox>
    <el-checkbox disabled>：自動計算</el-checkbox>

  </div>

  <table class="sub-table">
    <thead>
      <th class="row-head"></th>
      <th
        v-for="one in columnData"
        :key="one.monthId"
      >
        {{ one.monthNum + "月" }}
      </th>
    </thead>
    <tbody>
      <tr>
        <td class="row-head">稼働日</td>
        <td
          v-for="one in columnData"
          :key="one.monthId"
        >
          <el-input
            type="number"
            v-if="editable"
            v-model="one.monthDays"
          ></el-input>
          <span v-else>{{ one.monthDays + "天" }}</span>
        </td>
      </tr>
    </tbody>
  </table>
  <br>

  <!-- 表格 -->
  <el-table
    :data="rowData"
    border
    :cell-class-name="getCellClassName"
    ref="elTable"
    class="projectTable"
  >
    <!-- 客户名 -->
    <el-table-column
      prop="customerName"
      label="顧客名"
      width="180"
      fixed="left"
    ></el-table-column>
    <!-- プロジェクト名 -->
    <el-table-column
      prop="projectName"
      label="プロジェクト名"
      width="150"
      aria-rowspan="3"
      fixed="left"
    ></el-table-column>
    <!-- 技術者名 -->
    <el-table-column
      prop="name"
      label="技術者氏名"
      width="100"
      aria-rowspan="3"
      fixed="left"
    ></el-table-column>
    <!-- 形态 -->
    <el-table-column
      prop="status"
      label="形態"
      width="100"
      aria-rowspan="3"
      fixed="left"
    ></el-table-column>
    <!-- 契约单价（顧客先） -->
    <el-table-column
      prop="cPrice"
      label="契約単価（顧客先）"
      width="100"
      aria-rowspan="3"
      fixed="left"
    ></el-table-column>
    <!-- 精算幅（下限） -->
    <el-table-column
      prop="cLowerHours"
      label="精算幅(下限)"
      width="80"
      aria-rowspan="3"
      fixed="left"
    ></el-table-column>
    <!-- 精算幅（上限） -->
    <el-table-column
      prop="cHigherHours"
      label="精算幅(上限)"
      width="80"
      aria-rowspan="3"
      fixed="left"
    ></el-table-column>
    <!-- 减单金 -->
    <el-table-column
      prop="cReductPrice"
      label="減単金"
      width="80"
      aria-rowspan="3"
      fixed="left"
    ></el-table-column>
    <!-- 增单金 -->
    <el-table-column
      prop="cIncreasePrice"
      label="増単金"
      width="80"
      aria-rowspan="3"
      fixed="left"
    ></el-table-column>
    <!-- 一日标准工作时间 -->
    <el-table-column
      prop="standardHours"
      label="1日標準稼働時間"
      width="80"
      aria-rowspan="3"
      fixed="left"
    ></el-table-column>
    <!-- 动态生成的月度数据 -->
    <template
      v-for="(monthData, index) in monthDataList"
      :key="index"
    >
      <el-table-column
        :label="columnData[index].monthNum + '月'"
        aria-colspan="4"
      >
        <el-table-column
          label="売上合計（見込）"
          width="100"
        >
          <el-table-column
            :prop="`monthDataList[${index}].presumedTime`"
            label="想定時"
            width="85"
          />
        </el-table-column>
        <el-table-column :label="columnData[index].expectedSum">
          <el-table-column
            :prop="`monthDataList[${index}].expectedPrice`"
            label="見込単価"
            width="85"
          >
            <template v-slot="{ row }">
              <el-input
                v-model="row.monthDataList[index].expectedPrice"
                v-if="editable"
              ></el-input>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column
          label="売上合計（実績）"
          width="85"
        >
          <el-table-column
            :prop="`monthDataList[${index}].actualHours`"
            label="実時間"
            width="85"
          >
            <template v-slot="{ row }">
              <el-input
                v-model="row.monthDataList[index].actualHours"
                v-if="editable"
              ></el-input>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column :label="columnData[index].actualSum">
          <el-table-column
            :prop="`monthDataList[${index}].actualPrice`"
            label="実績単価"
            width="115"
          >
            <template v-slot="{ row }">
              <div v-if="editable">
                <el-checkbox v-model="row.monthDataList[index].actualPriceEdit" />&nbsp;
                <el-input
                  v-model="row.monthDataList[index].actualPrice"
                  style="width:65px;"
                ></el-input>
              </div>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table-column>
      <el-table-column
        label="稼働日"
        width="115"
      >
        <el-table-column
          label="合計人数"
          width="115"
        >
          <el-table-column
            :prop="`monthDataList[${index}].from`"
            :label="'乖離：' +  columnData[index].fromSum"
            width="150"
          >
            <template v-slot="{ row }">
              <div v-if="editable">
                <el-checkbox v-model="row.monthDataList[index].fromEdit" />&nbsp;
                <el-input
                  v-model="row.monthDataList[index].from"
                  style="width:65px;"
                ></el-input>
              </div>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table-column>
      <el-table-column :label="columnData[index].monthDays + '天'">
        <el-table-column :label="columnData[index].totalNumber + '人'">
          <el-table-column
            :prop="`monthDataList[${index}].subcontractPrice`"
            label="下請単価"
            width="90"
          >
          </el-table-column>
        </el-table-column>
      </el-table-column>
    </template>
  </el-table>
  <br />

</template>

<script>
import ExcelJS from "exceljs";

export default {
  data() {
    return {
      startTime: "",
      endTime: "",
      editable: false,
      rowData: [],
      columnData: [
        { totalNumber: 0, monthDays: 0, monthNum: 1 },
        { totalNumber: 0, monthDays: 0, monthNum: 2 },
        { totalNumber: 0, monthDays: 0, monthNum: 3 },
        { totalNumber: 0, monthDays: 0, monthNum: 4 },
        { totalNumber: 0, monthDays: 0, monthNum: 5 },
        { totalNumber: 0, monthDays: 0, monthNum: 6 },
      ],
      monthDataList: [
        {
          presumedTime: "",
          expectedPrice: "",
          actualHours: "",
          actualPrice: "",
          from: "",
          subcontractPrice: "",
        },
        {
          presumedTime: "",
          expectedPrice: "",
          actualHours: "",
          actualPrice: "",
          from: "",
          subcontractPrice: "",
        },
        {
          presumedTime: "",
          expectedPrice: "",
          actualHours: "",
          actualPrice: "",
          from: "",
          subcontractPrice: "",
        },
        {
          presumedTime: "",
          expectedPrice: "",
          actualHours: "",
          actualPrice: "",
          from: "",
          subcontractPrice: "",
        },
        {
          presumedTime: "",
          expectedPrice: "",
          actualHours: "",
          actualPrice: "",
          from: "",
          subcontractPrice: "",
        },
        {
          presumedTime: "",
          expectedPrice: "",
          actualHours: "",
          actualPrice: "",
          from: "",
          subcontractPrice: "",
        },
      ],
    };
  },
  computed: {
    tableName() {
      return "売上管理表（" + this.startTime + "~" + this.endTime + "）";
    },
  },
  methods: {
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
    getCellClassName(cel) {
      let that = this;
      if (cel.columnIndex < 10) return "";

      let monthIndex = Math.floor((cel.columnIndex - 10) / 6);
      let rowIndex = cel.rowIndex;

      if (cel.column.label == "想定時") {
        return that.rowData[rowIndex].monthClassArr[monthIndex].presumedTime;
      } else if (cel.column.label == "見込単価") {
        return that.rowData[rowIndex].monthClassArr[monthIndex].expectedPrice;
      } else if (cel.column.label == "実時間") {
        return that.rowData[rowIndex].monthClassArr[monthIndex].actualHours;
      } else if (cel.column.label == "実績単価") {
        return that.rowData[rowIndex].monthClassArr[monthIndex].actualPrice;
      } else if (cel.column.label == "乖離") {
        return that.rowData[rowIndex].monthClassArr[monthIndex].from;
      } else if (cel.column.label == "下請単価") {
        return that.rowData[rowIndex].monthClassArr[monthIndex]
          .subcontractPrice;
      }
    },
    // 初始化函数
    init(startTime, endTime) {
      this.startTime = startTime;
      this.endTime = endTime;
      this.getDataList();
      this.editable = false;
    },
    getDataList() {
      let that = this;
      that.$httpV2(
        "/table/projectTechnicianList",
        "GET",
        {
          dateStart: that.startTime,
          dateEnd: that.endTime,
        },
        false,
        function (resp) {
          that.rowData = resp.result.row ? resp.result.row : [];
          if (resp.result.column != null) {
            that.columnData = resp.result.column;
          }
          if (parseInt(that.startTime.slice(-2)) == 3) {
            // 上半期 3 4 5 6 7 8
            that.columnData.forEach((one) => {
              one.monthNum = one.monthNum + 2;
            });
          } else if (parseInt(that.startTime.slice(-2)) == 9) {
            // 下半期 9 10 11 12 1 2
            that.columnData.forEach((one) => {
              one.monthNum = ((one.monthNum + 7) % 12) + 1;
            });
          }

          // 标灰、空白的月，清除数据
          that.rowData.forEach((one) => {
            if (!that.isAuth(["price_search"])) {
              one.cPrice = "";
              one.hPrice = "";
              one.cReductPrice = "";
              one.cIncreasePrice = "";
            }
            one.monthDataList.forEach((oneMonth, index) => {
              if (!that.isAuth(["price_search"])) {
                oneMonth.expectedPrice = "";
                oneMonth.actualPrice = "";
                oneMonth.from = "";
                oneMonth.subcontractPrice = "";
              }
              // 标灰--退場
              if (oneMonth.colorFlag == 3) {
                one.monthClassArr[index].presumedTime = "grey-grid ";
                one.monthClassArr[index].expectedPrice = "grey-grid ";
                one.monthClassArr[index].actualHours = "grey-grid ";
                one.monthClassArr[index].actualPrice = "grey-grid ";
                one.monthClassArr[index].from = "grey-grid ";
                one.monthClassArr[index].subcontractPrice = "grey-grid ";
              } else if (oneMonth.colorFlag == 1) {
                // 蓝色--价格变动
                one.monthClassArr[index].presumedTime = "blue-grid ";
                one.monthClassArr[index].expectedPrice = "blue-grid ";
                one.monthClassArr[index].actualHours = "blue-grid ";
                one.monthClassArr[index].actualPrice = "blue-grid ";
                one.monthClassArr[index].from = "blue-grid ";
                one.monthClassArr[index].subcontractPrice = "blue-grid ";
              }
              // 如果是需要标灰或者空白的，删除数据
              if (
                oneMonth.colorFlag == 1 ||
                oneMonth.colorFlag == 2 ||
                oneMonth.colorFlag == 3
              ) {
                oneMonth.presumedTime = "";
                oneMonth.expectedPrice = "";
                oneMonth.actualHours = "";
                oneMonth.actualPrice = "";
                oneMonth.from = "";
                oneMonth.subcontractPrice = "";
              }

              // 如果公司是ucl，删除下请单价
              if (one.belongCompany == "UCL") {
                oneMonth.subcontractPrice = "";
              }

              for (let key in oneMonth) {
                // 如果传过来-1，显示空字符串
                if (oneMonth.hasOwnProperty(key) && oneMonth[key] === -1) {
                  oneMonth[key] = "";
                }

                // 标红
                if (
                  key == "presumedTime" &&
                  oneMonth.presumedTime !=
                    one.standardHours * that.columnData[index].monthDays
                ) {
                  one.monthClassArr[index].presumedTime += "font-red";
                } else if (
                  key == "expectedPrice" &&
                  oneMonth.expectedPrice != one.cPrice
                ) {
                  one.monthClassArr[index].expectedPrice += "font-red";
                } else if (key == "actualPrice") {
                  if (
                    oneMonth.actualHours < one.cLowerHours &&
                    oneMonth.actualPrice !=
                      oneMonth.expectedPrice -
                        one.cReductPrice *
                          (one.cLowerHours - oneMonth.actualHours)
                  ) {
                    one.monthClassArr[index].actualPrice += "font-red";
                  } else if (
                    oneMonth.actualHours > one.cHigherHours &&
                    oneMonth.actualPrice !=
                      oneMonth.expectedPrice +
                        one.cIncreasePrice *
                          (oneMonth.actualHours - one.cHigherHours)
                  ) {
                    one.monthClassArr[index].actualPrice += "font-red";
                  } else if (
                    oneMonth.actualHours >= one.cHigherHours &&
                    oneMonth.actualHours <= one.cLowerHours &&
                    oneMonth.actualPrice != oneMonth.expectedPrice
                  ) {
                    one.monthClassArr[index].actualPrice += "font-red";
                  }
                } else if (
                  key == "from" &&
                  oneMonth.from != oneMonth.actualPrice - oneMonth.expectedPrice
                ) {
                  one.monthClassArr[index].from += "font-red";
                } else if (
                  key == "subcontractPrice" &&
                  oneMonth.subcontractPrice != one.hPrice
                ) {
                  one.monthClassArr[index].subcontractPrice += "font-red";
                }
              }
            });
          });

          that.columnData.forEach((oneMonth, index) => {
            oneMonth.expectedSum = 0;
            oneMonth.actualSum = 0;
            oneMonth.fromSum = 0;
            that.rowData.forEach((oneRow) => {
              if (oneRow.monthDataList[index].expectedPrice != "") {
                oneMonth.expectedSum +=
                  oneRow.monthDataList[index].expectedPrice;
              }
              if (oneRow.monthDataList[index].actualPrice != "") {
                oneMonth.actualSum += oneRow.monthDataList[index].actualPrice;
              }
              if (oneRow.monthDataList[index].from != "") {
                oneMonth.fromSum += oneRow.monthDataList[index].from;
              }
            });
            oneMonth.expectedSum = parseFloat(oneMonth.expectedSum).toFixed(2);
            oneMonth.actualSum = parseFloat(oneMonth.actualSum).toFixed(2);
            oneMonth.fromSum = parseFloat(oneMonth.fromSum).toFixed(2);
          });
        }
      );
    },

    // 返回
    backToMenu() {
      this.$emit("backToMenu");
    },
    editHandle() {
      this.editable = true;
    },
    saveHandle() {
      this.editable = false;
      let that = this;

      let row = [];
      let col = [];
      let monthDataList = {};

      this.rowData.forEach((one) => {
        let projectTechnicianId = one.projectTechnicianId;
        let monthDataList = [];
        one.monthDataList.forEach((oneMonth) => {
          var tmp = {
            monthId: oneMonth.monthId,
            presumedTime:
              oneMonth.presumedTime == "" ? 0 : oneMonth.presumedTime,
            expectedPrice:
              oneMonth.expectedPrice == "" ? 0 : oneMonth.expectedPrice,
            actualHours: oneMonth.actualHours == "" ? 0 : oneMonth.actualHours,
            actualPrice: oneMonth.actualPrice == "" ? 0 : oneMonth.actualPrice,
            from: oneMonth.from == "" ? 0 : oneMonth.from,
            subcontractPrice:
              oneMonth.subcontractPrice == "" ? 0 : oneMonth.subcontractPrice,
            actualPriceEdit: oneMonth.actualPriceEdit,
            fromEdit: oneMonth.fromEdit,
          };
          monthDataList.push(tmp);
        });
        row.unshift({
          projectTechnicianId: one.projectTechnicianId,
          monthDataList: monthDataList,
        });
      });

      let monthYears = [];
      let startYear = parseInt(this.startTime.slice(0, 4));
      if (parseInt(this.startTime.slice(-2)) == 3) {
        monthYears.push(startYear + "-03");
        monthYears.push(startYear + "-04");
        monthYears.push(startYear + "-05");
        monthYears.push(startYear + "-06");
        monthYears.push(startYear + "-07");
        monthYears.push(startYear + "-08");
      } else if (parseInt(this.startTime.slice(-2)) == 9) {
        monthYears.push(startYear + "-09");
        monthYears.push(startYear + "-10");
        monthYears.push(startYear + "-11");
        monthYears.push(startYear + "-12");
        monthYears.push(startYear + 1 + "-01");
        monthYears.push(startYear + 1 + "-02");
      }
      this.columnData.forEach((one, index) => {
        col.unshift({
          totalNumber: parseInt(one.totalNumber),
          monthDays: parseInt(one.monthDays),
          yearMonth: monthYears[index],
        });
      });
      col.reverse();

      this.$httpV2(
        "/table/projectTechnicianList",
        "PUT",
        {
          row: row,
          column: col,
        },
        false,
        function (resp) {}
      );
      this.getDataList();
    },
    exportHandle() {
      let that = this;
      const workbook = new ExcelJS.Workbook();
      const worksheet = workbook.addWorksheet(that.tableName);
      const header1 = [
        "顧客名",
        "プロジェクト名",
        "技術者氏名",
        "形態",
        "契約単価\n（顧客先）",
        "精算幅\n（下限）",
        "精算幅\n（上限）",
        "減単金",
        "増単金",
        "1日標準稼働時間",
      ];
      const header2 = [];
      const header3 = [];
      for (var i = 0; i < 10; i++) {
        header2.push("");
        header3.push("");
      }
      that.columnData.forEach((one) => {
        header1.push(one.monthNum + "月");
        header1.push("");
        header1.push("");
        header1.push("");
        header1.push("稼働日");
        header1.push(one.monthDays);

        header2.push("売上合計（見込）");
        header2.push(one.expectedSum);
        header2.push("売上合計（実績）");
        header2.push(one.actualSum);
        header2.push("合計人数");
        header2.push(one.totalNumber + "人");

        header3.push("想定時");
        header3.push("見込単価");
        header3.push("実時間");
        header3.push("実績単価");
        header3.push("乖離:" + one.fromSum);
        header3.push("下請単価");
      });
      worksheet.addRow(header1);
      worksheet.addRow(header2);
      worksheet.addRow(header3);

      // 合并前十个
      worksheet.mergeCells("A1:A3");
      worksheet.mergeCells("B1:B3");
      worksheet.mergeCells("C1:C3");
      worksheet.mergeCells("D1:D3");
      worksheet.mergeCells("E1:E3");
      worksheet.mergeCells("F1:F3");
      worksheet.mergeCells("G1:G3");
      worksheet.mergeCells("H1:H3");
      worksheet.mergeCells("I1:I3");
      worksheet.mergeCells("J1:J3");

      // 第一个月
      worksheet.mergeCells("K1:N1");
      // 第二个月
      worksheet.mergeCells("Q1:T1");
      // 第三个月
      worksheet.mergeCells("W1:Z1");
      // 第四个月
      worksheet.mergeCells("AC1:AF1");
      // 第五个月
      worksheet.mergeCells("AI1:AL1");
      // 第六个月
      worksheet.mergeCells("AO1:AR1");

      // 添加数据
      var rowCount = 0;
      that.rowData.forEach((one, index) => {
        var tmp = [
          one.customerName,
          one.projectName,
          one.name,
          one.status,
          one.cPrice,
          one.cLowerHours,
          one.cHigherHours,
          one.cReductPrice,
          one.cIncreasePrice,
          one.standardHours,
        ];
        one.monthDataList.forEach((oneMonth) => {
          tmp.push(oneMonth.presumedTime);
          tmp.push(oneMonth.expectedPrice);
          tmp.push(oneMonth.actualHours);
          tmp.push(oneMonth.actualPrice);
          tmp.push(oneMonth.from);
          tmp.push(oneMonth.subcontractPrice);
        });
        worksheet.addRow(tmp);
        rowCount++;
      });

      const classList = that.tableCellInfo();

      worksheet.getColumn(1).width = 25;
      worksheet.getColumn(2).width = 20;
      worksheet.getColumn(3).width = 11;
      worksheet.getColumn(5).width = 11;
      worksheet.getColumn(10).width = 18;
      for (var col = 0; col < 46; col++) {
        for (var row = 0; row < 3 + rowCount; row++) {
          const cell = worksheet.getCell(row + 1, col + 1);
          if (row < 3) {
            cell.fill = {
              type: "pattern",
              pattern: "solid",
              fgColor: { argb: "ebf1de" }, // 灰色
            };
          }
          cell.font = {
            name: "Yu Gothic", // 字体名称
            size: 11, // 字号
          };
          cell.alignment = {
            horizontal: "center", // 水平居中
            vertical: "middle", // 垂直居中
            wrapText: true, // 启用自动换行
          };
        }
      }

      classList.forEach((cell) => {
        if (cell.cellClasses.includes("grey-grid")) {
          worksheet.getCell(cell.rowIndex + 4, cell.columnIndex + 1).fill = {
            type: "pattern",
            pattern: "solid",
            fgColor: { argb: "d9d9d9" }, // 灰色
          };
        }
        if (cell.cellClasses.includes("blue-grid")) {
          worksheet.getCell(cell.rowIndex + 4, cell.columnIndex + 1).fill = {
            type: "pattern",
            pattern: "solid",
            fgColor: { argb: "daf0fa" },
          };
        }
        if (cell.cellClasses.includes("font-red")) {
          worksheet.getCell(cell.rowIndex + 4, cell.columnIndex + 1).font = {
            color: { argb: "ff1f1f" },
            name: "Yu Gothic", // 字体名称
          };
        }
      });

      workbook.xlsx.writeBuffer().then((buffer) => {
        const blob = new Blob([buffer], {
          type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        });
        const url = URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = that.tableName;
        a.click();
        URL.revokeObjectURL(url);
      });
    },
  },
};
</script>

<style>
/* 标题和图标 */
el-table {
  border: 1px solid black;
}

.projectTable .el-table__body-wrapper tr td:nth-child(11),
.projectTable .el-table__body-wrapper tr td:nth-child(17),
.projectTable .el-table__body-wrapper tr td:nth-child(23),
.projectTable .el-table__body-wrapper tr td:nth-child(29),
.projectTable .el-table__body-wrapper tr td:nth-child(35),
.projectTable .el-table__body-wrapper tr td:nth-child(41) {
  border-left: 1px solid black; /* 设置指定列的左边框 */
}

h3 {
  display: flex;
  align-items: center; /* 垂直对齐 */
}

/* 行表头 */
.row-head {
  width: 100px;
  font-weight: bold;
}

/* 统计表格边框 */
.sub-table {
  border-collapse: collapse;
  width: 580px;
}

/* 统计表格长宽 */
.sub-table th,
.sub-table td {
  border: 1px solid black;
  text-align: center;
  width: 80px;
  height: 35px;
}

.font-red {
  color: red;
}

.grey-grid {
  background-color: #d9d9d9;
}

.blue-grid {
  background-color: #daf0fa;
}
</style>