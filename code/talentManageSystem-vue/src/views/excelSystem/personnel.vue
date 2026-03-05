<template>
    <!-- 表格 -->
    <el-table :data="dataList" style="width: 100%; margin-bottom: 20px;" row-key="companyId" border
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" @expand-change="handleExpandChange"
        height="500">
        <!-- 折叠面板 -->
        <el-table-column type="expand">
            <template #default="scope">
                <div> 
                    <!-- 検索 追加 削除 -->
                    <el-form :inline="true" :model="dataForm" ref="dataForm">
                        <el-form-item>
                            <el-button size="medium" type="primary" @click="addHandle(scope.row.companyId)">追加</el-button>
                        </el-form-item>
                    </el-form>
                </div>
                <div v-if="scope.row.staffCount > 0">
                <div class="staff-table-container">                
                    <!-- 小表格 -->
                    <table class="content" v-show="isRowExpanded(scope.row)">
                        <tr>
                            <th class="header-cell">NO</th>
                            <th class="header-cell" :width="calcCharWidth(21)">要員名</th>
                            <th class="header-cell" :width="calcCharWidth(21)">要員名（フリガナ）</th>
                            <th class="header-cell" :width="calcCharWidth(11)">稼働状況</th>
                            <th class="header-cell" :width="calcCharWidth(11)">直近評価</th>
                            <th class="header-cell">操作</th>
                        </tr>
                        <tr v-for="(staff, index) in scope.row.staffList" :key="staff.staffIId">
                            <!-- <td>{{ index + 1 }}</td> -->
                            <td>{{ staff.staffId }}</td>
                            <td>{{ staff.staffNameKanji }}</td>
                            <td>{{ staff.staffNameFurikana || '-' }}</td>
                            <td>{{ staff.workStatus || '-' }}</td>
                            <td>{{ staff.latestEvaluation || '-' }}</td>
                            <td>
                                <!-- 变更按钮 -->
                                <el-button type="default" @click="updateHandle(scope.row.companyId,staff.staffId)">変更</el-button>
                                <!-- 消除按钮
                                <el-button type="default" @click="deleteHandle(staff.staffId)">削除</el-button> -->
                            </td>
                        </tr>
                    </table>
                </div>
                </div>    
            </template>
        </el-table-column>

        <el-table-column prop="companyId" label="番号" width="60" header-align="center" align="center"></el-table-column>
        <el-table-column prop="companyAbbreviation" label="会社略称"  header-align="center" align="left" :width="calcCharWidth(56)"></el-table-column>
        <el-table-column prop="staffCount" label="登録要員数" header-align="center" align="right" :width="calcCharWidth(14)"></el-table-column>
    </el-table>



    <!-- 分页 -->
  <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50]"
      :page-size="pageSize"
      :total="totalCount"
      layout="total, sizes, prev, pager, next, jumper">
  </el-pagination>

    <add-or-update ref="addOrUpdate" @refreshDataList="loadStaffList"></add-or-update>

</template>

<script>
import { ElMessage } from "element-plus";
import AddOrUpdate from "./personnel-add-or-update.vue";

export default {

    components: {
        AddOrUpdate
    },

    data() {
        return {
            // 示例表格数据
            dataList: [],

            pageIndex: 1,
            pageSize: 10,
            totalCount: 0,

            // 过滤要員的姓名列表
            filteredStaffNames: [],

            // 技術者下拉列表
            custList: [],

            // 提交表单时的数据
            dataForm: {
                staffId: null,
                // 照片
                photoUrl: null,
                // 英文
                staffNameRoma: null,
                // 假名
                staffNameFurikana: null,
                // 中文
                staffNameKanji: null,
                // 营业担当者
                salesRepresentitive: null,
                // 公司 id
                companyId: null,
                // 生年月
                staffBirthday: null,
                // 营业开始日期
                operationStartDate: null,
                // 营业结束日期
                operationEndDate: null,
                // 直近评价
                latestEvaluation: null,
                activeFlg: null,
                comment: null,
            },

            dataListSelections: [],

            allSelected: false,  // 全选的状态
            isIndeterminate: false,  // 是否是部分选中的状态
            expandedRows: [],  // 存储展开行的id
        }
    },
    methods: {
        calcCharWidth(chars) {
    return Math.ceil(chars * 8.2); // 可以换成 8.5、9 微调
  },
        // 处理展开状态
        handleExpandChange(row, expandedRows) {
            if (expandedRows.length > 0) {
                // 当前行展开，添加到 expandedRows
                if (!this.expandedRows.includes(row.companyId)) {
                    this.expandedRows.push(row.companyId);
                }
            } else {
                // 当前行收起，从 expandedRows 移除
                const index = this.expandedRows.indexOf(row.companyId);
                if (index > -1) {
                    this.expandedRows.splice(index, 1);
                }
            }
        },

        // 检查当前行是否展开
        isRowExpanded(row) {
            return this.expandedRows.includes(row.companyId);  // 根据 companyId 判断是否展开
        },

        // 加载要員列表
        loadStaffList() {
            let that = this;
            that.dataListLoading = true;
            let data = {
                page: that.pageIndex,
                length: that.pageSize,
            
            };
            that.$httpV2('/staff/getCompanyList', 'GET', data, true, function (resp) {
                let result = resp.result;
                that.dataList = result.list;
                that.totalCount = result.totalCount;
                that.dataListLoading = false;
            })
        },

        searchHandle() {
            this.pageIndex = 1;
            this.loadStaffList();
        },
        filterStaffName(query) {
            if (query !== '') {
                this.filteredStaffNames = this.custList.filter(item => {
                    return item.staffNameKanji.toLowerCase().includes(query.toLowerCase());
                });
            } else {
                this.filteredStaffNames = this.custList;
            }
        },
        addHandle(companyId) {
            const companyInfo = this.dataList.find((company) => company.companyId === companyId);
            let id=null;
            this.$nextTick(() => {
                this.$refs.addOrUpdate.init(id, companyInfo);
            });
        },
        deleteHandle(id) {
            console.log("id: " + id)
            let that = this;
            let staffIds = id
                ? [id]
                : that.dataListSelections.map(item => {
                    console.log(item)
                    return item.staffId;
                });
            console.log("staffIds: " + staffIds)
            if (staffIds.length == 0) {
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
                    that.$httpV2('/staff/deleteStaffByIds', 'DELETE', { "ids": staffIds }, true, function (resp) {
                        ElMessage({
                            message: '削除完了しました。',
                            type: 'success',
                            duration: 1200,
                            onClose: () => {
                                that.loadStaffList();
                            }
                        });
                    });
                });
            }
        },
        updateHandle: function (companyId,id) {
            const companyInfo = this.dataList.find((company) => company.companyId === companyId);
            this.$nextTick(() => {
                this.$refs.addOrUpdate.init(id,companyInfo);
            });
        },
        getRowKeys(row) {
            return row.id;
        },
        sizeChangeHandle(val) {
            this.pageSize = val;
            this.pageIndex = 1;
            this.loadStaffList();
        },
        currentChangeHandle(val) {
            this.pageIndex = val;
            this.loadStaffList();
        },

    },
    created() {
        this.loadStaffList();
    }
}

</script>

<style>

.content {
    width: 100%;
    border-collapse: collapse;
}

.header-cell {
    background-color: #f2f2f2;
    text-align:center;
    padding: 8px;
    border: 1px solid #ddd;
}


.content td {
    padding: 8px;
    border: 1px solid #ddd;
    text-align: left;
}
.content th {
    text-align: center !important;
    padding: 8px;
    border: 1px solid #ddd;
}

.content tr:nth-child(even) {
    background-color: #f9f9f9;
}

.content tr:hover {
    background-color: #f1f1f1;
}
.staff-table-container {
    max-height: 300px;
    overflow: auto;
    border: 1px solid #ddd;
}
</style>
