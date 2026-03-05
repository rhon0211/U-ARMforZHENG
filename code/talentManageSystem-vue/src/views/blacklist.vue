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
      <el-button size="medium" type="primary" @click="searchHandle()"
        >{{ $t('Query') }}</el-button
      >
    </el-form-item>
  </el-form>
  <el-table
    :data="dataList"
    border
    v-loading="dataListLoading"
    @selection-change="selectionChangeHandle"
    :cell-style="{ padding: '3px 0' }"
    style="width: 100%"
    @expand-change="expand"
    :row-key="getRowKeys"
    :expand-row-keys="expands"
    @sort-change="orderHandle"
  >
    <el-table-column type="expand">
      <table class="content" >
        <tr>
          <th>{{$t('blacklist reason')}}</th>
          <td style="width: 600px">{{ showBlacklistReason }}</td>
        </tr>
      </table>
    </el-table-column>
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
          :disabled="!isAuth(['talent_update_blacklist'])"
          @click="updateHandle(scope.row)"
        >
          {{$t('eidt blacklist reason')}}
        </el-button>
        <el-button
          type="text"
          :disabled="!isAuth(['talent_remove_blacklist'])"
          @click="removeHandle(scope.row.talentId)"
        >
          {{$t('remove blacklist')}}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-dialog
    :close-on-click-modal="false"
    v-model="dialogVisible"
    width="550px"
  >
    <el-form :model="newBlacklistReason" ref="dataForm" label-width="80px">
      <el-form-item :label="$t('eidt blacklist reason')" prop="newBlacklistReason">
        <el-input
          v-model="newBlacklistReason"
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
        <el-button @click="visible = false">{{$t('Cancel')}}</el-button>
        <el-button type="primary" @click="dataFormSubmit">{{$t('Confirm')}}</el-button>
      </span>
    </template>
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
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalCount: 0,
      dataListLoading: false,
      dataListSelections: [],
      expands: [],
      dialogVisible: false,
      newBlacklistReason: "",
      showBlacklistReason: "",
      adminList: [],
      dataForm: {
        name: "",
        pseudonym: "",
        englishName: "",
        blacklistBy: "",
        orderColumn: "",
        orderSeq: "",
      },
      selectId: "",
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
        status: 1,
        name: that.dataForm.name,
        englishName: that.dataForm.englishName,
        pseudonym: that.dataForm.pseudonym,
        blacklistBy: that.dataForm.blacklistBy,
        orderColumn: that.dataForm.orderColumn,
        orderSeq: that.dataForm.orderSeq,
        page: that.pageIndex,
        length: that.pageSize,
      };
      that.$http(
        "/talent/searchBasicInfo",
        "POST",
        data,
        true,
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
    // 获取扩展行的黑名单信息
    expand: function (row, expandedRows) {
      let that = this;
      if (expandedRows.length > 0) {
        that.expands = [];
        that.expands.push(row.talentId);
        that.showBlacklistReason = row.blacklistReason;
      } else {
        that.expands = [];
      }
    },
    // 点击编辑
    updateHandle: function (row) {
      this.selectId = row.talentId;
      this.newBlacklistReason = row.blacklistReason;
      this.dialogVisible = true;
    },
    // 编辑对话框点击确定
    dataFormSubmit: function () {
      let that = this;
      let data = {
        blacklistReason: this.newBlacklistReason,
        talentId: this.selectId,
      };
      this.$http("/talent/blacklist", "PUT", data, true, function (resp) {
        that.loadDataList();
      });
      this.dialogVisible = false;
    },
    removeHandle: function (id) {
      let that = this;
      let ids = id
        ? [id]
        : that.dataListSelections.map((item) => {
            return item.id;
          });
      if (ids.length == 0) {
        ElMessage({
          message: that.$t('No Records Selected'),
          type: "warning",
          duration: 1200,
        });
      } else {
        that
          .$confirm(that.$t('Are you sure to remove from blacklist?'), that.$t('tip'), {
            confirmButtonText: that.$t('Confirm'),
            cancelButtonText: that.$t('Cancel'),
            type: "warning",
          })
          .then(() => {
            let data = {
              talentId: id,
              blacklistReason: "",
              status: 0,
            };
            // 拉出黑名单
            that.$http("/talent/blacklist", "PUT", data, true, function (resp) {
              ElMessage({
                message: that.$t('Successful operation'),
                type: "success",
                duration: 1200,
                onClose: () => {
                  that.loadDataList();
                },
              });
            });
          });
      }
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
      if (param.prop === "blacklistTime") {
        this.dataForm.orderColumn = "blacklist_time";
      }
      this.dataList = [];
      this.loadDataList();
    },
    searchHandle: function () {
      this.pageIndex = 1;
      this.loadDataList();
    },
    loadAdminList: function () {
      let that = this;
      // that.$http("/user/allUser", "GET", null, true, function (resp) {
      //   // 只把管理员过滤出来
      //   that.adminList = resp.result.filter((admin) => admin.type <= 2);
      // });
    },
    refresh: function () {
      this.loadAdminList();
      this.loadDataList();
    },
  },
  created: function () {
    this.loadAdminList();
    this.loadDataList();
    this.$bus.on("refreshTabs", this.refresh);
  },
};
</script>

<style lang="less" scoped="scoped">
@import url(talent.less);
</style>