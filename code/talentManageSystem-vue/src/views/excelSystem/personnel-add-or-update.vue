<template>
    <el-dialog :title="!dataForm.staffId ? '要員追加' : '要員変更'" :close-on-click-modal="false" width="850px"
        v-model="visible" @open="handleDialogOpen">

        <!-- 表单 -->
        <el-scrollbar style="max-height:800px;">
            <el-form :model="dataForm" ref="dataForm" :rules="dataRule" :validate-on-rule-change="false">
                <!-- 公司略称 -->
                <el-form-item label="企業略称：" prop="companyAbbreviation">
                    <el-select v-model="dataForm.companyAbbreviation" placeholder="企業略称を選択してください" clearable
                        @change="handleCompanyChange">
                        <el-option v-for="item in companyList" :key="item.companyId" :label="item.companyAbbreviation"
                            :value="item.companyAbbreviation">
                        </el-option>
                    </el-select>
                </el-form-item>
                <!-- 中文 -->
                <el-form-item label="要員名：" prop="staffNameKanji">
                    <el-input v-model="dataForm.staffNameKanji" clearable @input="limitLength('staffNameKanji', 10)" />
                </el-form-item>
                <!-- 假名 -->
                <el-form-item label="要員名（フリガナ）：" prop="staffNameFurikana">
                    <el-input v-model="dataForm.staffNameFurikana" clearable
                        @input="limitLength('staffNameFurikana', 10)" />
                </el-form-item>
                <!-- 英文 -->
                <el-form-item label="要員名_英語名：" prop="staffNameRoma">
                    <el-input v-model="dataForm.staffNameRoma" clearable />
                </el-form-item>
                <!-- 营业担当者 -->
                <el-form-item label="営業担当者：" prop="salesRepresentitive">
                    <el-input v-model="dataForm.salesRepresentitive" clearable />
                </el-form-item>
                <!-- 生年月 -->
                <el-form-item label="生年月日：" prop="staffBirthday">
                    <el-date-picker v-model="dataForm.staffBirthday" type="date" placeholder="生年月日を選択してください"
                        value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable style="width: 300px;" />
                </el-form-item>
                <!-- 营业开始日期 -->
                <el-form-item label="稼働開始年月日" prop="operationStartDate">
                    <el-date-picker v-model="dataForm.operationStartDate" type="date" placeholder="稼働開始年月日を選択してください"
                        value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable style="width: 300px;" />
                </el-form-item>
                <!-- 营业结束日期 -->
                <el-form-item label="稼働終了年月日" prop="operationEndDate">
                    <el-date-picker v-model="dataForm.operationEndDate" type="date" placeholder="稼働終了年月日を選択してください"
                        value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable style="width: 300px;"
                        @change="handleOperationEndDateChange" />
                </el-form-item>
                <!-- 直近评价 -->
                <el-form-item label="直近評価：" prop="latestEvaluation">
                    <el-select v-model="dataForm.latestEvaluation" placeholder="評価を選択してください" clearable filterable>
                        <el-option label="優" value="優"></el-option>
                        <el-option label="良" value="良"></el-option>
                        <el-option label="可" value="可"></el-option>
                        <el-option label="不可" value="不可"></el-option>
                    </el-select>
                </el-form-item>
                <!-- activeFlg -->
                <el-form-item label="アクティブフラグ：" prop="activeFlg">
                    <el-radio-group v-model="dataForm.activeFlg" @change="handleActiveFlgChange">
                        <el-radio-button :label="1">アクティブ</el-radio-button>
                        <el-radio-button :label="0">非アクティブ</el-radio-button>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="備考：" prop="comment">
                    <el-input type="textarea" :rows="1" maxlength="200" :show-word-limit="true"
                        v-model="dataForm.comment" clearable />
                </el-form-item>
            </el-form>
        </el-scrollbar>

        <!-- 底部 -->
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
    data: function () {
        return {
            visible: false,
            companyList: [],
            dataForm: {
                staffId: null,

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
                companyAbbreviation: null,
            },
            dataRule: {
                staffNameFurikana: [
                    {
                        required: true,
                        message: "要員名（フリガナ）は必須です",
                        trigger: 'blur'
                    },
                ],
                staffNameKanji: [
                    {
                        required: true,
                        message: "要員名は必須です",
                        trigger: 'blur'
                    },
                ],
                activeFlg: [
                    {
                        required: true,
                        message: "アクティブ状態を選択してください",
                        trigger: 'blur'
                    },
                ],
                companyAbbreviation: [
                    {
                        required: true,
                        message: "企業略称を選択してください",
                        trigger: 'change'
                    }
                ],
            },
        }
    },
    created() {
        this.debouncedSubmit = debounce(this.dataFormSubmit, 1000);
    },
    methods: {
        handleOperationEndDateChange(value) {
            // 如果用户清空了结束日期，强制设置为“アクティブ”
            if (!value) {
                this.dataForm.activeFlg = 1;
            }
        },

        limitLength(field, maxLength) {
            if (!this.dataForm[field]) return;

            // 计算全角长度（全角2，半角1）
            const getZenkakuLength = (str) => {
                return [...str].reduce((len, ch) => {
                    return len + (ch.charCodeAt(0) > 255 ? 2 : 1);
                }, 0);
            };

            let raw = this.dataForm[field];
            let result = "";
            let len = 0;

            for (const ch of raw) {
                let charLen = ch.charCodeAt(0) > 255 ? 2 : 1;
                if (len + charLen > maxLength * 2) break;
                result += ch;
                len += charLen;
            }

            this.dataForm[field] = result;
        },

        init(staffId, companyInfo) {
            let that = this;
            that.visible = true;  // 先显示对话框
            that.reset();
            that.dataForm.staffId = staffId;

            // 先获取公司列表
            that.getAllCompanys();

            // 使用 nextTick 确保 DOM 已更新
            that.$nextTick(() => {
                if (that.$refs.dataForm) {
                    that.$refs.dataForm.clearValidate();  // 清除验证状态

                    // 如果是修改操作（staffId 存在），则获取数据
                    if (staffId) {
                        that.$httpV2(`/staff/getStaffById/${staffId}`, 'GET', null, true, function (resp) {
                            if (resp.code === 200) {
                                // 将获取的数据填充到表单中
                                that.dataForm = {
                                    ...that.dataForm,  // 保持原有值
                                    ...resp.result,    // 用后端数据更新
                                };
                                that.dataForm.companyAbbreviation = companyInfo.companyAbbreviation;
                            } else {
                                that.$message.error('データの取得に失敗しました');
                            }
                        });
                    } else if (companyInfo) {
                        // 新增时，如果有公司信息，设置默认值
                        that.dataForm.companyId = companyInfo.companyId;
                        that.dataForm.companyAbbreviation = companyInfo.companyAbbreviation;
                    }
                }
            });
        },

        reset() {
            let dataForm = {
                staffId: null,
                staffNameRoma: null,
                staffNameFurikana: null,
                staffNameKanji: null,
                salesRepresentitive: null,
                companyId: null,
                companyAbbreviation: null,  // 添加这个字段
                staffBirthday: null,
                operationStartDate: null,
                operationEndDate: null,
                latestEvaluation: null,
                activeFlg: 1,  // 设置默认值为1
                comment: null,
            };
            this.dataForm = dataForm;

            // 清除验证
            if (this.$refs["dataForm"]) {
                this.$refs["dataForm"].clearValidate();
            }
        },

        handleActiveFlgChange(value) {
            let that = this;
            const staffId = this.dataForm.staffId;

            if (value === 0) { // 用户选择“非アクティブ”
                // 先判断是否有稼働終了年月日
                if (!that.dataForm.operationEndDate) {
                    ElMessage.warning({
                        message: "稼働終了年月日が未入力のため、非アクティブに設定できません",
                        duration: 1500
                    });

                    // 还原为“アクティブ”
                    that.dataForm.activeFlg = 1;
                    return;
                }

                if (!staffId) return;

                const url = `/staff/canSetInactive/${staffId}`;
                console.log(`发送 GET 请求到后端: ${url}`);

                that.$httpV2(url, 'GET', null, true, function (response) {
                    console.log("后端返回的数据:", response);

                    let canSetInactive = response && response.result;

                    if (!canSetInactive) {
                        ElMessage.error({
                            message: "この要員は非アクティブにできません",
                            type: "warning",
                            duration: 1200
                        });

                        that.dataForm.activeFlg = 1;
                    }
                }).catch(error => {
                    console.error(" 请求失败:", error);
                    ElMessage.error({
                        message: "エラーが発生しました",
                        type: "error",
                        duration: 1200
                    });

                    that.dataForm.activeFlg = 1;
                });
            }
        },

        getAllCompanys() {
            let that = this;
            that.$httpV2('/staff/getCompanys', 'GET', null, true, function (resp) {
                if (resp.code === 200) {
                    that.companyList = resp.result;
                    // 按照companyAbbreviation排序
                    that.companyList.sort((a, b) => {
                        if (a.companyAbbreviation < b.companyAbbreviation) return -1;
                        if (a.companyAbbreviation > b.companyAbbreviation) return 1;
                        return 0;
                    });
                    console.log('Companies loaded:', that.companyList);
                } else {
                    that.$message.error('企業データの取得に失敗しました');
                }
            });
        },

        handleCompanyChange(value) {
            const selectedCompany = this.companyList.find(
                company => company.companyAbbreviation === value
            );
            if (selectedCompany) {
                this.dataForm.companyId = selectedCompany.companyId;
            }
        },

        dataFormSubmit: function () {
            let that = this;
            that.$refs["dataForm"].validate(function (valid) {
                if (valid) {
                    let data = {
                        staffId: that.dataForm.staffId,

                        // 英文
                        staffNameRoma: that.dataForm.staffNameRoma,
                        // 假名
                        staffNameFurikana: that.dataForm.staffNameFurikana,
                        // 中文
                        staffNameKanji: that.dataForm.staffNameKanji,
                        // 营业担当者
                        salesRepresentitive: that.dataForm.salesRepresentitive,
                        // 公司 id
                        companyId: that.dataForm.companyId,
                        // 生年月
                        staffBirthday: that.dataForm.staffBirthday,
                        // 营业开始日期
                        operationStartDate: that.dataForm.operationStartDate,
                        // 营业结束日期
                        operationEndDate: that.dataForm.operationEndDate,
                        // 直近评价
                        latestEvaluation: that.dataForm.latestEvaluation,
                        activeFlg: that.dataForm.activeFlg,
                        comment: that.dataForm.comment,
                    };
                    console.log('data: ', JSON.stringify(data, null, 2));
                    let method = "";
                    let url = "";

                    // 判断 staffId 是否存在，决定请求类型
                    if (that.dataForm.staffId == "" || that.dataForm.staffId == null) {
                        method = "POST";
                        url = "/staff/addStaff";  // 新增的请求路径
                    } else {
                        method = "PUT";
                        url = `/staff/updateStaff`;  // 修改的请求路径
                    }
                    that.$httpV2(url, method, data, false, function (resp) {
                        ElMessage({
                            message: "操作が成功しました",
                            type: "success",
                        });
                        that.visible = false;
                        that.$emit("refreshDataList");
                    })
                }
            });
        },
        handleDialogOpen() {
            console.log("Dialog opened");
        },
    }
}
</script>
