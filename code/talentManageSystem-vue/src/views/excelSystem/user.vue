<template>
    <!-- 查询表单，按照名字、权限查询；新增按钮 -->
    <el-form :inline="true">
        <el-form-item style="width: 150px">
            <el-input type="text" v-model="queryParams.name" placeholder="名前" clearable></el-input>
        </el-form-item>
        <el-form-item style="width: 150px">
            <el-select v-model="queryParams.type" placeholder="権限" clearable>
                <el-option value="5" label="高権限"></el-option>
                <el-option value="4" label="一般ユーザー"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="searchHandle">検索</el-button>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="addHandle">追加</el-button>
        </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="dataList" v-loading="dataListLoading">
        <!-- 序号 -->
        <el-table-column label="番号" type="index" align="center" width="60">
            <template #default="scope">
                <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
            </template>
        </el-table-column>
        <!-- 操作 -->
        <el-table-column label="操作" align="center" width="160">
            <template #default="scope">
                <el-select @change="operHandle(scope.row.userId, $event)">
                    <el-option value="1">更新</el-option>
                    <el-option value="2">消去</el-option>
                    <el-option value="3">パスワードを変更</el-option>
                </el-select>
            </template>
        </el-table-column>
        <!-- 用户名 -->
        <el-table-column label="名前" width="120" align="center" prop="userName"></el-table-column>
        <!-- 邮箱 -->
        <el-table-column label="メール" width="260" align="center" prop="email"></el-table-column>
        <!-- 权限 -->
        <el-table-column label="権限" width="100" align="center" prop="permission"></el-table-column>
        <!-- 员工编号 -->
        <el-table-column label="社員コード" width="100" align="center" prop="code"></el-table-column>
        <!-- 手机号 -->
        <el-table-column label="携帯番号" width="160" align="center" prop="phone"></el-table-column>
        <!-- 启用状态 -->
        <el-table-column label="アクティブ" width="100" align="center" prop="active"></el-table-column>
        <!-- 备注 -->
        <el-table-column label="備考欄" width="360" align="center" prop="remark"></el-table-column>
    </el-table>
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
        :page-sizes="[10, 20, 50]" :page-size="pageSize" :total="totalCount"
        layout="total, sizes, prev, pager, next, jumper"></el-pagination>
    <add-or-update ref="addOrUpdate" @refreshDataList="loadDataList"></add-or-update>
</template>

<script>
import addOrUpdate from './user-add-or-update.vue';
export default {
    components: {
        addOrUpdate,
    },
    data() {
        return {
            queryParams: {
                name: '',
                type: '',
            },
            dataList: [
                { userId: 1, userName: 'lxy', email: 'lxy.com', permission: '高権限', code: '1234', phone: '12345', active: 'true', remark: '122' },
            ],
            pageIndex: 1,
            pageSize: 10,
            totalCount: 0,
            dataListLoading: false,
        }
    },
    methods: {
        loadDataList() {
            let that = this;
            that.dataListLoading = true;

            let data = {
                type: (that.queryParams.type != '4' && that.queryParams.type != "5") ? null : that.queryParams.type,
                name: that.queryParams.name == null || that.queryParams.name == '' ? '' : that.queryParams.name,
                page: that.pageIndex,
                length: that.pageSize,
            };

            that.$httpV2('/user/list', "GET", data, false, function (resp) {
                that.dataList = resp.result.list || [];

                // 因为接口的参数命名不规范，产生了以下代码：
                that.dataList.forEach(one => {
                    one.userName = one.name;
                    one.active = one.status;
                });

                that.dataList.forEach(one => {
                    if (one.type == 5) {
                        one.permission = "高権限";
                    } else if (one.type == 4) {
                        one.permission = "普通権限"
                    }
                    if (one.status == 0) {
                        one.active = 'アクティブ'
                    } else {
                        one.active = '非アクティブ'
                    }
                });
                that.totalCount = resp.result.totalCount;

                that.dataListLoading = false;
            });
        },
        searchHandle() {
            let that = this;
            that.loadDataList();
        },
        addHandle() {
            // 新增
            let that = this;
            this.$nextTick(() => {
                that.$refs.addOrUpdate.init(0);
            });
        },
        operHandle(userId, oper) {
            let that = this;
            if (oper == 1) {
                // 更新
                that.$nextTick(() => {
                    that.$refs.addOrUpdate.init(1, userId);
                })
            } else if (oper == 2) {
                // 删除
                that.$confirm('このユーザーを削除しますか。', '確認', {
                    confirmButtonText: '削除',
                    cancelButtonText: 'キャンセル',
                    type: 'warning'
                }).then(() => {
                    that.$httpV2('/user/delete', "DELETE", { 'userId': userId }, false, function (resp) {
                        that.loadDataList();
                    });
                })
            } else if (oper == 3) {
                // 修改密码
                that.$nextTick(() => {
                    that.$refs.addOrUpdate.init(2, userId);
                })
            }
        },
        // 改变分页大小
        sizeChangeHandle(val) {
            this.pageSize = val;
            this.pageIndex = 1;
            this.loadDataList();
        },
        // 翻页
        currentChangeHandle: function (val) {
            this.pageIndex = val;
            this.loadDataList();
        },
    },
    created() {
        this.loadDataList();
    },
}
</script>

<style></style>