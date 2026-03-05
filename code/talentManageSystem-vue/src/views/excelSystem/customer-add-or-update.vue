<template>
  <el-dialog
      :title=" dataForm.customerId == '' || dataForm.customerId == null ? '新增' : '修改' "
      :close-on-click-modal="false"
      v-model="visible"
  >
    <el-scrollbar>
      <el-form
          :model="dataForm"
          ref="dataForm"
          :rules="dataRule"
      >
        <el-form-item label="顧客名：" prop="customerName">
          <el-input v-model="dataForm.customerName" clearable/>
        </el-form-item>
        <el-form-item label="備考：" prop="remark">
          <el-input type="textarea" rows="1" v-model="dataForm.remark" clearable/>
        </el-form-item>
      </el-form>
    </el-scrollbar>
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
      visible: false,

      dataForm: {
        customerId: null,
        customerName: null,
        remark: null,
      },

      dataRule: {
        customerName: [
          {
            required: true,
            message: "顧客名不能为空",
          },
        ],
      },
    }
  },
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
        }
      })
      that.visible = true;
    },
    reset() {
      let dataForm = {
        customerId: null,
        customerName: null,
        remark: null,
      };
      this.dataForm = dataForm;
    },
    dataFormSubmit: function () {
      let that = this;
      that.$refs["dataForm"].validate(function (valid) {
        if (valid) {
          let data = {
            customerId: that.dataForm.customerId,
            customerName: that.dataForm.customerName,
            remark: that.dataForm.remark,
          };
          console.log('data: ', JSON.stringify(data, null, 2));
          let method = "";
          if (that.dataForm.customerId == "" || that.dataForm.customerId == null) {
            method = "POST";
          } else {
            method = "PUT";
            data.customerId = that.dataForm.customerId;
          }
          that.$httpV2("/customer", method, data, false, function (resp) {
            ElMessage({
              message: "成功した操作",
              type: "success",
            });
            that.visible = false;
            that.$emit("refreshDataList");
          })
        }
      });
    },
  }
}
</script>