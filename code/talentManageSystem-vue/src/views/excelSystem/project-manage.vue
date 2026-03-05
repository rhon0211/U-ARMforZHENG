<template>
    <!-- 操作面板 -->
    <el-form :inline="true" :model="dataForm" ref="dataForm">
        <el-form-item prop="year" label="対象年度">
            <el-select v-model="selectedYear" placeholder="年度" clearable>
                <el-option v-for="year in yearList" :key="year" :label="year + '年度'" :value="year">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="month" label="対象月">
            <el-select v-model="selectedMonth" placeholder="月を選択してください" clearable>
                <el-option v-for="month in monthList" :key="month" :label="month + '月度'" :value="month">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="loadDataList()">検索</el-button>
            <el-button type="success" @click="addHandle()">追加</el-button>
        </el-form-item>
    </el-form>

    <!-- 数据展示面板 -->
    <el-table ref="table" :data="dataList" border v-loading="dataListLoading" :row-key="getRowKeys"
        :header-cell-style="{ background: '#DCDFE6', color: ' #333' }"
        style="width: 90%; font-size: 15px; margin-bottom: 20px;" :max-height="470">
        <!-- 折叠部门表格 -->
        <el-table-column type="expand">
            <template #default="company_scope">
                <el-table :data="dataList[company_scope.$index].departmentList" border ref="departmentTable"
                    :row-key="getRowDepartmentKeys" :header-cell-style="{ background: '#EBEEF5', color: '#333' }"
                    style="padding-left: 30px; font-size: 14px;width: 100%;" :max-height="400">

                    <!-- 折叠案件表格 -->
                    <el-table-column type="expand">
                        <template #default="department_scope">
                            <!-- 案件数据 -->
                            <el-table
                                :data="dataList[company_scope.$index].departmentList[department_scope.$index].projectList"
                                border ref="projectTable" :row-key="getRowProjectKeys"
                                :header-cell-style="{ background: '#f5f7fa', color: '#333' }"
                                style="padding-left: 30px; font-size: 13px;width: 100%;" :max-height="350">
                                <el-table-column label="案件名" prop="projectName" header-align="center" align="left"
                                    :width="calcCharWidth(35)"></el-table-column>
                                <el-table-column label="要員数" prop="staffCount" header-align="center" align="right"
                                    :width="calcCharWidth(12)"></el-table-column>
                                <el-table-column label="案件開始日" prop="projectStartDate" header-align="center"
                                    align="right" :width="calcCharWidth(17)"></el-table-column>
                                <el-table-column label="案件予定終了日" prop="projectScheduledEndDate" header-align="center"
                                    align="right" :width="calcCharWidth(17)"></el-table-column>
                                <el-table-column label="案件終了日" prop="projectEndDate" header-align="center" align="right"
                                    :width="calcCharWidth(17)"></el-table-column>

                                <!-- 操作列 -->
                                <el-table-column label="操作" header-align="center" align="center"
                                    :width="calcCharWidth(15)">
                                    <template #default="project_scope">
                                        <el-button type="default" size="default"
                                            @click="updateProject(project_scope.row.projectId, department_scope.row.departmentId, company_scope.row.salesCompanyId)">編集</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <!-- <el-button size="medium" type="primary"
                                @click="addProject(department_scope.row.departmentId, company_scope.row.salesCompanyId)">追加</el-button> -->
                        </template>
                    </el-table-column>

                    <!-- 部门数据 -->
                    <el-table-column label="部署略称" header-align="center" align="left" :width="calcCharWidth(56)">
                        <template #default="scope">
                            <span>
                                {{ scope.row.departmentAbbreviation }}
                                <template v-if="scope.row.principalName">
                                    &nbsp;{{ scope.row.principalName }}
                                </template>
                            </span>
                        </template>
                    </el-table-column>


                    <el-table-column label="案件数" prop="departmentProjectCount" header-align="center" align="right"
                        :width="calcCharWidth(55)"></el-table-column>

                </el-table>
            </template>

        </el-table-column>

        <el-table-column label="企業略称" prop="companyAbbreviation" header-align="center" align="left"
            :width="calcCharWidth(56)">
        </el-table-column>
        <el-table-column label="案件数" prop="projectCount" header-align="center" align="right" :width="calcCharWidth(80)">
        </el-table-column>

    </el-table>

    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
        :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="companyCount"
        layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <add-or-update ref="addOrUpdate" @refreshDataList="loadDataList"></add-or-update>
    <add ref="add" @refreshDataList="loadDataList"></add>
</template>
<script>
import AddOrUpdate from './project-manage-add-or-update.vue';
import Add from './project-manage-add.vue';

export default {
    components: { AddOrUpdate, Add },
    data() {
        return {
            dataForm: {
                year: null,
                month: null,
            },
            // 选中的年度
            selectedYear: null,
            // 选中的月份
            selectedMonth: null,
            // 年度和月份列表
            yearList: [],
            monthList: [],

            // 表格数据加载中
            dataListLoading: false,
            // 全部数据列表
            dataList: [],

            //分页
            pageSize: 10,
            pageIndex: 1,
            companyCount: 0,


        };
    },
    created() {
        const { year, month } = this.getDefaultTargetYearMonth();
        this.selectedYear = year;
        this.selectedMonth = month;

        this.generateYearList(); // 先生成列表，才能正确绑定下拉选项
        this.generateMonthList();
        this.loadDataList();
    },
    methods: {
        getDefaultTargetYearMonth() {
            const now = new Date();
            const year = now.getFullYear();
            const month = now.getMonth() + 1;
            const day = now.getDate();

            let targetYear = year;
            let targetMonth = month;

            if (day < 16) {
                // 当前日期是月初（1〜15日）
                if (month === 1) {
                    // 特殊处理：1月 → 前一年12月
                    targetYear = year - 1;
                    targetMonth = 12;
                } else {
                    targetMonth = month - 1;
                }
            }

            return {
                year: targetYear,
                month: targetMonth
            };
        },

        calcCharWidth(chars) {
            return Math.ceil(chars * 8.2); // 可以换成 8.5、9 微调
        },
        generateYearList() {
            const currentYear = new Date().getFullYear();
            const startYear = currentYear - 10; // 可调整起始年份
            const endYear = currentYear + 10; // 可调整结束年份

            // 生成年份范围数组
            this.yearList = [];
            for (let year = startYear; year <= endYear; year++) {
                this.yearList.push(year);
            }
        },

        generateMonthList() {
            // 生成年份列表 1 到 12
            this.monthList = Array.from({ length: 12 }, (_, index) => index + 1);
        },

        //初始化页面数据/搜索按钮合并
        loadDataList() {
            let that = this;
            let data = {
                page: that.pageIndex,
                length: that.pageSize,
                year: that.selectedYear,
                month: that.selectedMonth,
            };
            that.$httpV2("/projectDetail/queryByYearMonth", "GET", data, true, function (resp) {
                let result = resp.result;
                that.dataList = result.companies;
                that.companyCount = result.companyCount;
                // that.dataListLoading = false;
            })
        },

        // 分页
        sizeChangeHandle(val) {
            this.pageSize = val;
            this.pageIndex = 1;
            this.loadDataList();
        },
        currentChangeHandle(val) {
            this.pageIndex = val;
            this.loadDataList();
        },

        //返回企业id
        getRowKeys(row) {
            return row.salesCompanyId;
        },
        getRowDepartmentKeys(row) {
            return row.departmentId;
        },
        getRowProjectKeys(row) {
            return row.projectId;
        },

        // 更新案件信息，传入项目id，部门id，企业id
        updateProject(projectId, departmentId, companyId) {
            let that = this;
            const companyInfo = that.dataList.find((company) => company.salesCompanyId === companyId);
            const departmentInfo = companyInfo.departmentList.find((department) => department.departmentId === departmentId);
            const proInfo = departmentInfo.projectList.find((project) => project.projectId === projectId);
            const startDate = `${that.selectedYear}-${String(that.selectedMonth).padStart(2, '0')}-01`;
            const projectInfo = {
                salesCompanyId: companyInfo.salesCompanyId,
                salesCompanyAbbreviation: companyInfo.companyAbbreviation,
                departmentId: departmentInfo.departmentId,
                departmentAbbreviation: departmentInfo.departmentAbbreviation,
                principalName: departmentInfo.principalName,
                projectId: proInfo.projectId,
                projectName: proInfo.projectName,
                startDate: startDate,
            };
            that.$nextTick(() => {
                that.$refs.addOrUpdate.init(projectInfo);
            })
        },
        //追加案件信息，传入项目id和部门id
        addProject: function (departmentId, companyId) {
            let that = this;
            const companyInfo = this.dataList.find((company) => company.salesCompanyId === companyId);
            const departmentInfo = companyInfo.departmentList.find((department) => department.departmentId === departmentId);
            const projectInfo = {
                salesCompanyId: companyInfo.salesCompanyId,
                salesCompanyAbbreviation: companyInfo.companyAbbreviation,
                departmentId: departmentInfo.departmentId,
                departmentAbbreviation: departmentInfo.departmentAbbreviation,
                principalName: departmentInfo.principalName,
                staffList: [],
            }
            this.$nextTick(() => {
                this.$refs.addOrUpdate.init(projectInfo);
            })
        },
        // 添加案件
        addHandle() {
            this.$nextTick(() => {
                this.$refs.add.init();
            })
        },
    }
}


</script>
<style></style>
