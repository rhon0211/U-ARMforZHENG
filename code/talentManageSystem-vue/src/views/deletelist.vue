<template>
  <el-form :inline="true" :model="dataForm" :rules="dataRule" ref="dataForm">
    <!-- 中文名 -->
    <el-form-item prop="name">
      <el-input
        v-model="dataForm.name"
        :placeholder="$t('JapaneseName')"
        size="medium"
        class="input"
        clearable="clearable"
      />
    </el-form-item>
    <!-- 英文名 -->
    <el-form-item prop="englishName">
      <el-input
        v-model="dataForm.englishName"
        :placeholder="$t('englishName')"
        size="medium"
        class="input"
        clearable="clearable"
      />
    </el-form-item>
    <!-- 假名 -->
    <el-form-item prop="pseudonym">
      <el-input
        v-model="dataForm.pseudonym"
        :placeholder="$t('pseudonym')"
        size="medium"
        class="input"
        clearable="clearable"
      />
    </el-form-item>
    <!-- 操作者 -->
    <!-- <el-form-item>
      <el-select
        v-model="dataForm.blacklistBy"
        class="input"
        :placeholder="$t('Log Operation Name')"
        size="medium"
        clearable="clearable"
      >
        <el-option
          v-for="(one, index) in adminList"
          :key="index"
          :label="one.name"
          :value="one.name"
        />
      </el-select>
    </el-form-item> -->
    <el-form-item>
      <el-button size="medium" type="primary" @click="searchHandle()">{{
        $t("Query")
      }}</el-button>
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
    <el-table-column
      type="selection"
      :selectable="selectable"
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
      :label="$t('JapaneseName')"
      min-width="120"
      :show-overflow-tooltip="true"
    />
    <el-table-column
      prop="englishName"
      header-align="center"
      align="center"
      :label="$t('englishName')"
      min-width="120"
    />
    <el-table-column
      prop="pseudonym"
      header-align="center"
      align="center"
      :label="$t('pseudonym')"
      min-width="120"
    />
    <!-- <el-table-column
      prop="blacklistBy"
      header-align="center"
      align="center"
      :label="$t('Log Operation Name')"
      min-width="120"
    /> -->
    <el-table-column
      prop="blacklistTime"
      header-align="center"
      align="center"
      :label="$t('Date')"
      min-width="180"
      sortable
    />
    <el-table-column
      header-align="center"
      align="center"
      width="200"
      :label="$t('operate')"
    >
      <template #default="scope">
        <el-button
          type="text"
          v-if="isAuth(['talent_recover_deletelist'])"
          @click="removeHandle(scope.row.talentId)"
        >
          {{ $t("recover") }}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
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
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalCount: 0,
      dataListLoading: false,
      dataListSelections: [],
      adminList: [],
      dataForm: {
        name: "",
        pseudonym: "",
        englishName: "",
        deletedtBy: "",
        orderColumn: "",
        orderSeq: "",
      },
      getRowKeys(row) {
        return row.talentId;
      },
    };
  },
  methods: {
    loadDataList: function () {
      let that = this;
      // 进度条
      that.dataListLoading = true;
      let data = {
        delFlag: 2,
        name: that.dataForm.name,
        englishName: that.dataForm.englishName,
        pseudonym: that.dataForm.pseudonym,
        deletedBy: that.dataForm.deletedBy,
        orderColumn: that.dataForm.orderColumn,
        orderSeq: that.dataForm.orderSeq,
        page: that.pageIndex,
        length: that.pageSize,
      };
      that.$http(
        "/talent/searchBasicInfo",
        "POST",
        data,
        false,
        function (resp) {
          let result = resp.result;
          that.dataList = result.list;
          that.totalCount = result.totalCount;
          that.dataListLoading = false;
        }
      );
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
    // 返回人才的主键值
    getRowKeys(row) {
      return row.talentId;
    },
    // 选择行变化
    selectionChangeHandle: function (val) {
      // val：被选中的行的全部信息
      this.dataListSelections = val;
    },
    removeHandle: function (id) {
      let that = this;

      that
        .$confirm(that.$t("Are you sure to recover"), that.$t("tip"), {
          confirmButtonText: that.$t("Confirm"),
          cancelButtonText: that.$t("Cancel"),
          type: "warning",
        })
        .then(() => {
          let data = {
            talentId: id,
          };
          that.$http(
            "/talent/undeletion",
            "PUT",
            { talentId: id },
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
    // 排序
    orderHandle: function (param) {
      if (param.order === "ascending") {
        this.dataForm.orderSeq = "ASC";
      } else if (param.order == "descending") {
        this.dataForm.orderSeq = "DESC";
      } else {
        return;
      }
      if (param.prop === "deletedTime") {
        this.dataForm.orderColumn = "deleted_time";
      }
      this.dataList = [];
      this.loadDataList();
    },
    searchHandle: function () {
      this.pageIndex = 1;
      this.loadDataList();
    },
    refresh: function () {
      this.loadDataList();
    },
  },
  created: function () {
    this.loadDataList();
    this.$bus.on("refreshTabs", this.refresh);
  },
};
</script>