<template>
    <el-dialog :title="dataForm.projectInfo.projectId == null || dataForm.projectInfo.projectId == '' ? '追加' : '変更'"
        :close-on-click-modal="false" v-model="visible" width="850px" @close="closeDialog">
        <div>
            <el-form :model="dataForm" :rules="dataRule" ref="dataForm">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="企業" prop="salesCompanyAbbreviation">
                            <el-input v-model="dataForm.salesCompanyAbbreviation" placeholder="企業略称"
                                :disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="部署" prop="departmentAbbreviation">
                            <el-input
                                :value="`${dataForm.departmentAbbreviation}${dataForm.principalName ? '（' + dataForm.principalName + '）' : ''}`"
                                placeholder="部署略称" :disabled="true"></el-input>
                        </el-form-item>
                    </el-col>

                </el-row>
                <el-form-item>
                    <el-form :model="dataForm.projectInfo" :rules="dataFormProRules" ref="dataFormProject">
                        <el-row :gutter="20">
                            <el-col :span="12">
                                <el-form-item label="案件名" prop="projectName">
                                    <el-input v-model="dataForm.projectInfo.projectName" placeholder="案件名" clearable
                                        @input="limitFullWidthLength(dataForm.projectInfo, 'projectName', 20)"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="案件略称" prop="projectNameAbbreviation">
                                    <el-input v-model="dataForm.projectInfo.projectNameAbbreviation" placeholder="案件略称"
                                        clearable
                                        @input="limitFullWidthLength(dataForm.projectInfo, 'projectNameAbbreviation', 10)"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="20">
                            <el-col :span="8">
                                <el-form-item label="案件開始日" prop="projectStartDate">
                                    <el-date-picker v-model="dataForm.projectInfo.projectStartDate" type="date"
                                        placeholder="案件開始年月日を選択してください" value-format="YYYY-MM-DD" format="YYYY-MM-DD"
                                        clearable style="width: 100%" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="案件予定終了日" prop="projectScheduledEndDate">
                                    <el-date-picker v-model="dataForm.projectInfo.projectScheduledEndDate" type="date"
                                        placeholder="案件予定終了日を選択してください" value-format="YYYY-MM-DD" format="YYYY-MM-DD"
                                        clearable style="width: 100%" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="案件終了日" prop="projectEndDate">
                                    <el-date-picker v-model="dataForm.projectInfo.projectEndDate" type="date"
                                        placeholder="案件終了日を選択してください" value-format="YYYY-MM-DD" format="YYYY-MM-DD"
                                        clearable style="width: 100%" />
                                </el-form-item>
                            </el-col>
                        </el-row>

                        <el-row :gutter="20">
                            <el-col :span="8">
                                <el-form-item label="就業時間" prop="startTime">
                                    <el-input v-model="dataForm.projectInfo.startTime" placeholder="HH:mm" clearable
                                        maxlength="5"
                                        @input="formatTimeInput(dataForm.projectInfo, 'startTime')"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="1">
                                <el-form-item label="-"></el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item prop="endTime">
                                    <el-input v-model="dataForm.projectInfo.endTime" placeholder="終了時:HH:mm" clearable
                                        maxlength="5"
                                        @input="formatTimeInput(dataForm.projectInfo, 'endTime')"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="日次稼働時間" prop="dailyOperatingHours">
                                    <el-input v-model="dataForm.projectInfo.dailyOperatingHours" placeholder="日次稼働時間"
                                        clearable></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="20">
                            <el-col :span="10">
                                <el-form-item label="清算幅：販売上限" prop="settlementUpperLimit">
                                    <el-input v-model="dataForm.projectInfo.settlementUpperLimit" placeholder="上限"
                                        clearable @input="formatleangth(dataForm.projectInfo, 'settlementUpperLimit')"
                                        @focus="removeCommas(dataForm.projectInfo, 'settlementUpperLimit')"
                                        @blur="formatWithCommas(dataForm.projectInfo, 'settlementUpperLimit')"
                                        style="ime-mode: disabled;" inputmode="numeric"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="販売下限" prop="settlementLowerLimit">
                                    <el-input v-model="dataForm.projectInfo.settlementLowerLimit" placeholder="下限"
                                        clearable @input="formatleangth(dataForm.projectInfo, 'settlementLowerLimit')"
                                        @focus="removeCommas(dataForm.projectInfo, 'settlementLowerLimit')"
                                        @blur="formatWithCommas(dataForm.projectInfo, 'settlementLowerLimit')"
                                        style="ime-mode: disabled;" inputmode="numeric"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>

                        <el-row :gutter="20">
                            <el-col :span="24">
                                <el-form-item label="コメント" prop="comment">
                                    <el-input v-model="dataForm.projectInfo.comment" placeholder="摘要" clearable
                                        type="textarea" :rows="3" maxlength="200" :show-word-limit="true"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </el-form-item>

                <!-- 动态展示要员信息表单 -->

                <el-scrollbar class="staff-form-container" always style="overflow:auto">
                    <el-form-item v-for="(item, index) in dataForm.projectDetails" :key="index" class="sub-form">
                        <!-- <div class="staff-title">要員情報{{ index + 1 }}</div> -->
                        <div class="staff-title">要員情報{{ index + 1 }}
                            <span>
                                <el-button type="danger" :disabled="item.isSystemData"
                                    @click="removeSubForm(index)">削除</el-button>
                            </span>
                        </div>
                        <el-form :model="item" :ref="'staffForm' + index" :rules="staffRule(index)">
                            <!-- 会社和要员以下拉框的形式展示 -->
                            <el-row :gutter="20">
                                <el-col :span="12">
                                    <el-form-item label="会社名" prop="companyAbbreviation">
                                        <el-select v-model="item.companyId" clearable placeholder="選択してください"
                                            @change="handleCompanyChange(index, item.companyId)"
                                            @clear="handleCompanyClear">
                                            <el-option v-for="companyi in companyList" :key="companyi.companyId"
                                                :label="companyi.companyAbbreviation" :value="companyi.companyId" />
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="要員名" prop="staffName">
                                        <el-select v-model="item.staffId" clearable placeholder="要員名を選択してください"
                                            @change="handleStaffChange(index, item.staffId)"
                                            :disabled="item.staffDisabled">
                                            <el-option v-for="staff in filteredStaffLists[index]" :key="staff.staffId"
                                                :label="staff.staffName" :value="staff.staffId"
                                                :disabled="selectedStaffIds.includes(staff.staffId)" />
                                        </el-select>
                                        <!-- 警告框 -->
                                        <el-dialog v-model="dialogVisible" title="警告メッセージ">
                                            <span>該当する期間は他プロジェクトにアサインされています。登録しますか？</span>
                                            <template #footer>
                                                <el-button @click="cancelSelection()">キャンセル</el-button>
                                                <el-button type="primary" @click="confirmSelection()">確定</el-button>
                                            </template>
                                        </el-dialog>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="8">
                                    <el-form-item label="入場日" prop="entryDate">
                                        <el-date-picker v-model="item.entryDate" type="date" placeholder="入場日"
                                            value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable
                                            style="width: 100%" />
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="退場予定日" prop="plannedExitDate">
                                        <el-date-picker v-model="item.plannedExitDate" type="date" placeholder="退場予定日"
                                            :disabled-date="() => false"
                                            value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable
                                            style="width: 100%" />
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="退場日" prop="actualExitDate">
                                        <el-date-picker v-model="item.actualExitDate" type="date" placeholder="退場日"
                                            :disabled-date="() => false"
                                            value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable
                                            style="width: 100%" />
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="6">
                                    <el-form-item label="販売金額" prop="salesAmount">
                                        <el-input v-model="item.salesAmount" placeholder="販売金額"
                                            @input="filterInput(item, 'salesAmount')"
                                            @focus="removeCommas(item, 'salesAmount')"
                                            @blur="formatWithCommas(item, 'salesAmount')" style="ime-mode: disabled;"
                                            inputmode="numeric"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="9">
                                    <el-form-item label="販売増単金（時間）" prop="salesIncrementUnitPriceHour">
                                        <el-input v-model="item.salesIncrementUnitPriceHour" placeholder="販売増単金"
                                            @input="filterInput(item, 'salesIncrementUnitPriceHour')"
                                            @focus="removeCommas(item, 'salesIncrementUnitPriceHour')"
                                            @blur="formatWithCommas(item, 'salesIncrementUnitPriceHour')"
                                            style="ime-mode: disabled;" inputmode="numeric"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="9">
                                    <el-form-item label="販売減単金（時間）" prop="salesDecrementUnitPriceHour">
                                        <el-input v-model="item.salesDecrementUnitPriceHour" placeholder="販売減単金"
                                            @input="filterInput(item, 'salesDecrementUnitPriceHour')"
                                            @focus="removeCommas(item, 'salesDecrementUnitPriceHour')"
                                            @blur="formatWithCommas(item, 'salesDecrementUnitPriceHour')"
                                            style="ime-mode: disabled;" inputmode="numeric"></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="6">
                                    <el-form-item label="調達金額" prop="amountRaised">
                                        <el-input
                                            :value="item.companyAbbreviation === 'UCL' ? item._displayAmountRaised : item.amountRaised"
                                            v-model="item.amountRaised" placeholder="調達金額"
                                            @input="item.companyAbbreviation !== 'UCL' && (item.amountRaised = $event) && filterInput(item, 'amountRaised')"
                                            @focus="item.companyAbbreviation !== 'UCL' && removeCommas(item, 'amountRaised')"
                                            @blur="item.companyAbbreviation !== 'UCL' && formatWithCommas(item, 'amountRaised')"
                                            :disabled="item.amountRaisedDisabled" style="ime-mode: disabled;"
                                            inputmode="numeric"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="9">
                                    <el-form-item label="調達増単金（時間）" prop="procurementIncrementUnitPriceHour">
                                        <el-input
                                            :value="item.companyAbbreviation === 'UCL' ? item._displayProcurementIncrementUnitPriceHour : item.procurementIncrementUnitPriceHour"
                                            placeholder="調達増単金" v-model="item.procurementIncrementUnitPriceHour"
                                            @input="item.companyAbbreviation !== 'UCL' && (item.procurementIncrementUnitPriceHour = $event) && filterInput(item, 'procurementIncrementUnitPriceHour')"
                                            @focus="item.companyAbbreviation !== 'UCL' && removeCommas(item, 'procurementIncrementUnitPriceHour')"
                                            @blur="item.companyAbbreviation !== 'UCL' && formatWithCommas(item, 'procurementIncrementUnitPriceHour')"
                                            :disabled="item.amountRaisedDisabled" style="ime-mode: disabled;"
                                            inputmode="numeric"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="9">
                                    <el-form-item label="調達減単金（時間）" prop="procurementDecrementUnitPriceHour">
                                        <el-input
                                            :value="item.companyAbbreviation === 'UCL' ? item._displayProcurementDecrementUnitPriceHour : item.procurementDecrementUnitPriceHour"
                                            placeholder="調達減単金" v-model="item.procurementDecrementUnitPriceHour"
                                            @input="item.companyAbbreviation !== 'UCL' && (item.procurementDecrementUnitPriceHour = $event) && filterInput(item, 'procurementDecrementUnitPriceHour')"
                                            @focus="item.companyAbbreviation !== 'UCL' && removeCommas(item, 'procurementDecrementUnitPriceHour')"
                                            @blur="item.companyAbbreviation !== 'UCL' && formatWithCommas(item, 'procurementDecrementUnitPriceHour')"
                                            :disabled="item.amountRaisedDisabled" style="ime-mode: disabled;"
                                            inputmode="numeric"></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="12">
                                    <el-form-item label="調達時間上限" prop="upperLimit">
                                        <el-input
                                            :value="item.companyAbbreviation === 'UCL' ? item._displayUpperLimit : item.upperLimit"
                                            v-model="item.upperLimit" placeholder="上限" clearable maxlength="3"
                                            @input="item.companyAbbreviation !== 'UCL' && formatleangth(item, 'upperLimit')"
                                            @focus="removeCommas(item, 'upperLimit')"
                                            @blur="formatWithCommas(item, 'upperLimit')" inputmode="numeric"
                                            :disabled="item.amountRaisedDisabled"></el-input>

                                    </el-form-item>
                                </el-col>

                                <el-col :span="12">
                                    <el-form-item label="調達時間下限" prop="lowerLimit">
                                        <el-input
                                            :value="item.companyAbbreviation === 'UCL' ? item._displayLowerLimit : item.lowerLimit"
                                            v-model="item.lowerLimit" placeholder="下限" clearable maxlength="3"
                                            @input="item.companyAbbreviation !== 'UCL' && formatleangth(item, 'lowerLimit')"
                                            @focus="removeCommas(item, 'lowerLimit')"
                                            @blur="formatWithCommas(item, 'lowerLimit')" inputmode="numeric"
                                            :disabled="item.amountRaisedDisabled"></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                        </el-form>
                    </el-form-item>
                </el-scrollbar>
            </el-form>
            <div class="actions">
                <el-button type="success" @click="addStaffForm">要員追加</el-button>
                <!-- <el-button type="danger" @click="removeSubForm(index)">删除</el-button> -->
            </div>

        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="visible = false">キャンセル</el-button>
                <el-button type="primary" @click="debouncedSubmit">確定</el-button>
            </span>
        </template>

    </el-dialog>
</template>

<script>
import { debounce } from "lodash";
export default {
    data() {
        return {
            visible: false,
            //警告框
            dialogVisible: false,
            //存放临时选择的要员id，用来给警告框进行判断
            tempSelectedStaffId: null,
            //临时存放的index值,仅用于在警告框中使用
            selectedIndex: null,
            dataForm: {
                salesCompanyAbbreviation: null,
                departmentAbbreviation: null,
                projectInfo: {
                    salesCompanyId: null,
                    departmentId: null,
                    projectId: null,
                    projectName: "",
                    projectNameAbbreviation: null,
                    projectStartDate: null,
                    projectScheduledEndDate: null,
                    projectEndDate: null,
                    startTime: null,
                    endTime: null,
                    dailyOperatingHours: null,
                    settlementUpperLimit: null,
                    settlementLowerLimit: null,

                    comment: null,
                },

                projectDetails: [
                    {
                        companyId: null,
                        companyAbbreviation: "",
                        staffId: null,
                        staffName: "",
                        entryDate: null,
                        plannedExitDate: null,
                        actualExitDate: null,
                        salesAmount: null,
                        salesIncrementUnitPriceHour: null,
                        salesDecrementUnitPriceHour: null,
                        amountRaised: null,
                        procurementIncrementUnitPriceHour: null,
                        procurementDecrementUnitPriceHour: null,
                        upperLimit: null,
                        lowerLimit: null,

                        staffDisabled: true,
                        isSystemData: false,//删除按钮禁用标识
                    }
                ],
            },

            dataRule: {
                salesCompanyAbbreviation: [{ required: true, message: '企業を選択してください', trigger: 'change' }],
                departmentAbbreviation: [{ required: true, message: '部署を選択してください', trigger: 'change' }],
            },
            dataFormProRules: {
                projectName: [
                    { required: true, message: '案件名を入力してください', trigger: 'blur' },
                    { validator: this.validateProjectName, trigger: "blur" } // **支持 Promise**
                ],
                projectNameAbbreviation: [
                    { required: true, message: '案件略称を入力してください', trigger: 'blur' },
                ],
                projectStartDate: [
                    { required: true, message: '案件開始日を入力してください', trigger: 'change' },
                ],
                dailyOperatingHours: [
                    { required: true, message: '日次稼働時間を入力してください', trigger: 'blur' },
                    { validator: this.validateDailyOperatingHours, trigger: 'blur' }
                ],
                settlementUpperLimit: [
                    { required: true, message: '清算幅販売上限を入力してください', trigger: 'blur' }
                ],
                settlementLowerLimit: [
                    { required: true, message: '販売下限を入力してください', trigger: 'blur' }
                ],
            },


            //公司下拉列表
            companyList: [],
            //下拉框过滤的公司列表
            filterCompanyNames: [],
            //下拉框要员列表
            staffList: [],
            //下拉框过滤的人员列表
            fillterStaffNames: [],



        }
    },
    computed: {
        selectedStaffIds() {
            return this.dataForm.projectDetails
                .map(item => item.staffId)  // 获取已选中的员工 ID
                .filter(id => id !== null); // 过滤掉 null
        },
        filteredStaffLists() {
            if (!this.dataForm || !this.dataForm.projectDetails) return []; // 确保 dataForm 存在
            return this.dataForm.projectDetails.map(item => {
                if (!item.companyId || !this.companyList) return []; // 没有公司时，返回空数组
                const selectedCompany = this.companyList.find(company => company.companyId === item.companyId);
                return selectedCompany ? selectedCompany.staff || [] : [];
            });
        }
    },
    mounted() {
        this.formatAllMoneyFields();
        this.companyList = this.companyList || []; // 确保 companyList 不是 undefined
        console.log("companyList:", this.companyList); // 调试日志
        this.$nextTick(() => {
            this.formatWithCommas(this.dataForm.projectInfo, 'settlementUpperLimit');
            this.formatWithCommas(this.dataForm.projectInfo, 'settlementLowerLimit');

            this.dataForm.projectDetails.forEach((item) => {
                this.formatWithCommas(item, 'salesAmount');
                this.formatWithCommas(item, 'salesIncrementUnitPriceHour');
                this.formatWithCommas(item, 'salesDecrementUnitPriceHour');
                this.formatWithCommas(item, 'amountRaised');
                this.formatWithCommas(item, 'procurementIncrementUnitPriceHour');
                this.formatWithCommas(item, 'procurementDecrementUnitPriceHour');
                this.formatWithCommas(item, 'upperLimit');
                this.formatWithCommas(item, 'lowerLimit');
            });
        });
    },
    created() {
        this.debouncedSubmit = debounce(this.submitAllForms, 1000);
        // 这里去掉了 return Promise
        this.validateProjectNameDebounced = debounce((rule, value, callback) => {
            this.validateProjectName(rule, value, callback);
        }, 500);
    },
    methods: {


        staffRule(index) {
            return {
                companyAbbreviation: [
                    { required: true, message: '会社を選択してください', trigger: 'change' },
                ],
                staffId: [
                    { required: true, message: '要員を選択してください', trigger: 'change' },
                ],
                entryDate: [
                    { required: true, message: '入場日を選択してください', trigger: 'change' },
                ],
                plannedExitDate: [
                    { required: false },
                    {
                        validator: (rule, value, callback) => this.validateYearMonth(rule, value, callback, index),
                        trigger: 'change'
                    }
                ],
                actualExitDate: [
                    { required: false },
                    {
                        validator: (rule, value, callback) => this.validateYearMonth(rule, value, callback, index),
                        trigger: 'change'
                    }
                ],
                salesAmount: [
                    { required: true, message: '販売金額を入力してください', trigger: 'blur' },
                ],
                amountRaised: [
                    { required: true, message: '調達金額を入力してください', trigger: 'blur' },
                ],
                salesDecrementUnitPriceHour: [
                    { required: true, message: '販売増単金を入力してください', trigger: 'blur' },
                ],
                salesIncrementUnitPriceHour: [
                    { required: true, message: '販売減単金を入力してください', trigger: 'blur' },
                ],
                procurementIncrementUnitPriceHour: [
                    { required: true, message: '調達増単金を入力してください', trigger: 'blur' },
                ],
                procurementDecrementUnitPriceHour: [
                    { required: true, message: '調達減単金を入力してください', trigger: 'blur' },
                ],
                lowerLimit: [
                    { required: true, message: '調達時間下限を入力してください', trigger: 'blur' }
                ],
                upperLimit: [
                    { required: true, message: '調達時間上限を入力してください', trigger: 'blur' }
                ],
            };
        },

        filterInput(row, field) {
            if (!row[field]) return;

            // **转换全角数字为半角**
            row[field] = row[field].replace(/[０-９]/g, (s) =>
                String.fromCharCode(s.charCodeAt(0) - 0xfee0)
            );

            // **删除所有英文字母**
            row[field] = row[field].replace(/[A-Za-z]/g, "");

            // **删除非数字字符（仅保留数字）**
            row[field] = row[field].replace(/[^0-9]/g, "");

            if (!row[field] || isNaN(Number(row[field]))) {
                row[field] = "0"; // 避免误清空
            }
        },


        formatleangth(row, field) {
            if (!row[field]) return;

            // **转换全角数字为半角**
            row[field] = row[field].replace(/[０-９]/g, (s) =>
                String.fromCharCode(s.charCodeAt(0) - 0xfee0)
            );

            // **删除所有英文字母**
            row[field] = row[field].replace(/[A-Za-z]/g, "");

            // **删除非数字字符（仅保留数字）**
            row[field] = row[field].replace(/[^0-9]/g, "");

            // **限制最大长度（最多 3 位）**
            if (row[field].length > 3) {
                row[field] = row[field].slice(0, 3);
            }

            // **如果值为空或 NaN，则设为 "0"**
            if (!row[field] || isNaN(Number(row[field]))) {
                row[field] = "0";
            }
        },
        validateYearMonth(rule, value, callback, index) {
            if (!value) return callback();

            // 编辑时允许选择当前日期之前的日期
            const projectId = this.dataForm?.projectInfo?.projectId;
            const isEdit = projectId !== null && projectId !== undefined && projectId !== "";
            if (isEdit) {
                return callback();
            }

            const [year, month, day] = value.split("-").map(Number);
            const selectedDate = new Date(year, month - 1, day);
            const today = new Date();

            const selectedTime = new Date(selectedDate.getFullYear(), selectedDate.getMonth(), selectedDate.getDate()).getTime();
            const todayTime = new Date(today.getFullYear(), today.getMonth(), today.getDate()).getTime();

            const item = this.dataForm.projectDetails[index];
            const field = rule.field;
            const originalValue = item?.[field + '_original'];
            const isSameAsOriginal = value === originalValue;

            if (selectedTime < todayTime && !isSameAsOriginal) {
                item[field] = null;
                this.$message?.warning?.("過去の日付は選択できません");
                return callback(new Error("過去の日付は選択できません"));
            }


            if (!originalValue && selectedTime >= todayTime) {
                item[field + '_original'] = value;
            }

            callback();
        },
        limitFullWidthLength(obj, field, maxLen) {
            if (!obj[field]) return;
            let str = obj[field];
            let result = "";
            let len = 0;

            for (const ch of str) {
                const charLen = ch.charCodeAt(0) > 255 ? 2 : 1; // 全角为2，半角为1
                if (len + charLen > maxLen * 2) break;
                result += ch;
                len += charLen;
            }

            obj[field] = result;
        },




        formatTimeInput(item, field) {
            let value = item[field].replace(/[^0-9:]/g, ''); // 只保留数字和冒号
            if (value.length > 5) {
                value = value.slice(0, 5); // 限制最大长度
            }

            // 自动补全 ":"（如果用户输入 12 就变成 12:）
            if (value.length === 2 && !value.includes(":")) {
                value += ":";
            }

            // 正则验证
            const timeRegex = /^([01]\d|2[0-3]):[0-5]\d$/;
            if (!timeRegex.test(value) && value.length === 5) {
                this.$message.warning("時間のフォーマットが間違っています (HH:mm)");
            }

            item[field] = value;
        },

        /** 失去焦点时：给数字添加千位分隔符 */
        formatWithCommas(row, field) {
            const value = row[field];

            // 忽略 "-" 或空值
            if (value === "-" || value === null || value === undefined || value === "") return;

            // 统一去掉逗号后再转数字
            const numericValue = Number(value.toString().replace(/,/g, ""));
            if (!isNaN(numericValue)) {
                row[field] = numericValue.toLocaleString(); // 添加千位分隔符
            }
        },

        /** 获得焦点时：去掉千位分隔符，恢复纯数字 */
        removeCommas(row, field) {
            if (!row[field]) {
                return;
            }
            row[field] = row[field].toString().replace(/,/g, ""); // 去掉千位分隔符
        },
        validateDailyOperatingHours(rule, value, callback) {
            if (!value) {
                callback(); // 允许空值
                return;
            }

            const numValue = parseFloat(value);
            if (isNaN(numValue)) {
                callback(new Error('数値を入力してください'));
            } else if (numValue > 23) {
                callback(new Error('日次稼働時間は23未満である必要があります'));
            } else {
                callback();
            }
        },


        //初始化页面
        init: function (projectInfo) {
            let that = this;
            that.reset();
            that.$nextTick(() => {
                that.$refs["dataForm"].resetFields();
                that.$refs["dataFormProject"]?.resetFields();
                that.dataForm.projectDetails.forEach((_, index) => {
                    const formRef = that.$refs['staffForm' + index];
                    if (formRef) {
                        (formRef[0] || formRef).resetFields();
                    }
                });

                if (projectInfo.projectId != null && projectInfo.projectId != "") {
                    const now = new Date();
                    const fallbackStartDate = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-01`;
                    let data = {
                        projectId: projectInfo.projectId,
                        salesCompanyId: projectInfo.salesCompanyId,
                        departmentId: projectInfo.departmentId,
                        startDate: projectInfo.startDate || fallbackStartDate,
                    };

                    that.$httpV2("/projectDetail/init/", "GET", data, true, function (resp) {
                        if (resp.code == 200) {
                            that.companyList = resp.result.laborCompaniesWithStaff || [];
                            const result = resp.result;

                            that.dataForm.departmentAbbreviation = result.departmentAbbreviation;
                            that.dataForm.principalName = result.principalName;
                            that.dataForm.salesCompanyAbbreviation = result.salesCompanyAbbreviation;
                            that.dataForm.projectInfo = result.projectInfo;
                            //that.dataForm.projectDetails = result.projectDetails;
                            that.dataForm.projectDetails = result.projectDetails.map((item) => ({
                                ...item,
                                isSystemData: true,
                                plannedExitDate_original: item.plannedExitDate,
                                actualExitDate_original: item.actualExitDate,
                            }));


                            // 先处理UCL公司的特殊情况
                            that.dataForm.projectDetails.forEach((item) => {
                                // 直接根据后端返回的数据判断是否为UCL公司
                                if (item.companyName === "UCL") {
                                    // BigDecimal类型字段设置为-9999
                                    item.amountRaised = -9999;
                                    item.procurementIncrementUnitPriceHour = -9999;
                                    item.procurementDecrementUnitPriceHour = -9999;
                                    // time类型字段设置为"00:00"
                                    //item.upperLimit = "00:00";
                                    //item.lowerLimit = "00:00";

                                    // 在界面上显示为"-"
                                    item._displayAmountRaised = "-";
                                    item._displayProcurementIncrementUnitPriceHour = "-";
                                    item._displayProcurementDecrementUnitPriceHour = "-";
                                    item._displayUpperLimit = "-";
                                    item._displayLowerLimit = "-";

                                    item.amountRaisedDisabled = true;
                                }
                            });

                            // 初始化公司和要員的下拉框数据
                            that.dataForm.projectDetails.forEach((item) => {
                                const selectedCompany = that.companyList.find(company => company.companyId === item.companyId);
                                if (selectedCompany) {
                                    item.companyAbbreviation = selectedCompany.companyAbbreviation; // 设置公司名
                                    item.filteredStaffList = selectedCompany.staff; // 只显示该公司的要員

                                    // 设置默认选中的要員
                                    const selectedStaff = selectedCompany.staff.find(staff => staff.staffId === item.staffId);
                                    if (selectedStaff) {
                                        item.staffName = selectedStaff.staffName;
                                        item.staffId = selectedStaff.staffId;
                                        item.noActiveProjects = selectedStaff.noActiveProjects;
                                    }
                                } else {
                                    item.filteredStaffList = [];// 如果公司不存在，设为空数组
                                }

                                // 根据公司是否存在，判断要员下拉框是否禁用
                                if (item.companyAbbreviation != null && item.companyAbbreviation != "") {
                                    item.staffDisabled = false;
                                } else {
                                    item.staffDisabled = true;
                                }
                                item.staffDisabled = !(item.companyAbbreviation && item.companyAbbreviation !== "");
                                // 处理UCL公司的特殊情况
                                if (item.companyAbbreviation === "UCL") {
                                    item.amountRaised = "-";
                                    item.procurementIncrementUnitPriceHour = "-";
                                    item.procurementDecrementUnitPriceHour = "-";
                                    item.upperLimit = "-";
                                    item.lowerLimit = "-";
                                    item.amountRaisedDisabled = true;
                                } else {
                                    item.amountRaisedDisabled = false;
                                }
                            });

                            // 格式化金额字段
                            that.formatWithCommas(that.dataForm.projectInfo, 'settlementUpperLimit');
                            that.formatWithCommas(that.dataForm.projectInfo, 'settlementLowerLimit');

                            that.dataForm.projectDetails.forEach((item) => {
                                if (item.companyAbbreviation !== "UCL") {  // 只对非UCL公司的数据进行格式化
                                    that.formatWithCommas(item, 'salesAmount');
                                    that.formatWithCommas(item, 'salesIncrementUnitPriceHour');
                                    that.formatWithCommas(item, 'salesDecrementUnitPriceHour');
                                    that.formatWithCommas(item, 'amountRaised');
                                    that.formatWithCommas(item, 'procurementIncrementUnitPriceHour');
                                    that.formatWithCommas(item, 'procurementDecrementUnitPriceHour');
                                    that.formatWithCommas(item, 'upperLimit');
                                    that.formatWithCommas(item, 'lowerLimit');
                                }
                            });
                        }
                    });
                }
            });

            // 再次调用一次格式化，确保默认空白数据也有千位符格式（可选）
            that.formatAllMoneyFields();
            that.visible = true;
        },

        reset: function () {
            let dataForm = {
                salesCompanyAbbreviation: null,
                departmentAbbreviation: null,
                projectInfo: {
                    salesCompanyId: null,
                    departmentId: null,
                    projectId: null,
                    projectName: "",
                    projectNameAbbreviation: null,
                    projectStartDate: null,
                    projectScheduledEndDate: null,
                    projectEndDate: null,
                    startTime: null,
                    endTime: null,
                    dailyOperatingHours: null,
                    settlementUpperLimit: null,
                    settlementLowerLimit: null,

                    comment: null,
                },
                projectDetails: [{
                    companyId: null,
                    companyAbbreviation: "",
                    staffId: null,
                    staffName: "",
                    entryDate: null,
                    plannedExitDate: null,
                    actualExitDate: null,
                    plannedExitDate_original: null,
                    actualExitDate_original: null,
                    salesAmount: null,
                    salesIncrementUnitPriceHour: null,
                    salesDecrementUnitPriceHour: null,
                    amountRaised: null,
                    procurementIncrementUnitPriceHour: null,
                    procurementDecrementUnitPriceHour: null,
                    //员工禁用标识
                    staffDisabled: true,
                    upperLimit: null,
                    lowerLimit: null,
                    amountRaisedDisabled: false,
                    isSystemData: false,//删除按钮
                },
                ],

            }
            this.dataForm = dataForm;
            this.staffList = [];

            // 确保在重置后也处理UCL公司的特殊情况
            this.$nextTick(() => {
                this.formatAllMoneyFields();
                if (this.dataForm.projectDetails) {
                    this.dataForm.projectDetails.forEach(item => {
                        if (item.companyAbbreviation === "UCL") {
                            item.amountRaised = "-";
                            item.procurementIncrementUnitPriceHour = "-";
                            item.procurementDecrementUnitPriceHour = "-";
                            item.upperLimit = "-";
                            item.lowerLimit = "-";
                            item.amountRaisedDisabled = true;
                        }
                    });
                }
            });
        },
        /** 弹窗关闭时清理 */
        closeDialog() {
            this.visible = false;
            this.reset();
        },
        addStaffForm() {
            // 动态添加一个空白的二级表单
            this.dataForm.projectDetails.push({
                companyId: null,
                companyAbbreviation: '',
                staffId: null,
                staffName: "",
                entryDate: null,
                plannedExitDate: null,
                actualExitDate: null,
                salesAmount: null,
                salesIncrementUnitPriceHour: null,
                salesDecrementUnitPriceHour: null,
                amountRaised: null,
                procurementIncrementUnitPriceHour: null,
                procurementDecrementUnitPriceHour: null,
                staffDisabled: true,
                isSystemData: false,//删除按钮
            });
        },
        addStaffForm() {
            this.dataForm.projectDetails.push({
                companyId: null,
                companyName: '',
                staffId: null,
                staffName: "",
                entryDate: null,
                plannedExitDate: null,
                actualExitDate: null,
                salesAmount: null,
                salesIncrementUnitPriceHour: null,
                salesDecrementUnitPriceHour: null,
                amountRaised: null,
                procurementIncrementUnitPriceHour: null,
                procurementDecrementUnitPriceHour: null,
                staffDisabled: true,
                upperLimit: null,
                lowerLimit: null,
                amountRaisedDisabled: false,
            });

            // 👉 添加格式化千位符（新增项初始显示也有千位符）
            this.$nextTick(() => {
                const newItem = this.dataForm.projectDetails[this.dataForm.projectDetails.length - 1];
                this.formatWithCommas(newItem, 'salesAmount');
                this.formatWithCommas(newItem, 'salesIncrementUnitPriceHour');
                this.formatWithCommas(newItem, 'salesDecrementUnitPriceHour');
                this.formatWithCommas(newItem, 'amountRaised');
                this.formatWithCommas(newItem, 'procurementIncrementUnitPriceHour');
                this.formatWithCommas(newItem, 'procurementDecrementUnitPriceHour');
                this.formatWithCommas(newItem, 'upperLimit');
                this.formatWithCommas(newItem, 'lowerLimit');
            });
        },
        //删除要员
        removeSubForm(index) {
            if (this.dataForm.projectDetails.length == 1) {
                this.$message({
                    type: "warning",
                    message: "少なくとも1件の要員情報を入力してください。",
                });
                return;
            } else {
                this.dataForm.projectDetails.splice(index, 1);
            }

        },
        handleCompanyChange(index, companyId) {
            const selectedCompany = (this.companyList || []).find(company => company.companyId === companyId);
            if (companyId) {
                const selectedCompany = this.companyList.find(company => company.companyId === companyId);
                this.staffList = selectedCompany.staff;
                // console.log(this.staffList)
                if (selectedCompany) {

                    if (selectedCompany.companyId != this.dataForm.projectDetails[index].companyId) {
                        this.$nextTick(() => {
                            this.dataForm.projectDetails[index].staffId = null;
                        });
                    }
                    this.dataForm.projectDetails[index].companyAbbreviation = selectedCompany.companyAbbreviation;
                    this.dataForm.projectDetails[index].companyId = selectedCompany.companyId;
                    this.dataForm.projectDetails[index].staffDisabled = false;

                    //**如果当前已选的 staffId 不在新公司的 staffList 里，则清空**
                    const selectedStaff = this.filteredStaffLists[index].find(staff => staff.staffId === this.dataForm.projectDetails[index].staffId);
                    if (!selectedStaff) {
                        this.dataForm.projectDetails[index].staffId = null;
                        this.dataForm.projectDetails[index].staffName = "";
                    }

                    // **处理UCL公司的特殊情况**
                    if (selectedCompany.companyAbbreviation === "UCL") {
                        // BigDecimal类型字段设置为-9999
                        this.dataForm.projectDetails[index].amountRaised = -9999;
                        this.dataForm.projectDetails[index].procurementIncrementUnitPriceHour = -9999;
                        this.dataForm.projectDetails[index].procurementDecrementUnitPriceHour = -9999;
                        // time类型字段设置为"00:00"
                        this.dataForm.projectDetails[index].upperLimit = 0;
                        this.dataForm.projectDetails[index].lowerLimit = 0;
                        this.dataForm.projectDetails[index].amountRaisedDisabled = true;

                        // 在界面上显示为"-"
                        this.dataForm.projectDetails[index]._displayAmountRaised = "-";
                        this.dataForm.projectDetails[index]._displayProcurementIncrementUnitPriceHour = "-";
                        this.dataForm.projectDetails[index]._displayProcurementDecrementUnitPriceHour = "-";
                        this.dataForm.projectDetails[index]._displayUpperLimit = "-";
                        this.dataForm.projectDetails[index]._displayLowerLimit = "-";
                    } else {
                        this.dataForm.projectDetails[index].amountRaisedDisabled = false;
                        this.dataForm.projectDetails[index].amountRaised = null;
                        this.dataForm.projectDetails[index].procurementIncrementUnitPriceHour = null;
                        this.dataForm.projectDetails[index].procurementDecrementUnitPriceHour = null;
                        this.dataForm.projectDetails[index].upperLimit = null;
                        this.dataForm.projectDetails[index].lowerLimit = null;

                        // 清除显示值
                        this.dataForm.projectDetails[index]._displayAmountRaised = null;
                        this.dataForm.projectDetails[index]._displayProcurementIncrementUnitPriceHour = null;
                        this.dataForm.projectDetails[index]._displayProcurementDecrementUnitPriceHour = null;
                        this.dataForm.projectDetails[index]._displayUpperLimit = null;
                        this.dataForm.projectDetails[index]._displayLowerLimit = null;
                    }
                }
            } else {
                this.handleCompanyClear(index);
            }
        },

        handleCompanyClear(index) {
            if (!this.dataForm.projectDetails || !this.dataForm.projectDetails[index]) {
                console.warn("handleCompanyClear: projectDetails is undefined or index is out of bounds");
                return;
            }
            this.dataForm.projectDetails[index].companyId = null;
            this.dataForm.projectDetails[index].companyAbbreviation = "";
            this.dataForm.projectDetails[index].staffId = null;
            this.dataForm.projectDetails[index].staffName = "";
            this.dataForm.projectDetails[index].staffDisabled = true; // 禁用要員下拉框
        },

        handleStaffChange(index, staffId) {
            console.log("当前 index:", index);
            if (staffId) {
                const selectedStaff = this.filteredStaffLists[index].find(staff => staff.staffId === staffId);
                if (selectedStaff.noActiveProjects == "FALSE") {
                    this.dataForm.projectDetails[index].staffId = selectedStaff.staffId;
                    //存放临时的要员id，要用来给警告框进行判断
                    this.tempSelectedStaffId = selectedStaff.staffId;
                    this.selectedIndex = index;
                    this.dialogVisible = true; // 显示警告弹窗
                } else {
                    if (selectedStaff) {
                        this.dataForm.projectDetails[index].staffName = selectedStaff.staffName;
                        this.dataForm.projectDetails[index].staffId = selectedStaff.staffId;
                    }
                }
            } else {
                this.dataForm.projectDetails[index].staffName = "";
                this.dataForm.projectDetails[index].staffId = null;
            }
            // this.$nextTick(() => {
            //     this.$refs[`staffForm${index}`][0]?.validateField("staffName");
            // });
        },
        //确定选择
        confirmSelection() {
            const index = this.selectedIndex;
            console.log("当前 index:", index);
            // const selectedProject = this.dataForm.projectDetails[index];
            // if (!selectedProject || !selectedProject.filteredStaffList) {
            //     return;
            // }
            const selectedStaff = this.filteredStaffLists[index].find(
                staff => staff.staffId === this.tempSelectedStaffId
            );
            console.log("list");
            console.log(selectedStaff);
            console.log(this.tempSelectedStaffId);
            this.dataForm.projectDetails[index].staffName = selectedStaff.staffName;
            this.dataForm.projectDetails[index].staffId = selectedStaff.staffId;
            this.dialogVisible = false;//关闭对话框
        },

        //取消选择
        cancelSelection() {
            const index = this.selectedIndex;
            this.dataForm.projectDetails[index].staffId = null;
            this.dataForm.projectDetails[index].staffName = null;
            this.dialogVisible = false;//关闭对话框
        },

        // 案件名验证
        validateProjectName(rule, value) {
            console.log("validateProjectName 被调用");

            const projectName = this.dataForm.projectInfo.projectName;
            const departmentId = this.dataForm.projectInfo.departmentId;
            const projectId = this.dataForm.projectInfo.projectId;
            if (!projectName) {
                return Promise.resolve(); // **允许为空，直接通过**
            }

            return new Promise((resolve, reject) => {
                this.$httpV2("/projectDetail/checkProjectName", "GET", { projectName, departmentId, projectId }, true, (resp) => {
                    console.log("API 响应:", resp);

                    if (resp.code === 200) {
                        if (resp.result) {  // **案件名已存在**
                            console.error("案件名已存在，验证失败");
                            reject(new Error("案件名はすでに存在します。他の名前を入力してください"));
                        } else {
                            console.log("案件名可用，验证通过");
                            resolve(); // **验证通过**
                        }
                    } else {
                        console.error("API 返回错误:", resp.msg);
                        reject(new Error(resp.msg || "案件名の検証中にエラーが発生しました"));
                    }
                });
            });
        },
        formatAllMoneyFields() {
            this.formatWithCommas(this.dataForm.projectInfo, 'settlementUpperLimit');
            this.formatWithCommas(this.dataForm.projectInfo, 'settlementLowerLimit');

            this.dataForm.projectDetails.forEach(item => {
                this.formatWithCommas(item, 'salesAmount');
                this.formatWithCommas(item, 'salesIncrementUnitPriceHour');
                this.formatWithCommas(item, 'salesDecrementUnitPriceHour');
                this.formatWithCommas(item, 'amountRaised');
                this.formatWithCommas(item, 'procurementIncrementUnitPriceHour');
                this.formatWithCommas(item, 'procurementDecrementUnitPriceHour');
                this.formatWithCommas(item, 'upperLimit');
                this.formatWithCommas(item, 'lowerLimit');
            });
        },

        //校验三张表单的必填项，通过后才能提交
        async submitAllForms() {
            try {
                await this.$refs.dataForm.validate();
            } catch {
                this.$message({
                    type: 'warning',
                    message: '企業もしくは部署を選択してください'
                });
                return; // 阻止继续执行
            }

            try {
                await this.$refs.dataFormProject.validate();
                // 👉 添加上限下限校验（在项目表单校验之后）
                const upper = Number(this.dataForm.projectInfo.settlementUpperLimit?.toString().replace(/,/g, ""));
                const lower = Number(this.dataForm.projectInfo.settlementLowerLimit?.toString().replace(/,/g, ""));
                if (isNaN(upper) || isNaN(lower)) {
                    this.$message.warning("上限・下限は数値で入力してください");
                    return;
                }
                if (upper <= lower) {
                    this.$message.warning("清算幅の上限は下限より大きくなければなりません");
                    return;
                }
            } catch {
                this.$message({
                    type: 'warning',
                    message: '案件情報の必須項目を入力してください'
                });
                return;
            }

            for (let i = 0; i < this.dataForm.projectDetails.length; i++) {
                try {
                    const formRef = this.$refs['staffForm' + i];
                    await (formRef[0] || formRef).validate();

                    // ✅ 校验「调达时间上限 > 下限」
                    const upper = Number(this.dataForm.projectDetails[i].upperLimit?.toString().replace(/,/g, ""));
                    const lower = Number(this.dataForm.projectDetails[i].lowerLimit?.toString().replace(/,/g, ""));
                    const disabled = this.dataForm.projectDetails[i].amountRaisedDisabled;

                    if (!disabled && (isNaN(upper) || isNaN(lower))) {
                        this.$message.warning(`要員情報${i + 1} の上限・下限は数値で入力してください`);
                        return;
                    }

                    if (!disabled && upper <= lower) {
                        this.$message.warning(`要員情報${i + 1} の調達時間の上限は下限より大きくなければなりません`);
                        return;
                    }

                } catch {
                    this.$message({
                        type: 'warning',
                        message: `要員情報${i + 1}の必須項目を入力してください`
                    });
                    return;
                }
            }
            if (!this.validateWorkTimeFormat()) {
                return;
            }

            this.dataFormSubmit();
        },
        validateWorkTimeFormat() {
            const timeRegex = /^([01]\d|2[0-3]):[0-5]\d$/;
            const { startTime, endTime } = this.dataForm.projectInfo;

            if (startTime && !timeRegex.test(startTime)) {
                this.$message({
                    type: 'warning',
                    message: '就業開始時間のフォーマットが間違っています（正しい形式：HH:mm）'
                });
                return false;
            }

            if (endTime && !timeRegex.test(endTime)) {
                this.$message({
                    type: 'warning',
                    message: '就業終了時間のフォーマットが間違っています（正しい形式：HH:mm）'
                });
                return false;
            }

            return true;
        },

        dataFormSubmit: function () {
            let that = this;
            that.$refs["dataForm"].validate((valid) => {
                if (valid) {
                    // 先去掉所有金额字段的千位符
                    that.removeCommas(that.dataForm.projectInfo, 'settlementUpperLimit');
                    that.removeCommas(that.dataForm.projectInfo, 'settlementLowerLimit');


                    that.dataForm.projectDetails.forEach((item) => {
                        that.removeCommas(item, 'salesAmount');
                        that.removeCommas(item, 'salesIncrementUnitPriceHour');
                        that.removeCommas(item, 'salesDecrementUnitPriceHour');
                        that.removeCommas(item, 'amountRaised');
                        that.removeCommas(item, 'procurementIncrementUnitPriceHour');
                        that.removeCommas(item, 'procurementDecrementUnitPriceHour');
                        that.removeCommas(item, 'upperLimit');
                        that.removeCommas(item, 'lowerLimit');

                        if (item.amountRaised == "-") {
                            item.amountRaised = -9999;
                            item.lowerLimit = -9999;
                            item.upperLimit = -9999;
                            item.procurementDecrementUnitPriceHour = -9999;
                            item.procurementIncrementUnitPriceHour = -9999;
                        } else {
                            that.removeCommas(item, 'amountRaised');
                        }

                    });

                    let data = {
                        salesCompanyAbbreviation: that.dataForm.salesCompanyAbbreviation,
                        departmentAbbreviation: that.dataForm.departmentAbbreviation,
                        projectInfo: that.dataForm.projectInfo,
                        projectDetails: that.dataForm.projectDetails,
                    };

                    let method = "";
                    let url = "";

                    if (that.dataForm.projectInfo.projectId == null || that.dataForm.projectInfo.projectId == "") {
                        method = "POST";
                        url = "/projectDetail/add";
                    } else {
                        method = "PUT";
                        url = "/projectDetail/update";
                    }

                    that.$httpV2(url, method, data, false, function (resp) {
                        ElMessage({
                            message: "成功した操作",
                            type: "success"
                        });
                        that.visible = false;
                        that.$emit("refreshDataList");
                    })
                }
            });
        },
    },


}

</script>
<style>
.staff-form-container {
    max-height: 300px;
    /* 设置滚动区域的最大高度 */
    overflow-y: auto;
    /* 允许垂直滚动 */
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    padding: 10px;
    margin-bottom: 20px;
}

.sub-form {
    margin-bottom: 20px;
    padding: 10px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    background-color: #f9f9f9;
}

.staff-title {
    font-size: 15px;
    font-weight: bold;
    color: #409eff;
    margin-bottom: 10px;
}
</style>
