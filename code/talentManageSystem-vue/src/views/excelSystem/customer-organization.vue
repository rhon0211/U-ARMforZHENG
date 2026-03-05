<template>
    <el-dialog title="組織管理" :close-on-click-modal="false" v-model="visible" style="height: 60%;">
        <div>
            <el-button-group v-model="type" size="medium" plain style="float: right;">
                <el-button @click="loadOrganizationList(1)">組織１</el-button>
                <el-button @click="loadOrganizationList(2)">組織２</el-button>
                <el-button @click="loadOrganizationList(3)">組織３</el-button>
            </el-button-group>
        </div>

        <div v-if="type == 1">
            <span>
                <el-button type="primary" size="medium" @click="addHandle1()">追加</el-button>
            </span>
            <el-table :data="dataList" border ref="table" v-loading="dataListLoading" :row-key="getRowKeys"
                @sort-change="orderHandle" :cell-style="{ padding: '3px 0' }" style="width: 100%">
                <!-- 序号 -->
                <el-table-column label="番号" type="index" header-align="center" align="center" width="70">
                    <!-- 序号展示 -->
                    <template #default="scope">
                        <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
                    </template>

                </el-table-column>
                <el-table-column label="人員" prop="organizationName" header-align="center" align="center" width="100">
                </el-table-column>
                <el-table-column label="所属" prop="belong" header-align="center" align="center" width="100">

                </el-table-column>

                <el-table-column header-align="center" align="center" label="操作" width="150">
                    <!-- 操作下有删除按钮和修改按钮 -->
                    <template #default="scope">
                        <el-button type="text" size="medium"
                            @click="updateHandle1(scope.row.organizationId)">変更</el-button>
                        <el-button type="text" size="medium"
                            @click="deleteHandle1(scope.row.organizationId)">削除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle"
                :current-page="pageIndex" :page-sizes="[5, 8]" :page-size="pageSize" :total="totalCount"
                layout="total, sizes, prev, pager, next, jumper"></el-pagination>
        </div>
        <div v-if="type == 2">
            <span>
                <el-button type="primary" size="medium" @click="addHandle2()">追加</el-button>
            </span>
            <el-table :data="dataList" border ref="table" v-loading="dataListLoading" :row-key="getRowKeys"
                @sort-change="orderHandle" :cell-style="{ padding: '3px 0' }" style="width: 100%">
                <!-- 序号 -->
                <el-table-column label="番号" type="index" header-align="center" align="center" width="70">
                    <!-- 序号展示 -->
                    <template #default="scope">
                        <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="人員" prop="organizationName" header-align="center" align="center" width="100">

                </el-table-column>
                <el-table-column label="所属" prop="belong" header-align="center" align="center" width="100">

                </el-table-column>

                <el-table-column label="組織１" prop="organizationOneName" header-align="center" align="center"
                    width="100">

                </el-table-column>
                <el-table-column label="組織１所属" prop="organizationOneBelong" header-align="center" align="center"
                    width="100">

                </el-table-column>

                <el-table-column header-align="center" align="center" label="操作" width="150">
                    <!-- 操作下有删除按钮和修改按钮 -->
                    <template #default="scope">
                        <el-button type="text" size="medium"
                            @click="updateHandle2(scope.row.organizationTwoId)">変更</el-button>
                        <el-button type="text" size="medium"
                            @click="deleteHandle2(scope.row.organizationTwoId)">削除</el-button>
                    </template>
                </el-table-column>

            </el-table>
            <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle"
                :current-page="pageIndex" :page-sizes="[5, 10]" :page-size="pageSize" :total="totalCount"
                layout="total, sizes, prev, pager, next, jumper"></el-pagination>
        </div>
        <div v-if="type == 3">
            <span>
                <el-button type="primary" size="medium" @click="addHandle3()">追加</el-button>
            </span>
            <el-table :data="dataList" border ref="table" v-loading="dataListLoading" :row-key="getRowKeys"
                @sort-change="orderHandle" :cell-style="{ padding: '3px 0' }" style="width: 100%">
                <!-- 序号 -->
                <el-table-column label="番号" type="index" header-align="center" align="center" width="70">
                    <!-- 序号展示 -->
                    <template #default="scope">
                        <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="人員" prop="organizationName" header-align="center" align="center" width="100">

                </el-table-column>
                <el-table-column label="所属" prop="belong" header-align="center" align="center" width="100">

                </el-table-column>
                <el-table-column label="組織１" prop="organizationOneName" header-align="center" align="center"
                    width="100">

                </el-table-column>
                <el-table-column label="組織１所属" prop="organizationOneBelong" header-align="center" align="center"
                    width="100">

                </el-table-column>
                <el-table-column label="組織２" prop="organizationTwoName" header-align="center" align="center"
                    width="100">

                </el-table-column>
                <el-table-column label="組織２所属" prop="organizationTwoBelong" header-align="center" align="center"
                    width="100">

                </el-table-column>

                <el-table-column header-align="center" align="center" label="操作" width="150">
                    <!-- 操作下有删除按钮和修改按钮 -->
                    <template #default="scope">
                        <el-button type="text" size="medium"
                            @click="updateHandle3(scope.row.organizationThreeId)">変更</el-button>
                        <el-button type="text" size="medium"
                            @click="deleteHandle3(scope.row.organizationThreeId)">削除</el-button>
                    </template>

                </el-table-column>

            </el-table>
            <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle"
                :current-page="pageIndex" :page-sizes="[5,]" :page-size="pageSize" :total="totalCount"
                layout="total, sizes, prev, pager, next, jumper"></el-pagination>
        </div>

        <el-scrollbar>
            <el-form :model="dataForm" ref="dataForm" :rules="dataRule">

            </el-form>
        </el-scrollbar>

    </el-dialog>
    <add-or-update-1 ref="addOrUpdate1" @refreshDataList="loadOrganizationList(1)"></add-or-update-1>
    <add-or-update-2 ref="addOrUpdate2" @refreshDataList="loadOrganizationList(2)"></add-or-update-2>
    <add-or-update-3 ref="addOrUpdate3" @refreshDataList="loadOrganizationList(3)"></add-or-update-3>

</template>
<script>
import { ElMessageBox } from "element-plus";
import addOrUpdate1 from './cust-organ-add-or-update-1.vue';
import addOrUpdate2 from './cust-organ-add-or-update-2.vue';
import addOrUpdate3 from './cust-organ-add-or-update-3.vue';
import { ElMessage } from "element-plus";
export default {
    components: {
        addOrUpdate1,
        addOrUpdate2,
        addOrUpdate3,
    },
    data: function () {
        return {
            visible: false,
            type: 1,
            // 提交表单时的数据
            dataForm: {
                organizationId: null,
                organizationName: null,
                belong: null,
                organizationOneName: null,
                organizationOneBelong: null,
                organizationTwoName: null,
                organizationTwoBelong: null,
            },
            // 存放查询到的据
            dataList: [],
            pageIndex: 1,
            pageSize: 5,
            totalCount: 0,
            // 加载进度条
            dataListLoading: false,
        }
    },
    methods: {

        loadOrganizationList(value) {
            let that = this;
            that.dataListLoading = true;
            let data = {
                page: that.pageIndex,
                length: that.pageSize,
            };
            this.changeTable(value);
            //判断type等于几，对应不同的url接口地址
            let url = '';
            if (that.type == 1) {
                url = '/organizationOne/getAllByPage';
            } else if (that.type == 2) {
                url = '/organizationTwo/getUpper';
            } else if (that.type == 3) {
                url = '/organizationThree/getUpper';
            }
            that.$httpV2(url, 'GET', data, true, function (resp) {
                let result = resp.result;
                // console.log(result.list);
                that.dataList = result.list;
                console.log(that.dataList);
                that.totalCount = result.totalCount;
                that.dataListLoading = false;
            })

        },
        getRowKeys(row) {
            return row.loadOrganizationId;
        },

        init(technicianInfo) {
            this.visible = true;
            this.loadOrganizationList(1);

        },
        reset() {
            this.dataForm = {
                loadOrganizationId: null,
                loadOrganizationName: null,
            }
        },
        // 定义一个方法，点击组织1，2，3按钮，切换显示的表格
        changeTable(value) {
            let that = this;
            this.type = value;
        },

        sizeChangeHandle(val) {
            let that = this;
            that.pageSize = val;
            that.pageIndex = 1;
            that.loadOrganizationList(that.type)
        },
        // searchHandle() {
        //     this.pageIndex = 1;
        //     this.loadCustomerList();
        // },
        currentChangeHandle(val) {
            let that = this;
            that.pageIndex = val;
            that.loadOrganizationList(that.type)
        },
        //修改方法,3个表格 3个方法
        addHandle1() {
            let that = this;
            that.$nextTick(() => {
                that.$refs.addOrUpdate1.init();
            });
            that.loadOrganizationList(that.type);
        },
        addHandle2() {
            let that = this;
            that.$nextTick(() => {
                that.$refs.addOrUpdate2.init();
            });
            that.loadOrganizationList(that.type);

        },
        addHandle3() {
            let that = this;
            that.$nextTick(() => {
                that.$refs.addOrUpdate3.init();
            });
            that.loadOrganizationList(that.type);
        },
        // 更新方法
        updateHandle1: function (id) {
            let that = this;
            const orgInfo = that.dataList.find((org) => org.organizationId === id);
            that.$nextTick(() => {
                that.$refs.addOrUpdate1.init(orgInfo);
                console.log(orgInfo);
            });
            that.loadOrganizationList(that.type);
        },
        updateHandle2: function (id) {
            let that = this;
            const orgInfo = that.dataList.find((org) => org.organizationTwoId === id);
            that.$nextTick(() => {
                that.$refs.addOrUpdate2.init(orgInfo);
            });
            that.loadOrganizationList(that.type);
        },
        updateHandle3: function (id) {
            let that = this;
            const orgInfo = that.dataList.find((org) => org.organizationThreeId === id);
            that.$nextTick(() => {
                that.$refs.addOrUpdate3.init(orgInfo);
                console.log(orgInfo);
            });
            that.loadOrganizationList(that.type);

        },

        // 删除方法 三张表格对应三个方法
        deleteHandle1: function (organizationId) {
            let that = this;
            ElMessageBox.confirm("レコード削除してよろしいでしょうか？", "警告", {
                confirmButtonText: "確定",
                cancelButtonText: "キャンセル",
                type: 'warning'
            }).then(() => {
                console.log(organizationId);
                // 确保 $httpV2 返回的是一个 Promise
                return that.$httpV2('/organizationOne/deleteOrganizationOne', 'DELETE', { "organizationId": organizationId }, true, function (resp) {
                        if (resp.code === 200) {
                            ElMessage({
                                message: '削除完了しました。',
                                type: 'success',
                                duration: 1200,
                            });
                        } else {
                            ElMessage({
                                message: '削除失敗しました。',
                                type: 'error',
                                duration: 1200,
                            });
                        }
                    })
                    .catch(error => {
                        if (error.response) {
                            console.log('Error:', error.response.data);
                            ElMessage({
                                message: error.response.data.message || 'エラーが発生しました。',
                                type: 'error',
                                duration: 5000,
                            });
                        } else if (error.request) {
                            console.log('Request Error:', error.request);
                            ElMessage({
                                message: 'リクエストに問題があります。',
                                type: 'error',
                                duration: 5000,
                            });
                        } else {
                            console.log('Setup Error:', error.message);
                            ElMessage({
                                message: '設定に問題があります。',
                                type: 'error',
                                duration: 5000,
                            });
                        }
                    });
            });
        },

        // deleteHandle2: function (organizationId) {
        //     let that = this;
        //     ElMessageBox.confirm("レコード削除してよろしいでしょうか？", "警告", {
        //         confirmButtonText: "確定",
        //         cancelButtonText: "キャンセル",
        //         type: 'warning'
        //     }).then(() => {
        //         //捕获异常,code不等于200，直接输出错误信息
        //         console.log(organizationId);
        //         that.$httpV2('/organizationTwo/deleteOrganizationTwo', 'DELETE', { "organizationId": organizationId }, true, function (resp) {
        //             ElMessage({
        //                 message: '削除完了しました。',
        //                 type: 'success',
        //                 duration: 1200,
        //                 onClose: () => {

        //                 }
        //             });
        //         })
        //             .catch((error) => {
        //                 if (error.response.status != 200) {
        //                     ElMessage({
        //                         message: '削除失敗しました。',
        //                         type: 'error',
        //                         duration: 1200
        //                     });
        //                 }
        //             })
        //         that.loadOrganizationList(that.type);

        //     });
        // },
        deleteHandle2: function (organizationId) {
            let that = this;
            ElMessageBox.confirm("レコード削除してよろしいでしょうか？", "警告", {
                confirmButtonText: "確定",
                cancelButtonText: "キャンセル",
                type: 'warning'
            }).then(() => {
                console.log(organizationId);
                // 使用 Promise 处理响应和错误
                that.$httpV2('/organizationTwo/deleteOrganizationTwo', 'DELETE', { "organizationId": organizationId }, true, function (resp) {
                    // 假设这里的响应中有状态码字段
                    if (resp.code === 200) {
                        ElMessage({
                            message: '削除完了しました。',
                            type: 'success',
                            duration: 1200,
                            onClose: () => {
                            }
                        });
                    } else {
                        ElMessage({
                            message: '削除失敗しました。',
                            type: 'error',
                            duration: 1200
                        });
                    }
                })
                    .catch(error => {
                        if (error.response) {
                            // 请求已发出，但服务器响应的状态码不在 2xx 范围内
                            console.log('Error:', error.response.data);
                            ElMessage({
                                message: error.response.data.message || 'エラーが発生しました。',
                                type: 'error',
                                duration: 5000,
                            });
                        } else if (error.request) {
                            // 请求已发出，但没有收到响应
                            console.log('Request Error:', error.request);
                            ElMessage({
                                message: 'リクエストに問題があります。',
                                type: 'error',
                                duration: 5000,
                            });
                        } else {
                            // 一些设置请求时发生错误
                            console.log('Setup Error:', error.message);
                            ElMessage({
                                message: '設定に問題があります。',
                                type: 'error',
                                duration: 5000,
                            });
                        }
                    });
            });
        },





        deleteHandle3: function (organizationId) {
            //根据id删除数据
            let that = this;
            ElMessageBox.confirm("レコード削除してよろしいでしょうか？", "警告", {
                confirmButtonText: "確定",
                cancelButtonText: "キャンセル",
                type: 'warning'
            }).then(() => {
                console.log(organizationId);
                that.$httpV2('/organizationThree/deleteOrganizationThree', 'DELETE', { "organizationId": organizationId }, true, function (resp) {
                    ElMessage({
                        message: '削除完了しました。',
                        type: 'success',
                        duration: 1200,
                        onClose: () => {

                        }
                    });
                })
            })
            // that.loadOrganizationList(that.type);

        }


    }
}



</script>