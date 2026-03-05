<template>
  <el-dialog
    :title="dataForm.projectId != '' ? $t('Modify') : $t('Add')"
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
      <!-- プロジェクト名称 -->
      <el-form-item :label="$t('Project Name')" prop="name">
        <el-input v-model="dataForm.name" clearable />
      </el-form-item>
      <!-- 任用开始日期 -->
      <el-form-item
        :label="$t('Appointment Begin Date')"
        prop="beginDate"
      >
       <el-date-picker
          v-model="dataForm.startDate"
          type="date"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          style="width: 100%"
        />
      </el-form-item>
      <!-- 任用结束日期 -->
       <el-form-item
        :label="$t('Appointment End Date')"
        prop="endDate"
      >
       <el-date-picker
          v-model="dataForm.endDate"
          type="date"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          style="width: 100%"
        />
      </el-form-item>
      <!-- 技术评价 -->
      <el-form-item
        :label="$t('Technical Evaluation')"
        prop="technicalEvaluation"
      >
        <el-input
          v-model="dataForm.technicalEvaluation"
          type="textarea"
          :rows="3"
          style="width: 100%"
          maxlength="350"
          show-word-limit
          clearable
        />
      </el-form-item>
      <!-- 沟通能力 -->
      <el-form-item
        :label="$t('Communication Skills')"
        prop="communicationSkills"
      >
        <el-input
          v-model="dataForm.communicationSkills"
          type="textarea"
          :rows="3"
          style="width: 100%"
          maxlength="350"
          show-word-limit
          clearable
      /></el-form-item>
      <!-- 态度评价 -->
      <el-form-item
        :label="$t('Attitude Evaluation')"
        prop="attitudeEvaluation"
      >
        <el-input
          v-model="dataForm.attitudeEvaluation"
          type="textarea"
          :rows="3"
          style="width: 100%"
          maxlength="350"
          show-word-limit
          clearable
        />
      </el-form-item>
      <!-- 勤务评价 -->
      <el-form-item
        :label="$t('Diligence Evaluation')"
        prop="diligenceEvaluation"
      >
        <el-input
          v-model="dataForm.diligenceEvaluation"
          type="textarea"
          :rows="3"
          style="width: 100%"
          maxlength="350"
          show-word-limit
          clearable
        />
      </el-form-item>
      <!-- 是否是leader -->
      <el-form-item :label="$t('Is Leader')" prop="idLeader">
        <el-radio-group v-model="dataForm.isLeader">
          <el-radio :label="true">{{ $t("yes") }}</el-radio>
          <el-radio :label="false">{{ $t("no") }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- Leader评价 -->
      <el-form-item
        v-show="dataForm.isLeader"
        :label="$t('Leader Evaluation')"
        prop="leaderEvaluation"
      >
        <el-input
          v-model="dataForm.leaderEvaluation"
          type="textarea"
          :rows="3"
          style="width: 100%"
          maxlength="350"
          show-word-limit
          clearable
        />
      </el-form-item>
      <!-- 備考 -->
      <el-form-item :label="$t('remark')" prop="remark">
        <el-input
          v-model="dataForm.remark"
          type="textarea"
          :rows="3"
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
        <el-button type="primary" @click="dataFormSubmit()">{{
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
      visible: false,
      talentId: "",
      dataForm: {
        projectId: "",
        name: "",
        technicalEvaluation: "",
        communicationSkills: "",
        attitudeEvaluation: "",
        diligenceEvaluation: "",
        isLeader: "",
        leaderEvaluation: "",
        remark: "",
        startDate: '',
        endDate: '',
      },
      dataRule: {
        name: {
          required: true,
          message: this.$t("Project name cannot be empty"),
        },
      },
    };
  },
  methods: {
    init: function (talentId, dataForm) {
      let that = this;
      that.reset();
      that.talentId = talentId;
      if (dataForm != null) {
        that.dataForm.projectId = dataForm.projectId;
        that.dataForm.name = dataForm.name;
        that.dataForm.startDate = dataForm.appointStartTime;
        that.dataForm.endDate = dataForm.appointEndTime;
        that.dataForm.technicalEvaluation = dataForm.technicalEvaluation;
        that.dataForm.communicationSkills = dataForm.communicationSkills;
        that.dataForm.attitudeEvaluation = dataForm.attitudeEvaluation;
        that.dataForm.diligenceEvaluation = dataForm.diligenceEvaluation;
        that.dataForm.isLeader = dataForm.isLeader;
        that.dataForm.leaderEvaluation = dataForm.leaderEvaluation;
        that.dataForm.remark = dataForm.remark;
      }
      that.visible = true;
    },
    reset: function () {
      let that = this;
      that.talentId = "";
      that.dataForm = {
        startDate: '',
        endDate: '',
        projectId: "",
        name: "",
        technicalEvaluation: "",
        communicationSkills: "",
        attitudeEvaluation: "",
        diligenceEvaluation: "",
        isLeader: "",
        leaderEvaluation: "",
        remark: "",
      };
    },
    dataFormSubmit: function () {
      let that = this;
      const allEvalution = `${that.dataForm.technicalEvaluation}-&&-${that.dataForm.communicationSkills}-&&-${that.dataForm.attitudeEvaluation}-&&-${that.dataForm.diligenceEvaluation}-&&-${that.dataForm.leaderEvaluation}-&&-${that.dataForm.remark}`;
      let data = {
        talentId: that.talentId,
        projectId: that.dataForm.projectId,
        name: that.dataForm.name,
        appointStartTime: that.dataForm.startDate == null ? '':that.dataForm.startDate,
        appointEndTime: that.dataForm.endDate == null ? '':that.dataForm.endDate,
        appointEvalution: allEvalution,
        remark: that.dataForm.remark,
      };
      that.$refs["dataForm"].validate(function (valid) {
        if (valid) {
          let method = that.dataForm.projectId == "" ? "POST" : "PUT";
          that.$http("/projectInfo", method, data, true, function (resp) {
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
