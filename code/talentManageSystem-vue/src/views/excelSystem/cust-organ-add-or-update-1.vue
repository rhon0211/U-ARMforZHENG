<template>
    <el-dialog :title="dataForm.organizationId == '' || dataForm.organizationId == null ? '追加' : '変更'"
        :close-on-click-modal="false" v-model="visible">
        <el-scrollbar>
            <el-form :model="dataForm" ref="dataForm" :rules="dataRule">
                <el-form-item label="姓名：" prop="organizationName">
                    <el-input v-model="dataForm.organizationName" clearable />
                </el-form-item>
                <el-form-item label="所属：" prop="belong">
                    <el-input type="textarea" rows="1" v-model="dataForm.belong" clearable />
                </el-form-item>

            </el-form>
        </el-scrollbar>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="visible = false">キャンセル</el-button>
                <el-button type="primary" @click="dataFormSubmit">確定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
export default {
    data() {
        return {
            visible: false,
            dataForm: {
                organizationId: '',
                organizationName: '',
                belong: '',
            },
            // dataList: [],
            dataRule: {
                organizationName: [
                    { required: true, message: '姓名不能為空', trigger: 'blur' }
                ],
                belong: [
                    { required: true, message: '所属不能為空', trigger: 'blur' }
                ]
            }
        };

    },
    methods: {
        init(orgInfo) {
            let that = this;
            that.reset();
            that.$nextTick(() => {
                that.$refs["dataForm"].resetFields();
                if (orgInfo != null && orgInfo != "") {
                    that.dataForm = {
                        ...JSON.parse(JSON.stringify(orgInfo)),
                    };
                }
            })
            that.visible = true;
        },
        reset() {
            let dataForm = {
                organizationId: null,
                organizationName: null,
                belong: null,
            };
            this.dataForm = dataForm;
        },

        dataFormSubmit: function () {
            let that = this;
            that.$refs["dataForm"].validate(function (valid) {
                if (valid) {
                    if (that.dataForm.organizationId == "" || that.dataForm.organizationId == null) {
                        let data = {
                            organizationName: that.dataForm.organizationName,
                            belong: that.dataForm.belong,
                        };
                        that.$httpV2("/organizationOne/insertOrganizationOne", "POST", data, false, function (resp) {
                            ElMessage({
                                message: "成功した操作",
                                type: "success",
                            });
                            that.visible = false;
                            that.$emit("refreshDataList");
                        })
                    } else {
                        let data = {
                        organizationId: that.dataForm.organizationId,
                        organizationName: that.dataForm.organizationName,
                        belong: that.dataForm.belong,
                    };
                        that.$httpV2("/organizationOne/updateOrganizationOne", "PUT", data, false, function (resp) {
                            ElMessage({
                                message: "成功した操作",
                                type: "success",
                            });
                            that.visible = false;
                            that.$emit("refreshDataList");
                        })
                    }
                }
            });
        },

    }
};
</script>