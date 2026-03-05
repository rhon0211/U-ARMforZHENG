<template>
    <!-- 操作面板 -->
    <el-form :inline="true" :model="dataForm" ref="dataForm">
        <el-form-item prop="companyName">
            <!-- 下拉列表 -->
            <el-select v-model="selectedValue" placeholder="企業を選択" clearable>
                <el-option v-for="option in options" :key="option.value" :label="option.label"
                    :value="option.value"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button size="default" type="primary" @click="searchHandle()">検索</el-button>
            <el-button size="default" type="success" @click="addHandle()">企業追加</el-button>
        </el-form-item>
    </el-form>
    <!-- 数据展示面板 -->
    <el-table ref="table" :data="dataList" border v-loading="dataListLoading" :row-key="getRowKeys"
        :cell-style="{ padding: '3px 0' }" style="width: 100%" class="data-list"
        max-height="400px">
        <!-- 折叠部门表格 -->
        <el-table-column type="expand">
            <template #default="company_scope">
                <el-table :data="dataList[company_scope.$index].departmentList" border ref="departmentTable"
                    :row-key="getRowDepartmentKeys" class="demo-table-expand">
                    <!-- 数据列 -->
                    <el-table-column header-align="center" align="left" label="部署名"
                        prop="departmentName" :width="calcCharWidth(56)"></el-table-column>
                    <el-table-column label="部署略称" prop="departmentAbbreviation" header-align="center"
                        align="left" :width="calcCharWidth(11)"></el-table-column>
                    <el-table-column label="部署責任者氏名" prop="principalName" header-align="center"
                        align="left" :width="calcCharWidth(16)"></el-table-column>
                    <el-table-column label="状態" prop="activeFlg" header-align="center" align="left" :width="calcCharWidth(17)">
                        <template #default="scope" >
                            <span>{{ scope.row.activeFlg === 1 ? 'アクティブ' : '非アクティブ' }}</span>
                        </template>
                    </el-table-column>

                    <!-- 操作列 -->
                    <el-table-column label="操作" header-align="center" align="center" :width="calcCharWidth(13)" >
                        <template #default="department_scope">
                            <!-- 编辑按钮 -->
                            <el-button type="default"
                                @click="updateDepartment(company_scope.row.companyId, department_scope.row.departmentId)">
                                編集
                            </el-button>
                        </template>
                    </el-table-column>

                </el-table>
                <el-button size="default" type="primary"
                    @click="addDepartment(company_scope.row.companyId)">部署追加</el-button>
            </template>
        </el-table-column>
        <!--   序号  -->
        <el-table-column type="index" header-align="center" align="center" width="60" label="番号">
            <template #default="scope">
                <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
            </template>
        </el-table-column>
        <!--   循环展示表格内容   -->
        <el-table-column v-for="(column, index) in visibleColumns" :label="column.label" :key="index"
            :prop="column.prop" :width="column.width" header-align="center" :align="getColumnAlign(column)"
             :sortable="column.sortable" :show-overflow-tooltip="true">
            <template #default="scope" v-if="column.prop === 'activeFlg'">
                <span>{{ scope.row.activeFlg === 1 ? 'アクティブ' : '非アクティブ' }}</span>
            </template>
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column label="操作" header-align="center" align="center" :width="calcCharWidth(15)">
            <template #default="scope">
                <!-- 编辑按钮 -->
                <el-button type="default" @click="updateHandle(scope.row.companyId)">
                    編集
                </el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
        :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalCount"
        layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <add-or-update ref="addOrUpdate" @refreshDataList="searchHandle"></add-or-update>
    <department-add-or-update ref="departmentAddOrUpdate" @refreshDataList="searchHandle">
    </department-add-or-update>
</template>

<script>
import AddOrUpdate from "./company-1-add-or-update.vue";
import DepartmentAddOrUpdate from "./department-add-or-update.vue";

export default {
    components: {
        AddOrUpdate,
        DepartmentAddOrUpdate
    },
    data() {
        return {

            dialogVisible: false, // 控制弹窗是否显示

            //提交表单时的数据
            dataForm: {
                companyName: null,
            },
            // 储存 二级下拉列表的选项
            selectedValue: "",
            // 二级下拉列表的配置,后端传入数据时，根据value值来判断是哪个二级下拉列表
            options: [
                {
                    value: '1',
                    label: '販売企業',
                },
                {
                    value: '2',
                    label: '調達企業',
                },
                {
                    value: '3',
                    label: '自社',
                }
            ],
            //过滤后的公司名称列表
            // filteredCompanyNames: [],

            //企業リスト
            dataList: [],
            //分页
            pageSize: 10,
            pageIndex: 1,
            totalCount: 0,

            //加载进度条
            dataListLoading: false,

            columns: [
                {
                    key: 0,
                    label: "企業名",
                    prop: "companyName",
                    visible: true,
                    sortable: false,
                    charLimit: 56,
                },
                {
                    key: 1,
                    label: "企業略称",
                    prop: "companyAbbreviation",
                    visible: true,
                    sortable: false,
                    charLimit: 21,
                },
                {
                    key: 2,
                    label: "登録部署数",
                    prop: "departmentCount",
                    visible: true,
                    sortable: false,
                    charLimit: 14,
                },
                {
                    key: 3,
                    label: "状態",
                    prop: "activeFlg",
                    visible: true,
                    sortable: false,
                    charLimit: 17,
                },
            ],
            // dataListSelections: [],选中行数据，该表不需要
            departmentList: [], // 部门列表

        }
    },
    computed: {
  visibleColumns() {
    const pxPerChar = 8.2; // 每个全角字符的宽度，按你字体来微调
    return this.columns
      .filter((column) => column.visible)
      .map((column) => ({
        ...column,
        width: Math.ceil((column.charLimit || 10) * pxPerChar),
      }));
  },
},
    methods: {
        calcCharWidth(chars) {
    return Math.ceil(chars * 8.2); // 可以换成 8.5、9 微调
  },

        //初始化页面，加载公司列表
        loadCompanyList() {
            let that = this;
            // that.dataListLoading = true;
            let data = {
                page: that.pageIndex,
                length: that.pageSize,
                saleOrProcurement: this.selectedValue ,
            };
            that.$httpV2("/companyAndDepartment/getByPage", "GET", data, true, function (resp) {
                let result = resp.result;
                that.dataList = result.list;
                // console.log(that.dataList);
                that.totalCount = result.totalCount;
                // that.dataListLoading = false;
            })
        },

        //设置对应内容居中，居右居左
        getColumnAlign(column) {
            // 控制列的内容对齐方式
            if (column.prop === 'departmentCount') {
                return 'right'; // 数字列居右对齐
            } else {
                return 'left'; // 其他数据列居左对齐
            }
        },

        // 分页
        sizeChangeHandle(val) {
            this.pageSize = val;
            this.pageIndex = 1;
            this.loadCompanyList();
        },
        currentChangeHandle(val) {
            this.pageIndex = val;
            this.loadCompanyList();
        },
        // 搜索
        searchHandle() {
            let that = this;
            that.pageIndex = 1;
            let data = {
                page: that.pageIndex,
                length: that.pageSize,
                saleOrProcurement: that.selectedValue,
            };
            that.$httpV2("/companyAndDepartment/getByPage", "GET", data, true, function (resp) {
                let result = resp.result;
                that.dataList = result.list;
                that.totalCount = result.totalCount;
                // that.dataListLoading = false;
            })



        },
        // 追加
        addHandle() {
          if (this.$refs.addOrUpdate.dialogVisible) {
            return; // 如果弹出框已经打开，则直接返回，不重复初始化
          }
          this.$refs.addOrUpdate.init();
        },
        updateHandle: function (id) {
            let that = this;
            const companyInfo = that.dataList.find((company) => company.companyId === id);
            that.$refs.addOrUpdate.init(companyInfo);
        },
        updateDepartment: function (companyid, id) {
            let that = this;
            //在dataList表中查找departmentId为id的行
            const companyInfo = that.dataList.find((company) => company.companyId === companyid);
            const departmentInfo = companyInfo.departmentList.find((department) => department.departmentId === id);

            // activeFlgを数値に変換
            if (departmentInfo) {
                departmentInfo.activeFlg = departmentInfo.activeFlg === 'アクティブ' ? 1 : 0;
            }

            console.log(departmentInfo);
            that.$nextTick(() => {
                that.$refs.departmentAddOrUpdate.init(departmentInfo);
            });
        },
      addDepartment(id) {
        if (this.$refs.departmentAddOrUpdate.dialogVisible) {
          return; // 如果弹出框已经打开，则直接返回，不重复初始化
        }
        const departmentInfo = {
          companyId: id,
          activeFlg: 1,
        };
        this.$nextTick(() => {
          this.$refs.departmentAddOrUpdate.init(departmentInfo);
        });
      },

      init(data) {
        if (this.dialogVisible) return; // 避免重复弹出
        this.dialogVisible = true;
        // 其他初始化逻辑
      },
        // 当用户通过勾选复选框选择行时触发，调用 selectionChangeHandle 方法。该表格暂时不需要
        // selectionChangeHandle:function(val){
        //     // val：被选中的行的全部信
        //     this.dataListSelections = val;
        //     // console.log(val)
        // },
        getRowKeys(row) {
            return row.companyId;
        },
        getRowDepartmentKeys(row) {
            return row.departmentId;
        },


    },
    created() {
        this.loadCompanyList();
        //this.loadComList();
    },
}
</script>
<style>
/* .data-list {

} */



.demo-table-expand {
    font-size: 14px;
    padding-left: 50px;

}
</style>