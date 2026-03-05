<template>
  <el-form :inline="true" :model="dataForm" ref="dataForm">
    <el-form-item prop="omerName">
      <el-select
          v-model="dataForm.customerName"
          filterable
          clearable
          placeholder="顧客名"
          :filter-method="filterCustomerName"
          @visible-change="handleVisibleChange"
          style="width: 170px;"
      >
        <el-option
            v-for="item in filteredCustomerNames"
            :key="item.customerId"
            :label="item.customerName"
            :value="item.customerName"
        />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button size="medium" type="primary" @click="searchHandle()">検索</el-button>
      <el-button size="medium" type="primary" @click="addHandle()">追加</el-button>
      <el-button size="medium" type="danger" @click="deleteHandle()">一括削除</el-button>
    </el-form-item>
  </el-form>
  <el-table
      ref="table"
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      :row-key="getRowKeys"
      @sort-change="orderHandle"
      :cell-style="{ padding: '3px 0' }"
      style="width: 100%"
  >
    <el-table-column
        type="selection"
        :selectable="selectable"
        header-align="center"
        align="center"
        width="40"
    />

    <!--   序号  -->
    <el-table-column
        type="index"
        header-align="center"
        align="center"
        width="60"
        label="番号"
    >
      <template #default="scope">
        <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
      </template>
    </el-table-column>
    <el-table-column
        header-align="center"
        align="center"
        width="150"
        label="操作"
    >
      <template #default="scope">
        <el-button
            type="text"
            @click="updateHandle(scope.row.customerId)"
        >
          変更
        </el-button>
        <el-button
            type="text"
            @click="organization(scope.row.customerId)"
        >
          組織
        </el-button>
        <el-button
            type="text"
            @click="deleteHandle(scope.row.customerId)"
        >
          削除
        </el-button>
      </template>
    </el-table-column>
    <!--   循环展示表格内容   -->
    <el-table-column
        v-for="(column, index) in visibleColumns"
        :label="column.label"
        :key="index"
        :prop="column.prop"
        header-align="center"
        align="center"
        min-width="150"
        :sortable="column.sortable"
        :show-overflow-tooltip="true"
    />
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
  <add-or-update
      ref="addOrUpdate"
      @refreshDataList="loadCustomerList"
  ></add-or-update>
  
  <organization
        ref="Organization"
        @refreshDataList="loadCustomerList"
  ></organization>
</template>

<script>
import {ElMessage} from "element-plus";
import AddOrUpdate from "./customer-add-or-update.vue";
import Organization from "./customer-organization.vue";
export default {
  components: { AddOrUpdate,Organization },
  data: function () {
    return {
      token: localStorage.getItem("token"),
      action: `${this.$baseUrl}/file/upload`,
      selectedOption: null,
      // 技術者下拉列表
      custList: [],

      // 提交表单时的数据
      dataForm: {
        customerName: null,
        remark: null,
      },
      // 过滤客户的姓名列表
      filteredCustomerNames: [],

      // 存放查询到的顧客数据
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalCount: 0,

      // 加载进度条
      dataListLoading: false,

      columns: [
        {
          key: 0,
          label: "顧客名",
          prop: "customerName",
          visible: true,
          sortable: true,
        },
        {
          key: 1,
          label: "備考",
          prop: "remark",
          visible: true,
        }
      ],

      dataListSelections: []
    }
  },
  computed: {
    visibleColumns() {
      return this.columns.filter( (column) => column.visible );
    },
  },
  methods: {
    loadCustomerList() {
      let that = this;
      that.dataListLoading = true;
      let data = {
        page: that.pageIndex,
        length: that.pageSize,
        customerName: that.dataForm.customerName == null ? '' : that.dataForm.customerName,
      };
      that.$httpV2('/customer', 'GET', data, true, function (resp) {
        let result = resp.result;
        that.dataList = result.list;
        that.totalCount = result.totalCount;
        that.dataListLoading = false;
      })
      this.loadCustList();
    },
    filterCustomerName(query) {
      if (query !== '') {
        this.filteredCustomerNames = this.custList.filter(item => {
          return item.customerName.toLowerCase().includes(query.toLowerCase());
        });
      } else {
        this.filteredCustomerNames = this.custList;
      }
    },
    handleVisibleChange(visible) {
      if (visible) {
        this.filteredCustomerNames = this.custList;
      }
    },
    // 获取顧客全部信息
    loadCustList() {
      let that = this;
      that.$httpV2('/protech/customer', 'GET', null, true, function (resp) {
        let result = resp.result;
        that.custList = result;
      })
    },
    selectionChangeHandle: function (val) {
      this.dataListSelections = val;
    },
    getRowKeys(row) {
      return row.projectCustomerId;
    },
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.loadCustomerList()
    },
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.loadCustomerList()
    },

    searchHandle() {
      this.pageIndex = 1;
      this.loadCustomerList();
    },
    addHandle() {
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init();
      });
    },
    updateHandle: function(id) {
      const technicianInfo = this.dataList.find((technician) => technician.customerId === id);
      console.log(technicianInfo);
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(technicianInfo);
      });
    },
    deleteHandle(id) {
      console.log("id: " + id)
      let that = this;
      let customerIds = id
          ? [id]
          : that.dataListSelections.map(item => {
            console.log(item)
            return item.customerId;
          });
      console.log("customerids: " + customerIds)
      if (customerIds.length == 0) {
        ElMessage({
          message: "レコードが選択されていません。",
          type: 'warning',
          duration: 1200
        });
      } else {
        ElMessageBox.confirm("レコード削除してよろしいでしょうか？", "警告", {
          confirmButtonText: "確定",
          cancelButtonText: "キャンセル",
          type: 'warning'
        }).then(() => {
          that.$httpV2('/customer', 'DELETE', { customerIds: customerIds }, true, function(resp) {
            ElMessage({
              message: '削除完了しました。',
              type: 'success',
              duration: 1200,
              onClose: () => {
                that.loadCustomerList();
              }
            });
          });
        });
      }
    },
    organization: function(id){
      const technicianInfo = this.dataList.find((technician) => technician.customerId === id);
      console.log(technicianInfo);
      //判断id值是否等于1 如果等于弹出组织管理页面
      if(technicianInfo.customerId== 1){
        this.$nextTick(() => {
          this.$refs.Organization.init(technicianInfo);
        });
      }else{
        ElMessage({
          message: "当該顧客には詳細な組織管理情報がありません。",
          type: 'warning',
          duration: 1200
        });
      }
    },
  },
  created() {
    this.loadCustomerList();
    this.loadCustList();
  }
}
</script>