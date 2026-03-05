<template>
  <el-dialog :close-on-click-modal="false" v-model="visible" width="550px">
    <el-form label-width="80px">
      <el-form-item label="拉黑原因" prop="blacklistReason">
        <el-input
          v-model="reason"
          ref="blacklistReason"
          type="textarea"
          :rows="6"
          style="width: 100%"
          maxlength="350"
          show-word-limit
          clearable
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="conformBlacklist">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      talentId: "",
      reason: "",
    };
  },
  methods: {
    init: function (id) {
      let that = this;
      that.visible = true;
      that.talentId = id;
    },
    conformBlacklist: function () {
      let that = this;
      that.$http(
        "/talent/blacklist",
        "PUT",
        {
          talentId: that.talentId,
          blacklistReason: that.reason,
          status: 1,
        },
        true,
        function (resp) {
          ElMessage({
            message: that.$t("Successful operation"),
            type: "success",
          });
          that.visible = false;
          that.$emit("refreshDataList");
        }
      );
      that.visible = false;
    },
  },
};
</script>
