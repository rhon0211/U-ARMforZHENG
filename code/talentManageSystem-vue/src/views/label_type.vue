<template>
	<div v-if="isAuth(['0', 'label_search'])">
		<el-form :inline="true" :model="dataForm" ref="dataForm">
			<el-form-item prop="typeName">
				<el-input v-model="dataForm.typeName" :placeholder="$t('Label Type Name')" class="input" clearable="clearable" />
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="searchHandle()">{{ $t('Query') }}</el-button>
				<el-button type="primary" @click="addHandle()" v-if="isAuth(['0', 'label_add'])">
					{{ $t('Add') }}
				</el-button>
				<el-button type="danger" @click="deleteHandle()" v-if="isAuth(['0', 'label_delete'])">
					{{ $t('Batch Delete') }}
				</el-button>
			</el-form-item>
		</el-form>
		<el-table
			:data="dataList"
			border
			v-loading="dataListLoading"
			@selection-change="selectionChangeHandle"
			:cell-style="{ padding: '3px 0' }" style="width: 100%;"
		>
			<el-table-column
                type="selection"
                :selectable="selectable"
                header-align="center"
                align="center"
                width="50"
            />
			<el-table-column type="index" header-align="center" align="center" width="100" :label="$t('Serial Number')">
                <template #default="scope">
                    <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
                </template>
            </el-table-column>
			<el-table-column
                prop="typeName"
                header-align="center"
                align="center"
                :label="$t('Label Type')"
                width="400"
                :show-overflow-tooltip="true"
            />
			<el-table-column
				header-align="center"
				align="center"
				width="150"
				v-if="isAuth(['0', 'label_update', 'label_delete'])"
				label="操作"
			>
                <template #default="scope">
                    <el-button
                        type="text"
                        @click="updateHandle(scope.row.typeId)"
						v-if="isAuth(['0', 'label_update'])"
                    >
                        {{ $t('Modify') }}
                    </el-button>
                    <el-button
                        type="text"
                        @click="deleteHandle(scope.row.typeId)"
						v-if="isAuth(['0', 'label_delete'])"
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
import AddOrUpdate from './label_type-add-or-update.vue';
export default {
	components: {
        AddOrUpdate
    },
    data: function() {
        return {
            dataForm: {
                typeName: null,
            },
            dataList: [],
            pageIndex: 1,
            pageSize: 10,
            totalCount: 0,
            dataListLoading: false,
            dataListSelections: [],
        };
    },
	methods: {
		loadDataList: function() {
            let that = this;
            that.dataListLoading = true;
            let data = {
                typeName: that.dataForm.typeName == '' ? null : that.dataForm.typeName,
                page: that.pageIndex,
                length: that.pageSize
            };

            that.$http('/labelType/list', 'POST', data, true, function(resp) {
                let result = resp.result;
                that.dataList = result.list;
                that.totalCount = result.totalCount;
                that.dataListLoading = false;
            });
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
		searchHandle: function() {
		    this.$refs['dataForm'].validate(valid => {
		        if (valid) {
		            this.$refs['dataForm'].clearValidate();
		            if (this.dataForm.typeName == '') {
		                this.dataForm.typeName = null;
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
		addHandle: function() {
		    this.$nextTick(() => {
		        this.$refs.addOrUpdate.init();
		    });
		},
		updateHandle: function(typeId) {
		    this.$nextTick(() => {
		        this.$refs.addOrUpdate.init(typeId);
		    });
		},
		selectionChangeHandle: function(val) {
		    this.dataListSelections = val;
		},
		selectable: function(row, index) {
			if (row.labelName > 0) {
				//含有标签，该行记录的复选框被禁用
				return false;
			}
			return true;
		},
		deleteHandle: function(id) {
			let that = this;
			let ids = id ? [id] : that.dataListSelections.map(item => {return item.typeId;});
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
					that.$http('/labelType', 'DELETE', { ids: ids }, true, function(resp) {
						let result = resp.result;
						if (result) {
							ElMessage({
								message: result,
								type: 'error',
								duration: 1200,
							});
						} else {
							ElMessage({
								message: '操作が正常に完了しました',
								type: 'success',
								duration: 1200,
								onClose: () => {
									that.loadDataList();
								}
							});
						}
					});
				});
			}
		},
		refresh: function () {
			this.loadDataList();
		},
	},
	created: function() {
		this.loadDataList();
		this.$bus.on("refreshTabs", this.refresh);
	}
};
</script>