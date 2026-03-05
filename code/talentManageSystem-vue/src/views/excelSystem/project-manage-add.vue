<template>
    <el-dialog :title="title" :close-on-click-modal="false" v-model="visible" width="850px" @close="closeDialog">
        <div>
            <el-form :model="dataForm" :rules="dataRule" ref="dataForm">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="企業" prop="salesCompanyAbbreviation">
                            <el-select v-model="dataForm.salesCompanyAbbreviation" clearable placeholder="選択してください"
                                @change="handleSalesCompanyChange(dataForm.salesCompanyAbbreviation)"
                                @clear="handleSalesCompanyClear">
                                <el-option v-for="companyi in salesCompanyList" :key="companyi.companyId"
                                    :label="companyi.companyAbbreviation" :value="companyi.companyId" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="部署" prop="departmentAbbreviation">
                            <el-select v-model="dataForm.departmentAbbreviation" clearable
                                :disabled="departmentDisabled" placeholder="部署名を選択してください"
                                @change="handleDepartmentChange(dataForm.departmentAbbreviation)">
                                <el-option v-for="i in departmentList" :key="i.departmentId"
                                    :label="`${i.departmentAbbreviation}${i.principalName ? '（' + i.principalName + '）' : ''}`"
                                    :value="i.departmentId" />

                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item>
                    <el-form :model="dataForm.projectInfo" :rules="dataFormProRules" ref="dataFormProject">
                        <el-row :gutter="20">
                            <el-col :span="12">
                                <el-form-item label="案件名" prop="projectName">
                                    <el-input v-model="dataForm.projectInfo.projectName" placeholder="案件名"
                                        @blur="validateProjectNameDebounced" clearable
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
                                        clearable :disabled-date="disablePastDates" style="width: 100%" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="案件終了日" prop="projectEndDate">
                                    <el-date-picker v-model="dataForm.projectInfo.projectEndDate" type="date"
                                        placeholder="案件終了日を選択してください" value-format="YYYY-MM-DD" format="YYYY-MM-DD"
                                        clearable :disabled-date="disablePastDates" style="width: 100%" />
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
                                <el-form-item label="清算幅： 上限" prop="settlementUpperLimit">
                                    <el-input v-model="dataForm.projectInfo.settlementUpperLimit" placeholder="上限"
                                        clearable @input="formatleangth(dataForm.projectInfo, 'settlementUpperLimit')"
                                        @focus="removeCommas(dataForm.projectInfo, 'settlementUpperLimit')"
                                        @blur="formatWithCommas(dataForm.projectInfo, 'settlementUpperLimit')"
                                        style="ime-mode: disabled;" inputmode="numeric"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="下限" prop="settlementLowerLimit">
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
                        <div class="staff-title">要員情報{{ index + 1 }}
                            <span>
                                <el-button type="danger" @click="removeSubForm(index)">削除</el-button>
                            </span>
                        </div>
                        <el-form :model="item" :ref="'staffForm' + index" :rules="staffRule">
                            <!-- 会社和要员以下拉框的形式展示 -->
                            <el-row :gutter="20">
                                <el-col :span="12">
                                    <el-form-item label="会社名" prop="companyAbbreviation">
                                        <el-select v-model="item.companyId" clearable placeholder="選択してください"
                                            @change="handleCompanyChange(index, item.companyId)"
                                            @clear="handleCompanyClear(index)">
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

                                        <el-dialog v-model="dialogVisible" title="警告メッセージ">
                                            <span>該当する期間は他プロジェクトにアサインされています。登録しますか？</span>
                                            <template #footer>
                                                <el-button @click="cancelSelection(index)">キャンセル</el-button>
                                                <el-button type="primary"
                                                    @click="confirmSelection(index, item.staffId)">確定</el-button>
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
                                            value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable
                                            :disabled-date="disablePastDates" style="width: 100%" />
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="退場日" prop="actualExitDate">
                                        <el-date-picker v-model="item.actualExitDate" type="date" placeholder="退場日"
                                            value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable
                                            :disabled-date="disablePastDates" style="width: 100%" />
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
                                        <el-input v-model="item.amountRaised" placeholder="調達金額"
                                            :disabled="item.amountRaisedDisabled"
                                            @input="filterInput(item, 'amountRaised')"
                                            @focus="removeCommas(item, 'amountRaised')"
                                            @blur="formatWithCommas(item, 'amountRaised')" style="ime-mode: disabled;"
                                            inputmode="numeric"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="9">
                                    <el-form-item label="調達増単金（時間）" prop="procurementIncrementUnitPriceHour">
                                        <el-input v-model="item.procurementIncrementUnitPriceHour" placeholder="調達増単金"
                                            @input="filterInput(item, 'procurementIncrementUnitPriceHour')"
                                            @focus="removeCommas(item, 'procurementIncrementUnitPriceHour')"
                                            @blur="formatWithCommas(item, 'procurementIncrementUnitPriceHour')"
                                            style="ime-mode: disabled;" inputmode="numeric"
                                            :disabled="item.amountRaisedDisabled"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="9">
                                    <el-form-item label="調達減単金（時間）" prop="procurementDecrementUnitPriceHour">
                                        <el-input v-model="item.procurementDecrementUnitPriceHour" placeholder="調達減単金"
                                            @input="filterInput(item, 'procurementDecrementUnitPriceHour')"
                                            @focus="removeCommas(item, 'procurementDecrementUnitPriceHour')"
                                            @blur="formatWithCommas(item, 'procurementDecrementUnitPriceHour')"
                                            style="ime-mode: disabled;" inputmode="numeric"
                                            :disabled="item.amountRaisedDisabled"></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="12">
                                    <el-form-item label="調達時間上限" prop="upperLimit">
                                        <el-input v-model="item.upperLimit" placeholder="上限" clearable maxlength="3"
                                            @input="filterInput(item, 'upperLimit')"
                                            @focus="removeCommas(item, 'upperLimit')"
                                            @blur="formatWithCommas(item, 'upperLimit')" style="ime-mode: disabled;"
                                            inputmode="numeric" :disabled="item.amountRaisedDisabled"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="調達時間下限" prop="lowerLimit">
                                        <el-input v-model="item.lowerLimit" placeholder="下限" clearable maxlength="3"
                                            @input="filterInput(item, 'lowerLimit')"
                                            @focus="removeCommas(item, 'lowerLimit')"
                                            @blur="formatWithCommas(item, 'lowerLimit')" style="ime-mode: disabled;"
                                            inputmode="numeric" :disabled="item.amountRaisedDisabled"></el-input>
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
            title: '追加',
            departmentDisabled: true,
            //警告框
            dialogVisible: false,

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
                        upperLimit: null,
                        lowerLimit: null,

                        amountRaised: null,
                        procurementIncrementUnitPriceHour: null,
                        procurementDecrementUnitPriceHour: null,
                        //员工下拉框禁用标识
                        staffDisabled: true,
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
            staffRule: {
                companyAbbreviation: [
                    { required: true, message: '会社を選択してください', trigger: 'change' },
                ],
                staffId: [
                    { required: true, message: '要員を選択してください', trigger: 'change' },
                ],
                entryDate: [
                    { required: true, message: '入場日を選択してください', trigger: 'change' },
                ],
                salesAmount: [
                    { required: true, message: '販売金額を入力してください', trigger: 'blur' },
                ],
                amountRaised: [
                    { required: true, message: '調達金額を入力してください', trigger: 'blur' },
                ],
                lowerLimit: [
                    { required: true, message: '調達時間下限を入力してください', trigger: 'blur' }
                ],
                upperLimit: [
                    { required: true, message: '調達時間上限を入力してください', trigger: 'blur' }
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
            },
            //公司下拉列表
            salesCompanyList: [],
            //部门下拉列表
            departmentList: [],
            //要員公司下拉列表
            companyList: [],
            //下拉框过滤的公司列表
            filterCompanyNames: [],
            //下拉框要员列表
            staffList: [],
            //下拉框过滤的人员列表
            fillterStaffNames: [],
        }

    },
    created() {
        this.debouncedSubmit = debounce(this.submitAllForms, 1000);

        // 这里去掉了 return Promise
        this.validateProjectNameDebounced = debounce((rule, value, callback) => {
            this.validateProjectName(rule, value, callback);
        }, 500);
    },
    computed: {
        selectedStaffIds() {
            return this.dataForm.projectDetails
                .map(item => item.staffId)  // 获取已选中的员工 ID
                .filter(id => id !== null); // 过滤掉 null
        },
        filteredStaffLists() {
            return this.dataForm.projectDetails.map(item => {
                if (!item.companyId) return []; // 没有公司时，返回空数组
                const selectedCompany = this.companyList.find(company => company.companyId === item.companyId);
                return selectedCompany ? selectedCompany.staff : [];
            });
        }

    },
    mounted() {
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

    methods: {
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
        disablePastDates(date) {
            const today = new Date();
            today.setHours(0, 0, 0, 0); // 去除时间影响
            return date < today; // 禁止选择今天之前的日期
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
            if (!row[field] || isNaN(Number(row[field]))) {
                return;
            }
            row[field] = Number(row[field]).toLocaleString(); // 添加千位分隔符
        },

        /** 获得焦点时：去掉千位分隔符，恢复纯数字 */
        removeCommas(row, field) {
            if (!row[field]) return;
            row[field] = row[field].toString().replace(/,/g, ""); // 去掉千位分隔符
            if (isNaN(Number(row[field]))) row[field] = "0"; // 避免无效输入
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

        formatMoneyFields() {
            this.dataForm.projectDetails.forEach(item => {
                item.salesAmount = item.salesAmount ? Number(item.salesAmount).toLocaleString() : "0";
                item.amountRaised = item.amountRaised ? Number(item.amountRaised).toLocaleString() : "0";
            });
        },
        //初始化页面
        init: function () {
            let that = this;
            that.visible = true;
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
            });
            that.loadList();

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
                    salesAmount: null,
                    salesIncrementUnitPriceHour: null,
                    salesDecrementUnitPriceHour: null,
                    amountRaised: null,
                    procurementIncrementUnitPriceHour: null,
                    procurementDecrementUnitPriceHour: null,
                    staffDisabled: true,
                    upperLimit: null,
                    lowerLimit: null,
                },
                ],
            }
            this.dataForm = dataForm;
            this.staffList = [];
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


        // 获取下拉列表
        loadList() {
            let that = this;
            that.$httpV2("/projectDetail/additional/", "GET", {}, true, function (resp) {
                let result = resp.result;
                that.salesCompanyList = result.salesCompaniesWithDepartments;
                that.companyList = result.laborCompaniesWithStaff;
                // that.dataListLoading = false;
            })
        },
        /** 弹窗关闭时清理 */
        closeDialog() {
            this.visible = false;
            this.reset();
        },
        //选择公司后，更新部门下拉框
        handleSalesCompanyChange(id) {
            if (id) {
                const selectedSalesCompany = this.salesCompanyList.find((company) => company.companyId === id);
                if (selectedSalesCompany) {
                    if (selectedSalesCompany.companyId != this.dataForm.projectInfo.salesCompanyId) {
                        this.$nextTick(() => {
                            this.dataForm.departmentAbbreviation = null;
                        });
                    }
                    this.dataForm.projectInfo.salesCompanyId = selectedSalesCompany.companyId;
                    this.dataForm.salesCompanyAbbreviation = selectedSalesCompany.companyAbbreviation;
                }
                this.departmentList = selectedSalesCompany.departments;
                this.departmentDisabled = false;
                console.log(this.departmentList);
            } else {
                this.handleSalesCompanyClear();
            }

        },
        //清除企业
        handleSalesCompanyClear() {
            this.departmentList = [];
            this.departmentDisabled = true;
            this.$nextTick(() => {
                this.dataForm.departmentAbbreviation = null;
            });
        },
        handleDepartmentChange(departmentId) {
            if (departmentId) {
                const selectedDepartment = this.departmentList.find((department) => department.departmentId === departmentId);
                if (selectedDepartment) {
                    this.dataForm.projectInfo.departmentId = selectedDepartment.departmentId;
                    this.dataForm.departmentAbbreviation = selectedDepartment.departmentAbbreviation;
                }
            } else {
                this.dataForm.projectInfo.departmentAbbreviation = null;
            }

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
                upperLimit: null,
                lowerLimit: null,
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
        //选择公司后，更新要员下拉框
        handleCompanyChange(index, id) {
            if (id) {
                const selectedCompany = this.companyList.find((company) => company.companyId === id);
                if (selectedCompany) {
                    if (selectedCompany.companyId != this.dataForm.projectDetails[index].companyId) {
                        this.$nextTick(() => {
                            this.dataForm.projectDetails[index].staffId = null;
                        });
                    }
                    this.dataForm.projectDetails[index].companyAbbreviation = selectedCompany.companyAbbreviation;
                    this.dataForm.projectDetails[index].companyId = selectedCompany.companyId;
                    //解除要员下拉框禁用
                    this.dataForm.projectDetails[index].staffDisabled = false;
                }
                this.staffList = selectedCompany.staff;
                console.log(this.staffList)
                if (selectedCompany.companyAbbreviation === "UCL") {
                    this.dataForm.projectDetails[index].amountRaised = "-";
                    this.dataForm.projectDetails[index].procurementIncrementUnitPriceHour = "-";
                    this.dataForm.projectDetails[index].procurementDecrementUnitPriceHour = "-";
                    this.dataForm.projectDetails[index].upperLimit = "-";
                    this.dataForm.projectDetails[index].lowerLimit = "-";
                    this.dataForm.projectDetails[index].amountRaisedDisabled = true;
                } else {
                    this.dataForm.projectDetails[index].amountRaisedDisabled = false;
                    this.dataForm.projectDetails[index].amountRaised = null;
                    this.dataForm.projectDetails[index].procurementIncrementUnitPriceHour = null;
                    this.dataForm.projectDetails[index].procurementDecrementUnitPriceHour = null;
                    this.dataForm.projectDetails[index].upperLimit = null;
                    this.dataForm.projectDetails[index].lowerLimit = null;
                }
            } else {
                this.handleCompanyClear(index);
            }
        },
        handleCompanyClear(index) {
            this.staffList = [];
            this.$nextTick(() => {
                if (this.dataForm.projectDetails && this.dataForm.projectDetails[index]) {
                    this.dataForm.projectDetails[index].staffId = null;
                    //要员下拉框禁用
                    this.dataForm.projectDetails[index].staffDisabled = true;
                }
            });
        },

        //选择要员后，更新要员下拉框，并且判断要员是否有其他案件
        handleStaffChange(index, id) {
            if (id) {
                const selectedStaff = this.staffList.find((staff) => staff.staffId === id);
                console.log("selectedStaff:");
                console.log(selectedStaff);

                if (selectedStaff.noActiveProjects == "FALSE") {
                    this.dataForm.projectDetails[index].staffId = selectedStaff.staffId;
                    this.dialogVisible = true; // 显示警告弹窗
                } else {
                    if (selectedStaff) {
                        this.dataForm.projectDetails[index].staffName = selectedStaff.staffName;
                        this.dataForm.projectDetails[index].staffId = selectedStaff.staffId;
                    }
                }
            } else {
                this.dataForm.projectDetails[index].staffName = null;
                this.dataForm.projectDetails[index].staffId = null;
            }

        },

        //确定选择
        confirmSelection(index, id) {
            const selectedStaff = this.staffList.find((staff) => staff.staffId === id);
            this.dataForm.projectDetails[index].staffName = selectedStaff.staffName;
            this.dataForm.projectDetails[index].staffId = selectedStaff.staffId;
            this.dialogVisible = false;//关闭对话框
        },

        //取消选择
        cancelSelection(index) {
            this.dataForm.projectDetails[index].staffId = null;
            this.dataForm.projectDetails[index].staffName = null;
            this.dialogVisible = false;//关闭对话框
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
                return;
            }

            try {
                await this.$refs.dataFormProject.validate();

                // 👉 案件 清算幅 上限/下限 校验
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

                    // ✅ 要員 调达时间 上限/下限 校验
                    const item = this.dataForm.projectDetails[i];
                    if (!item.amountRaisedDisabled) {
                        const upper = Number(item.upperLimit?.toString().replace(/,/g, ""));
                        const lower = Number(item.lowerLimit?.toString().replace(/,/g, ""));

                        if (isNaN(upper) || isNaN(lower)) {
                            this.$message.warning(`要員情報${i + 1} の上限・下限は数値で入力してください`);
                            return;
                        }
                        if (upper <= lower) {
                            this.$message.warning(`要員情報${i + 1} の調達時間の上限は下限より大きくなければなりません`);
                            return;
                        }
                    }

                } catch {
                    this.$message({
                        type: 'warning',
                        message: `要員情報${i + 1}の必須項目を入力してください`
                    });
                    return;
                }
                if (!this.validateWorkTimeFormat()) {
                    return;
                }
            }

            // ✅ 全部校验通过，提交
            this.dataFormSubmit();
        },
        dataFormSubmit: function () {
            let that = this;
            that.$refs["dataForm"].validate((valid) => {
                if (valid) {
                    // **提交前，去掉所有金额字段的千位分隔符**
                    const removeCommasFromFields = (row, fields) => {
                        fields.forEach(field => {
                            if (row[field]) {
                                row[field] = row[field].toString().replace(/,/g, ""); // 去掉千位符
                            }
                        });
                    };

                    removeCommasFromFields(that.dataForm.projectInfo, ['settlementUpperLimit', 'settlementLowerLimit']);

                    that.dataForm.projectDetails.forEach((item) => {
                        if (item.amountRaised === "-") {
                            //或者检测item.amountRaisedDisabled==true
                            item.amountRaised = -9999;
                            item.lowerLimit = 0;
                            item.upperLimit = 0;
                            item.procurementDecrementUnitPriceHour = -9999;
                            item.procurementIncrementUnitPriceHour = -9999;
                        }
                        removeCommasFromFields(item, [
                            'salesAmount', 'salesIncrementUnitPriceHour', 'salesDecrementUnitPriceHour',
                            'amountRaised', 'procurementIncrementUnitPriceHour', 'procurementDecrementUnitPriceHour',
                            'upperLimit', 'lowerLimit'
                        ]);
                    });

                    let data = {
                        salesCompanyAbbreviation: that.dataForm.salesCompanyAbbreviation,
                        departmentAbbreviation: that.dataForm.departmentAbbreviation,
                        projectInfo: that.dataForm.projectInfo,
                        projectDetails: that.dataForm.projectDetails,
                    };

                    let method = that.dataForm.projectInfo.projectId ? "PUT" : "POST";
                    let url = that.dataForm.projectInfo.projectId ? "/projectDetail/update" : "/projectDetail/add";

                    that.$httpV2(url, method, data, false, function (resp) {
                        ElMessage({ message: "成功した操作", type: "success" });
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
    display: flex;
    /* 使用 Flexbox */
    justify-content: space-between;
    /* 左右对齐 */
    align-items: center;
    /* 垂直居中 */
    width: 100%;
    /* 让父容器占满宽度 */
}
</style>
