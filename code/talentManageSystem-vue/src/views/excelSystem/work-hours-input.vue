<template>
  <!-- 添加月份显示 -->
  <div class="month-display">
    <h3>対象年月: {{ formattedMonth }} &nbsp;&nbsp;&nbsp; 企業略称：{{ this.companyAbbreviation }} &nbsp;&nbsp;&nbsp; 部署略称：{{
      this.departmentAbbreviation }}
      &nbsp;&nbsp;&nbsp; 案件略称：{{ this.projectNameAbbreviation }}
    </h3>
  </div>

  <!-- 表格 -->
  <el-table :data="dataList1" style="width: 100%; margin-bottom: 20px;" row-key="tradingId" border show-summary
    :summary-method="getSummaries" :editable="isEditable">
    <div>1. 要員</div>
    <el-table-column label="NO" width="150" type="index" :index="indexMethod" header-align="center"
      align="center"></el-table-column>
    <el-table-column prop="staffNameKanji" label="要員名" width="180" header-align="center" align="left"></el-table-column>
    <el-table-column prop="procurementCompanyName" label="会社名" width="180" header-align="center"
      align="left"></el-table-column>
    <el-table-column prop="operatingTime" label="稼働時間" width="180" header-align="center" align="right">
      <template #default="{ row, column, $index }">
        <el-input v-if="isEditable" v-model="row.operatingTime" size="small" @input="handleOperatingTimeInput(row)"
          @focus="removeCommas(row, 'operatingTime')" @blur="opformatWithCommas(row, 'operatingTime')"
          style="ime-mode: disabled;" inputmode="numeric" input-style="text-align: right;"
          :disabled="businessConfirmation == 1"></el-input>
        <span v-else>{{ row.operatingTime }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="overTime" label="超過時間" width="180" header-align="center" align="right">
      <template #default="{ row }">
        <span :class="{ 'negative-value': Number(row.overTime) < 0 }">
          {{ getOverTimeText(row) }}
        </span>
      </template>
    </el-table-column>
    <el-table-column prop="salesAmount" label="販売金額" width="180" header-align="center" align="right">
      <template #default="{ row, column, $index }">
        <el-input v-if="isEditable" v-model="row.salesAmount" size="small" @input="filterInput(row, 'salesAmount')"
          @focus="removeCommas(row, 'salesAmount')" @blur="formatWithCommas(row, 'salesAmount')" :input-style="{
            textAlign: 'right',
            color: Number(row.salesAmount?.toString().replace(/,/g, '')) < 0 ? 'red' : '',
            fontWeight: Number(row.salesAmount?.toString().replace(/,/g, '')) < 0 ? 'bold' : ''
          }" style="ime-mode: disabled;" inputmode="numeric" :disabled="businessConfirmation == 1" />
        <span v-else :class="{ 'negative-value': Number(row.salesAmount?.toString().replace(/,/g, '')) < 0 }">
          {{ row.salesAmount }}
        </span>
      </template>
    </el-table-column>
    <el-table-column prop="amountRaised" label="調達金額" width="180" header-align="center" align="right">
      <template #default="{ row, column, $index }">
        <!-- 判断是否可编辑并且金额不是"-9,999" -->
        <el-input v-if="isEditable && row.amountRaised !== '-9,999' && row.procurementCompanyName !== 'UCL'"
          v-model="row.amountRaised" size="small" @input="filterInput(row, 'amountRaised')"
          @focus="removeCommas(row, 'amountRaised')" @blur="formatWithCommas(row, 'amountRaised')" :input-style="{
            textAlign: 'right',
            color: Number(row.amountRaised?.toString().replace(/,/g, '')) < 0 ? 'red' : '',
            fontWeight: Number(row.amountRaised?.toString().replace(/,/g, '')) < 0 ? 'bold' : ''
          }" style="ime-mode: disabled;" inputmode="numeric" :disabled="businessConfirmation == 1" />
        <!-- 如果金额为"-9,999"，则显示span，否则显示金额 -->
        <span v-else :class="{
          'negative-value': row.procurementCompanyName !== 'UCL' &&
            Number(row.amountRaised?.toString().replace(/,/g, '')) < 0
        }">
          {{ (row.amountRaised === "-9,999" && row.procurementCompanyName === 'UCL') ? "-" : row.amountRaised
          }}
        </span>
      </template>
    </el-table-column>
  </el-table>

  <el-table :data="dataList2" style="width: 100%; margin-bottom: 20px;" row-key="companyId" border
    @expand-change="handleExpandChange">
    <div>2.清算</div>
    <el-table-column prop="estimatedSalesAmount" label="想定販売金額" width="180" header-align="center"
      align="right"></el-table-column>
    <el-table-column prop="salesAmount" label="販売金額" width="180" header-align="center" align="right">
      <template #default>
        <span :class="{ 'negative-value': Number(dataList2[0].salesAmount?.toString().replace(/,/g, '')) < 0 }">
          {{ dataList2[0].salesAmount }}
        </span>
      </template>
    </el-table-column>


    <el-table-column prop="theMonthActuarialAmount" label="当月清算" width="180" header-align="center" align="right">
      <template #default="{ row, column, $index }">
        <el-input v-if="isEditable" v-model="row.theMonthActuarialAmount" size="small"
          @input="tMAAInput(row, 'theMonthActuarialAmount')" @focus="removeCommas(row, 'theMonthActuarialAmount')"
          @blur="formatWithCommas(row, 'theMonthActuarialAmount')" style="ime-mode: disabled;" inputmode="numeric"
          input-style="text-align: right;" :disabled="businessConfirmation == 1"></el-input>
        <span v-else>{{ row.theMonthActuarialAmount }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="previousMonthActuarialBalance" label="前月残清算" width="180" header-align="center" align="right">
      <template #default="{ row }">
        <span
          :class="{ 'negative-value': Number(row.previousMonthActuarialBalance?.toString().replace(/,/g, '')) < 0 }">
          {{ row.previousMonthActuarialBalance }}
        </span>
      </template>
    </el-table-column>
    <el-table-column prop="currentMonthBillingAmount" label="当月請求" width="180" header-align="center" align="right">
      <template #default="{ row, column, $index }">
        <el-input v-if="isEditable" v-model="row.currentMonthBillingAmount" size="small"
          @input="filterInput(row, 'currentMonthBillingAmount')" @focus="removeCommas(row, 'currentMonthBillingAmount')"
          @blur="formatWithCommas(row, 'currentMonthBillingAmount')" style="ime-mode: disabled;" inputmode="numeric"
          input-style="text-align: right;" :disabled="businessConfirmation == 1"></el-input>
        <span v-else>{{ row.currentMonthBillingAmount }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="theMonthResidualAmount" label="当月残清算" width="180" header-align="center" align="right">
      <template #default="{ row }">
        <span :class="{ 'negative-value': updateResidualAmountRaw(row) < 0 }">
          {{ updateResidualAmount(row) }}
        </span>
      </template>
    </el-table-column>
    <el-table-column prop="totalAmountRaised" label="調達合計" width="180" header-align="center" align="right">
      <template #default>
        <span :class="{ 'negative-value': Number(dataList2[0].totalAmountRaised?.toString().replace(/,/g, '')) < 0 }">
          {{ dataList2[0].totalAmountRaised }}
        </span>
      </template>

    </el-table-column>
  </el-table>

  <el-row>
    <el-button :type="businessConfirmation == 1 ? 'info' : 'primary'" :disabled="businessConfirmation == 1"
      @click="saveData" v-if="isAuth(['price_search'])">保存</el-button>
    <el-button :type="businessConfirmation == 1 ? 'info' : 'primary'" :disabled="businessConfirmation == 1"
      @click="confirmInput">{{ buttonText }}</el-button>
    <el-button :type="businessConfirmation == 1 ? 'info' : 'primary'" :disabled="businessConfirmation == 1"
      @click="confirmBusiness" v-if="isAuth(['super'])">営業確定</el-button>
    <el-button :type="businessConfirmation == 1 ? 'primary' : 'info'" @click="goBack">戻る</el-button>
  </el-row>
</template>

<script>
export default {
  components: {},
  data: function () {
    const routeQuery = this.$route.query;
    console.log('Route query full:', routeQuery);
    console.log('Route query month:', routeQuery.month);
    console.log('Route query projectId:', routeQuery.projectId);
    console.log('Route query businessConfirmation:', routeQuery.businessConfirmation);

    return {
      month: routeQuery.month || '',
      projectId: routeQuery.projectId,
      businessConfirmation: Number(routeQuery.businessConfirmation) || 0,
      tradingStatus: Number(this.$route.query.tradingStatus) || 0,
      companyAbbreviation: routeQuery.companyAbbreviation || '',
      departmentAbbreviation: routeQuery.departmentAbbreviation || '',
      principalName: routeQuery.principalName || '',
      projectNameAbbreviation: routeQuery.projectNameAbbreviation || '',
      tradingId: null,
      isEditable: true,  // 控制表格是否可编辑
      buttonText: '入力確定',  // 添加按钮文字状态
      // 提交表单时的数据
      dataForm1: {
        //查询条件
        "tradingId": null,
        "procurementCompanyName": null,
        "staffId": null,
        "staffNameKanji": null,
        "operatingTime": null,
        "overTime": null,
        "salesAmount": null,
        "amountRaised": null,
        "settlementLowerLimit": null,
        "settlementUpperLimit": null,
        "upperLimit": null,
        "lowerLimit": null,
        "baseSalesAmount": null,
        "baseAmountRaised": null,
        "salesIncrementUnitPriceHour": null,
        "salesDecrementUnitPriceHour": null,
        "procurementIncrementUnitPriceHour": null,
        "procurementDecrementUnitPriceHour": null,
      },
      dataForm2: {
        "estimatedSalesAmount": null,
        "salesAmount": null,
        "theMonthActuarialAmount": null,
        "previousMonthActuarialAmount": null,
        "currentMonthBillingAmount": null,
        "theMonthResidualAmount": null,
        "totalAmountRaised": null
      },
      dataList1: [],
      dataList2: [{
        estimatedSalesAmount: '0',
        estimatedSalesAmountRaw: 0,
        salesAmount: '0',
        theMonthActuarialAmount: '0',
        previousMonthActuarialBalance: '0',
        totalAmountRaised: '0',
        currentMonthBillingAmount: '0',
        theMonthResidualAmount: '0'
      }],

      settlementLowerLimit: null,
      settlementUpperLimit: null, // 存接口返回的上下限
      hasEstimatedBeenCalculated: false,



    }
  },
  computed: {
    formattedMonth() {
      if (!this.month) return '';
      // 将 YYYY-MM-DD 格式转换为 YYYY-MM
      return this.month.substring(0, 7);
    }
  },
  methods: {

    /**
     * 处理输入金额时的过滤逻辑
     * @param {Object} row - 当前行对象
     * @param {String} field - 需要处理的字段
     */
    filterInput(row, field) {
      if (!row[field]) return;

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
    tMAAInput(row, field) {
      if (typeof row[field] !== 'string') row[field] = String(row[field] || '');

      // 1. 转换全角数字为半角
      row[field] = row[field].replace(/[０-９]/g, s =>
        String.fromCharCode(s.charCodeAt(0) - 0xfee0)
      );

      // 2. 删除英文字母
      row[field] = row[field].replace(/[A-Za-z]/g, '');

      // 3. 只保留数字、小数点、负号
      row[field] = row[field].replace(/[^0-9.\-]/g, '');

      // 4. 只允许一个负号，并且必须在开头
      const minusCount = (row[field].match(/-/g) || []).length;
      if (minusCount > 1) {
        row[field] = row[field].replace(/-/g, ''); // 去除所有负号
        row[field] = '-' + row[field]; // 重新加一个在最前面
      } else if (minusCount === 1 && row[field].indexOf('-') !== 0) {
        // 如果负号不在最前面
        row[field] = row[field].replace(/-/g, ''); // 清除错误位置
        row[field] = '-' + row[field]; // 放回开头
      }
    },


    opInput(row, field) {
      if (!row[field]) return;

      // 转换全角数字为半角
      row[field] = row[field].replace(/[０-９]/g, (s) =>
        String.fromCharCode(s.charCodeAt(0) - 0xfee0)
      );

      // 删除字母
      row[field] = row[field].replace(/[A-Za-z]/g, "");

      // 允许负号、小数点，保留有效数字
      row[field] = row[field].replace(/[^\d\.]/g, "");

      // 限制小数点后最多三位
      const match = row[field].match(/^(-?\d+)(\.\d{0,3})?/);
      if (match) {
        row[field] = match[1] + (match[2] || "");
      }

      if (!row[field] || isNaN(Number(row[field]))) {
        row[field] = "0"; // 避免误清空
      }
    },

    /**
 * 根据 staff 列表计算总销售金额，并更新到 dataList2 中
 * 来源字段：baseSalesAmount
 */
    setEstimatedSalesAmount(staffList, force = false) {

      if (!Array.isArray(staffList)) return;

      if (this.hasEstimatedBeenCalculated && !force) {
        console.log('estimatedSalesAmount 已存在，跳过更新');
        return;
      }

      const total = staffList.reduce((sum, item) => {
        return sum + (item.baseSalesAmount ? Number(item.baseSalesAmount) : 0);
      }, 0);

      if (!this.dataList2[0]) this.$set(this.dataList2, 0, {});
      this.dataList2[0].estimatedSalesAmountRaw = total;
      this.dataList2[0].estimatedSalesAmount = total.toLocaleString();
      this.hasEstimatedBeenCalculated = true;
    },


    handleOperatingTimeInput(row) {
      this.removeCommas(row, 'operatingTime')       // 移除千位符（避免计算出错）
      this.opInput(row, 'operatingTime')        // 处理输入格式（半角数字、去字母）
      this.getOverTimeText(row)                     // 刷新超過時間字段
      this.calculateAmounts(row)                    // 👉 自动计算销售金额和调达金额
    },


    /**
     * 失去焦点时：给数字添加千位分隔符
     */
    formatWithCommas(row, field) {
      if (!row[field]) {
        row[field] = '0';
        return;
      }

      // 去除逗号并解析为数字
      const raw = row[field].toString().replace(/,/g, '');

      const num = Number(raw);

      // ❗ 最终判断是否为合法数值
      if (isNaN(num)) {
        row[field] = '0';
      } else {
        row[field] = num.toLocaleString(); // 加千分位
      }
      if (num === 0) {
        row[field] = '0';
      }

    },
    opformatWithCommas(row, field) {
      if (!row[field] || isNaN(Number(row[field]))) {
        return;
      }
      row[field] = Number(row[field]).toLocaleString(undefined, {
        minimumFractionDigits: 0,
        maximumFractionDigits: 3
      });
    },


    /**
     * 获得焦点时：去掉千位分隔符，恢复纯数字
     */
    removeCommas(row, field) {
      if (!row[field]) {
        return;
      }
      row[field] = row[field].toString().replace(/,/g, ""); // 去掉千位分隔符，保持原始数值
    },
    updateResidualAmountRaw(row) {
      const parseNumber = (value) => {
        if (!value) return 0;
        return Number(value.toString().replace(/,/g, '')) || 0;
      };

      const salesAmount = parseNumber(this.dataList2[0].salesAmount);
      const actuarialAmount = parseNumber(row.theMonthActuarialAmount);
      const previousBalance = parseNumber(row.previousMonthActuarialBalance);
      const billingAmount = parseNumber(row.currentMonthBillingAmount);

      return salesAmount + actuarialAmount + previousBalance - billingAmount;
    },

    //自动响应当月残清算的值
    updateResidualAmount(row) {
      // 处理带有千位分隔符的字符串，确保转换为数字
      const parseNumber = (value) => {
        if (!value) return 0;
        return Number(value.toString().replace(/,/g, '')) || 0;
      };

      // 计算值
      const salesAmount = parseNumber(this.dataList2[0].salesAmount);
      const actuarialAmount = parseNumber(row.theMonthActuarialAmount);
      const previousBalance = parseNumber(row.previousMonthActuarialBalance);
      const billingAmount = parseNumber(row.currentMonthBillingAmount);

      const residualAmount = salesAmount + actuarialAmount + previousBalance - billingAmount;
      this.dataList2[0].theMonthResidualAmount = Number(residualAmount).toLocaleString();

      return (residualAmount).toLocaleString(); // 计算后再格式化
    },

    // 超过时间处理
    getOverTimeText(row) {
      const lowerLimit = this.settlementLowerLimit;
      const upperLimit = this.settlementUpperLimit;
      const operatingTime = parseFloat(row.operatingTime);
      let overTime = null;

      if (!operatingTime) {
        return row.overTime ?? '';
      }

      if (operatingTime >= lowerLimit && operatingTime <= upperLimit) {
        row.overTime = 0;
        return 0;
      } else if (operatingTime < lowerLimit) {
        overTime = operatingTime - lowerLimit;
      } else if (operatingTime > upperLimit) {
        overTime = operatingTime - upperLimit;
      }

      // ⭐ 统一保留三位小数，避免精度误差
      const fixed = parseFloat(overTime.toFixed(3));
      row.overTime = fixed;
      return fixed;
    },

    //获取序号
    indexMethod(index) {
      return index + 1
    },
    loadStaffList() {
      let that = this;
      let data = {
        projectId: that.projectId,
        month: that.month
      };
      that.$httpV2('/worktimeinput/queryAndUpdate', 'GET', data, true, function (resp) {
        let result = resp.result;
        that.dataList1 = result.map(item => {
          const rawSalesAmount = item.salesAmount ? Number(item.salesAmount) : 0;
          const rawAmountRaised = item.amountRaised ? Number(item.amountRaised) : 0;
          const rawbaseSalesAmount = item.baseSalesAmount ? Number(item.baseSalesAmount) : 0;
          const rawbaseAmountRaised = item.baseAmountRaised ? Number(item.baseAmountRaised) : 0;
          if (item.procurementCompanyName === "UCL") {
            return {
              ...item,
              baseSalesAmount: rawbaseSalesAmount,       //  添加基准販売金額
              baseAmountRaised: null,     //  添加基准調達金額
              amountRaised: "-9,999", // BigDecimal类型字段设置为-9999
              amountRaisedDisabled: true,
              _displayAmountRaised: "-", // 在界面上显示为"-"
              salesAmount: rawSalesAmount.toLocaleString(),
              totalSalesAmount: item.totalSalesAmount ? Number(item.totalSalesAmount).toLocaleString() : "0",
              totalOperatingTime: item.totalOperatingTime ? item.totalOperatingTime.toString() : "0",
              originalOperatingTime: item.operatingTime ? item.operatingTime.toString() : "0",
              operatingTime: item.operatingTime ? item.operatingTime.toString() : "0",

              overTime: item.overTime ? item.overTime.toString() : "0"
            };
          } else {
            return {
              ...item,
              baseSalesAmount: rawbaseSalesAmount,       //  添加基准販売金額
              baseAmountRaised: rawbaseAmountRaised,     //  添加基准調達金額
              salesAmount: rawSalesAmount.toLocaleString(),
              amountRaised: rawAmountRaised.toLocaleString(),
              totalSalesAmount: item.totalSalesAmount ? Number(item.totalSalesAmount).toLocaleString() : "0",
              totalOperatingTime: item.totalOperatingTime ? item.totalOperatingTime.toString() : "0",
              operatingTime: item.operatingTime ? item.operatingTime.toString() : "0",
              overTime: item.overTime ? item.overTime.toString() : "0",
              originalOperatingTime: item.operatingTime ? item.operatingTime.toString() : "0",
              operatingTime: item.operatingTime ? item.operatingTime.toString() : "0",

            };
          }
        });
        that.setEstimatedSalesAmount(that.dataList1, true);


      });

      //获取上下限
      that.$httpV2('/worktimeinput/getSettlementUpperAndLowerLimit', 'GET', { projectId: that.projectId }, true, function (res) {
        that.settlementLowerLimit = res.result.settlementLowerLimit;
        that.settlementUpperLimit = res.result.settlementUpperLimit;
        console.log(that.settlementLowerLimit, that.settlementUpperLimit);
      });

    },

    loadClearList() {
      let that = this;
      let data = { projectId: that.projectId, month: that.month };

      that.$httpV2('/worktimeinput/getBalanceByProjectAndMonth', 'GET', data, true, function (resp) {
        let result = resp.result || {};

        // 确保 dataList2[0] 存在
        if (!that.dataList2[0]) that.$set(that.dataList2, 0, {});

        // 用 Object.assign 更新字段，避免覆盖 estimatedSalesAmount
        Object.assign(that.dataList2[0], {
          salesAmount: result.salesAmount ? Number(result.salesAmount).toLocaleString() : "0",
          theMonthActuarialAmount: result.theMonthActuarialAmount ? Number(result.theMonthActuarialAmount).toLocaleString() : "0",
          previousMonthActuarialBalance: result.previousMonthActuarialBalance ? Number(result.previousMonthActuarialBalance).toLocaleString() : "0",
          totalAmountRaised: result.totalAmountRaised ? Number(result.totalAmountRaised).toLocaleString() : "0",
          currentMonthBillingAmount: result.currentMonthBillingAmount ? Number(result.currentMonthBillingAmount).toLocaleString() : "0",
          theMonthResidualAmount: result.theMonthResidualAmount ? Number(result.theMonthResidualAmount).toLocaleString() : "0"
        });

        // 🔄 最后确保 estimatedSalesAmount 是最新的
        that.setEstimatedSalesAmount(that.dataList1, true);
      });
    },

    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];

      let salesAmountSum = 0;  // 存储第5列（索引4）的合计值
      let totalAmountRaisedSum = 0; // 存储第6列（索引5）的合计值

      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '要員小計';
          return;
        }

        if (index === 1 || index === 2) {
          sums[index] = '';
          return;
        }

        // 第4, 5, 6, 7列求和
        if (index >= 3 && index <= 6) {
          const values = data.map(item => {
            const value = item[column.property];

            // 如果值为-9,999，不参与统计
            if (item[columns[2].property] === 'UCL' && value === "-9,999") {
              return 0;  // 跳过值为-9,999的行
            }

            // 如果没有值，则跳过
            if (!value) return 0;

            // 转换成数字，去掉逗号等非数字字符
            const num = Number(value.toString().replace(/[^0-9.-]+/g, ''));
            return isNaN(num) ? 0 : num;
          });

          const sum = values.reduce((prev, curr) => prev + curr, 0);
          sums[index] = sum.toLocaleString(); // 确保不使用 `toLocaleString()`
          //将贩卖金额与调达金额的合计值与另一个表中的贩卖金额，调达合计的值对应
          // 存储第6列（index=5）和第7列（index=6）的合计值
          if (index === 5) salesAmountSum = sum;
          if (index === 6) totalAmountRaisedSum = sum;
        } else {
          sums[index] = '';
        }
      });

      // 更新 dataList2 中的 salesAmount 和 totalAmountRaised
      this.dataList2[0].salesAmount = Number(salesAmountSum).toLocaleString();
      this.dataList2[0].totalAmountRaised = Number(totalAmountRaisedSum).toLocaleString();

      return sums;
    },

    // 入力確定按钮，禁用编辑功能
    confirmInput() {
      let that = this;
      that.isEditable = !that.isEditable;  // 切换编辑状态

      that.saveData();

      let projectId = that.projectId;
      let month = that.month;
      let status = that.isEditable ? 1 : 2;  // 👉 编辑中为1，確定为2

      // 拼接带 status 的 URL
      const url = `/worktimeinput/updateConfirmInput?projectId=${encodeURIComponent(projectId)}&month=${encodeURIComponent(month)}&status=${status}`;

      // 发送 GET 请求
      that.$httpV2(url, 'GET', null, true, (response) => {
        if (response.code === 200) {
          that.$message.success(status === 2 ? '入力確定しました' : '編集状態に切り替えました');
        } else {
          that.$message.error('保存に失敗しました');
        }
      });

      that.buttonText = that.isEditable ? '入力確定' : '編集する';  // 更新按钮文本
    },


    getProjectId() {
      return this.projectId;
    },
    // 保存按钮，执行保存操作
    saveData() {
      if (!this.month) {
        this.$message.warning('日付を選択してください');
        return;
      }
      if (!this.projectId) {
        this.$message.warning('プロジェクト ID を空にすることはできません');
        return;
      }
      this.saveData1();
      this.saveData2();
      this.setEstimatedSalesAmount(this.dataList1, true);

      // ⬇️ 在保存后调用 updateConfirmInput 接口，并传 status = 1
      const url = `/worktimeinput/updateConfirmInput?projectId=${encodeURIComponent(this.projectId)}&month=${encodeURIComponent(this.month)}&status=1`;

      this.$httpV2(url, 'GET', null, true, (response) => {
        if (response.code === 200) {
          this.$message.success('保存成功しました');
        } else {
          this.$message.error('保存失敗しました');
        }
      });
    },


    saveData1() {
      let that = this;
      let sanitizedDataList1 = that.dataList1.map(item => ({
        ...item,
        salesAmount: item.salesAmount ? item.salesAmount.replace(/,/g, '') : "0",
        amountRaised: item.amountRaised ? item.amountRaised.replace(/,/g, '') : "0",
        totalSalesAmount: item.totalSalesAmount ? item.totalSalesAmount.replace(/,/g, '') : "0",
        totalOperatingTime: item.totalOperatingTime ? item.totalOperatingTime.replace(/,/g, '') : "0",
        operatingTime: item.operatingTime ? item.operatingTime.replace(/,/g, '') : "0",
        overTime: item.overTime
        // ? item.overTime.replace(/,/g, '') : "0"
      }));

      console.log("提交数据:", JSON.stringify(sanitizedDataList1));

      this.$httpV2('/worktimeinput/update', 'POST', { data: sanitizedDataList1 }, true, function (response) {
        if (response.code === 200) {
          that.loadStaffList(); // 重新加载数据
        } else {
          that.$message.error(response.msg);
        }
      });
    },

    saveData2() {
      let that = this;
      if (!that.dataList2 || that.dataList2.length === 0) {
        that.$message.warning('dataList2 が空なので保存できません');
        return;
      }

      this.$httpV2('/worktimeinput/balanceUpdate', 'POST', {
        projectId: that.projectId,
        month: that.month,
        estimatedSalesAmount: that.dataList2[0]?.estimatedSalesAmountRaw || '0',
        theMonthActuarialAmount: that.dataList2[0]?.theMonthActuarialAmount.replace(/,/g, ''), // 去掉 `,`
        currentMonthBillingAmount: that.dataList2[0] ? String(that.dataList2[0].currentMonthBillingAmount || '').replace(/,/g, '') : '',// 去掉 `,`
        theMonthResidualAmount: that.dataList2[0]?.theMonthResidualAmount.replace(/,/g, ''),
        salesAmount: that.dataList2[0]?.salesAmount.replace(/,/g, ''),
        totalAmountRaised: that.dataList2[0]?.totalAmountRaised.replace(/,/g, ''),
      }, true, function (response) {
        if (response.code === 200) {
          that.loadClearList(); // 重新加载数据
        } else {
          that.$message.error(response.msg);
        }
      });
    },
    //
    goBack() {
      this.$router.push({ name: 'Sale' });
    },
    calculateAmounts(row) {
      const parseFloatSafe = (val) => parseFloat(val?.toString().replace(/,/g, '')) || 0;

      const inputHours = parseFloatSafe(row.operatingTime); // 用户输入的稼働時間
      // debugger;

      // ---------- 参数准备 --------
      const salesLower = parseFloatSafe(row.settlementLowerLimit);
      const salesUpper = parseFloatSafe(row.settlementUpperLimit);
      const salesAddUnit = parseFloatSafe(row.salesIncrementUnitPriceHour);
      const salesSubUnit = parseFloatSafe(row.salesDecrementUnitPriceHour);
      const baseSalesAmount = parseFloatSafe(row.baseSalesAmount);

      const procurementLower = parseFloatSafe(row.lowerLimit);
      const procurementUpper = parseFloatSafe(row.upperLimit);
      const procurementAddUnit = parseFloatSafe(row.procurementIncrementUnitPriceHour);
      const procurementSubUnit = parseFloatSafe(row.procurementDecrementUnitPriceHour);
      const baseAmountRaised = parseFloatSafe(row.baseAmountRaised);

      // ---------- 销售金额计算 ----------
      let salesAdjustment = 0;
      if (inputHours > salesUpper) {
        salesAdjustment = (inputHours - salesUpper) * salesAddUnit;
      } else if (inputHours < salesLower) {
        salesAdjustment = (inputHours - salesLower) * salesSubUnit;
      }
      const finalSalesAmount = baseSalesAmount + salesAdjustment;

      // ---------- 調達金额计算 ----------
      let raisedAdjustment = 0;
      if (inputHours > procurementUpper) {
        raisedAdjustment = (inputHours - procurementUpper) * procurementAddUnit;
      } else if (inputHours < procurementLower) {
        raisedAdjustment = (inputHours - procurementLower) * procurementSubUnit;
      }
      const finalAmountRaised = baseAmountRaised + raisedAdjustment;

      // ---------- 写入结果 ----------
      row.salesAmount = Math.round(finalSalesAmount).toLocaleString();

      if (row.procurementCompanyName !== 'UCL') {
        row.amountRaised = Math.round(finalAmountRaised).toLocaleString();
      }

      // ✅ 自动刷新 summary（更新清算表）
      if (this.$refs.staffTable?.columns) {
        this.getSummaries({
          columns: this.$refs.staffTable.columns,
          data: this.dataList1
        });
      }
    },




    // 添加营业确定方法
    confirmBusiness() {
      if (this.isSubmittingBusiness) return;
      this.isSubmittingBusiness = true;

      const data = {
        projectId: this.projectId,
        month: this.month
      };

      this.$httpV2('/worktimeinput/updateStatus', 'GET', data, true, (response) => {
        this.isSubmittingBusiness = false;

        if (response.code === 200) {
          this.businessConfirmation = 1;
          this.isEditable = false;
          this.$message.success('営業確定しました');

          // ✅ 更新 URL 参数，保证刷新后也禁用
          this.$router.replace({
            path: this.$route.path,
            query: {
              ...this.$route.query,
              businessConfirmation: 1
            }
          });
        } else {
          this.$message.error(response.msg || '営業確定が失敗しました');
        }
      }, () => {
        this.isSubmittingBusiness = false;
      });
    },



    // 添加统一加载数据的方法
    loadAllData() {
      this.loadStaffList();
      this.loadClearList();
      setTimeout(() => {
        this.setEstimatedSalesAmount(this.dataList1, true);
        if (this.businessConfirmation == 1 || this.businessConfirmation === '1') {
          this.isEditable = false;
        }
      }, 200); // 略延迟，确保 dataList1 被填
    },

  },
  created() {
    let that = this;
    if (!this.projectId || !this.month) {
      return;
    }
  },

  mounted() {
    console.log(this.$route.query.projectNameAbbreviation);
    console.log(this.$route.query.companyAbbreviation);
    console.log(this.$route.query.departmentAbbreviation);
    console.log(this.$route.query.principalName);
    if (!this.projectId) {
      this.$message.warning('プロジェクトIDが見つかりません');
      return;
    }
    console.log('[mounted] businessConfirmation:', this.businessConfirmation);
    if (this.businessConfirmation == 1 || this.businessConfirmation === '1') {
      this.isEditable = false;
    }
    if (this.tradingStatus == 2 && this.businessConfirmation == 0) {
      this.isEditable = false;
      this.buttonText = '編集する';
    } else {
      this.isEditable = true;
      this.buttonText = '入力確定';
    }

    if (!this.month) {
      this.$message.warning('対象年月が見つかりません');
      return;
    }
    let that = this;
    that.loadAllData();
    // mounted中不再主动加载数据，等待created中的doData完成后再加载

  },

  watch: {
    month(newVal) {
      if (newVal) {
        this.loadStaffList();
        this.loadClearList();
      }
    },
  },
}
</script>

<style scoped>
.month-display {
  margin-bottom: 20px;
  padding: 10px;
}

.negative-value {
  color: red !important;
  font-weight: bold;
}
</style>