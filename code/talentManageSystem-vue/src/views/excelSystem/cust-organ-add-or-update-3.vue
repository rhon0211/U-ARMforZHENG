<template>
    <el-dialog :title="dataForm.organizationThreeId == '' || dataForm.organizationThreeId == null ? '追加' : '変更'"
        :close-on-click-modal="false" v-model="visible">
        <el-scrollbar>
            <el-form :model="dataForm" ref="dataForm" :rules="dataRule">
                <el-form-item label="姓名：" prop="organizationName">
                    <el-input v-model="dataForm.organizationName" clearable />
                </el-form-item>
                <el-form-item label="所属：" prop="belong">
                    <el-input type="textarea" rows="1" v-model="dataForm.belong" clearable />
                </el-form-item>
                <el-form-item prop="organizationOneName" label="組織１：">
                    <el-select v-model="dataForm.organizationOneName" clearable placeholder="組織１を選択してください"
                        @change="orgTwoChanged" @clear="OrganHandleClear" style="width: 170px;">
                        <el-option v-for="item in organizationOneList" :key="item.organizationId"
                            :label="item.organizationName" :value="item.organizationId" />
                    </el-select>
                </el-form-item>
                <el-form-item label="組織１所属：" prop="organizationOneBelong">
                    <el-input type="textarea" rows="1" v-model="dataForm.organizationOneBelong" clearable disabled
                        style="width: 170px;" />
                </el-form-item>
                <el-form-item prop="organizationTwoName" label="組織2：">
                    <el-select v-model="dataForm.organizationTwoName" clearable placeholder="組織2を選択してください"
                        @change="orgTwoBelongChanged" @clear="OrganTwoHandleClear" style="width: 170px;">
                        <el-option v-for="item in organizationTwoList" :key="item.organizationId"
                            :label="item.organizationName" :value="item.organizationId" />
                    </el-select>
                </el-form-item>
                <el-form-item label="組織2所属：" prop="organizationTwoBelong">
                    <el-input type="textarea" rows="1" v-model="dataForm.organizationTwoBelong" clearable disabled
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
                organizationThreeId: '',
                organizationName: '',
                belong: '',
                organizationOneId: '',
                organizationOneName: '',
                organizationOneBelong: '',
                organizationTwoName: '',
                organizationTwoBelong: '',
                organizationTwoId: '',
                relatedOrg1AndOrg2Id: '',
                relatedOrg2AndOrg3Id: '',

            },
            organizationTwoList: [],
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
                ],
                organizationTwoName: [{
                    required: true, message: '組織２不能為空', trigger: 'blur'
                }],
                organizationTwoBelong: [{
                    required: true, message: '組織２所属不能為空', trigger: 'blur'
                }]
            }
        };

    },
    methods: {
        
        orgTwoBelongChanged(val) {
            let that = this;
            that.dataForm.organizationTwoBelong = that.organizationTwoList.find(one => one.organizationId == val).belong;
            that.dataForm.organizationTwoId = that.organizationTwoList.find(one => one.organizationId == val).organizationId;
        },
        orgTwoChanged(val) {
            let that = this;
            that.dataForm.organizationOneBelong = that.organizationOneList.find(one => one.organizationId == val).belong;
            that.dataForm.organizationOneId = that.organizationOneList.find(one => one.organizationId == val).organizationId;


            //向后端发送请求，获取组织1下挂靠的组织2列表
            that.$httpV2("/organizationOne/selectById", "GET", { organizationId: that.dataForm.organizationOneId }, false, function (resp) {
                that.organizationTwoList = resp.result;
            })
            // 获取组织2所属
            // that.dataForm.organizationTwoBelong = that.organizationTwoList.find(one => one.organizationId == that.dataForm.organizationTwoId).belong;
            // that.dataForm.organizationTwoName = that.organizationTwoList.find(one => one.organizationId == that.dataForm.organizationTwoId).organizationName;
        },
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
            }),
            console.log(that.organizationOneList);
            console.log("23333");

            // that.dataForm.organizationOneBelong = that.organizationOneList.find(one => one.organizationId == that.dataForm.organizationOneId).belong;
            // that.dataForm.organizationOneId = that.organizationOneList.find(one => one.organizationId == that.dataForm.organizationOneId).organizationId;
            
            that.visible = true;
        },
        reset() {
            let dataForm = {
                organizationThreeId: null,
                organizationName: null,
                belong: null,
                organizationOneId: null,
                organizationOneName: null,
                organizationOneBelong: null,
                organizationTwoName: null,
                organizationTwoBelong: null,
                organizationTwoId: null,
                relatedOrg1AndOrg2Id: null,
                relatedOrg2AndOrg3Id: null,
            };
            this.dataForm = dataForm;
        },
        
        // 清除选择时触发此方法
        OrganHandleClear() {
            this.dataForm.organizationOneName = '';
            this.dataForm.organizationOneBelong = '';
            // 当清楚选择时，也清除所属
        },
        //清除组织2时触发此方法
        OrganTwoHandleClear() {
            this.dataForm.organizationTwoName = '';
            this.dataForm.organizationTwoBelong = '';
        },
        dataFormSubmit: function () {
            let that = this;
            that.$refs["dataForm"].validate(function (valid) {
                if (valid) {
                    if (that.dataForm.organizationThreeId == "" || that.dataForm.organizationThreeId == null) {
                        let data = {
                            organizationName: that.dataForm.organizationName,
                            belong: that.dataForm.belong,
                            preOrganizationId: that.dataForm.organizationTwoId,

                        };
                        that.$httpV2("/organizationThree/insertOrganizationThree", "POST", data, true, function (resp) {
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
                            organizationThreeId: that.dataForm.organizationThreeId,
                            organizationId: that.dataForm.organizationTwoId,
                            relatedOrg2AndOrg3Id: that.dataForm.relatedOrg2AndOrg3Id,

                        };
                        that.$httpV2("/organizationThree/updateOrganizationThree", "PUT", data, true, function (resp) {
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
}
</script>