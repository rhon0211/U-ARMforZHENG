<template>
  <div v-if="!isAuth(['interviewer_search'])">
    <el-button
      size="medium"
      type="primary"
      :disabled="!isAuth(['interviewer_add'])"
      @click="addHandle()"
    >
      {{ $t("add") }}
    </el-button>
     <add-or-update
      ref="addOrUpdate"
      @refreshDataList="loadDataList"
    ></add-or-update>
  </div>
  <div v-else>
    <el-form :inline="true" :model="dataForm" :rules="dataRule" ref="dataForm">
      <el-form-item prop="name">
        <el-input
          v-model="dataForm.name"
          :placeholder="$t('Interviewer Name')"
          size="medium"
          class="input"
          clearable="clearable"
        />
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="dataForm.type"
          :placeholder="$t('interviewer type')"
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
      <el-form-item>
        <el-button size="medium" type="primary" @click="searchHandle()">{{
          $t("Query")
        }}</el-button>
        <el-button
          size="medium"
          type="primary"
          :disabled="!isAuth(['interviewer_add'])"
          @click="addHandle()"
        >
          {{ $t("add") }}
        </el-button>
        <el-button
          size="medium"
          type="danger"
          :disabled="!isAuth(['interviewer_del'])"
          @click="deleteHandle()"
        >
          {{ $t("Batch Delete") }}
        </el-button>
      </el-form-item>
      <br />
      <!-- 日期 -->
      <el-form-item prop="date" :label="$t('Date')">
        <el-radio-group v-model="date">
          <el-radio
            v-for="one in dicts.date"
            :key="one.dictValue"
            :label="one.dictValue"
            >{{ one.dictLabel }}</el-radio
          >
        </el-radio-group>
      </el-form-item>
      <el-form-item v-show="datePickerVisible" style="marginleft: 20px">
        <el-date-picker
          v-model="dataForm.date"
          style="width: 240px"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="~"
          :start-placeholder="$t('Begin Date')"
          :end-placeholder="$t('End Date')"
        ></el-date-picker>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      :cell-style="{ padding: '3px 0' }"
      style="width: 100%"
      size="medium"
      @selection-change="selectionChangeHandle"
      @expand-change="expand"
      :row-key="getRowKeys"
      :expand-row-keys="expands"
      @sort-change="orderHandle"
    >
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50"
      />
      <el-table-column
        type="index"
        header-align="center"
        align="center"
        width="100"
        :label="$t('Serial Number')"
      >
        <template #default="scope">
          <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="name"
        header-align="center"
        align="center"
        min-width="120"
        :label="$t('Interviewer Name')"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        prop="type"
        header-align="center"
        align="center"
        min-width="100"
        :label="$t('interviewer type')"
      />
      <el-table-column
        prop="interviewedNum"
        header-align="center"
        align="center"
        min-width="120"
        :label="$t('Interview count')"
        sortable="true"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        prop="hiredNum"
        header-align="center"
        align="center"
        min-width="120"
        :label="$t('appointed count')"
        :show-overflow-tooltip="true"
        sortable="true"
      />
      <el-table-column
        header-align="center"
        align="center"
        width="150"
        :label="$t('operate')"
      >
        <template #default="scope">
          <el-button
            type="text"
            size="medium"
            v-if="isAuth(['interviewer_update'])"
            @click="updateHandle(scope.row.interviewerId)"
          >
            {{ $t("Modify") }}
          </el-button>
          <el-button
            type="text"
            size="medium"
            v-if="isAuth(['interviewer_del'])"
            @click="deleteHandle(scope.row.interviewerId)"
          >
            {{ $t("Delete") }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50]"
      :page-size="pageSize"
      :total="totalCount"
      layout="total, sizes, prev, pager, next, jumper"
    ></el-pagination>
    <add-or-update
      ref="addOrUpdate"
      @refreshDataList="loadDataList"
    ></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from "./interviewer-add-or-update.vue";
export default {
  components: {
    AddOrUpdate,
  },
  data() {
    return {
      // 请求参数：
      pageIndex: 1,
      pageSize: 10,
      dataForm: {
        name: "",
        type: "",
        date: "",
        status: "",
        orderSeq: "",
        orderColumn: "",
      },
      // 响应参数
      totalCount: 0,
      dataList: [],
      // 字典
      dicts: {
        interviewerType: [],
        date: [],
      },
      //   这个data存 1 6 12 100
      date: "",
      dataListLoading: false,
      dataListSelections: [],
      getRowKeys(row) {
        return row.id;
      },
    };
  },
  computed: {
    datePickerVisible() {
      if (this.date == "100") {
        return true;
      } else {
        return false;
      }
    },
  },
  methods: {
    loadDicts: function () {
      this.dicts.interviewerType = this.$searchDict("dict_interviewer_type");
      this.dicts.date = this.$searchDict("dict_date");
    },
    loadDataList: function () {
      let that = this;
      if (!this.isAuth(['interviewer_search'])){
        return;
      }
      that.dataListLoading = true;
      // 处理date
      let startDate = "";
      let endDate = new Date().toISOString().slice(0, 10);
      if (that.date == 100 && that.dataForm.date != null) {
        startDate = that.dataForm.date[0];
        endDate = that.dataForm.date[1];
      } else if (that.date == 1 || that.date == 6 || that.date == 12) {
        startDate = this.getDateBefore(that.date);
      }
      let data = {
        page: that.pageIndex,
        length: that.pageSize,
        name: that.dataForm.name,
        type: that.dataForm.type,
        startDate: startDate,
        endDate: endDate,
        status: that.dataForm.status,
        orderSeq: that.dataForm.orderSeq,
        orderColumn: that.dataForm.orderColumn,
      };
      that.$http("/interviewer/list", "POST", data, true, function (resp) {
        let result = resp.result;
        that.dataList = result.list;
        that.totalCount = result.totalCount;
        that.dataListLoading = false;
      });
    },
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.loadDataList();
    },
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.loadDataList();
    },
    // 点击查询调的函数
    searchHandle: function () {
      this.pageIndex = 1;
      this.loadDataList();
    },
    // 返回面试官的主键值
    getRowKeys(row) {
      return row.interviewerId;
    },
    addHandle: function () {
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init();
      });
    },
    updateHandle: function (id) {
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
    selectionChangeHandle: function (val) {
      this.dataListSelections = val;
    },
    orderHandle: function (param) {
      let prop = param.prop;
      let order = param.order;
      if (order == "ascending") {
        this.dataForm.orderSeq = "ASC";
      } else if (order == "descending") {
        this.dataForm.orderSeq = "DESC";
      } else {
        return;
      }
      this.dataForm.orderColumn = param.prop;
      this.dataList = [];
      this.loadDataList();
    },
    deleteHandle: function (id) {
      let that = this;
      let ids = id
        ? [id]
        : that.dataListSelections.map((item) => {
            return item.interviewerId;
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
              "/interviewer",
              "DELETE",
              {
                interviewerIds: ids,
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
    getDateBefore: function (duration) {
      let currentDate = new Date();
      let year = currentDate.getFullYear();
      let month = currentDate.getMonth();
      let day = currentDate.getDate();

      // 计算前一个月的年份和月份
      let prevYear = year;
      let prevMonth = month - duration + 1;

      // 如果月份小于等于0，向前回滚到上一年
      if (prevMonth <= 0) {
        prevYear -= 1;
        prevMonth += 12;
      }

      // 获取前一个月的最后一天
      let lastDayOfPrevMonth = new Date(prevYear, prevMonth, 0).getDate();

      // 如果当前日期大于前一个月的最后一天，将日期设置为前一个月的最后一天
      if (day > lastDayOfPrevMonth) {
        day = lastDayOfPrevMonth;
      }

      // 格式化前一个月的日期
      let formattedDate =
        prevYear +
        "-" +
        (prevMonth < 10 ? "0" + prevMonth : prevMonth) +
        "-" +
        (day < 10 ? "0" + day : day);
      return formattedDate;
    },
    refresh: function () {
      this.loadDataList();
      this.loadDicts();
    },
  },
  created: function () {
    this.loadDataList();
    this.loadDicts();
    this.$bus.on("refreshTabs", this.refresh);
  },
};
</script>
