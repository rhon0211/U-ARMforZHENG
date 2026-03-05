<template>
    <el-dialog :title="dataForm.departmentId == null || dataForm.departmentId == '' ? '追加' : '変更'"
        :close-on-click-modal="false" v-model="visible" width="800px" @open="handleDialogOpen">
        <el-form :model="dataForm" ref="dataForm" :rules="dataRule" :validate-on-rule-change="false"
            @dbclick.stop.prevent>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="部署名" prop="departmentName">
                        <el-input v-model="dataForm.departmentName" placeholder="部署名を入力してください" clearable
                            @input="limitLength('departmentName', 30)"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="締日" prop="closingDate">
                        <el-select v-model="dataForm.closingDate" placeholder="締日">
                            <el-option v-for="item in closingDateOptions" :key="item.value" :label="item.label"
                                :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="部署略称" prop="departmentAbbreviation">
                        <el-input v-model="dataForm.departmentAbbreviation" placeholder="部署略称" clearable
                            @input="limitLength('departmentAbbreviation', 4)"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="責任者役職" prop="principalJob">
                        <el-input v-model="dataForm.principalJob" placeholder="責任者役職" clearable></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="責任者名" prop="principalName">
                        <el-input v-model="dataForm.principalName" placeholder="責任者名" clearable
                            @input="limitLength('principalName', 7)"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="体制開始年月日" prop="departmentStartdate">
                        <el-date-picker v-model="dataForm.departmentStartdate" type="date"
                            placeholder="体制開始年月日を選択してください" value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable
                            style="width: 100%" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="状態" prop="activeFlg">
                        <el-radio-group v-model="dataForm.activeFlg">
                            <el-radio-button label="1">アクティブ</el-radio-button>
                            <el-radio-button label="0"
                                :disabled="dataForm.departmentId == null || dataForm.departmentId === ''">
                                非アクティブ
                            </el-radio-button>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="体制終了年月日" prop="departmentEnddate">
                        <el-date-picker v-model="dataForm.departmentEnddate" type="date" placeholder="体制終了年月日を選択してください"
                            value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable style="width: 100%" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="摘要" prop="comment">
                        <el-input v-model="dataForm.comment" placeholder="摘要" clearable type="textarea" :rows="4"
                            maxlength="200" :show-word-limit="true"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
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
            dataForm: {
                companyId: null,
                closingDate: null,
                departmentId: null,
                departmentName: null,
                departmentAbbreviation: null,//部署略称
                principalJob: null,//部门責任者
                principalName: null,//部门責任者姓名
                departmentStartdate: null,//開始日期
                departmentEnddate: null,//結束日期
                comment: null,//摘要
                activeFlg: 1,//1:アクティブ。null：非アクティブ。默认为1
            },
            dataRule: {
                departmentName: [
                    { required: true, message: '部署名を入力してください', trigger: 'blur' }
                ],
                closingDate: [
                    { required: true, message: '締日を選択してください', trigger: 'blur' }
                ],
                departmentAbbreviation: [
                    { required: true, message: '部署略称を入力してください', trigger: 'blur' }
                ],
                principalJob: [
                    { required: true, message: '責任者役職を入力してください', trigger: 'blur' }
                ],
                principalName: [
                    { required: true, message: '責任者名を入力してください', trigger: 'blur' }
                ],
                departmentStartdate: [
                    { required: true, message: '体制開始年月日を選択してください', trigger: 'blur' }
                ],
            },
            closingDateOptions: [
                { value: '10日締め', label: '10日締め' },
                { value: '15日締め', label: '15日締め' },
                { value: '20日締め', label: '20日締め' },
                { value: '末日締め', label: '末日締め' },
            ],
        }
    },
    created() {
        this.debouncedSubmit = debounce(this.dataFormSubmit, 1000);
    },
    watch: {
        "dataForm.activeFlg"(newValue) {
            if (newValue === "0") {
                this.checkCanDeactivate();
            }
        }
    },
    methods: {
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

        //打开弹窗，页面初始化
        init: function (departmentInfo) {
            let that = this;
            that.visible = true;  // 先显示对话框
            that.reset();

            // 使用 nextTick 确保表单已经渲染
            that.$nextTick(() => {
                if (that.$refs["dataForm"]) {
                    that.$refs["dataForm"].clearValidate();  // 清除验证
                }

                if (departmentInfo && departmentInfo.departmentId != null && departmentInfo.departmentId != "") {
                    that.$httpV2("/companyAndDepartment/getDepartmentById", "GET", { departmentId: departmentInfo.departmentId }, true, function (resp) {
                        if (resp.code == 200 && resp.result) {
                            let result = resp.result;
                            // activeFlgの安全な変換
                            if (typeof result.activeFlg === 'string') {
                                result.activeFlg = result.activeFlg === 'アクティブ' ? 1 : 0;
                            } else if (result.activeFlg === null || result.activeFlg === undefined) {
                                result.activeFlg = 0;
                            }
                            that.dataForm = result;
                        }
                    });
                } else if (departmentInfo) {
                    // 新規追加または直接編集の場合
                    let newData = JSON.parse(JSON.stringify(departmentInfo));
                    // activeFlgの安全な変換
                    if (typeof newData.activeFlg === 'string') {
                        newData.activeFlg = newData.activeFlg === 'アクティブ' ? 1 : 0;
                    } else if (newData.activeFlg === null || newData.activeFlg === undefined) {
                        newData.activeFlg = 0;
                    }
                    that.dataForm = newData;
                }
            });
        },
        reset: function () {
            let dataForm = {
                companyId: null,
                departmentId: null,
                departmentName: null,
                departmentAbbreviation: null,
                principalJob: null,
                closingDate: null,//締日
                principalName: null,
                departmentStartdate: null,
                departmentEnddate: null,
                comment: null,
                activeFlg: 1,
            }
            this.dataForm = dataForm;
            // 清除验证
            if (this.$refs["dataForm"]) {
                this.$refs["dataForm"].clearValidate();
            }
        },

        dataFormSubmit: function () {
            let that = this;
            that.$refs["dataForm"].validate((valid) => {
                if (valid) {
                    let data = {
                        companyId: that.dataForm.companyId,
                        departmentId: that.dataForm.departmentId,
                        departmentName: that.dataForm.departmentName,
                        departmentAbbreviation: that.dataForm.departmentAbbreviation,
                        principalJob: that.dataForm.principalJob,
                        closingDate: that.dataForm.closingDate,
                        principalName: that.dataForm.principalName,
                        departmentStartdate: that.dataForm.departmentStartdate,
                        departmentEnddate: that.dataForm.departmentEnddate,
                        comment: that.dataForm.comment,
                        activeFlg: that.dataForm.activeFlg,
                    };

                    let method = "";
                    let url = "";
                    if (that.dataForm.departmentId == null || that.dataForm.departmentId == "") {
                        method = "POST";
                        url = "/companyAndDepartment/insertDepartment";
                    } else {
                        method = "PUT";
                        url = "/companyAndDepartment/updateDepartment";
                    }

                    that.$httpV2(url, method, data, false, function (resp) {
                        ElMessage({
                            message: "成功した操作",
                            type: "success"
                        });
                        that.visible = false;
                        that.$emit("refreshDataList");
                    });
                }
            });
        },
        checkCanDeactivate() {
            const departmentId = this.dataForm.departmentId;
            if (!departmentId) return;

            this.$httpV2(
                "/companyAndDepartment/hasActivePj",
                "GET",
                { departmentId: departmentId },
                false,
                (resp) => {
                    console.log("接口响应数据：", resp);

                    if (resp.result === false) {
                        this.$alert(
                            "案件がすべて終了していないので、非アクティブ化できません",
                            "警告",
                            {
                                confirmButtonText: "OK",
                                type: "warning"
                            }
                        );
                        this.dataForm.activeFlg = "1"; // 还原为「アクティブ」
                    }
                }
            );
        },

        handleDialogOpen: function () {
            // 在对话框打开时清除验证状态
            if (this.$refs["dataForm"]) {
                this.$refs["dataForm"].clearValidate();
            }
        },

    },
}
</script>