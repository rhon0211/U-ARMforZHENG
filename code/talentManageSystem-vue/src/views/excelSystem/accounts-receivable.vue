<template>
  <!-- 操作面板 -->
  <el-form :model="dataForm" ref="dataForm">
    <el-form-item label="売買種別" prop="tradeType">
      <el-radio-group v-model="dataForm.tradeType" size="default" @change="handleTradeTypeChange()">
        <el-radio-button label="売掛"></el-radio-button>
        <el-radio-button label="買掛"></el-radio-button>
      </el-radio-group>
    </el-form-item>

    <el-row>
      <el-col :span="7">
        <el-form-item label="入出金予定日" prop="expectedDateOfPayment">
          <el-date-picker v-model="dataForm.expectedDateOfPayment" type="daterange" format="YYYY-MM-DD"
            value-format="YYYY-MM-DD" clearable style="width: 100%" range-separator="～" start-placeholder="開始日"
            end-placeholder="終了日" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="7">
        <el-form-item label="締　　　　日" prop="closingDate">
          <el-date-picker v-model="dataForm.closingDate" type="daterange" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
            clearable style="width: 100%" range-separator="～" start-placeholder="開始日" end-placeholder="終了日" />
        </el-form-item>
      </el-col>
      <el-col :span="1">
        <el-form-item>
          <el-button type="success" @click="searchHandle">検索</el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
  <div class="table-container">
    <!-- 売掛数据展示与操作 -->
    <el-table v-if="showTableAccounts" ref="dataList" :data="accountList" border v-loading="dataListLoading"
      style="width: 100%;" :cell-style="{ padding: '3px 0' }" :summary-method="getAccountSummaries" show-summary
      :row-key="getRowKeys" :max-height="500" :expand-row-keys="expandRowKeys" @expand-change="handleExpandChange">
      <!-- 折叠数据 -->
      <el-table-column type="expand">
        <template #default="accounts_scope">
          <el-table :data="accounts_scope.row.uniqueList" border ref="departmentTable" :row-key="getRowDepartmentKeys"
            style="width: 90%; margin-left: 40px;" :max-height="250">
            <el-table-column type="index" header-align="center" align="center" width="50" label="No.">
              <template #default="scope">
                <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="部署略称" prop="departAbbreviation" header-align="center" align="left"
              width="150"></el-table-column>
            <el-table-column label="案件略称" prop="projectAbbreviation" header-align="center" align="left"
              width="150"></el-table-column>
            <el-table-column label="締日" prop="closingDate" header-align="center" align="right"
              width="130"></el-table-column>
            <el-table-column label="明細合計" prop="totalSalesAmount" header-align="center" align="right" width="150">
              <template #default="{ row }">
                {{ row.totalSalesAmount ? Number(row.totalSalesAmount).toLocaleString() : "0" }}
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
      <el-table-column label="企業名" prop="companyAbbreviation" header-align="center" align="left" width="300">
      </el-table-column>
      <el-table-column label="締日" prop="closingDate" header-align="center" align="right" width="130">
      </el-table-column>
      <el-table-column label="明細合計" prop="lineTotal" header-align="center" align="right" width="150">
      </el-table-column>
      <el-table-column label="請求額(税込)" prop="amountCharged" header-align="center" align="right">
      </el-table-column>

      <!-- 操作数据 -->
      <el-table-column label="入出金額" prop="depositAmount" header-align="center" align="right">
        <template #default="{ row }">
          <el-input v-model="row.depositAmount" size="small" type="text" input-style="text-align: right;"
            placeholder="金额を入力" @input="filterInput(row, 'depositAmount')" @focus="removeCommas(row, 'depositAmount')"
            @blur="formatWithCommas(row, 'depositAmount'); calculateAmounts(accountList);" style="ime-mode: disabled;"
            inputmode="numeric" />
        </template>
      </el-table-column>
      <el-table-column label="手数料" prop="commission" header-align="center" align="right">
      </el-table-column>
      <el-table-column label="入出金予定日" prop="expectedDateOfPayment" header-align="center" align="right">
      </el-table-column>
      <!-- 操作数据 -->
      <el-table-column label="入出金日" prop="depositDate" header-align="center" align="right" width="130">
        <template #default="{ row }">
          <el-date-picker v-model="row.depositDate" type="date" placeholder="日付を選択" format="YYYY-MM-DD"
            value-format="YYYY-MM-DD" size="small" style="width: 100%" />
        </template>
      </el-table-column>
    </el-table>
    <!-- 買掛数据展示与操作 -->
    <el-table v-if="showTableReceivable" ref="dataList" :data="receivableList" border v-loading="dataListLoading"
      style="width: 100%;" :cell-style="{ padding: '3px 0' }" :summary-method="getSummaries" show-summary
      :row-key="getRowKeys" :max-height="500" :expand-row-keys="expandRowKeys" @expand-change="handleExpandChange">
      <!-- 折叠数据 -->
      <el-table-column type="expand" :max-height="300">
        <template #default="receivable_scope">
          <el-table :data="receivable_scope.row.uniqueList" border ref="departmentTable" :row-key="getRowDepartmentKeys"
            :max-height="250">
            <!--   No.   -->
            <el-table-column type="index" header-align="center" align="center" width="50" label="No.">
              <template #default="scope">
                <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="要員名" prop="staffNameKanji" header-align="center" align="left"
              width="200"></el-table-column>
            <el-table-column label="締日" prop="closingDate" header-align="center" align="right"
              width="130"></el-table-column>
            <el-table-column label="調達金額" prop="amountRaised" header-align="center" align="right" width="150">
              <template #default="{ row }">
                <span>
                  {{ row.amountRaised === -9999 ? "-" : Number(row.amountRaised).toLocaleString() }}
                </span>
              </template>
            </el-table-column>

          </el-table>
        </template>
      </el-table-column>
      <el-table-column label="企業名" prop="companyAbbreviation" header-align="center" align="left" width="200">
      </el-table-column>
      <el-table-column label="締日" prop="closingDate" header-align="center" align="right" width="130">
      </el-table-column>
      <el-table-column label="調達金額" prop="lineTotal" header-align="center" align="right" width="150">
      </el-table-column>
      <el-table-column label="請求額(税込)" prop="amountCharged" header-align="center" align="right">
      </el-table-column>
      <!-- 操作数据 -->
      <el-table-column label="入出金額" prop="depositAmount" header-align="center" align="right">
        <template #default="{ row }">
          <el-input v-model="row.depositAmount" size="small" type="text" input-style="text-align: right;"
            placeholder="金额を入力" @input="filterInput(row, 'depositAmount'); calculateAmounts(accountList);"
            @focus="removeCommas(row, 'depositAmount')" @blur="formatWithCommas(row, 'depositAmount')"
            style="ime-mode: disabled;" inputmode="numeric" />
        </template>
      </el-table-column>
      <el-table-column label="手数料" prop="commission" header-align="center" align="right">
      </el-table-column>
      <el-table-column label="入出金予定日" prop="expectedDateOfPayment" header-align="center" align="right">
      </el-table-column>
      <!-- 操作数据 -->
      <el-table-column label="入出金日" prop="depositDate" header-align="center" align="right" width="130">
        <template #default="{ row }">
          <el-date-picker v-model="row.depositDate" type="date" placeholder="日付を選択" size="small" format="YYYY-MM-DD"
            value-format="YYYY-MM-DD" style="width: 100%" />
        </template>
      </el-table-column>
    </el-table>
  </div>
  <!-- 分页 -->
  <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
    :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalCount"
    layout="total, sizes, prev, pager, next, jumper">
  </el-pagination>
  <!-- 保存按钮 -->
  <div>
    <el-button class="custom-btn" type="primary" style="float: right;" @click="dataListSubmit" plain>保存</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dataForm: {
        tradeType: "売掛", // 默认选中「売掛」
        expectedDateOfPayment: [], // 出入金预定日
        closingDate: [], // 截止日期
      },
      accountList: [],
      expandRowKeys: [],
      receivableList: [],
      // dataList: [],
      //分页
      pageSize: 10,
      pageIndex: 1,
      totalCount: 0,
      dataListLoading: false,
    };
  },
  watch: {
    accountList: {
      handler(newList) {
        this.calculateAmounts(newList);
      },
      deep: true,
    },

    receivableList: {
      handler(newList) {
        this.calculateAmounts(newList);
      },
      deep: true,
    },
  },
  computed: {
    showTableAccounts() {
      return this.dataForm.tradeType === "売掛";
    },
    showTableReceivable() {
      return this.dataForm.tradeType === "買掛";
    },
  },
  methods: {
    calculateAmounts(list) {
      list.forEach((item) => {
        let lineTotal = Number(item.lineTotal?.toString().replace(/,/g, "")) || 0;
        let depositAmount = Number(item.depositAmount?.toString().replace(/,/g, "")) || 0;
        // item.amountCharged = (lineTotal * 1.1).toFixed(2);
        let amountCharged = lineTotal * 1.1;  // 含税计算
        let commission = amountCharged - depositAmount;

        item.lineTotal = lineTotal.toLocaleString();
        item.amountCharged = amountCharged.toLocaleString();
        item.depositAmount = depositAmount.toLocaleString();
        item.commission = commission.toLocaleString();
      });
    },
    filterInput(row, field) {
      if (!row[field]) {
        return;
      }

      // **转换全角数字为半角**
      row[field] = row[field].replace(/[０-９]/g, (s) =>
        String.fromCharCode(s.charCodeAt(0) - 0xfee0)
      );

      // **删除所有英文字母**
      row[field] = row[field].replace(/[A-Za-z]/g, "");

      // **删除非数字字符（仅保留数字）**
      row[field] = row[field].replace(/[^0-9]/g, "");

      if (!row[field] || isNaN(Number(row[field]))) {
        row[field] = "0"; // 避免误清空
      }
    },

    /** 失去焦点时：给数字添加千位分隔符 */
    formatWithCommas(row, field) {
      if (!row[field] || isNaN(Number(row[field].toString().replace(/,/g, "")))) {
        return;
      }
      row[field] = Number(row[field].toString().replace(/,/g, "")).toLocaleString(); // 添加千位分隔符
    },

    /** 获得焦点时：去掉千位分隔符，恢复纯数字 */
    removeCommas(row, field) {
      if (!row[field]) {
        return;
      }
      row[field] = row[field].toString().replace(/,/g, ""); // 去掉千位分隔符
    },
    formatDataList(list) {
      return list.map(item => ({
        ...item,
        totalSalesAmount: item.totalSalesAmount ? Number(item.totalSalesAmount).toLocaleString() : "0",
        lineTotal: item.lineTotal ? Number(item.lineTotal).toLocaleString() : "0",
        depositAmount: item.depositAmount ? Number(item.depositAmount).toLocaleString() : "0",
        commission: item.commission ? Number(item.commission).toLocaleString() : "0"
      }));
    },
    // 获取数据列表
    loadDataList() {
      let that = this;
      let data = {
        page: that.pageIndex,
        length: that.pageSize || 10,
      };
      that.dataListLoading = true;
      console.log("请求参数:", data); // ✅ 调试日志，检查 `length` 是否传递
      if (that.dataForm.tradeType === "売掛") {
        that.$httpV2("/receivableAndPayable/getReceivableByPageHelper", "GET", data, true, function (resp) {
          let result = resp.result;
          that.accountList = result.list.map(item => ({
            ...item,
            amountCharged: item.amountCharged ? Number(item.amountCharged).toLocaleString() : "0", // **防止空值**
            depositAmount: Number(item.depositAmount).toLocaleString(), // **格式化千位符**
            commission: Number(item.commission).toLocaleString(), // **格式化千位符**
            totalSalesAmount: item.totalSalesAmount ? Number(item.totalSalesAmount).toLocaleString() : "0",
            lineTotal: Number(item.lineTotal).toLocaleString(),
            uniqueList: item.uniqueList || []
          }));
          that.totalCount = result.total;
          that.dataListLoading = false;
        });
      }

      if (that.dataForm.tradeType === "買掛") {
        that.$httpV2("/receivableAndPayable/getPayableByPageHelper", "GET", data, true, function (resp) {
          let result = resp.result;
          that.receivableList = result.list.map(item => ({
            ...item,
            depositAmount: Number(item.depositAmount).toLocaleString(), // **格式化千位符**
            commission: Number(item.commission).toLocaleString(), // **格式化千位符**
            uniqueList: item.uniqueList || []
          }));
          that.totalCount = result.total;
          that.dataListLoading = false;
        });
      }
    },

    handleTradeTypeChange() {
      this.loadDataList();
    },

    // 分页
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.loadDataList();
    },
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.loadDataList();
    },

    // 根据时间搜索
    searchHandle() {
      let that = this;
      that.pageIndex = 1;
      let data = {
        page: that.pageIndex,
        length: that.pageSize,
        expectedDateOfPayment: that.dataForm.expectedDateOfPayment,
        closingDate: that.dataForm.closingDate,
      };
      that.dataListLoading = true;

      // ✅ 売掛（应收）数据处理
      if (that.dataForm.tradeType === "売掛") {
        that.$httpV2("/receivableAndPayable/getReceivableByPageHelper", "GET", data, true, function (resp) {
          let result = resp.result;
          that.accountList = result.list.map(item => ({
            ...item,
            uniqueList: item.uniqueList || []  // ✅ 重点添加
          }));
          that.totalCount = result.total;
          that.dataListLoading = false;
        });
      }

      // ✅ 買掛（应付）数据处理
      if (that.dataForm.tradeType === "買掛") {
        that.$httpV2("/receivableAndPayable/getPayableByPageHelper", "GET", data, true, function (resp) {
          let result = resp.result;
          that.receivableList = result.list.map(item => ({
            ...item,
            uniqueList: item.uniqueList || []  // ✅ 重点添加
          }));
          that.totalCount = result.total;
          that.dataListLoading = false;
        });
      }
    },
    //返回企业id
    getRowKeys(row) {
      return row.accountsReceivableAndPayableId;
    },
    getRowDepartmentKeys(row) {
      return row.departmentId;
    },
    handleExpandChange(row, expandedRows) {
      // expandedRows 是当前所有已展开行的数据
      // 如果只允许展开一个，则只保留当前点击这一行
      // this.expandRowKeys = [ row.accountsReceivableAndPayableId ];
      //允许展开多行
      // this.expandRowKeys = expandedRows.map(r => r.accountsReceivableAndPayableId);
      //允许展开多行，并且可以关闭
      const rowKey = row.accountsReceivableAndPayableId;
      const index = this.expandRowKeys.indexOf(rowKey);

      if (index === -1) {
        // 如果当前行未展开，则添加到展开数组
        this.expandRowKeys.push(rowKey);
      } else {
        // 如果当前行已展开，则从数组中移除，实现关闭功能
        this.expandRowKeys.splice(index, 1);
      }
    },

    // 获取表格合计行数据
    getAccountSummaries(param) {
      const { columns, data } = param;
      const sums = [];

      columns.forEach((column, index) => {
        // 序号/操作列等不处理
        if (index === 0 || index === columns.length - 1 || index === columns.length - 2) {
          sums[index] = '';
          return;
        }

        if (index === 1) {
          sums[index] = '入金合計';
          return;
        }

        if (column.property === "closingDate") {
          sums[index] = '';
          return;
        }

        const values = data.map(item => {
          let value = item[column.property];

          // 🛑 判断值为 -9999 时不参与计算
          if (value === -9999 || value === "-9999") {
            return 0;
          }

          // 去除千位符
          if (typeof value === "string") {
            value = value.replace(/,/g, "");
          }

          return Number(value) || 0;
        });

        if (!values.every(val => isNaN(val))) {
          sums[index] = values.reduce((a, b) => a + b, 0).toLocaleString();
        } else {
          sums[index] = ' ';
        }
      });

      return sums;
    },


    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];

      columns.forEach((column, index) => {
        if (index === 1) {
          sums[index] = '出金合計';
          return;
        }

        const values = data.map(item => {
          let value = item[column.property];

          if (value === -9999 || value === "-9999") {
            return 0;
          }

          if (typeof value === "string") {
            value = value.replace(/,/g, "");
          }

          return Number(value) || 0;
        });

        if (!values.every(val => isNaN(val))) {
          sums[index] = values.reduce((a, b) => a + b, 0).toLocaleString();
        } else {
          sums[index] = ' ';
        }
      });

      return sums;
    },



    cleanDataForSubmit(list) {
      return list.map(item => ({
        ...item,
        depositAmount: item.depositAmount ? item.depositAmount.toString().replace(/,/g, "") : "0",
        commission: item.commission ? item.commission.toString().replace(/,/g, "") : "0",
        amountCharged: item.amountCharged ? item.amountCharged.toString().replace(/,/g, "") : "0" // 确保 amountCharged 不为空
      }));
    },

    // 表格数据保存按钮
    dataListSubmit: function () {
      let that = this;

      const cleanData = (list) => {
        return list.map(item => ({
          ...item,
          lineTotal: item.lineTotal ? item.lineTotal.toString().replace(/,/g, "") : "0",
          depositAmount: item.depositAmount ? item.depositAmount.toString().replace(/,/g, "") : "0",
          commission: item.commission ? item.commission.toString().replace(/,/g, "") : "0",
          amountCharged: item.amountCharged ? item.amountCharged.toString().replace(/,/g, "") : "0" // 重要：确保不为空
        }));
      };

      if (that.dataForm.tradeType === "売掛") {
        let data = { dataList: cleanData(that.accountList) };
        that.$httpV2("/receivableAndPayable/updateReceivable", "POST", data, false, function (resp) {
          ElMessage({ message: "成功した操作", type: "success" });
        });
      } else if (that.dataForm.tradeType === "買掛") {
        let data = { dataList: cleanData(that.receivableList) };
        that.$httpV2("/receivableAndPayable/updatePayable", "POST", data, false, function (resp) {
          ElMessage({ message: "成功した操作", type: "success" });
        });
      }
    }
  },
  created() {
    this.loadDataList();
  },

};
</script>
<style>
.custom-btn {
  width: 100px;
  height: 35px;
  font-size: 16px;
}

/* 限制表格容器的高度，并启用滚动 */
.table-container {
  overflow-y: auto;
  border: 1px solid #ddd;
  /* 添加边框，让滚动区域更明显 */
  padding: 10px;
}

/* 可选：美化滚动条 */
.table-container::-webkit-scrollbar {
  width: 8px;
}

.table-container::-webkit-scrollbar-thumb {
  background-color: #ccc;
  border-radius: 4px;
}

.table-container::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}
</style>