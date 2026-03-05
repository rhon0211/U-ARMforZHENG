<template>
  <el-dialog
    :title="!dataForm.userId ? $t('Add') : $t('Modify')"
    v-if="
      isAuth([
        '0',
        'supadm_create',
        'ordadm_create',
        'user_create',
        'supadm_update',
        'ordadm_update',
        'user_update',
      ])
    "
    :close-on-click-modal="false"
    v-model="visible"
    width="450px"
  >
    <el-form
      :model="dataForm"
      ref="dataForm"
      :rules="dataRule"
      label-width="150px"
    >
      <el-form-item :label="$t('Japanese name')" prop="name">
        <el-input v-model="dataForm.name" style="width: 100%" clearable />
      </el-form-item>

      <el-form-item :label="$t('Pseudonym')" prop="pseudonym">
        <el-input v-model="dataForm.pseudonym" style="width: 100%" clearable />
      </el-form-item>

      <el-form-item
        :label="$t('Account')"
        prop="account"
        v-if="isAuth(['0', 'supadm_create', 'supadm_update'])"
      >
        <el-input v-model="dataForm.account" style="width: 100%" clearable />
      </el-form-item>

      <el-form-item
        :label="$t('Password')"
        prop="password"
        v-if="isAuth(['0', 'supadm_create', 'supadm_update'])"
      >
        <el-input
          type="password"
          v-model="dataForm.password"
          style="width: 100%"
          clearable
        />
      </el-form-item>

      <el-form-item :label="$t('Email address')" prop="email">
        <el-input v-model="dataForm.email" style="width: 100%" clearable />
      </el-form-item>

      <el-form-item :label="$t('Telephone number')" prop="pseudonym">
        <el-input v-model="dataForm.phone" style="width: 100%" clearable />
      </el-form-item>

      <el-form-item :label="$t('User type')" prop="type">
        <el-select
          v-model="dataForm.type"
          class="input"
          :placeholder="$t('Select Label Type')"
          clearable="clearable"
        >
          <el-option
            v-for="one in typeList"
            :label="one.name"
            :value="one.id"
          />
        </el-select>
      </el-form-item>
    </el-form>
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
      visible: false,
      typeList: [],
      dataForm: {
        userId: null,
        name: null,
        pseudonym: null,
        account: null,
        password: null,
        email: null,
        phone: null,
        type: null,
      },
      dataRule: {
        name: [],
        email: [],
        type: [],
      },
    };
  },

  methods: {
    reset: function () {
      let dataForm = {
        userId: null,
        name: null,
        pseudonym: null,
        account: null,
        password: null,
        email: null,
        phone: null,
        type: null,
      };
      this.dataForm = dataForm;
    },
    loadTypeList: function () {
      let that = this;
      that.$http("/user/allUserType", "GET", null, true, function (resp) {
        let result = resp.result;
        that.typeList = result;
        console.log(result);
      });
    },
    init: function (userId) {
      let that = this;
      that.setFormRules();
      that.reset();
      that.dataForm.userId = userId || 0;
      that.visible = true;
      that.$nextTick(() => {
        that.$refs["dataForm"].resetFields();
        that.loadTypeList();
        if (userId) {
          that.$http(
            "/user/updateSearchById",
            "POST",
            { userId: userId },
            true,
            function (resp) {
              that.dataForm.userId = userId;
              that.dataForm.name = resp.result.name;
              that.dataForm.pseudonym = resp.result.pseudonym;
              that.dataForm.account = resp.result.account;
              that.dataForm.password = resp.result.password;
              that.dataForm.email = resp.result.email;
              that.dataForm.phone = resp.result.phone;
              that.dataForm.type = resp.result.type;
            }
          );
        }
      });
    },
    dataFormSubmit: function () {
      let that = this;
      this.$refs["dataForm"].validate((valid) => {
        // 根据条件设置 operation 变量的值
        const operation = !that.dataForm.userId ? "insert" : "update";
        // 根据 operation 的值设置 method 变量的值
        const method = operation === "insert" ? "POST" : "PUT";
        if (valid) {
          that.$http("/user", method, that.dataForm, true, function (resp) {
            ElMessage({
              message: "操作が正常に完了しました",
              type: "success",
            });
            that.visible = false;
            that.$emit("refreshDataList");
          });
        }
      });
    },
    setFormRules: function () {
      this.dataRule.name = [
        { required: true, message: this.$t("Can not be empty") },
      ];
      this.dataRule.email = [
        { required: true, message: this.$t("Can not be empty") },
      ];
      this.dataRule.type = [
        { required: true, message: this.$t("Can not be empty") },
      ];
    },
  },
};
</script>

<style lang="less" scoped="scoped"></style>
