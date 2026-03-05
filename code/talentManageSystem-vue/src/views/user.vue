<template>
	<div style="width: 100%;" class="container" v-if="isAuth(['0', 'supadm_search', 'ordadm_search', 'user_search'])">
		<el-form :inline="true" :model="dataForm" ref="dataForm">
            <el-form-item prop="name">
                <el-input v-model="dataForm.name" :placeholder="$t('Japanese name')" class="input" clearable="clearable" />
            </el-form-item>
            <el-form-item prop="pseudonym">
                <el-input v-model="dataForm.pseudonym" :placeholder="$t('Pseudonym')" class="input" clearable="clearable" />
            </el-form-item>
            <el-form-item prop="type">
                <el-select v-model="dataForm.type" class="input" :placeholder="$t('Select User Type')" clearable="clearable">
                    <el-option v-for="one in typeList" :label="one.name" :value="one.id" :key="one.id" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="searchHandle()">{{ $t('Query') }}</el-button>
                <el-button type="primary" @click="addHandle()" v-if="isAuth(['0', 'supadm_search', 'ordadm_search', 'user_search'])">
                    {{ $t('Add') }}
                </el-button>
                <el-button
                    type="danger"
					v-if="isAuth(['0', 'supadm_delete', 'ordadm_delete', 'user_delete'])"
                    @click="deleteHandle()"
                >
                    {{ $t('Batch Delete') }}
                </el-button>
                <el-button
                    type="danger"
					v-if="isAuth(['0', 'supadm_delete', 'ordadm_delete', 'user_delete']) && dataForm.status==='启用'"
                    @click="disableHandle()"
                >
                    {{ $t('Batch Disable') }}
                </el-button>
                <el-button
                    type="danger"
					v-if="isAuth(['0', 'supadm_delete', 'ordadm_delete', 'user_delete']) && dataForm.status==='禁用'"
                    @click="enableHandle()"
                >
                    {{ $t('Batch Able') }}
                </el-button>
            </el-form-item>
			<div style="float: right">
				<el-radio-group v-model="dataForm.status" @change="searchHandle()">
					<el-radio-button label="启用"/>
					<el-radio-button label="禁用"/>
				</el-radio-group>
			</div>
        </el-form>
		<el-table
            :data="dataList"
            border
            v-loading="dataListLoading"
            @selection-change="selectionChangeHandle"
            @sort-change="orderHandle"
            :cell-style="{ padding: '3px 0' }"
            style="width: auto; max-width: 100%;"
		>
            <el-table-column
                type="selection"
                :selectable="selectable"
                header-align="center"
                align="center"
                min-width="50"
            />
            <el-table-column
				type="index"
				header-align="center"
				align="center"
				width="60"
				:label="$t('Serial Number')"
			>
                <template #default="scope">
                    <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="typeName"
                header-align="center"
                align="center"
                :label="$t('User type')"
                sortable
				min-width="160"
                :show-overflow-tooltip="true"
            />
            <el-table-column
                prop="name"
                header-align="center"
                align="center"
                :label="$t('Japanese name')"
				min-width="200"
                :show-overflow-tooltip="true"
            />
            <el-table-column
                prop="pseudonym"
                header-align="center"
                align="center"
                :label="$t('Pseudonym')"
				min-width="200"
            />
            <el-table-column
				v-if="isAuth(['0', 'supadm_search'])"
                prop="account"
                header-align="center"
                align="center"
                :label="$t('Account')"
				min-width="100"
            />
			<el-table-column
				v-if="isAuth(['0', 'supadm_search'])"
				prop="password"
				header-align="center"
				align="center"
				:label="$t('Password')"
				min-width="100"
			>
				<template #default="scope">
					<span
						@click="togglePasswordVisibility(scope.$index)"
						:class="{'password-mask': !dataList[scope.$index].showPassword}">
						{{ dataList[scope.$index].showPassword ? scope.row.password : '●●●●●●' }}
					</span>
				</template>
			</el-table-column>
            <el-table-column
                prop="email"
                header-align="center"
                align="center"
                :label="$t('Email address')"
				min-width="200"
                :show-overflow-tooltip="true"
            />
            <el-table-column
                prop="phone"
                header-align="center"
                align="center"
                :label="$t('Telephone number')"
				min-width="200"
                :show-overflow-tooltip="true"
            />
            <el-table-column
                prop="code"
                header-align="center"
                align="center"
                :label="$t('Employee number')"
				min-width="100"
                :show-overflow-tooltip="true"
            />
            <el-table-column
				header-align="center"
				v-if="isAuth(['0', 'supadm_update', 'supadm_delete', 'ordadm_update', 'ordadm_delete', 'user_update', 'user_delete', 'supadm_disable', 'ordadm_disable', 'user_disable'])"
				align="center"
				min-width="150"
				label="操作"
			>
                <template #default="scope">
					<el-button
						type="text"
						v-if="isAuth(['0', 'supadm_disable', 'ordadm_disable', 'user_disable']) && dataForm.status === '启用'"
						@click="disableHandle(scope.row.userId)"
					>
						{{ $t('Disable') }}
					</el-button>
					<el-button
						type="text"
						v-if="isAuth(['0', 'supadm_disable', 'ordadm_disable', 'user_disable']) && dataForm.status === '禁用'"
						@click="enableHandle(scope.row.userId)"
					>
						{{ $t('Able') }}
					</el-button>
                    <el-button
                        type="text"
						v-if="isAuth(['0', 'supadm_update', 'ordadm_update', 'user_update'])"
                        @click="updateHandle(scope.row.userId)"
                    >
                        {{ $t('Modify') }}
                    </el-button>
                    <el-button
                        type="text"
						v-if="isAuth(['0', 'supadm_delete', 'ordadm_disable', 'user_disable'])"
                        @click="deleteHandle(scope.row.userId)"
                    >
                        {{ $t('Delete') }}
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
		<el-pagination
            @size-change="sizeChangeHandle"
            @current-change="currentChangeHandle"
            :current-page="pageIndex"
            :page-sizes="[10, 20, 50]"
            :page-size="pageSize"
            :total="totalCount"
            layout="total, sizes, prev, pager, next, jumper"
        ></el-pagination>
		<add-or-update ref="addOrUpdate" @refreshDataList="loadDataList"></add-or-update>
	</div>
</template>

<script>
import AddOrUpdate from './user_add-or-update.vue';
export default {
	components: {
         AddOrUpdate
    },
	data: function() {
		return {
			dataForm: {
				name: null,
				pseudonym: null,
				account: null,
				password: null,
				email: null,
				phone: null,
				code: null,
				type: null,
				status: '启用',
				typeName: null,
				order: null,
			},
			dataList: [],
			pageIndex: 1,
			pageSize: 10,
			totalCount: 0,
			dataListLoading: false,
			dataListSelections: [],
		}
	},
	methods: {
		togglePasswordVisibility(index) {
            this.dataList[index].showPassword = !this.dataList[index].showPassword;
        },
		loadTypeList: function() {
		    let that = this;
		    that.$http('/user/allUserType', 'GET', null, true, function(resp) {
				let result = resp.result;
				that.typeList = result;
                console.log(result);
			});
		},
		loadDataList: function() {
			let that = this;
            that.dataListLoading = true;
            let json = { 启用: 0, 禁用: 2 };
            let data = {
                name: that.dataForm.name == '' ? null : that.dataForm.name,
				pseudonym: that.dataForm.pseudonym == '' ? null : that.dataForm.pseudonym,
				type: that.dataForm.type == '' ? null : that.dataForm.type,
				status: json[that.dataForm.status],
				typeName: that.dataForm.typeName == '' ? null : that.dataForm.typeName,
                order: that.dataForm.order,
                page: that.pageIndex,
                length: that.pageSize
            };
			that.$http('/user/list', 'POST', data, true, function(resp) {
				let result = resp.result;
				that.dataList = result.list.map(user => ({ ...user, showPassword: false }));
				// that.dataList = result.list;
				that.totalCount = result.totalCount;
				that.dataListLoading = false;
			});
		},
		searchHandle: function() {
			this.$refs['dataForm'].validate(valid => {
		        if (valid) {
		            this.$refs['dataForm'].clearValidate();
		            if (this.dataForm.userId == '') {
		                this.dataForm.userId = null;
		            }
		            if (this.dataForm.name == '') {
		                this.dataForm.name = null;
		            }
					if (this.dataForm.pseudonym == '') {
						this.dataForm.pseudonym = null;
					}
					if (this.dataForm.email == '') {
						this.dataForm.email  = null;
					}
					if (this.dataForm.phone == '') {
						this.dataForm.phone  = null;
					}
					if (this.dataForm.code == '') {
						this.dataForm.code  = null;
					}
					if (this.dataForm.type == '') {
						this.dataForm.type  = null;
					}
		            if (this.pageIndex != 1) {
		                this.pageIndex = 1;
		            }
		            this.loadDataList();
		        } else {
		            return false;
		        }
		    });
		},
		enableHandle: function(id) {
			let that = this;
		    let ids = id
		        ? [id]
		        : that.dataListSelections.map(item => {
		              return item.userId;
		          });
		    if (ids.length == 0) {
		        ElMessage({
		            message: that.$t('No Records Selected'),
		            type: 'warning',
		            duration: 1200
		        });
		    } else{
		        ElMessageBox.confirm(this.$t('Are you sure you want to enable the selected records?'), this.$t('Prompt Information'), {
		            confirmButtonText: this.$t('Confirm'),
		            cancelButtonText: this.$t('Cancel'),
		            type: 'warning'
		        }).then(() => {
		            that.$http('/user/enable', 'POST', { ids: ids }, true, function(resp) {
		                ElMessage({
		                    message: '操作が正常に完了しました',
		                    type: 'success',
		                    duration: 1200,
		                    onClose: () => {
		                        that.loadDataList();
		                    }
		                });
		            });
		        });
		    }
		},
		disableHandle: function(id) {
			let that = this;
		    let ids = id
		        ? [id]
		        : that.dataListSelections.map(item => {
		              return item.userId;
		          });
		    if (ids.length == 0) {
		        ElMessage({
		            message: that.$t('No Records Selected'),
		            type: 'warning',
		            duration: 1200
		        });
		    } else {
		        ElMessageBox.confirm(this.$t('Are you sure you want to disable the selected records?'), this.$t('Prompt Information'), {
		            confirmButtonText: this.$t('Confirm'),
		            cancelButtonText: this.$t('Cancel'),
		            type: 'warning'
		        }).then(() => {
		            that.$http('/user/disable', 'POST', { ids: ids }, true, function(resp) {
		                ElMessage({
		                    message: '操作が正常に完了しました',
		                    type: 'success',
		                    duration: 1200,
		                    onClose: () => {
		                        that.loadDataList();
		                    }
		                });
		            });
		        });
		    }
		},
		deleteHandle: function(id) {
		    let that = this;
		    let ids = id
		        ? [id]
		        : that.dataListSelections.map(item => {
		              return item.userId;
		          });
		    if (ids.length == 0) {
		        ElMessage({
		            message: that.$t('No Records Selected'),
		            type: 'warning',
		            duration: 1200
		        });
		    } else {
		        ElMessageBox.confirm(this.$t('Are you sure you want to delete the selected records?'), this.$t('Prompt Information'), {
		            confirmButtonText: this.$t('Confirm'),
		            cancelButtonText: this.$t('Cancel'),
		            type: 'warning'
		        }).then(() => {
		            that.$http('/user', 'DELETE', { ids: ids }, true, function(resp) {
		                ElMessage({
		                    message: '操作が正常に完了しました',
		                    type: 'success',
		                    duration: 1200,
		                    onClose: () => {
		                        that.loadDataList();
		                    }
		                });
		            });
		        });
		    }
		},
		sizeChangeHandle: function(val) {
		    this.pageSize = val;
		    this.pageIndex = 1;
		    this.loadDataList();
		},
		currentChangeHandle: function(val) {
		    this.pageIndex = val;
		    this.loadDataList();
		},
		selectionChangeHandle: function (val) {
			this.dataListSelections = val;
		},
		addHandle: function() {
		    this.$nextTick(() => {
		        this.$refs.addOrUpdate.init();
		    });
		},
		updateHandle: function(userId) {
		    this.$nextTick(() => {
		        this.$refs.addOrUpdate.init(userId);
		    });
		},
		selectable: function(row, index) {
		    if (row.emps > 0) {
		        return false;
		    }
		    return true;
		},
		refresh: function () {
			this.loadTypeList();
			this.loadDataList();
		},
	},
	created: function() {
		this.loadTypeList();
		this.loadDataList();
		this.$bus.on("refreshTabs", this.refresh);
	}
}
</script>

<style scoped>
.password-mask {
    cursor: pointer;
    user-select: none;
}
</style>