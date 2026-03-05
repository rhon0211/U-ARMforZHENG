<template>
    <!-- 选择年月 -->
    <el-form>
        <el-form-item label="対象年月">
            <el-date-picker type="month" v-model="pickedDate" @change="handleDateChange" value-format="YYYY-MM-01"
                :disabledDate="disabledDate"></el-date-picker>
        </el-form-item>
    </el-form>

    <el-form-item>
        <span style="font-weight: bold; font-size: 14px;">
            選択中の年月：{{ displayPickedMonth }}
        </span>
    </el-form-item>


    <!-- 第一层：企业，开始 -->
    <el-table :data="dataList" border style="width: 100%; text-align: center;" :row-key="getRowKey" :max-height="500">
        <el-table-column type="expand" fixed="left">
            <template #default="companyScope">
                <el-table :data="dataList[companyScope.$index].departmentList" border
                    style="margin-left: 50px;width: 90%">
                    <el-table-column type="expand" :row-key="getRowKey" fixed="left">
                        <template #default="departScope">
                            <el-table style="margin-left: 100px; width: 90%"
                                :data="dataList[companyScope.$index].departmentList[departScope.$index].projectList"
                                border :max-height="200">
                                <el-table-column label="番号">
                                    <template #default="scope">
                                        <span>{{ scope.$index + 1 }}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column label="案件略称" prop="projectNameAbbreviation" header-align="center"
                                    align="left"></el-table-column>
                                <el-table-column label="売上" prop="totalSalesAmount" header-align="center" align="right">
                                    <template #default="scope">
                                        <div>{{ scope.row.totalSalesAmount === "-9,999" ? "未入力" :
                                            scope.row.totalSalesAmount }}</div>
                                    </template>
                                </el-table-column>
                                <el-table-column label="清算金額" prop="previousMonthActuarialBalance" header-align="center"
                                    align="right"></el-table-column>
                                <!-- <el-table-column label="当月清算" prop="monthlySettlement" header-align="center" align="right"></el-table-column> -->
                                <el-table-column label="工数入力" prop="tradingStatus" header-align="center" align="center">
                                    <template #default="scope">
                                        <el-button :type="getButtonType(scope.row.tradingStatus, 1)" plain
                                            class="status-button" @click="workHoursInput(scope.row, dataList[companyScope.$index].departmentList[departScope.$index].departmentAbbreviation,
                                                dataList[companyScope.$index].companyAbbreviation)">{{
                                                    getText(scope.row.tradingStatus, 1) }}
                                        </el-button>
                                    </template>
                                </el-table-column>
                                <el-table-column label="総務入力" prop="generalAffairs" header-align="center" align="left">
                                    <template #default="scope">
                                        <div :style="{ color: getTextColor(scope.row.generalAffairs, 2) }">{{
                                            getText(scope.row.generalAffairs, 2) }}</div>
                                    </template>
                                </el-table-column>
                                <el-table-column label="営業確認" prop="businessConfirmation" header-align="center"
                                    align="left">
                                    <template #default="scope">
                                        <div :style="{ color: getTextColor(scope.row.businessConfirmation, 0) }">{{
                                            getText(scope.row.businessConfirmation, 0) }}
                                        </div>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </template>
                    </el-table-column>
                    <el-table-column type="index" header-align="center" align="center" width="100" label="番号"
                        fixed="left">
                        <template #default="scope">
                            <span>{{ scope.$index + 1 }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="departmentAbbreviation" label="部署略称" header-align="center" align="left">
                        <template #default="scope">
                            <span>{{ scope.row.departmentAbbreviation }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="departmentStaffNum" label="稼働人数" header-align="center"
                        align="right"></el-table-column>
                    <el-table-column prop="departmentTotalSalesAmount" label="売上" header-align="center"
                        align="right"></el-table-column>
                    <el-table-column prop="departmentPreviousMonthActuarialBalance" label="清算残高" header-align="center"
                        align="right"></el-table-column>
                    <el-table-column prop="departmentStatus" label="状態" header-align="center" align="left">
                        <template #default="scope">
                            <div :style="{ color: getTextColor(scope.row.departmentStatus, 1) }">{{
                                getText(scope.row.departmentStatus, 1) }}</div>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
        </el-table-column>
        <el-table-column type="index" header-align="center" align="center" width="100" label="番号" fixed="left">
            <template #default="scope">
                <span>{{ (page - 1) * length + scope.$index + 1 }}</span>
            </template>
        </el-table-column>
        <el-table-column label="企業略称" prop="companyAbbreviation" style="width: 200px;" header-align="center"
            align="left"></el-table-column>
        <el-table-column label="稼働人数" prop="companyStaffNum" style="width: 200px;" header-align="center"
            align="right"></el-table-column>
        <el-table-column label="売上合計" prop="companyTotalSalesAmount" style="width: 200px;" header-align="center"
            align="right"></el-table-column>
        <el-table-column label="状態" prop="companyStatus" style="width: 200px;" header-align="center" align="left">
            <template #default="scope">
                <div :style="{ color: getTextColor(scope.row.companyStatus, 1) }">{{ getText(scope.row.companyStatus, 1)
                }}</div>
            </template>
        </el-table-column>
    </el-table>
    <!-- 第一层：企业，结束 -->



    <!-- 翻页器 -->
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="page"
        :page-sizes="[10, 20, 50]" :page-size="length" :total="totalCount"
        layout="total, sizes, prev, pager, next, jumper"></el-pagination>

</template>

<script>
export default {
    data() {
        const now = new Date();
        const year = now.getFullYear();
        const month = now.getMonth() + 1;
        const day = now.getDate();

        let targetYear = year;
        let targetMonth = month;

        if (day < 16) {
            if (month === 1) {
                targetYear = year - 1;
                targetMonth = 12;
            } else {
                targetMonth = month - 1;
            }
        }

        const initialDate = `${targetYear}-${String(targetMonth).padStart(2, '0')}-01`;

        return {
            pickedDate: initialDate,
            length: 10,
            page: 1,
            totalCount: 0,
            dataList: [],
        };
    },


    methods: {
        handleDateChange(date) {
            if (date) {
                this.pickedDate = date;  // date will already be in YYYY-MM-01 format
                const [year, month] = date.split('-');
                this.displayPickedMonth = `${year}年${month}月`;
                this.loadDataList();
            }
        },
        disabledDate(time) {
            // Get current date
            const currentDate = new Date();
            // Set current date to first day of the month for comparison
            const currentMonthStart = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);
            // Disable dates after current month
            return time.getTime() > currentMonthStart.getTime();
        },
        loadDataList() {
            let that = this;
            let data = {
                date: that.pickedDate,
                length: that.length,
                page: 1,
            };
            // console.log(data);
            that.$httpV2('/trade/getByPage', "GET", data, false, function (resp) {
                that.page = resp.result.pageIndex;
                that.totalCount = resp.result.totalCount;
                if (!resp.result || !resp.result.list) {
                    that.dataList = []; // 如果后端返回 null，确保 dataList 是空数组
                    return;
                } else {
                    that.dataList = resp.result.list.map(company => ({
                        ...company,
                        companyTotalSalesAmount: company.companyTotalSalesAmount ? Number(company.companyTotalSalesAmount).toLocaleString() : "0",
                        companyStaffNum: company.companyStaffNum ? Number(company.companyStaffNum).toLocaleString() : "0",
                        departmentList: company.departmentList.map(department => ({
                            ...department,
                            departmentAbbreviation: `${department.departmentAbbreviation} ${department.principalName || ''}`,
                            departmentTotalSalesAmount: department.departmentTotalSalesAmount ? Number(department.departmentTotalSalesAmount).toLocaleString() : "0",
                            departmentPreviousMonthActuarialBalance: department.departmentPreviousMonthActuarialBalance ? Number(department.departmentPreviousMonthActuarialBalance).toLocaleString() : "0",
                            departmentStaffNum: department.departmentStaffNum ? Number(department.departmentStaffNum).toLocaleString() : "0",
                            projectList: department.projectList.map(project => ({
                                ...project,
                                totalSalesAmount: project.totalSalesAmount ? Number(project.totalSalesAmount).toLocaleString() : "0",
                                previousMonthActuarialBalance: project.previousMonthActuarialBalance ? Number(project.previousMonthActuarialBalance).toLocaleString() : "0",
                                // monthlySettlement: project.monthlySettlement ? Number(project.monthlySettlement).toLocaleString() : "0"
                            }))
                        }))
                    }));
                }
                console.log(that.pickedDate);
                if (resp.code === 200) {
                    that.submitProjectData();
                }

            });
        },
        // 改变分页大小
        sizeChangeHandle(val) {
            this.length = val;
            this.page = 1;
            this.loadDataList();
        },
        // 翻页
        currentChangeHandle: function (val) {
            this.page = val;
            this.loadDataList();
        },
        getRowKey(row) {
            return row.companyId || row.departmentId || row.projectId || '';
        },
        getText(flag, type) {
            // type == 0 ： 确定、未确定
            // type == 1 ： 未入力、入力中、入力完
            // flag == - : -
            if (type == 0 && flag == 0) {
                return '未確認';
            } else if (type == 0 && flag == 1) {
                return '確認';
            } else if (type == 1 && flag == 0) {
                return '未入力';
            } else if (type == 1 && flag == 1) {
                return '入力中';
            } else if (type == 1 && flag == 2) {
                return '入力済';
            } else if (type == 2 && flag == "-") {
                return '-';
            } else if (type == 2 && flag == 0) {
                return '未入力';
            } else if (type == 2 && flag == 1) {
                return '入力済';
            } else if (type == 2 && flag == 2) {
                return '入力中';
            } else {
                return '';
            }
        },
        //按钮与字体颜色
        getButtonType(flag, type) {
            // 未入力(0) 和 入力中(1) -> danger (红色)
            // 入力済(2) -> info (灰黑色)
            if (type == 1 && flag == 0) {
                return 'danger';
            } else if (type == 1 && flag == 1) {
                return 'danger';
            } else if (type == 1 && flag == 2) {
                return 'info';
            } else {
                return '';
            }
        },
        //状态的字体颜色
        getTextColor(flag, type) {
            // 入力中(2) 和 未入力(0) -> 红色
            // 入力済(1) -> 黑色
            if (type == 2 && flag == 0) {
                return 'red';
            } else if (type == 2 && flag == 2) {
                return 'red';
            } else if (type == 2 && flag === 1) {
                return 'black';
            } else if (type == 0 && flag == 0) {
                return 'red';
            } else if (type == 0 && flag == 1) {
                return 'black';
            } else if (type == 1 && flag == 0) {
                return 'red';
            } else if (type == 1 && flag == 1) {
                return 'red';
            } else if (type == 1 && flag == 2) {
                return 'black';
            } else {
                return '#909399'; // 默认灰色
            }
        },
        workHoursInput(row, departmentAbbreviation, companyAbbreviation) {
            console.log('pickedDate value:', this.pickedDate);
            if (!this.pickedDate) {
                this.$message.warning('対象年月を選択してください');
                return;
            }

            this.$router.push({
                name: 'WorkHoursInput',
                query: {
                    projectId: row.projectId,
                    projectNameAbbreviation: row.projectNameAbbreviation,
                    departmentAbbreviation: departmentAbbreviation,
                    companyAbbreviation: companyAbbreviation,
                    month: this.pickedDate,
                    businessConfirmation: row.businessConfirmation,
                    tradingStatus: row.tradingStatus
                }
            }).catch(err => {
                console.error('Navigation error:', err);
            });
        },
        submitProjectData() {
            let projectIds = [];

            // 遍历 dataList 获取所有项目的 projectId
            this.dataList.forEach(company => {
                company.departmentList.forEach(department => {
                    department.projectList.forEach(project => {
                        if (project.projectId) {
                            projectIds.push(Number(project.projectId)); // 确保是整数类型
                        }
                    });
                });
            });

            if (projectIds.length === 0) {
                this.$message.warning('没有可提交的项目ID');
                return;
            }

            let requestData = {
                projectIds: projectIds, // 传递项目 ID 数组
                month: this.pickedDate  // 选择的月份
            };

            // 发送 POST 请求
            this.$httpV2('/trade/doData', "POST", requestData, false, (resp) => {

            });
        }
    },
    created() {
        if (!this.pickedDate) {
            // If somehow pickedDate is not set, set it to current month's first day
            const currentDate = new Date();
            this.pickedDate = currentDate.getFullYear() + '-' +
                String(currentDate.getMonth() + 1).padStart(2, '0') + '-01';
        }
        const [year, month] = this.pickedDate.split('-');
        this.displayPickedMonth = `${year}年${month}月`;
        this.loadDataList();
    }
}
</script>
<style>
.table-container {
    overflow-y: auto;
    /* 垂直滚动 */
}

.underlined-link {
    text-decoration: underline;
    text-underline-offset: 2px;
    /* 调整下划线与文本的距离 */
    color: #409eff !important;
    /* 颜色保持和其他蓝色文本一致 */
    font-weight: bold !important;
    /* 加粗 */
    font-size: 14px;
    /* 适当调整字体大小 */
    line-height: 1.3;
    /* 增加行高，使下划线更明显 */
    text-decoration-thickness: 1px;
    /* 让下划线更细 */
}

.status-button {
    font-weight: bold;
}
</style>