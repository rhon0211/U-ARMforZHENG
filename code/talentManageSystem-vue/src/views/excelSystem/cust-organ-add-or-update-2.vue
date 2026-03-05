<template>
    <el-dialog :title="dataForm.organizationTwoId == '' || dataForm.organizationTwoId == null ? '追加' : '変更'"
        :close-on-click-modal="false" v-model="visible">
        <el-scrollbar>
            <el-form :model="dataForm" ref="dataForm" :rules="dataRule">
                <el-form-item label="姓名：" prop="organizationName">
                    <el-input v-model="dataForm.organizationName" clearable style="width: 170px;" />
                </el-form-item>
                <el-form-item label="所属：" prop="belong">
                    <el-input type="textarea" rows="1" v-model="dataForm.belong" clearable style="width: 170px;" />
                </el-form-item>
                <el-form-item prop="organizationOneName" label="組織１：">
                    <el-select v-model="dataForm.organizationOneName" clearable placeholder="組織１を選択してください"
                        @change="orgOneChanged" @clear="OrganHandleClear" style="width: 170px;">
                        <el-option v-for="item in organizationOneList" :key="item.organizationId"
                            :label="item.organizationName" :value="item.organizationId" />
                    </el-select>
                </el-form-item>
                <el-form-item label="組織１所属：" prop="organizationOneBelong">
                    <el-input type="textarea" rows="1" v-model="dataForm.organizationOneBelong" clearable disabled
                        style="width: 170px;" />
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
                organizationTwoId: '',
                organizationName: '',
                belong: '',
                organizationOneName: '',
                organizationOneBelong: '',
                organizationOneId: '',
                relatedOrg1AndOrg2Id: '',
            },
            dataList: [],
            //获取的组织1的列表
            organizationOneList: [],
            dataRule: {
                organizationName: [
                    { required: true, message: '姓名不能為空', trigger: 'blur' }
                ],
                belong: [
                    { required: true, message: '所属不能為空', trigger: 'blur' }
                ],
                organizationOneName: [
                    { required: true, message: '組織１不能為空', trigger: 'blur' }
                ],
                organizationOneBelong: [
                    { required: true, message: '組織１所属不能為空', trigger: 'blur' }
                ]
            }
        };

    },
    methods: {
        orgOneChanged(val) {
            let that = this;
            that.dataForm.organizationOneBelong = that.organizationOneList.find(one => one.organizationId == val).belong;
            that.dataForm.organizationOneId = that.organizationOneList.find(one => one.organizationId == val).organizationId;
        },
        // init(orgInfo) {
        //     let that = this;
        //     if (orgInfo == null || orgInfo == "") {
        //         that.reset();
        //         that.$refs["dataForm"].resetFields();
        //     } else {
        //         that.dataForm = {
        //             ...JSON.parse(JSON.stringify(orgInfo)),
        //         };
        //     }
        //     //console.log("1111111");
        //     console.log(that.dataForm);
        //     that.$httpV2("/organizationOne/selectAll", "GET", {}, true, function (resp) {
        //         that.organizationOneList = resp.result;
        //     })
        //     that.visible = true;
        // },

        init(orgInfo) {
            let that = this;

            // 判断 orgInfo 是否有效
            if (!orgInfo || Object.keys(orgInfo).length === 0) {
                that.reset();

                // 安全调用 resetFields 方法
                if (that.$refs["dataForm"] && typeof that.$refs["dataForm"].resetFields === "function") {
                    that.$refs["dataForm"].resetFields();
                } else {
                    console.error("dataForm 或 resetFields 未定义");
                }
            } else {
                // 深拷贝 orgInfo
                that.dataForm = {
                    ...JSON.parse(JSON.stringify(orgInfo)),
                };
            }
            console.log(that.dataForm);
            that.$httpV2("/organizationOne/selectAll", "GET", {}, true, function (resp) {
                that.organizationOneList = resp.result;
            })
            that.visible = true;
        },

            reset() {
                let dataForm = {
                    organizationTwoId: null,
                    organizationName: null,
                    belong: null,
                    organizationOneName: null,
                    organizationOneBelong: null,
                    organizationOneId: null,
                    //关联表ID
                    relatedOrg1AndOrg2Id: null,
                };
                this.dataForm = dataForm;
            },
            // 清除选择时触发此方法
            OrganHandleClear() {
                this.dataForm.organizationOneName = '';
                this.dataForm.organizationOneBelong = ''; // 当清楚选择时，也清除所属
            },

            dataFormSubmit: function () {
                let that = this;
                console.log(that.dataForm);
                console.log("222");
                that.$refs["dataForm"].validate(function (valid) {
                    if (that.dataForm.organizationTwoId == "" || that.dataForm.organizationTwoId == null) {
                        let data = {
                            organizationName: that.dataForm.organizationName,
                            belong: that.dataForm.belong,
                            preOrganizationId: that.dataForm.organizationOneId,
                        };
                        that.$httpV2("/organizationTwo/insertOrganizationTwo", "POST", data, false, function (resp) {
                            ElMessage({
                                message: "成功した操作",
                                type: "success",
                            });
                            that.visible = false;
                            that.$emit("refreshDataList");
                        })
                    } else {
                        let data = {
                            organizationName: that.dataForm.organizationName,
                            belong: that.dataForm.belong,
                            organizationId: that.dataForm.organizationTwoId,
                            preOrganizationId: that.dataForm.organizationOneId,
                            relatedId: that.dataForm.relatedOrg1AndOrg2Id,
                        };
                        that.$httpV2("/organizationTwo/updateOrganizationTwo", "PUT", data, false, function (resp) {
                            ElMessage({
                                message: "成功した操作",
                                type: "success",
                            });
                            that.visible = false;
                            that.$emit("refreshDataList");
                        })
                    }
                }
                )
            }

        },


    }

</script>