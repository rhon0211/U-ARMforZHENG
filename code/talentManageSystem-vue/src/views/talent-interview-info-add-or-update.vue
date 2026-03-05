<template>
  <el-dialog
    :title="dataForm.interId != '' ? $t('Modify') : $t('Add')"
    :close-on-click-modal="false"
    v-model="visible"
    width="550px"
    :destroy-on-close="true"
  >
    <el-form
      :model="dataForm"
      ref="dataForm"
      :rules="dataRule"
      label-width="120px"
    >
      <el-form-item
        :label="$t('Business interview time')"
        prop="busDate"
      >
        <el-date-picker
          v-model="dataForm.busDate"
          type="date"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item
        :label="$t('Business interviewer')"
        prop="busInterviewerId"
      >
        <el-select
          v-model="dataForm.busInterviewerId"
          style="width: 240px"
          clearable
        >
          <el-option
            v-for="one in busInterviewers"
            :key="one.interviewerId"
            :value="one.interviewerId"
            :label="one.name"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('Business interview evaluation')"
        prop="busEvaluation"
      >
        <el-input
          v-model="dataForm.busEvaluation"
          type="textarea"
          :rows="6"
          style="width: 100%"
          maxlength="350"
          show-word-limit
          clearable
        />
      </el-form-item>
      <el-form-item
        :label="$t('Technology interview time')"
        prop="techDate"
      >
        <el-date-picker
          v-model="dataForm.techDate"
          type="date"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item
        :label="$t('Technology interviewer')"
        prop="techInterviewerId"
      >
        <el-select
          v-model="dataForm.techInterviewerId"
          style="width: 240px"
          clearable
        >
          <el-option
            v-for="one in techInterviewers"
            :key="one.interviewerId"
            :value="one.interviewerId"
            :label="one.name"
          /> </el-select></el-form-item>
      <el-form-item
        :label="$t('Technology interview evaluation')"
        prop="techEvaluation"
      >
        <el-input
          v-model="dataForm.techEvaluation"
          type="textarea"
          :rows="6"
          style="width: 100%"
          maxlength="350"
          show-word-limit
          clearable
        />
      </el-form-item>
      <el-form-item
        :label="$t('Interview result')"
        prop="status"
      >
        <el-select v-model="dataForm.status">
          <el-option
            v-for="one in dicts.hire"
            :key="one.dictValue"
            :value="one.dictValue"
            :label="one.dictLabel"
          />
        </el-select>
      </el-form-item>

      <!-- 能传图片 -->
      <el-form-item :label="$t('picture')" prop="picture">
        <el-upload
          :action="action"
          :headers="{ token : token }"
          with-credentials="true"
          :on-success="updatePhotoSuccess"
          :on-error="updatePhotoError"
          :show-file-list="false"
        >
          <el-image
            style="width: 100px; height: 100px;"
            :src="dataForm.picture"
            :fit="fit"
          >
            <template #error>
              <div>
                <el-icon>
                  <Picture />
                </el-icon>
              </div>
            </template>
          </el-image>
        </el-upload>
      </el-form-item>

      <el-form-item
        :label="$t('remark')"
        prop="remark"
      >
        <el-input
          v-model="dataForm.remark"
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
        <el-button @click="visible = false">{{ $t("Cancel") }}</el-button>
        <el-button
          type="primary"
          @click="dataFormSubmit()"
        >{{
          $t("Confirm")
        }}</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      token: localStorage.getItem("token"),
      action: `${this.$baseUrl}/file/upload`,
      visible: false,
      talentId: "",
      dataForm: {
        interId: "",
        busDate: "",
        busInterviewerId: "",
        busEvaluation: "",
        techDate: "",
        techInterviewerId: "",
        techEvaluation: "",
        status: "",
        remark: "",
        picture: null,
      },
      dataRule: {
        status: {
          required: true,
          message: this.$t("Status required"),
        },
      },
      busInterviewers: [],
      techInterviewers: [],
      dicts: {
        hire: [],
      },
    };
  },
  methods: {
    updatePhotoSuccess: function (resp) {
      let that = this;
      that.dataForm.picture = resp.file.url;
    },
    init: function (talentId, dataForm) {
      let that = this;
      that.reset();
      that.loadInterviewers();
      that.loadDicts();
      that.talentId = talentId;
      if (dataForm != null) {
        that.dataForm.interId = dataForm.interId;
        that.dataForm.busDate = dataForm.busDate;
        that.dataForm.busInterviewerId = dataForm.busInterviewer;
        that.dataForm.busEvaluation = dataForm.busEvaluation;
        that.dataForm.techDate = dataForm.techDate;
        that.dataForm.techInterviewerId = dataForm.techInterviewer;
        that.dataForm.techEvaluation = dataForm.techEvaluation;
        that.dataForm.status = dataForm.status;
        that.dataForm.remark = dataForm.remark;
        that.dataForm.picture = dataForm.picture;
      }
      that.visible = true;
    },
    loadInterviewers: function () {
      let that = this;
      that.$http(
        "/interviewer/searchInterviewerByType",
        "POST",
        { type: "0" },
        false,
        function (resp) {
          that.busInterviewers = resp.result;
        }
      );
      that.$http(
        "/interviewer/searchInterviewerByType",
        "POST",
        { type: "1" },
        false,
        function (resp) {
          that.techInterviewers = resp.result;
        }
      );
    },
    reset: function () {
      let that = this;
      that.talentId = "";
      that.dataForm = {
        interId: "",
        busDate: "",
        busInterviewerId: "",
        busEvaluation: "",
        techDate: "",
        techInterviewerId: "",
        techEvaluation: "",
        status: "",
        remark: "",
      };
    },
    loadDicts: function () {
      let that = this;
      that.dicts.hire = that.$searchDict("dict_hire");
    },
    dataFormSubmit: function () {
      let that = this;
      let data = {
        interId: that.dataForm.interId,
        busDate: that.dataForm.busDate == null ? "" : that.dataForm.busDate,
        busInterviewerId:
          that.dataForm.busInterviewerId == null
            ? ""
            : that.dataForm.busInterviewerId,
        busEvaluation:
          that.dataForm.busEvaluation == null
            ? ""
            : that.dataForm.busEvaluation,
        techDate: that.dataForm.techDate == null ? "" : that.dataForm.techDate,
        techInterviewerId:
          that.dataForm.techInterviewerId == null
            ? ""
            : that.dataForm.techInterviewerId,
        techEvaluation:
          that.dataForm.techEvaluation == null
            ? ""
            : that.dataForm.techEvaluation,
        status: that.dataForm.status == null ? "" : that.dataForm.status,
        remark: that.dataForm.remark == null ? "" : that.dataForm.remark,
        talentId: that.talentId,
        picture: that.dataForm.picture,
      };
      that.$refs["dataForm"].validate(function (valid) {
        if (valid) {
          let method = that.dataForm.interId == "" ? "POST" : "PUT";
          that.$http("/interviewInfo", method, data, false, function (resp) {
            ElMessage({
              message: that.$t("Successful operation"),
              type: "success",
            });
            that.visible = false;
            that.$emit("addOrUpdateCallback");
          });
        }
      });
    },
  },
};
</script>