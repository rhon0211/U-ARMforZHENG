<template>
  <el-dialog
    :title=" dataForm.projectId == '' || dataForm.projectId == null ? '新增' : '修改' "
    :close-on-click-modal="false"
    v-model="visible"
    style="width:600px"
  >
    <el-scrollbar>
      <el-form
        :model="dataForm"
        ref="dataForm"
        :rules="dataRule"
        label-width="200px"
      >
        <el-form-item
          label="顧客名："
          prop="customerName"
        >
          <el-select
            v-model="dataForm.customerName"
            placeholder="選んでお願いします"
            :disabled="isUpdated"
            clearable
            style="width: 250px;"
          >
            <el-option
              v-for="item in custList"
              :key="item.customerId"
              :label="item.customerName"
              :value="item.customerId"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="プロジェクト名："
          prop="projectName"
        >
          <el-input
            style="width: 250px;"
            v-model="dataForm.projectName"
            clearable
          />
        </el-form-item>
        <!-- 如果顾客不是アルファ，案件责任者是自己填的 -->
        <div v-if="dataForm.customerName != 1">
          <el-form-item
            label="案件責任者（顧客先）："
            prop="principal"
          >
            <el-input
              v-model="dataForm.principal"
              clearable
              style="width: 250px;"
            />
          </el-form-item>
          <el-form-item
            label="案件責任者所属（顧客先）："
            prop="principalCompany"
          >
            <el-input
              style="width: 250px;"
              v-model="dataForm.principalCompany"
              clearable
            />
          </el-form-item>
        </div>

        <!-- 如果顾客是アルファ，案件责任者就变成选择的了 -->
        <div v-else>
          <!-- 组织1 -->
          <el-form-item label="组织1">
            <el-select
              v-model="dataForm.org1Id"
              @change="loadOrg2()"
              style="width: 250px;"
            >
              <el-option
                v-for="oneOrg1 in org1List"
                :key="oneOrg1.organizationId"
                :value="oneOrg1.organizationId"
                :label="oneOrg1.organizationName"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <!-- 组织2 -->
          <el-form-item label="组织2">
            <el-select
              v-model="dataForm.org2Id"
              @change="loadOrg3()"
              style="width: 250px;"
            >
              <el-option
                v-for="oneOrg2 in org2List"
                :key="oneOrg2.organizationId"
                :value="oneOrg2.organizationId"
                :label="oneOrg2.organizationName"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <!-- 组织3 -->
          <el-form-item label="组织3">
            <el-select
              style="width: 250px;"
              v-model="dataForm.org3Id"
              @change="org3ChangeHandle()"
            >
              <el-option
                v-for="oneOrg3 in org3List"
                :key="oneOrg3.organizationId"
                :value="oneOrg3.organizationId"
                :label="oneOrg3.organizationName"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <!-- 所属 -->
          <el-form-item label="所属">
            <el-input
              style="width: 250px;"
              disabled
              v-model="belongOnShow"
            ></el-input>
          </el-form-item>
        </div>

        <el-form-item
          label="カテグリー"
          prop="category"
        >
          <el-input
            style="width: 250px;"
            v-model="dataForm.category"
            clearable
          />
        </el-form-item>
        <el-form-item
          label="UCL開発部担当者"
          prop="uclPrincipal"
        >
          <el-input
            style="width: 250px;"
            v-model="dataForm.uclPrincipal"
            clearable
          />
        </el-form-item>
        <el-form-item
          label="備考："
          prop="remark"
        >
          <el-input
            style="width: 250px;"
            type="textarea"
            rows="5"
            v-model="dataForm.remark"
            clearable
          />
        </el-form-item>
      </el-form>
    </el-scrollbar>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">キャンセル</el-button>
        <el-button
          type="primary"
          @click="dataFormSubmit"
        >確定</el-button>
      </span>
    </template>
  </el-dialog>

</template>

<script>
import { ElMessage } from "element-plus";

export default {
  data: function () {
    return {
      belongOnShow: "",
      visible: false,
      isUpdated: false,
      org1List: [],
      org2List: [],
      org3List: [],
      custList: [],
      dataForm: {
        customerName: null,
        projectId: null,
        projectName: null,
        principal: null,
        principalCompany: null,
        remark: null,
        org1Id: "",
        org2Id: "",
        org3Id: "",
      },

      dataRule: {
        customerName: [
          {
            required: true,
            message: "顧客名不能为空",
          },
        ],
        projectName: [
          {
            required: true,
            message: "プロジェクト名不能为空",
          },
        ],
      },
    };
  },
  computed: {},
  methods: {
    init(technicianInfo) {
      let that = this;
      that.reset();
      that.$nextTick(() => {
        that.$refs["dataForm"].resetFields();
        if (technicianInfo != null && technicianInfo != "") {
          that.dataForm = {
            ...JSON.parse(JSON.stringify(technicianInfo)),
          };
          that.isUpdated = true;
          console.log(that.dataForm);
        }
      });
      that.loadOrg1();
      that.visible = true;
    },
    loadOrg1(belong) {
      let that = this;
      that.belongOnShow = "";
      that.$httpV2(
        "/organizationOne/selectAll",
        "GET",
        {},
        false,
        function (resp) {
          that.org1List = resp.result || [];
        }
      );
      that.org2List = [];
      that.org3List = [];
    },
    loadOrg2() {
      let that = this;
      that.$httpV2(
        "/organizationOne/selectById",
        "GET",
        { organizationId: that.dataForm.org1Id },
        false,
        function (resp) {
          that.org2List = resp.result || [];
        }
      );
      that.dataForm.org2Id = "";
      that.dataForm.org3Id = "";
      that.belongOnShow =
        that.org1List.find((one) => one.organizationId == that.dataForm.org1Id)
          .belong + "-";
      that.org3List = [];
    },
    loadOrg3() {
      let that = this;
      that.$httpV2(
        "/organizationTwo/selectById",
        "GET",
        { organizationId: that.dataForm.org2Id },
        false,
        function (resp) {
          that.org3List = resp.result || [];
        }
      );
      that.dataForm.org3Id = "";
      that.belongOnShow = that.belongOnShow.match(/^.*?-/)[0];
      that.belongOnShow += that.org2List.find(
        (one) => one.organizationId == that.dataForm.org2Id
      ).belong;
      that.belongOnShow += "-";
    },
    org3ChangeHandle() {
      let that = this;
      that.belongOnShow += that.org3List.find(
        (one) => one.organizationId == that.dataForm.org3Id
      ).belong;
    },
    reset() {
      let dataForm = {
        customerName: null,
        projectId: null,
        projectName: null,
        principal: null,
        principalCompany: null,
        remark: null,
      };
      this.dataForm = dataForm;
      this.isUpdated = false;
    },
    dataFormSubmit: function () {
      let that = this;
      console.log(this.dataForm);
      that.$refs["dataForm"].validate(function (valid) {
        if (valid) {
          let data = {
            customerId: that.dataForm.customerName,
            projectId: that.dataForm.projectId,
            projectName: that.dataForm.projectName,
            principal: that.dataForm.customerName == 1 ? that.dataForm.org3Id : that.dataForm.principal, 
            principalCompany: that.dataForm.customerName == 1 ? that.belongOnShow : that.dataForm.principalCompany,
            category: that.dataForm.category,
            uclPrincipal: that.dataForm.uclPrincipal,
            remark: that.dataForm.remark,
          };
          let method = "";
          if (
            that.dataForm.projectId == "" ||
            that.dataForm.projectId == null
          ) {
            method = "POST";
          } else {
            method = "PUT";
            data.projectId = that.dataForm.projectId;
          }
          that.$httpV2("/project", method, data, false, function (resp) {
            ElMessage({
              message: "成功した操作",
              type: "success",
            });
            that.visible = false;
            that.$emit("refreshDataList");
          });
        }
      });
    },
    // 获取顧客全部信息
    loadCustList() {
      let that = this;
      that.$httpV2("/protech/customer", "GET", null, true, function (resp) {
        let result = resp.result;
        that.custList = result;
        // console.log('custList111: ', JSON.stringify(that.custList, null, 2));
      });
    },
  },
  created() {
    this.loadCustList();
  },
};
</script>