<template>
  <el-dialog
    :title="$t('Check Talent Project Information')"
    :close-on-click-modal="false"
    v-model="visible"
    width="650px"
  >
    <el-scrollbar height="500px">
      <el-form :model="projectInfoList" label-width="80px">
        <h2 v-if="projectInfoList.length == 0">
          {{ $t("There is currently no project information") }}
        </h2>
        <div v-else>
          <el-divider></el-divider>
          <div
            v-for="oneProjectInfo in projectInfoList"
            :key="oneProjectInfo.projectId"
          >
            <table class="content" style="width: 500px">
              <!-- 第一行 プロジェクト名称 -->
              <tr>
                <th style="width: 200px">{{ $t("Project Name") }}</th>
                <td colspan="3">{{ oneProjectInfo.name }}</td>
              </tr>

              <!-- 第二行 任用起止时间 -->
              <tr>
                <th>{{ $t("Appointment Begin And End Date") }}</th>
                <td colspan="3">
                  {{ oneProjectInfo.appointStartTime }} ~
                  {{ oneProjectInfo.appointEndTime }}
                </td>
              </tr>
              <!-- 第三四五六七行  技术评价、沟通能力、态度评价、勤务评价、leader评价-->
              <tr>
                <th>{{ $t("Technical Evaluation") }}</th>
                <td colspan="3">{{ oneProjectInfo.technicalEvaluation }}</td>
              </tr>
              <tr>
                <th>{{ $t("Communication Skills") }}</th>
                <td colspan="3">{{ oneProjectInfo.communicationSkills }}</td>
              </tr>
              <tr>
                <th>{{ $t("Attitude Evaluation") }}</th>
                <td colspan="3">{{ oneProjectInfo.attitudeEvaluation }}</td>
              </tr>
              <tr>
                <th>{{ $t("Diligence Evaluation") }}</th>
                <td colspan="3">{{ oneProjectInfo.diligenceEvaluation }}</td>
              </tr>
              <tr>
                <th>{{ $t("Leader Evaluation") }}</th>
                <td colspan="3">{{ oneProjectInfo.leaderEvaluation }}</td>
              </tr>
              <tr>
                <th>{{ $t("remark") }}</th>
                <td colspan="3">{{ oneProjectInfo.remark }}</td>
              </tr>
              <tr>
                <!-- 操作 -->
                <th>{{ $t("operate") }}</th>
                <td colspan="3">
                  <el-button type="text" @click="updateHandle(oneProjectInfo)">
                    {{ $t("Click to update") }}
                  </el-button>
                  <el-button
                    type="text"
                    @click="deleteHandle(oneProjectInfo.projectId)"
                  >
                    {{ $t("Click to delete") }}
                  </el-button>
                </td>
              </tr>
            </table>
            <el-divider></el-divider>
          </div>
        </div>
      </el-form>
    </el-scrollbar>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="AddHandle()" type="primary">{{
          $t("Add")
        }}</el-button>
        <el-button @click="visible = false">{{ $t("Close") }}</el-button>
      </span>
    </template>
  </el-dialog>
  <add-or-update
    ref="addOrUpdate"
    @addOrUpdateCallback="loadProjectInfoList"
  ></add-or-update>
</template>

<script>
import AddOrUpdate from "./talent-project-info-add-or-update.vue";
export default {
  components: { AddOrUpdate },
  data() {
    return {
      talentId: "",
      projectInfoList: [],
      visible: false, //整个对话框
    };
  },
  watch: {
    visible(val) {
      let that = this;
      if (!val) {
        // visible为false
        that.$emit("refreshDataList");
      }
    },
  },
  methods: {
    init: function (talentId, projectInfoList) {
      let that = this;
      that.talentId = talentId;
      that.projectInfoList = projectInfoList;
      that.format();
      that.visible = true;
    },
    format: function () {
      let that = this;
      that.projectInfoList.forEach((one) => {
        let allEvalution = one.appointEvalution;
        if (allEvalution != null && allEvalution != "") {
          let evalutionArr = allEvalution.split("-&&-");
          one.technicalEvaluation =
            evalutionArr[0] == undefined ? "" : evalutionArr[0];
          one.communicationSkills =
            evalutionArr[1] == undefined ? "" : evalutionArr[1];
          one.attitudeEvaluation =
            evalutionArr[2] == undefined ? "" : evalutionArr[2];
          one.diligenceEvaluation =
            evalutionArr[3] == undefined ? "" : evalutionArr[3];
          one.leaderEvaluation =
            evalutionArr[4] == undefined ? "" : evalutionArr[4];
          one.remark = evalutionArr[5] == undefined ? "" : evalutionArr[5];
        }
        if (one.leaderEvaluation == "" || one.leaderEvaluation == null) {
          one.isLeader = false;
        } else {
          one.isLeader = true;
        }
      });
    },
    loadProjectInfoList: function () {
      let that = this;
      that.$http(
        "/talent/searchInfoByTalentId",
        "POST",
        { talentId: that.talentId },
        false,
        function (resp) {
          that.projectInfoList = resp.result.projectInfoList;
          that.format();
        }
      );
    },
    // 新增プロジェクト信息
    AddHandle: function () {
      let that = this;
      that.$nextTick(() => {
        that.$refs.addOrUpdate.init(that.talentId);
      });
    },
    // 修改プロジェクト信息
    updateHandle: function (dataForm) {
      let that = this;
      that.$nextTick(() => {
        that.$refs.addOrUpdate.init(that.talentId, dataForm);
      });
    },
    // 删除プロジェクト信息
    deleteHandle: function (id) {
      let that = this;
      that
        .$confirm(that.$t("Delete this project information?"), that.$t("tip"), {
          confirmButtonText: that.$t("Confirm"),
          cancelButtonText: that.$t("Cancel"),
          type: that.$t("warning"),
        })
        .then(() => {
          that.$http(
            "/projectInfo",
            "DELETE",
            {
              projectIds: [id],
            },
            true,
            function (resp) {
              ElMessage({
                message: that.$t("Successful operation"),
                type: "success",
                duration: 1200,
              });
              that.loadProjectInfoList();
            }
          );
        });
    },
  },
};
</script>

<style lang="less" scoped="scoped">
@import url(talent.less);
</style>
