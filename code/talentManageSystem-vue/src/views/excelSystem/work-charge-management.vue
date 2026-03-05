<template>
  <div>

    <!-- 年度和月份选择 -->
    <el-form inline>
      <el-form-item label="対象年度">
        <el-select v-model="selectedYear" placeholder="対象年度をお選びください" style="width: 150px;">
          <el-option v-for="year in years" :key="year" :label="year" :value="year" />
        </el-select>
      </el-form-item>

      <el-form-item label="対象月">
        <el-select v-model="selectedMonth" placeholder="対象月をお選びください" style="width: 150px;">
          <el-option v-for="month in months" :key="month" :label="month" :value="month" />
        </el-select>
      </el-form-item>
    </el-form>
    <!-- 工资表 -->
    <el-table :data="tableData" border style="width: 100%; margin-top: 20px" height="500px" show-summary
      :summary-method="getSummaries" row-key="staffId" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="staffId" label="社員番号" width="100" header-align="center" align="center" />
      <el-table-column prop="staffNameKanji" label="氏名" width="100" header-align="center" align="left" />

      <!-- 用函数显示格式化的值 + 更新原始数据 -->
      <el-table-column prop="baseSalary" label="基本給" width="100" header-align="center" align="right">
        <template #default="scope">
          <el-input :value="formatNumber(scope.row.baseSalary)" inputmode="numeric" size="small"
            @input="onInput($event, scope.row, 'baseSalary'); updateTaxableTotal(scope.row)"
            @focus="onFocus(scope.row, 'baseSalary')" :disabled="scope.row.laborStatus === 1"
            :input-style="{ textAlign: 'right' }" />
        </template>
      </el-table-column>

      <!-- 以下所有类似的列都改为同样的写法 -->
      <el-table-column prop="positionAllowance" label="役職手当" width="100" header-align="center" align="right">
        <template #default="scope">
          <el-input :value="formatNumber(scope.row.positionAllowance)" inputmode="numeric" size="small"
            @input="onInput($event, scope.row, 'positionAllowance'); updateTaxableTotal(scope.row)"
            @focus="onFocus(scope.row, 'positionAllowance')" :disabled="scope.row.laborStatus === 1"
            :input-style="{ textAlign: 'right' }" />
        </template>
      </el-table-column>

      <el-table-column prop="overtimeAllowance" label="時間外手当" width="100" header-align="center" align="right">
        <template #default="scope">
          <el-input :value="formatNumber(scope.row.overtimeAllowance)" inputmode="numeric" size="small"
            @input="onInput($event, scope.row, 'overtimeAllowance'); updateTaxableTotal(scope.row)"
            @focus="onFocus(scope.row, 'overtimeAllowance')" :disabled="scope.row.laborStatus === 1"
            :input-style="{ textAlign: 'right' }" />
        </template>
      </el-table-column>

      <el-table-column prop="otherAllowances" label="その他手当" width="100" header-align="center" align="right">
        <template #default="scope">
          <el-input :value="formatNumber(scope.row.otherAllowances)" inputmode="numeric" size="small"
            @input="onInput($event, scope.row, 'otherAllowances'); updateTaxableTotal(scope.row)"
            @focus="onFocus(scope.row, 'otherAllowances')" :disabled="scope.row.laborStatus === 1"
            :input-style="{ textAlign: 'right' }" />
        </template>
      </el-table-column>

      <el-table-column prop="advancePayment" label="立替金" width="100" header-align="center" align="right">
        <template #default="scope">
          <el-input :value="formatNumber(scope.row.advancePayment)" inputmode="numeric" size="small"
            @input="onInput($event, scope.row, 'advancePayment'); updateTaxableTotal(scope.row)"
            @focus="onFocus(scope.row, 'advancePayment')" :disabled="scope.row.laborStatus === 1"
            :input-style="{ textAlign: 'right' }" />
        </template>
      </el-table-column>

      <el-table-column prop="commutingExpense" label="通勤費" width="100" header-align="center" align="right">
        <template #default="scope">
          <el-input :value="formatNumber(scope.row.commutingExpense)" inputmode="numeric" size="small"
            @input="onInput($event, scope.row, 'commutingExpense'); updateTaxableTotal(scope.row)"
            @focus="onFocus(scope.row, 'commutingExpense')" :disabled="scope.row.laborStatus === 1"
            :input-style="{ textAlign: 'right' }" />
        </template>
      </el-table-column>

      <el-table-column prop="taxablePaymentTotal" label="課税支給合計" width="100" header-align="center" align="right">
        <template #default="scope">
          <span>{{ formatNumber(scope.row.taxablePaymentTotal) }}</span>
        </template>
      </el-table-column>

      <!-- 社保费用（健康保険料、介護保険料、厚生年金保険等） -->
      <el-table-column prop="healthInsuranceFee" label="健康保険料" width="100" header-align="center" align="right">
        <template #default="scope">
          <el-input :value="formatNumber(scope.row.healthInsuranceFee)" inputmode="numeric" size="small"
            @input="onInput($event, scope.row, 'healthInsuranceFee'); updateLaborCostTotal(scope.row)"
            @focus="onFocus(scope.row, 'healthInsuranceFee')" :disabled="scope.row.laborStatus === 1"
            :input-style="{ textAlign: 'right' }" />
        </template>
      </el-table-column>

      <el-table-column prop="nursingInsuranceFee" label="介護保険料" width="100" header-align="center" align="right">
        <template #default="scope">
          <el-input :value="formatNumber(scope.row.nursingInsuranceFee)" inputmode="numeric" size="small"
            @input="onInput($event, scope.row, 'nursingInsuranceFee'); updateLaborCostTotal(scope.row)"
            @focus="onFocus(scope.row, 'nursingInsuranceFee')" :disabled="scope.row.laborStatus === 1"
            :input-style="{ textAlign: 'right' }" />
        </template>
      </el-table-column>

      <el-table-column prop="welfarePensionInsurance" label="厚生年金保険" width="100" header-align="center" align="right">
        <template #default="scope">
          <el-input :value="formatNumber(scope.row.welfarePensionInsurance)" inputmode="numeric" size="small"
            @input="onInput($event, scope.row, 'welfarePensionInsurance'); updateLaborCostTotal(scope.row)"
            @focus="onFocus(scope.row, 'welfarePensionInsurance')" :disabled="scope.row.laborStatus === 1"
            :input-style="{ textAlign: 'right' }" />
        </template>
      </el-table-column>

      <el-table-column prop="employmentInsuranceFee" label="雇用保険料" width="100" header-align="center" align="right">
        <template #default="scope">
          <el-input :value="formatNumber(scope.row.employmentInsuranceFee)" inputmode="numeric" size="small"
            @input="onInput($event, scope.row, 'employmentInsuranceFee'); updateLaborCostTotal(scope.row)"
            @focus="onFocus(scope.row, 'employmentInsuranceFee')" :disabled="scope.row.laborStatus === 1"
            :input-style="{ textAlign: 'right' }" />
        </template>
      </el-table-column>

      <el-table-column prop="otherLaborExpenses" label="その他労務費" width="100" header-align="center" align="right">
        <template #default="scope">
          <el-input :value="formatNumber(scope.row.otherLaborExpenses)" inputmode="numeric" size="small"
            @input="onInput($event, scope.row, 'otherLaborExpenses'); updateLaborCostTotal(scope.row)"
            @focus="onFocus(scope.row, 'otherLaborExpenses')" :disabled="scope.row.laborStatus === 1"
            :input-style="{ textAlign: 'right' }" />
        </template>
      </el-table-column>

      <el-table-column prop="totalLaborExpenses" label="労務費合計" width="100" header-align="center" align="right">
        <template #default="scope">
          <span>{{ formatNumber(scope.row.totalLaborExpenses) }}</span>
        </template>
      </el-table-column>
    </el-table>


    <!-- <p v-if="loading">数据加载中...</p> -->
    <p v-if="error">{{ error }}</p>

    <!-- 操作按钮 -->
    <div style="margin-top: 20px; text-align: right;">
      <el-button type="primary" @click="save">保存</el-button>
      <el-button type="success" @click="confirm">入力確定</el-button>
      <el-button type="danger" @click="reset">確定解除</el-button>
    </div>
  </div>
</template>

<script>
import axios from "axios"; // 引入 axios
export default {
  data() {
    return {
      tableData: [],
      page: 1,             // 当前页
      pageSize: 100,        // 每页条数
      totalCount: 0,       // 总数据量
      selectedRows: [],    // 选中的行
      isConfirmed: false,
      selectedYear: null,
      selectedMonth: null,
      loading: false,
      error: null,
      years: [],
      months: ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"],
    };
  },
  created() {
    this.generateYears();
  },

  methods: {

    // 显示值格式化为带逗号的字符串
    formatNumber(val) {
      if (val === null || val === undefined || val === "") return "";
      const num = Number(val.toString().replace(/,/g, ""));
      return isNaN(num) ? "" : num.toLocaleString();
    },

    // 输入时去掉逗号并更新实际值
    onInput(val, row, field) {
      const raw = val.replace(/,/g, "").replace(/\D/g, "");
      row[field] = raw === "" ? null : Number(raw);
      this.updateTaxableTotal(row);
    },

    // 失焦后，添加逗号（不需要修改数据，只影响显示）
    onBlur(row, field) {
      // 空方法可以保留，也可以不做处理
    },

    // 聚焦时去掉逗号
    onFocus(row, field) {
      if (typeof row[field] === "string") {
        row[field] = row[field].replace(/,/g, "");
      }
    },


    generateYears() {
      const currentYear = new Date().getFullYear();
      const startYear = currentYear - 10; // 可调整起始年份
      const endYear = currentYear + 10; // 可调整结束年份

      // 生成年份范围数组
      this.years = [];
      for (let year = startYear; year <= endYear; year++) {
        this.years.push(year);
      }
    },
    // 入力時：数字のみ許可（カンマを削除）
    filterInput(row, field) {
      if (row[field] === null || row[field] === undefined || row[field] === "") {
        row[field] = null; // 保持未输入状态
      } else {
        row[field] = row[field].toString().replace(/[０-９]/g, (match) => {
          return String.fromCharCode(match.charCodeAt(0) - 0xFEE0); // 全角转半角
        });
        row[field] = row[field].toString().replace(/\D/g, "");
        row[field] = row[field] ? Number(row[field]) : 0;
      }
    },
    // フォーカスが外れたら、桁区切り（カンマ追加）
    formatWithCommas(row, field) {
      if (row[field] === null || row[field] === undefined || row[field] === "") {
        row[field] = null; // 不改成 0
      } else {
        row[field] = Number(row[field]).toLocaleString(); // 添加千分位逗号
      }
    },
    // フォーカス時：カンマを削除し、生の数値に戻す
    removeCommas(row, field) {
      if (row && row[field] != null) {
        row[field] = row[field].toString().replace(/,/g, ""); // 去掉千分位逗号
      } else {
        row[field] = "";
      }
    },
    parseNumber(value) {
      if (value === null || value === undefined || value === "") {
        return null; // 允许 `null`
      }
      if (typeof value === "string") {
        value = value.replace(/,/g, ""); // 去掉千分位符
      }
      return isNaN(Number(value)) ? 0 : Number(value); // 确保数值转换正常
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection; // 存储选中的行
    },
    save() {

      if (!this.selectedRows || this.selectedRows.length === 0) {
        this.$message.warning("保存するデータを選択してください!");
        return;
      }

      this.loading = true; // 启用 loading 状态

      let reqData = this.selectedRows.map(item => ({
        id: item.id,
        staffId: item.staffId,
        laborStatus: 2,
        // laborStatus: item.laborStatus || 2,
        time: `${this.selectedYear}-${String(this.selectedMonth).padStart(2, '0')}`,
        staffNameKanji: item.staffNameKanji || "",

        baseSalary: this.parseNumber(item.baseSalary),
        positionAllowance: this.parseNumber(item.positionAllowance),
        overtimeAllowance: this.parseNumber(item.overtimeAllowance),
        otherAllowances: this.parseNumber(item.otherAllowances),
        advancePayment: this.parseNumber(item.advancePayment),
        commutingExpense: this.parseNumber(item.commutingExpense),

        taxablePaymentTotal:
          (this.parseNumber(item.baseSalary) ?? 0) +
          (this.parseNumber(item.positionAllowance) ?? 0) +
          (this.parseNumber(item.overtimeAllowance) ?? 0) +
          (this.parseNumber(item.otherAllowances) ?? 0) +
          (this.parseNumber(item.advancePayment) ?? 0) +
          (this.parseNumber(item.commutingExpense) ?? 0),

        healthInsuranceFee: this.parseNumber(item.healthInsuranceFee),
        nursingInsuranceFee: this.parseNumber(item.nursingInsuranceFee),
        welfarePensionInsurance: this.parseNumber(item.welfarePensionInsurance),
        employmentInsuranceFee: this.parseNumber(item.employmentInsuranceFee),
        otherLaborExpenses: this.parseNumber(item.otherLaborExpenses),

        totalLaborExpenses:
          (this.parseNumber(item.taxablePaymentTotal) ?? 0) +
          (this.parseNumber(item.healthInsuranceFee) ?? 0) +
          (this.parseNumber(item.nursingInsuranceFee) ?? 0) +
          (this.parseNumber(item.welfarePensionInsurance) ?? 0) +
          (this.parseNumber(item.employmentInsuranceFee) ?? 0) +
          (this.parseNumber(item.otherLaborExpenses) ?? 0),

        comment: item.comment || "无备注"
      }));


      this.$httpV2(
        "/laborManagement/update",
        "POST",
        reqData,
        false,
        (resp) => {
          this.loading = false;
          if (resp && resp.code === 200) {
            this.$message.success("選択した行は保存しました！");
            this.fetchData();
          } else {
            this.$message.error("エラーが発生しました！");
          }
        },
        (error) => {
          this.loading = false;
          this.$message.error("通信エラー。ネットワークを確認してください。");
          console.error("❌ 通信エラー:", error);
        }
      );
    },


    confirm() {
      if (!this.selectedRows || this.selectedRows.length === 0) {
        this.$message.warning("データを選択してください！");
        return;
      }

      const reqData = this.selectedRows.map(item => ({
        id: item.id,
        staffId: item.staffId,
        laborStatus: 1,
        time: `${this.selectedYear}-${String(this.selectedMonth).padStart(2, '0')}`,
        staffNameKanji: item.staffNameKanji || "",

        baseSalary: this.parseNumber(item.baseSalary),
        positionAllowance: this.parseNumber(item.positionAllowance),
        overtimeAllowance: this.parseNumber(item.overtimeAllowance),
        otherAllowances: this.parseNumber(item.otherAllowances),
        advancePayment: this.parseNumber(item.advancePayment),
        commutingExpense: this.parseNumber(item.commutingExpense),

        taxablePaymentTotal:
          (this.parseNumber(item.baseSalary) ?? 0) +
          (this.parseNumber(item.positionAllowance) ?? 0) +
          (this.parseNumber(item.overtimeAllowance) ?? 0) +
          (this.parseNumber(item.otherAllowances) ?? 0) +
          (this.parseNumber(item.advancePayment) ?? 0) +
          (this.parseNumber(item.commutingExpense) ?? 0),

        healthInsuranceFee: this.parseNumber(item.healthInsuranceFee),
        nursingInsuranceFee: this.parseNumber(item.nursingInsuranceFee),
        welfarePensionInsurance: this.parseNumber(item.welfarePensionInsurance),
        employmentInsuranceFee: this.parseNumber(item.employmentInsuranceFee),
        otherLaborExpenses: this.parseNumber(item.otherLaborExpenses),

        totalLaborExpenses:
          (this.parseNumber(item.taxablePaymentTotal) ?? 0) +
          (this.parseNumber(item.healthInsuranceFee) ?? 0) +
          (this.parseNumber(item.nursingInsuranceFee) ?? 0) +
          (this.parseNumber(item.welfarePensionInsurance) ?? 0) +
          (this.parseNumber(item.employmentInsuranceFee) ?? 0) +
          (this.parseNumber(item.otherLaborExpenses) ?? 0),

        comment: item.comment || "无备注"
      }));

      this.$httpV2(
        "/laborManagement/confirm",
        "POST",
        reqData,
        false,
        (resp) => {
          if (resp && resp.code === 200) {
            this.selectedRows.forEach(row => {
              row.laborStatus = 1;
            });
            this.$message.success("入力が確定されました！");
            this.fetchData();
          } else {
            this.$message.error("入力確定に失敗しました。");
          }
        },
        (error) => {
          this.$message.error("通信エラー。ネットワークを確認してください。");
          console.error("❌ リクエストエラー:", error);
        }
      );
    },


    reset() {
      if (!this.selectedRows || this.selectedRows.length === 0) {
        this.$message.warning("解除するデータを選択してください！");
        return;
      }

      const reqData = this.selectedRows.map(item => ({
        id: item.id,
        staffId: item.staffId,
        laborStatus: 2, // 👈 解除锁定状态
        time: `${this.selectedYear}-${String(this.selectedMonth).padStart(2, '0')}`,
        staffNameKanji: item.staffNameKanji || "",

        baseSalary: this.parseNumber(item.baseSalary),
        positionAllowance: this.parseNumber(item.positionAllowance),
        overtimeAllowance: this.parseNumber(item.overtimeAllowance),
        otherAllowances: this.parseNumber(item.otherAllowances),
        advancePayment: this.parseNumber(item.advancePayment),
        commutingExpense: this.parseNumber(item.commutingExpense),

        taxablePaymentTotal:
          (this.parseNumber(item.baseSalary) ?? 0) +
          (this.parseNumber(item.positionAllowance) ?? 0) +
          (this.parseNumber(item.overtimeAllowance) ?? 0) +
          (this.parseNumber(item.otherAllowances) ?? 0) +
          (this.parseNumber(item.advancePayment) ?? 0) +
          (this.parseNumber(item.commutingExpense) ?? 0),

        healthInsuranceFee: this.parseNumber(item.healthInsuranceFee),
        nursingInsuranceFee: this.parseNumber(item.nursingInsuranceFee),
        welfarePensionInsurance: this.parseNumber(item.welfarePensionInsurance),
        employmentInsuranceFee: this.parseNumber(item.employmentInsuranceFee),
        otherLaborExpenses: this.parseNumber(item.otherLaborExpenses),

        totalLaborExpenses:
          (this.parseNumber(item.taxablePaymentTotal) ?? 0) +
          (this.parseNumber(item.healthInsuranceFee) ?? 0) +
          (this.parseNumber(item.nursingInsuranceFee) ?? 0) +
          (this.parseNumber(item.welfarePensionInsurance) ?? 0) +
          (this.parseNumber(item.employmentInsuranceFee) ?? 0) +
          (this.parseNumber(item.otherLaborExpenses) ?? 0),

        comment: item.comment || "无备注"
      }));

      this.$httpV2(
        "/laborManagement/confirm",
        "POST",
        reqData,
        false,
        (resp) => {
          if (resp && resp.code === 200) {
            this.selectedRows.forEach(row => {
              row.laborStatus = 1;
            });
            this.$message.success("確定が解除されました！");
            this.fetchData(); // 可选
          } else {
            this.$message.error("解除に失敗しました。");
          }
        },
        (error) => {
          this.$message.error("通信エラー。ネットワークを確認してください。");
          console.error("❌ リクエストエラー:", error);
        }
      );
    },


    // 计算 课税支给合计 taxablePaymentTotal
    updateTaxableTotal(row) {
      row.baseSalary = this.parseNumber(row.baseSalary);
      row.positionAllowance = this.parseNumber(row.positionAllowance);
      row.overtimeAllowance = this.parseNumber(row.overtimeAllowance);
      row.otherAllowances = this.parseNumber(row.otherAllowances);
      row.advancePayment = this.parseNumber(row.advancePayment);
      row.commutingExpense = this.parseNumber(row.commutingExpense);

      row.taxablePaymentTotal =
        row.baseSalary +
        row.positionAllowance +
        row.overtimeAllowance +
        row.otherAllowances +
        row.advancePayment +
        row.commutingExpense;
      this.updateLaborCostTotal(row);  // 确保更新后立即反映在労務費合計

    },

    // 计算 労務費合計 totalLaborExpenses
    updateLaborCostTotal(row) {
      row.healthInsuranceFee = this.parseNumber(row.healthInsuranceFee);
      row.nursingInsuranceFee = this.parseNumber(row.nursingInsuranceFee);
      row.welfarePensionInsurance = this.parseNumber(row.welfarePensionInsurance);
      row.employmentInsuranceFee = this.parseNumber(row.employmentInsuranceFee);
      row.otherLaborExpenses = this.parseNumber(row.otherLaborExpenses);

      // 👇 加入課税支給合計
      row.taxablePaymentTotal = this.parseNumber(row.taxablePaymentTotal);

      row.totalLaborExpenses =
        row.taxablePaymentTotal + // ✅ 新加部分
        row.healthInsuranceFee +
        row.nursingInsuranceFee +
        row.welfarePensionInsurance +
        row.employmentInsuranceFee +
        row.otherLaborExpenses;
    },


    getSummaries({ columns, data }) {
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "合計";
          return;
        }
        if (index === 1) {
          sums[index] = "";  // 不显示合计
          return;
        }
        if (index === 2) {
          sums[index] = "";  // 不显示合计
          return;
        }

        try {
          // 获取当前列的数据，去掉 `,` 确保正确解析
          const values = data.map((item) => {
            let val = item[column.property] ? item[column.property].toString().replace(/,/g, '') : null;
            return val !== null ? Number(val) : null;
          });

          // **情况 1**：表格数据为空，返回 `null`
          if (data.length === 0) {
            sums[index] = null;
            return;
          }

          // **情况 2**：所有值都是 `null`，返回 `null`
          if (values.every(v => v === null)) {
            sums[index] = null;
            return;
          }

          // **情况 3**：有 `0` 存在，且没有其他非 `null` 数字，返回 `0`
          if (values.every(v => v === null || v === 0) && values.some(v => v === 0)) {
            sums[index] = "0";
            return;
          }

          // **情况 4**：正常计算合计
          let total = values.reduce((prev, curr) => prev + (curr ?? 0), 0);
          sums[index] = total.toLocaleString();
        } catch (error) {
          console.error("合計計算エラー:", error);
          sums[index] = "計算エラー";
        }
      });

      return sums;
    },
    validateParams() {
      if (!this.selectedYear || !this.selectedMonth) {
        throw new Error("年もしくは月が未選択です。");
      }
    },
    formatDate() {
      return `${this.selectedYear}-${this.selectedMonth.padStart(2, '0')}`;
    },
    formatNumberWithCommas(value) {
      if (value === "" || value === undefined || value === null) {
        return "0";
      }
      return Number(value).toLocaleString();  // ✅ 确保传入的是数值
    },
    fetchData() {
      let that = this;
      that.validateParams();
      that.loading = true;
      that.error = null;

      let data = {
        page: that.page,
        pageSize: that.pageSize,
        time: that.formatDate(),
      };

      that.$httpV2("/laborManagement/query", "GET", data, true, (resp) => {
        console.log("后端返回的数据：", resp);

        if (resp.code === 200) {
          that.tableData = resp.list.data.map(item => ({
            ...item,
            baseSalary: item.baseSalary === null ? null : this.formatNumberWithCommas(this.parseNumber(item.baseSalary)),
            positionAllowance: item.positionAllowance === null ? null : this.formatNumberWithCommas(this.parseNumber(item.positionAllowance)),
            overtimeAllowance: item.overtimeAllowance === null ? null : this.formatNumberWithCommas(this.parseNumber(item.overtimeAllowance)),
            otherAllowances: item.otherAllowances === null ? null : this.formatNumberWithCommas(this.parseNumber(item.otherAllowances)),
            advancePayment: item.advancePayment === null ? null : this.formatNumberWithCommas(this.parseNumber(item.advancePayment)),
            commutingExpense: item.commutingExpense === null ? null : this.formatNumberWithCommas(this.parseNumber(item.commutingExpense)),
            taxablePaymentTotal: item.taxablePaymentTotal ?? null,
            healthInsuranceFee: item.healthInsuranceFee === null ? null : this.formatNumberWithCommas(this.parseNumber(item.healthInsuranceFee)),
            nursingInsuranceFee: item.nursingInsuranceFee === null ? null : this.formatNumberWithCommas(this.parseNumber(item.nursingInsuranceFee)),
            welfarePensionInsurance: item.welfarePensionInsurance === null ? null : this.formatNumberWithCommas(this.parseNumber(item.welfarePensionInsurance)),
            employmentInsuranceFee: item.employmentInsuranceFee === null ? null : this.formatNumberWithCommas(this.parseNumber(item.employmentInsuranceFee)),
            otherLaborExpenses: item.otherLaborExpenses === null ? null : this.formatNumberWithCommas(this.parseNumber(item.otherLaborExpenses)),
            totalLaborExpenses: item.totalLaborExpenses ?? null,
          }));
          this.totalCount = resp.list.total || this.tableData.length;
        }
        that.loading = false;
      });
    },
    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.page = 1;
      this.fetchData();
    },
    handlePageChange(newPage) {
      this.page = newPage;
      this.fetchData();
    },
    update() {
      let that = this;
      that.$httpV2(
        "/laborManagement/update",
        "POST",
        that.tableData,
        false,
        (resp) => {
          this.loading = false;
          if (resp && resp.code === 200) {
            this.$message.success("データが保存されました。");
          } else {
            this.$message.error("保存に失敗しました。");
          }
        }
      );
    }
  },
  mounted() {
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1; // 1〜12 月
    const day = now.getDate();

    // 判断逻辑：如果今天是当月16日 ~ 次月15日 → 対象月 = 当前月
    // 否则 → 対象月 = 前一月（注意跨年情况）
    if (day >= 16) {
      // 当前月的 16日～月末 → 対象月 = 当前月
      this.selectedMonth = month.toString().padStart(2, '0');
      this.selectedYear = year;
    } else {
      // 当前月的 1〜15日 → 対象月 = 上一个月（注意 1月 → 前一年12月）
      const prevMonth = month - 1 === 0 ? 12 : month - 1;
      const targetYear = month - 1 === 0 ? year - 1 : year;

      this.selectedMonth = prevMonth.toString().padStart(2, '0');
      this.selectedYear = targetYear;
    }

    this.fetchData();
  },

  watch: {
    selectedYear(newYear) {
      console.log("年を変更:", newYear);
      this.fetchData(); // ✅ 选择年份时自动更新数据
    },
    selectedMonth(newMonth) {
      console.log("月の変更:", newMonth);
      this.fetchData(); // ✅ 选择月份时自动更新数据
    }
  },
  computed: {
    isSaveDisabled() {
      return !this.isConfirmed;  // 确认输入前，或解除确认后，禁用保存按钮
    }
  }
};
</script>

<style>
.filter-form {
  margin-bottom: 20px;
}

.el-scrollbar {
  max-height: 500px;
  /* 限制表格高度 */
  overflow-y: auto;
  /* 使表格支持垂直滚动 */
}

.el-table__footer-wrapper {
  position: sticky;
  bottom: 0;
  background-color: #fff;
  z-index: 10;
  box-shadow: 0 -1px 10px rgba(0, 0, 0, 0.1);
}
</style>