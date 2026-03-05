<template>
  <el-form :inline="true" :model="dataForm" :rules="dataRule" ref="dataForm">
    <!-- 模块标题 -->
    <el-form-item :prop="$t('Log Title')">
      <el-select
        v-model="queryParam.title"
        class="input"
        :placeholder="$t('Log Title')"
        size="medium"
        clearable="clearable"
      >
        <el-option
          v-for="(one, index) in logTitleList"
          :key="index"
          :label="one"
          :value="one"
        />
      </el-select>
    </el-form-item>
    <!-- 操作类型 -->
    <el-form-item :prop="$t('Log Operation Type')">
      <el-select
        v-model="queryParam.businessType"
        class="input"
        :placeholder="$t('Log Operation Type')"
        size="medium"
        clearable="clearable"
      >
        <el-option
          v-for="one in logOperNameList"
          :key="one.value"
          :label="one.name"
          :value="one.value"
        />
      </el-select>
    </el-form-item>
    <!-- 状态 -->
    <el-form-item :prop="$t('Log Status')">
      <el-select
        v-model="queryParam.status"
        class="input"
        :placeholder="$t('Log Status')"
        size="medium"
        clearable="clearable"
      >
        <el-option
          v-for="one in logOperStatusList"
          :key="one.value"
          :label="one.name"
          :value="one.value"
        />
      </el-select>
    </el-form-item>
    <!-- 操作者 -->
    <el-form-item :prop="$t('Log Operation Name')">
      <el-select
        v-model="queryParam.operName"
        class="input"
        :placeholder="$t('Log Operation Name')"
        size="medium"
        clearable="clearable"
      >
        <el-option
          v-for="one in userList"
          :key="one.userId"
          :label="one.name"
          :value="one.name"
        />
      </el-select>
    </el-form-item>
    <!-- 操作时间 -->
    <el-form-item :prop="$t('Log Operation Time')">
      <el-date-picker
        v-model="dataRange"
        style="width: 240px"
        value-format="YYYY-MM-DD"
        type="daterange"
        range-separator="~"
        :start-placeholder="$t('Start Date')"
        :end-placeholder="$t('End Date')"
      ></el-date-picker>
    </el-form-item>
    <el-form-item>
      <el-button size="medium" type="primary" @click="searchHandle()">{{
        $t("Query")
      }}</el-button>
      <el-button size="medium" type="primary" @click="deleteHandle()">{{
        $t("Batch Delete")
      }}</el-button>
      <el-button size="medium" type="danger" @click="clearHandle">
        {{ $t("Clear Data") }}
      </el-button>
    </el-form-item>
  </el-form>

  <el-table
    :data="dataList"
    border
    v-loading="dataListLoading"
    @selection-change="selectionChangeHandle"
    :cell-style="{ padding: '3px 0' }"
    style="width: 100%"
    :row-key="getRowKeys"
    @sort-change="orderHandle"
  >
    <!-- 复选框 -->
    <el-table-column
      type="selection"
      :selectable="selectable"
      header-align="center"
      align="center"
      width="50"
    />
    <!-- 序号 -->
    <el-table-column
      type="index"
      header-align="center"
      align="center"
      width="100"
      :label="$t('Number')"
    >
      <template #default="scope">
        <span>{{
          (queryParam.page - 1) * queryParam.length + scope.$index + 1
        }}</span>
      </template>
    </el-table-column>
    <!-- 模块标题 -->
    <el-table-column
      prop="title"
      header-align="center"
      align="center"
      :label="$t('Log Title')"
      min-width="120"
      :show-overflow-tooltip="true"
    />
    <!-- 操作类型 -->
    <el-table-column
      prop="businessType"
      header-align="center"
      align="center"
      :label="$t('Log Operation Type')"
      min-width="120"
    />

    <!-- 请求方法 -->
    <el-table-column
      prop="requestMethod"
      header-align="center"
      align="center"
      :label="$t('Operation Request Method')"
      min-width="120"
    />
    <!-- 操作人员 -->
    <el-table-column
      prop="operName"
      header-align="center"
      align="center"
      :label="$t('Log Operation Name')"
      min-width="180"
    />
    <!-- 操作ip -->
    <el-table-column
      prop="operIp"
      header-align="center"
      align="center"
      :label="$t('Operation IP')"
      min-width="180"
    />
    <!-- 操作状态 -->
    <el-table-column
      prop="status"
      header-align="center"
      align="center"
      :label="$t('Log Status')"
      min-width="180"
    />
    <!-- 操作时间 -->
    <el-table-column
      prop="operTime"
      header-align="center"
      align="center"
      :label="$t('Log Operation Time')"
      min-width="120"
      sortable
    />
    <el-table-column
      header-align="center"
      align="center"
      width="200"
      label="操作"
    >
      <template #default="scope">
        <el-button type="text" @click="handleView(scope.row, scope.index)">
          {{ $t("Detail") }}
        </el-button>
        <el-button type="text" @click="deleteHandle(scope.row.operId)">
          {{ $t("Delete") }}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 详情 -->
  <el-dialog v-model="open" width="800px" append-to-body>
    <el-form ref="form" :model="form" label-width="100px" size="mini">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('Log Title')"
            >{{ form.title }}
          </el-form-item>
          <el-form-item :label="$t('Login Info')"
            >{{ form.operName }} / {{ form.operIp }}</el-form-item
          >
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('Operation URL')">{{
            form.operUrl
          }}</el-form-item>
          <el-form-item :label="$t('Operation Request Method')">{{
            form.requestMethod
          }}</el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('Operation Method')">{{
            form.method
          }}</el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('Request Params')">{{
            form.operParam
          }}</el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('Response Params')">{{
            form.jsonResult
          }}</el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('Log Status')">
            {{ form.status }}
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('Cost Time')"
            >{{ form.costTime }}mm</el-form-item
          >
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('Log Operation Time')">{{
            form.operTime
          }}</el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('Error Message')">{{
            form.errorMsg
          }}</el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="open = false">{{ this.$t("Close") }}</el-button>
    </div>
  </el-dialog>
  <el-pagination
    @size-change="sizeChangeHandle"
    @current-change="currentChangeHandle"
    :current-page="pageIndex"
    :page-sizes="[10, 20, 50]"
    :page-size="pageSize"
    :total="totalCount"
    layout="total, sizes, prev, pager, next, jumper"
  ></el-pagination>
</template>

<script>
export default {
  data() {
    return {
      // 是否显示弹出层
      open: false,
      // 详情页
      form: {},
      dataListSelections: [],
      totalCount: 0,
      dataListLoading: "",
      getRowKeys(row) {
        return row.operId;
      },
      dataRange: [],
      // 查询参数
      queryParam: {
        title: "",
        businessType: "",
        status: "",
        operName: "",
        startTime: "",
        endTime: "",
        page: 1,
        length: 10,
        orderColumn: "oper_time",
        orderSeq: "DESC",
      },
      //   表格数据
      dataForm: {
        operId: 0,
        title: "",
        businessType: "",
        method: "",
        requestMethod: "",
        operatorType: "",
        operName: "",
        deptName: "",
        operUrl: "",
        operIp: "",
        operLocation: "",
        operParam: "",
        jsonResult: "",
        status: "",
        errorMsg: "",
        operTime: "",
        costTime: "",
      },
      // 模块名称
      logTitleList: [
        "人才管理",
        "标签管理",
        "面试统计管理",
        "用户管理",
        "日志管理",
      ],
      // 操作名称
      logOperNameList: [
        { value: 1, name: this.$t("Add") },
        { value: 2, name: this.$t("Modify") },
        { value: 3, name: this.$t("Delete") },
        { value: 4, name: this.$t("Authorize") },
        { value: 5, name: this.$t("Export") },
        { value: 6, name: this.$t("Import") },
        { value: 7, name: this.$t("Force Logout") },
        { value: 8, name: this.$t("Generate Code") },
        { value: 9, name: this.$t("Clear Data") },
        { value: 0, name: this.$t("Other") },
      ],
      //   操作状态
      logOperStatusList: [
        { value: 0, name: this.$t("Normal") },
        { value: 1, name: this.$t("Abnormal") },
      ],
      //   保存所有用户
      userList: [],
    };
  },
  methods: {
    clearHandle: function () {
      let that = this;
      that
        .$confirm(
          that.$t("Are you sure you want to clear all logs?"),
          that.$t("tip"),
          {
            confirmButtonText: that.$t("Confirm"),
            cancelButtonText: that.$t("Cancel"),
            type: "warning",
          }
        )
        .then(() => {
          that.$http(
            "/operlog/clean",
            "DELETE",
            null,
            true,
            function (resp) {
              ElMessage({
                message: that.$t("Successful operation"),
                type: "success",
                duration: 1200,
                onClose: () => {
                  that.loadDataList();
                },
              });
            }
          );
        });
    },
    loadDataList: function () {
      let that = this;
      that.dataListLoading = true;
      // 处理date
      if (that.dataRange != null) {
        that.queryParam.startTime = that.dataRange[0];
        that.queryParam.endTime = that.dataRange[1];
      }
      let data = {
        title: that.queryParam.title,
        businessType: that.queryParam.businessType,
        status: that.queryParam.status,
        operName: that.queryParam.operName,
        startTime: that.queryParam.startTime,
        endTime: that.queryParam.endTime,
        page: that.queryParam.page,
        length: that.queryParam.length,
        orderSeq: that.queryParam.orderSeq,
        orderColumn: that.queryParam.orderColumn,
      };
      that.$http("/operlog/list", "POST", data, true, function (resp) {
        let result = resp.result;
        that.dataList = result.list;

        that.dataList.forEach((data) => {
          data.businessType = that.logOperNameList.find(
            (one) => one.value === data.businessType
          ).name;
          data.status = that.logOperStatusList.find(
            (one) => one.value === data.status
          ).name;
        });

        that.totalCount = result.totalCount;
        that.dataListLoading = false;
      });
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    loadUserList: function () {
      let that = this;
      that.$http("/user/allUser", "GET", null, false, function (resp) {
        that.userList = resp.result;
      });
    },
    sizeChangeHandle(val) {
      this.queryParam.length = val;
      this.queryParam.page = 1;
      this.loadDataList();
    },
    currentChangeHandle(val) {
      this.queryParam.page = val;
      this.loadDataList();
    },
    // 返回日志记录的主键值
    getRowKeys(row) {
      return row.operId;
    },
    // 选择行变化
    selectionChangeHandle: function (val) {
      // val：被选中的行的全部信息
      this.dataListSelections = val;
    },
    // 排序
    orderHandle: function (param) {
      if (param.order === "ascending") {
        this.queryParam.orderSeq = "ASC";
      } else if (param.order == "descending") {
        this.queryParam.orderSeq = "DESC";
      } else {
        return;
      }
      this.queryParam.orderColumn = "oper_time";
      this.dataList = [];
      this.loadDataList();
    },
    searchHandle: function () {
      this.pageIndex = 1;
      this.loadDataList();
    },
    deleteHandle: function (id) {
      let that = this;
      let ids = id
        ? [id]
        : that.dataListSelections.map((item) => {
            return item.operId;
          });
      if (ids.length == 0) {
        ElMessage({
          message: that.$t("No Records Selected"),
          type: "warning",
          duration: 1200,
        });
      } else {
        that
          .$confirm(
            that.$t("Are you sure you want to delete the selected records?"),
            that.$t("tip"),
            {
              confirmButtonText: that.$t("Confirm"),
              cancelButtonText: that.$t("Cancel"),
              type: "warning",
            }
          )
          .then(() => {
            that.$http(
              "/operlog",
              "DELETE",
              {
                operIds: ids,
              },
              true,
              function (resp) {
                ElMessage({
                  message: that.$t("Successful operation"),
                  type: "success",
                  duration: 1200,
                  onClose: () => {
                    that.loadDataList();
                  },
                });
              }
            );
          });
      }
    },
    refresh: function () {
      this.loadUserList();
      this.loadDataList();
      // 操作名称
      this.logOperNameList = [
        { value: 1, name: this.$t("Add") },
        { value: 2, name: this.$t("Modify") },
        { value: 3, name: this.$t("Delete") },
        { value: 4, name: this.$t("Authorize") },
        { value: 5, name: this.$t("Export") },
        { value: 6, name: this.$t("Import") },
        { value: 7, name: this.$t("Force Logout") },
        { value: 8, name: this.$t("Generate Code") },
        { value: 9, name: this.$t("Clear Data") },
        { value: 0, name: this.$t("Other") },
      ];
      //   操作状态
      this.logOperStatusList = [
        { value: 0, name: this.$t("Normal") },
        { value: 1, name: this.$t("Abnormal") },
      ];
    },
  },
  created: function () {
    this.$nextTick(() => {
      this.loadUserList();
      this.loadDataList();
      this.$bus.on("refreshTabs", this.refresh);
    });
  },
};
</script>

