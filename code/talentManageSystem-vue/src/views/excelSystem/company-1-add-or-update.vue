<template>
  <el-dialog :title="dataForm.companyId == null || dataForm.companyId == '' ? '追加' : '変更'" :close-on-click-modal="false"
    v-model="visible" width="800px">
    <el-form :model="dataForm" ref="dataForm" :rules="dataRule">

      <!-- 1つの el-row の中に 左右2カラムを配置 -->
      <el-row :gutter="20">

        <!-- ▼ 左カラム :span="12" -->
        <el-col :span="12">
          <el-form-item label="企業名" prop="companyName">
            <el-input v-model="dataForm.companyName" placeholder="企業名を入力してください" clearable
              @input="limitLength('companyName', 30)" />
          </el-form-item>

          <el-form-item label="企業略称" prop="companyAbbreviation">
            <el-input v-model="dataForm.companyAbbreviation" placeholder="企業略称" clearable
              @input="limitLength('companyAbbreviation', 10)" />
          </el-form-item>

          <el-form-item label="本社郵便番号" prop="postalCode">
            <el-input v-model="dataForm.postalCode" placeholder="本社郵便番号" clearable @input="filterInput('postalCode')"
              @focus="removeCommas('postalCode')" style="ime-mode: disabled;" inputmode="numeric" />
          </el-form-item>

          <el-form-item label="本社都道府県" prop="companyCity">
            <el-input v-model="dataForm.companyCity" placeholder="本社都道府県" clearable />
          </el-form-item>

          <el-form-item label="本社所在地" prop="companyAddress">
            <el-input v-model="dataForm.companyAddress" placeholder="本社所在地" clearable />
          </el-form-item>

          <el-form-item label="本社電話番号" prop="companyPhonenumber">
            <el-input v-model="dataForm.companyPhonenumber" placeholder="本社電話番号" clearable
              @input="filterInput('companyPhonenumber')" @focus="removeCommas('companyPhonenumber')"
              style="ime-mode: disabled;" inputmode="numeric" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="AddrequestDate">
              請求先は本社と同じ
            </el-button>
          </el-form-item>

          <el-form-item label="請求先郵便番号" prop="billPostalcode">
            <el-input v-model="dataForm.billPostalcode" placeholder="請求先郵便番号" clearable
              @input="filterInput('billPostalcode')" @focus="removeCommas('billPostalcode')" style="ime-mode: disabled;"
              inputmode="numeric" />
          </el-form-item>

          <el-form-item label="請求先都道府県" prop="billCity">
            <el-input v-model="dataForm.billCity" placeholder="請求先都道府県" clearable />
          </el-form-item>

          <el-form-item label="請求先住所" prop="billAddress">
            <el-input v-model="dataForm.billAddress" placeholder="請求先住所" clearable />
          </el-form-item>
          <el-form-item label="請求先電話番号" prop="billPhonenumber">
            <el-input v-model="dataForm.billPhonenumber" placeholder="請求先電話番号" clearable
              @input="filterInput('billPhonenumber')" @focus="removeCommas('billPhonenumber')"
              style="ime-mode: disabled;" inputmode="numeric" />
          </el-form-item>
        </el-col>
        <!-- ▲ 左カラムここまで -->

        <!-- ▼ 右カラム :span="12" -->
        <el-col :span="12">


          <el-form-item label="代表者役職名" prop="representitivePosition">
            <el-input v-model="dataForm.representitivePosition" placeholder="代表者役職名" clearable />
          </el-form-item>

          <el-form-item label="代表者氏名" prop="representitiveName">
            <el-input v-model="dataForm.representitiveName" placeholder="代表者名" clearable />
          </el-form-item>

          <el-form-item label="インボイス番号" prop="invoiceCode">
            <el-input v-model="dataForm.invoiceCode" placeholder="インボイス番号" clearable @input="filterInput('invoiceCode')"
              @focus="removeCommas('invoiceCode')" style="ime-mode: disabled;" inputmode="numeric" />
          </el-form-item>

          <el-form-item label="支払条件" prop="paymentTerm">
            <el-select v-model="dataForm.paymentTerm" placeholder="支払条件" @change="changePaymentTerm">
              <el-option v-for="item in paymentTermOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>

          <el-form-item label="販売調達フラグ" prop="saleOrProcurement">
            <el-select v-model="dataForm.saleOrProcurement" placeholder="販売調達フラグ">
              <el-option v-for="item in saleOrProcurementOptions" :key="item.value" :label="item.label"
                :value="item.value" />
            </el-select>
          </el-form-item>

          <el-form-item label="契約開始年月日" prop="contractStartDate">
            <el-date-picker v-model="dataForm.contractStartDate" type="date" placeholder="契約開始年月日を選択してください"
              value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable />
          </el-form-item>

          <el-form-item label="契約終了年月日" prop="contractEndDate">
            <el-date-picker v-model="dataForm.contractEndDate" type="date" placeholder="契約終了年月日を選択してください"
              value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable style="width: 100%" />
          </el-form-item>

          <el-form-item label="状態" prop="activeFlg">
            <el-radio-group v-model="dataForm.activeFlg">
              <el-radio-button label="1">アクティブ</el-radio-button>
              <el-radio-button label="0" :disabled="dataForm.companyId == null || dataForm.companyId === ''">
                非アクティブ
              </el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="締日" prop="closingDate">
            <el-select v-model="dataForm.closingDate" placeholder="締日">
              <el-option v-for="item in closingDateOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>

          <el-form-item label="摘要" prop="comment">
            <el-input v-model="dataForm.comment" placeholder="摘要" clearable type="textarea" :rows="2" maxlength="2000"
              :show-word-limit="true" />
          </el-form-item>
        </el-col>
        <!-- ▲ 右カラムここまで -->

      </el-row>
    </el-form>

    <!-- ダイアログのフッターはそのままでもOKです -->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">キャンセル</el-button>
        <el-button type="primary" @click="dataFormSubmit">確定</el-button>
      </span>
    </template>
  </el-dialog>
</template>



<script>


export default {
  data: function () {
    return {
      isSubmitting: false, // 防止重复提交
      visible: false,
      dataForm: {
        companyId: null,
        companyName: null,
        companyAbbreviation: null,//略称
        postalCode: null,//企業郵便番号
        companyCity: null,
        companyAddress: null,
        companyPhonenumber: null,
        billPostalcode: null,//請求先郵編
        billCity: null,//請求先所在地
        billAddress: null,//請求先住所
        billPhonenumber: null,//請求先電話
        representitivePosition: null,//代表者職稱
        representitiveName: null,//代表者名
        invoiceCode: null,//インボイス番号　发票号
        paymentTerm: null,//支払条件
        closingDate: null,//締切日
        saleOrProcurement: null,//公司類型
        contractStartDate: null,//開始日
        contractEndDate: null,//終了日
        comment: null,//摘要
        activeFlg: "1",//1:アクティブ。null：非アクティブ。默认为1
      },




      dataRule: {
        companyName: [
          { required: true, message: '企業名を入力してください', },
        ],
        companyAbbreviation: [
          { required: true, message: '企業略称を入力してください', },
        ],
        postalCode: [
          { required: true, message: '本社郵便番号を入力してください' },
          { pattern: /^[0-9]{3}-[0-9]{4}$/, message: '郵便番号の形式は「XXX-XXXX」にしてください' }
        ],
        companyCity: [
          { required: true, message: '本社都道府県を入力してください', },
        ],
        companyAddress: [
          { required: true, message: '本社住所を入力してください', },
        ],
        billPostalcode: [
          { required: true, message: '請求先郵便番号を入力してください' },
          { pattern: /^[0-9]{3}-[0-9]{4}$/, message: '郵便番号の形式は「XXX-XXXX」にしてください' }
        ],
        billCity: [
          { required: true, message: '請求先都道府県を入力してください', },
        ],
        billAddress: [
          { required: true, message: '請求先住所を入力してください', },
        ],
        representitivePosition: [
          { required: true, message: '代表者役職名を入力してください', },
        ],
        representitiveName: [
          { required: true, message: '代表者名を入力してください', },
        ],
        paymentTerm: [
          { required: true, message: '支払条件を選択してください', },
        ],
        closingDate: [
          { required: true, message: '締切日を選択してください', },
        ],
        saleOrProcurement: [
          { required: true, message: '販売調達フラグを選択してください', },
        ],
        contractStartDate: [
          { required: true, message: '開始日を選択してください', },
        ],
        companyPhonenumber: [
          { required: true, message: '本社電話番号を入力してください' }
        ],
        billPhonenumber: [
          { required: true, message: '請求先電話番号を入力してください' }
        ],
      },
      paymentTermOptions: [
        { value: '翌月末', label: '翌月末' },
        { value: '翌々月末', label: '翌々月末' },
        { value: '翌月10日', label: '翌月10日' },
        { value: '翌月15日', label: '翌月15日' },
        { value: '翌月20日', label: '翌月20日' },
        { value: '翌月25日', label: '翌月25日' },
      ],
      closingDateOptions: [
        { value: '10日締め', label: '10日締め' },
        { value: '15日締め', label: '15日締め' },
        { value: '20日締め', label: '20日締め' },
        { value: '末日締め', label: '末日締め' },
      ],
      saleOrProcurementOptions: [
        { value: '1', label: '販売企業' },
        { value: '2', label: '調達企業' },
      ],
    }
  },
  watch: {
    "dataForm.activeFlg"(newValue) {
      if (newValue === "0") {
        this.checkCanDeactivate();
      }
    }
  },
  methods: {
    limitLength(field, maxLength) {
      if (!this.dataForm[field]) return;

      // 计算全角长度（全角2，半角1）
      const getZenkakuLength = (str) => {
        return [...str].reduce((len, ch) => {
          return len + (ch.charCodeAt(0) > 255 ? 2 : 1);
        }, 0);
      };

      let raw = this.dataForm[field];
      let result = "";
      let len = 0;

      for (const ch of raw) {
        let charLen = ch.charCodeAt(0) > 255 ? 2 : 1;
        if (len + charLen > maxLength * 2) break;
        result += ch;
        len += charLen;
      }

      this.dataForm[field] = result;
    },


    // 过滤输入：删除英文字母，转换全角数字为半角，仅允许数字和 `-`
    filterInput(field) {
      if (!this.dataForm[field]) return;

      let value = this.dataForm[field];

      // 1. 转换全角数字为半角
      value = value.replace(/[０-９]/g, (s) => String.fromCharCode(s.charCodeAt(0) - 0xfee0));

      // 2. 仅允许数字和 `-`
      value = value.replace(/[^0-9-]/g, "");

      // 更新数据
      this.dataForm[field] = value;
    },

    // 失去焦点时：自动添加千位逗号
    formatWithCommas(field) {
      if (this.dataForm[field] && !isNaN(this.dataForm[field])) {
        this.dataForm[field] = Number(this.dataForm[field]).toLocaleString();
      }
    },

    // 点击输入框时：移除千位逗号，方便用户编辑
    removeCommas(field) {
      this.dataForm[field] = this.dataForm[field]?.replace(/,/g, "") || "";
    },


    //打开弹窗，初始化页面数据
    init(companyInfo) {
      this.reset();
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (companyInfo && companyInfo.companyId) {
          // 编辑模式：从接口获取
          this.$httpV2(
            "/companyAndDepartment/getCompanyById",
            "GET",
            { companyId: companyInfo.companyId },
            true,
            (resp) => {
              if (resp.code === 200) {
                this.dataForm = resp.result;
                if (this.dataForm.saleOrProcurement == 1) {
                  this.dataForm.saleOrProcurement = "販売企業";
                } else if (this.dataForm.saleOrProcurement == 2) {
                  this.dataForm.saleOrProcurement = "調達企業";
                }
              }
            }
          );
        } else {
          // 追加模式：手动设为默认アクティブ状态
          this.dataForm.activeFlg = "1";
        }
      });
      this.visible = true;
    },
    reset: function () {
      let dataForm = {
        companyId: null,
        companyName: null,
        companyAbbreviation: null,
        postalCode: null,
        companyCity: null,
        companyAddress: null,
        companyPhonenumber: null,
        billPostalcode: null,//請求先郵編
        billCity: null,//請求先所在地
        billAddress: null,//請求先住所
        billPhonenumber: null,//請求先電話
        representitivePosition: null,//代表者職稱
        representitiveName: null,//代表者名
        invoiceCode: null,
        paymentTerm: null,
        saleOrProcurement: null,//公司類型
        contractStartDate: null,//開始日
        contractEndDate: null,//終了日
        closingDate: null,//締切日
        comment: null,
        activeFlg: "1", //1:アクティブ。0：非アクティブ。
      }
      this.dataForm = dataForm;
    },

    //触发事件，点击按钮将本社情报赋值给请求先
    AddrequestDate: function () {
      this.dataForm.billPostalcode = this.dataForm.postalCode;
      this.dataForm.billCity = this.dataForm.companyCity;
      this.dataForm.billAddress = this.dataForm.companyAddress;
      this.dataForm.billPhonenumber = this.dataForm.companyPhonenumber;
    },
    checkCanDeactivate() {
      const companyId = this.dataForm.companyId;
      if (!companyId) return;

      this.$httpV2(
        "/companyAndDepartment/is-empty",
        "GET",
        { companyId },
        false,
        (resp) => {
          console.log("接口响应数据：", resp);

          if (resp.result === false) {
            this.$alert(
              "案件がすべて終了していないので、非アクティブ化できません",
              "警告",
              {
                confirmButtonText: "OK",
                type: "warning"
              }
            );
            this.dataForm.activeFlg = "1"; // 还原为「アクティブ」
          }
        }
      );
    },

    //提交表单
    dataFormSubmit() {
      let that = this;

      // 防止重复提交
      if (that.isSubmitting) {
        return;
      }

      that.$refs["dataForm"].validate((valid) => {
        if (valid) {
          if (that.dataForm.saleOrProcurement == "販売企業") {
            that.dataForm.saleOrProcurement = 1;
          } else if (that.dataForm.saleOrProcurement == "調達企業") {
            that.dataForm.saleOrProcurement = 2;
          }
          let data = {
            companyId: that.dataForm.companyId,
            companyName: that.dataForm.companyName,
            companyAbbreviation: that.dataForm.companyAbbreviation,
            postalCode: that.dataForm.postalCode,
            companyCity: that.dataForm.companyCity,
            companyAddress: that.dataForm.companyAddress,
            companyPhonenumber: that.dataForm.companyPhonenumber,
            billPostalcode: that.dataForm.billPostalcode,
            billCity: that.dataForm.billCity,
            billAddress: that.dataForm.billAddress,
            billPhonenumber: that.dataForm.billPhonenumber,
            representitivePosition: that.dataForm.representitivePosition,
            representitiveName: that.dataForm.representitiveName,
            invoiceCode: that.dataForm.invoiceCode,
            paymentTerm: that.dataForm.paymentTerm,
            closingDate: that.dataForm.closingDate,
            saleOrProcurement: that.dataForm.saleOrProcurement,
            contractStartDate: that.dataForm.contractStartDate,
            contractEndDate: that.dataForm.contractEndDate,
            comment: that.dataForm.comment,
            activeFlg: that.dataForm.activeFlg,
          };

          console.log("提交的数据:", JSON.stringify(data, null, 2));

          // 先进行唯一性校验
          that.$httpV2("/companyAndDepartment/checkUnique", "post", data, false, function (resp) {
            if (resp.code !== 200) {
              // 如果校验失败，显示后端返回的错误信息
              ElMessage({ message: resp.msg || "唯一性校验失败", type: "error" });
              return; // 阻止提交
            }

            // 校验通过后，进行提交
            that.isSubmitting = true; // 标记为正在提交

            let method = that.dataForm.companyId == null || that.dataForm.companyId == "" ? "post" : "put";
            let url = method === "post" ? "/companyAndDepartment/insertCompany" : "/companyAndDepartment/updateCompany";

            that.$httpV2(url, method, data, false, function (resp) {
              if (resp.code === 200) {
                ElMessage({ message: "操作成功", type: "success" });
                that.visible = false;
                that.$emit("refreshDataList");
              } else {
                ElMessage({ message: "提交失败，请重试", type: "error" });
              }

              // 提交完成后，恢复 isSubmitting 状态
              that.isSubmitting = false;
            }, function () {
              // 失败回调，也需要恢复 isSubmitting 状态
              that.isSubmitting = false;
            });

          }, function () {
            // 唯一性校验失败的回调
            ElMessage({ message: "唯一性校验请求失败，请重试", type: "error" });
          });
        }
      });
    }

  }
}

</script>