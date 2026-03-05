<template>
  <el-dialog
    :title="dataForm.id == '' || dataForm.id == null ? $t('add') : $t('update')"
    :close-on-click-modal="false"
    v-model="visible"
    width="480px"
  >
    <el-scrollbar height="150px">
      <el-form
        :model="dataForm"
        ref="dataForm"
        :rules="dataRule"
        label-width="100px"
      >
        <el-form-item :label="$t('name')" prop="name"
          ><el-input v-model="dataForm.name" clearable
        /></el-form-item>
        <el-form-item :label="$t('Interviewer Type')" prop="type">
          <el-select
            v-model="dataForm.type"
            :placeholder="$t('Interviewer Type')"
            size="medium"
            clearable="clearable"
          >
            <el-option
              v-for="dict in dicts.interviewerType"
              :label="dict.dictLabel"
              :key="dict.dictValue"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
      </el-form>
    </el-scrollbar>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">{{ $t("Cancel") }}</el-button>
        <el-button type="primary" @click="dataFormSubmit">{{
          $t("Confirm")
        }}</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
export default {
  data: function () {
    return {
      // 字典
      dicts: {
        interviewerType: [],
      },
      visible: false,
      dataForm: {
        id: "",
        name: "",
        type: "",
      },
      dataRule: {
        name: {
          required: true,
          message: this.$t("Name required"),
        },
        type: {
          required: true,
          message: this.$t("Can not be empty"),
        },
      },
    };
  },
  methods: {
    loadDict: function () {
      this.dicts.interviewerType = this.$searchDict("dict_interviewer_type");
    },
    reset: function () {
      let dataForm = {
        id: "",
        name: null,
        type: null,
      };
      this.dataForm = dataForm;
    },
    init: function (id) {
      let that = this;
      that.reset();
      that.loadDict();
      that.dataForm.id = id;
      that.visible = true;
      let data = {
        interviewerId: id,
      };
      that.$nextTick(() => {
        that.$refs["dataForm"].resetFields();
        if (that.dataForm.id) {
          that.$http(
            "/interviewer/searchById",
            "POST",
            data,
            false,
            function (resp) {
              that.dataForm.name = resp.result.name;
              that.dataForm.type = resp.result.type;
            }
          );
        }
      });
    },
    dataFormSubmit: function () {
      let that = this;
      let data = {
        name: that.dataForm.name,
        type: that.dataForm.type,
      };
      let method = "";
      if (that.dataForm.id) {
        method = "PUT";
        data.interviewerId = that.dataForm.id;
      } else {
        method = "POST";
      }
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          that.$http("/interviewer", method, data, false, function (resp) {
            ElMessage({
              message: that.$t("Successful operation"),
              type: "success",
            });
            that.visible = false;
            that.$emit("refreshDataList");
          });
        }
      });
    },
  },
};
</script>

<style lang="less" scoped="scoped"></style>