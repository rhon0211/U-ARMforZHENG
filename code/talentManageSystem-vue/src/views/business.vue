<template>
	<div>
		<el-form :inline="true" :model="dataForm" ref="dataForm">
			<el-form-item>
				<el-button type="primary" @click="addHandle()">
					{{ $t('Add') }}
				</el-button>
			</el-form-item>
		</el-form>
		<el-table
			:data="dataList"
			border
			v-loading="dataListLoading"
			:cell-style="{ padding: '3px 0' }" style="width: 100%;"
		>
			<el-table-column
                type="selection"
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
                prop="businessName"
                header-align="center"
                align="center"
                :label="$t('Industry Classification')"
                width="200"
                :show-overflow-tooltip="true"
            />
			<el-table-column
                prop="remark"
                header-align="center"
                align="center"
                :label="$t('remark')"
                width="400"
                :show-overflow-tooltip="true"
            />
			<el-table-column
				header-align="center"
				align="center"
				width="150"
				label="操作"
			>
                <template #default="scope">
                    <el-button
                        type="text"
                        @click="updateHandle(scope.row.businessId,scope.row.businessName,scope.row.remark)"
                    >
                        {{ $t('Modify') }}
                    </el-button>
                </template>
            </el-table-column>
		</el-table>
		<add-or-update ref="addOrUpdate" @refreshDataList="loadDataList"></add-or-update>
	</div>
</template>

<script>
import AddOrUpdate from './business-add-or-update.vue';
export default {
	components: {
        AddOrUpdate
    },
    data: function() {
        return {
            dataForm: {
                businessName: null,
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
            that.dataListLoading = false;
            let data = {
                businessName: that.dataForm.businessName == '' ? null : that.dataForm.businessName,
            };
            that.$http('/business', 'GET', data, true, function(resp) {
                that.dataList = resp.result;
                that.dataListLoading = false;
            });
        },
		searchHandle: function() {
		    this.$refs['dataForm'].validate(valid => {
		        if (valid) {
		            this.$refs['dataForm'].clearValidate();
		            if (this.dataForm.businessName == '') {
		                this.dataForm.businessName = null;
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
		updateHandle: function(businessId,businessName,remark) {
		    this.$nextTick(() => {
		        this.$refs.addOrUpdate.init(businessId,businessName,remark);
		    });
		},
		selectionChangeHandle: function(val) {
		    this.dataListSelections = val;
		},

		deleteHandle: function(id) {
			let that = this;
			let ids = id ? [id] : that.dataListSelections.map(item => {return item.businessId;});
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
					that.$http('/business', 'DELETE', { ids: ids }, true, function(resp) {
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