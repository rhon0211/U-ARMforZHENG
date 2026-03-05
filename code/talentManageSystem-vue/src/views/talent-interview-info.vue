<template>
  <el-dialog
    :title="$t('Check Talent Interview Information')"
    :close-on-click-modal="false"
    v-model="visible"
    width="750px"
  >
    <el-scrollbar height="500px">
      <el-form
        :model="interviewInfoList"
        label-width="80px"
      >
        <h2 v-if="interviewInfoList.length == 0">
          {{ $t("There is currently no interview information") }}
        </h2>
        <div v-else>
          <el-divider></el-divider>
          <div
            v-for="oneInterviewInfo in interviewInfoList"
            :key="oneInterviewInfo.interId"
          >
            <table class="content">
              <!-- 第一行 业务面试时间 面试官 -->
              <tr>
                <th style="width: 120px">{{ $t("Business interview time") }}</th>
                <td style="width: 200px">{{ oneInterviewInfo.busDate }}</td>
                <th style="width: 120px">{{ $t("Business interviewer") }}</th>
                <td style="width: 200px">{{ oneInterviewInfo.busName }}</td>
              </tr>
              <!-- 第二行 业务面试评价 -->
              <tr>
                <th>{{ $t("Business interview evaluation") }}</th>

                <td colspan="3">{{ oneInterviewInfo.busEvaluation }}</td>
              </tr>
              <!-- 第三行 技术面试时间 面试官 -->
              <tr>
                <th>{{ $t("Technology interview time") }}</th>

                <td>{{ oneInterviewInfo.techDate }}</td>
                <th>{{ $t("Technology interviewer") }}</th>

                <td>{{ oneInterviewInfo.techName }}</td>
              </tr>
              <!-- 第四行 技术 面试评价 -->
              <tr>
                <th>{{ $t("Technology interview evaluation") }}</th>

                <td colspan="3">{{ oneInterviewInfo.techEvaluation }}</td>
              </tr>
              <!-- 第五行 備考 -->
              <tr>
                <th>{{ $t("remark") }}</th>

                <td colspan="3">{{ oneInterviewInfo.remark }}</td>
              </tr>
              <!-- 第六行 是否采用 -->
              <tr>
                <th>{{ $t("Interview result") }}</th>
                <td>
                  {{
                    dicts.hire.find(
                      (one) => one.dictValue == oneInterviewInfo.status
                    ).dictLabel
                  }}
                </td>
                <th>{{ $t("operate") }}</th>
                <td>
                  <el-button
                    type="text"
                    @click="updateHandle(oneInterviewInfo)"
                  >
                    {{ $t("Click to update") }}
                  </el-button>
                  <el-button
                    type="text"
                    @click="deleteHandle(oneInterviewInfo.interId)"
                  >
                    {{ $t("Click to delete") }}
                  </el-button>
                  <el-button
                    v-if="oneInterviewInfo.picture != '' && oneInterviewInfo.picture != null"
                    type="text"
                    @click="downloadHandle(oneInterviewInfo.picture)"
                  >
                    {{ $t("download picture") }}
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
        <el-button
          @click="AddHandle()"
          type="primary"
        >{{
          $t("Add")
        }}</el-button>
        <el-button @click="visible = false">{{ $t("Close") }}</el-button>
      </span>
    </template>
  </el-dialog>
  <add-or-update
    ref="addOrUpdate"
    @addOrUpdateCallback="loadInterviewInfoList"
  ></add-or-update>
</template>

<script>
import AddOrUpdate from "./talent-interview-info-add-or-update.vue";
export default {
  components: { AddOrUpdate },
  data() {
    return {
      talentId: "",
      interviewInfoList: [],
      visible: false, //整个对话框
      dicts: {
        hire: [],
      },
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
    init: function (talentId, interviewInfoList) {
      let that = this;
      that.talentId = talentId;
      that.interviewInfoList = interviewInfoList;
      that.loadDicts();
      that.visible = true;

      // 如果当前没有面试信息，直接弹出新增窗口
      if (
        that.interviewInfoList == null ||
        that.interviewInfoList.length == 0
      ) {
        that.$nextTick(() => {
          that.$refs.addOrUpdate.init(that.talentId);
        });
      }
    },
    loadInterviewInfoList: function () {
      let that = this;
      that.$http(
        "/talent/searchInfoByTalentId",
        "POST",
        { talentId: that.talentId },
        false,
        function (resp) {
          that.interviewInfoList = resp.result.interviewInfoList;
        }
      );
    },
    loadDicts: function () {
      let that = this;
      that.dicts.hire = that.$searchDict("dict_hire");
    },
    // 新增面试信息
    AddHandle: function () {
      let that = this;
      that.$nextTick(() => {
        that.$refs.addOrUpdate.init(that.talentId);
      });
    },
    // 修改面试信息
    updateHandle: function (dataForm) {
      let that = this;
      that.$nextTick(() => {
        that.$refs.addOrUpdate.init(that.talentId, dataForm);
      });
    },
    // 删除面试信息
    deleteHandle: function (id) {
      let that = this;
      that
        .$confirm(
          that.$t("Delete this interview information?"),
          that.$t("tip"),
          {
            confirmButtonText: that.$t("Confirm"),
            cancelButtonText: that.$t("Cancel"),
            type: that.$t("warning"),
          }
        )
        .then(() => {
          that.$http(
            "/interviewInfo",
            "DELETE",
            {
              interIds: [id],
            },
            true,
            function (resp) {
              ElMessage({
                message: that.$t("Successful operation"),
                type: "success",
                duration: 1200,
              });
              that.loadInterviewInfoList();
            }
          );
        });
    },
    getFileName: function (url) {
      const match = url.match(/\/([^\/?#]+)$/);
      const fileName = match ? match[1] : null;
      return fileName;
    },
    downloadFile: function (blob, filename) {
      // 创建一个链接元素
      var link = document.createElement("a");
      // 创建一个指向 Blob 对象的 URL
      link.href = window.URL.createObjectURL(blob);
      // 设置下载属性，包括文件名
      link.download = filename;
      // 隐藏该元素
      link.style.display = "none";
      // 将其添加到 DOM 中
      document.body.appendChild(link);
      // 模拟点击
      link.click();
      // 从 DOM 中移除元素
      document.body.removeChild(link);
      // 清理 blob URL
      window.URL.revokeObjectURL(link.href);
    },
    downloadHandle: function (url) {
      let that = this;
      var xhr = new XMLHttpRequest();
      var requestUrl =
        "http://192.168.1.2:8092/talentManageSystem-api/api/v1/file/download?name=" +
        encodeURIComponent(url);
      console.log("requestUrl:" + requestUrl);
      xhr.open("GET", requestUrl, true);
      xhr.responseType = "blob"; // 重要：设置响应类型为 Blob
      xhr.setRequestHeader("token", localStorage.getItem("token"));
      xhr.send();
      xhr.onload = function () {
        if (xhr.status === 200) {
          // 响应成功，处理下载
          that.downloadFile(xhr.response, that.getFileName(url)); // 假设你知道文件类型和扩展名
        } else {
          console.error("Request failed. Status:", xhr.status);
        }
      };
    },
  },
};
</script>

<style lang="less" scoped="scoped">
@import url(talent.less);
</style>
