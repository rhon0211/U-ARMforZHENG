<template>
	<div v-if="isAuth(['0', 'label_search'])">
		<el-form :inline="true" :model="dataForm" ref="dataForm">
            <el-form-item prop="">
                <el-input v-model="dataForm.labelName" :placeholder="$t('Label Name')" class="input" clearable="clearable" />
            </el-form-item>
            <el-form-item>
                <el-select v-model="dataForm.typeId" class="input" :placeholder="$t('Label Type')" clearable="clearable">
                    <el-option v-for="one in typeList" :label="one.name" :value="one.id" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="searchHandle()">{{ $t('Query') }}</el-button>
                <el-button type="primary" @click="addHandle()" v-if="isAuth(['0', 'label_add'])">
					{{ $t('Add') }}
                </el-button>
                <el-button
                    type="danger"
					v-if="isAuth(['0', 'label_delete'])"
                    @click="deleteHandle()"
                >
					{{ $t('Batch Delete') }}
                </el-button>
            </el-form-item>
        </el-form>
		<el-table
            :data="dataList"
            border
            v-loading="dataListLoading"
            @selection-change="selectionChangeHandle"
            @sort-change="orderHandle"
            :cell-style="{ padding: '3px 0' }"
            style="width: 100%;"
		>
            <el-table-column
                type="selection"
                :selectable="selectable"
                header-align="center"
                align="center"
                width="50"
            />
            <el-table-column
				type="index"
				header-align="center"
				align="center"
				width="100"
				:label="$t('Serial Number')"
			>
                <template #default="scope">
                    <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="labelName"
                header-align="center"
                align="center"
                :label="$t('Label')"
				width="400"
                :show-overflow-tooltip="true"
            />
            <el-table-column
                prop="typeName"
                header-align="center"
                align="center"
                :label="$t('Label Type')"
                sortable
				width="400"
            />
            <el-table-column
				header-align="center"
				v-if="isAuth(['0', 'label_update', 'label_delete'])"
				align="center"
				width="150"
				label="操作"
			>
                <template #default="scope">
                    <el-button
                        type="text"
						v-if="isAuth(['0', 'label_update'])"
                        @click="updateHandle(scope.row.labelId)"
                    >
                        {{ $t('Modify') }}
                    </el-button>
                    <el-button
                        type="text"
						v-if="isAuth(['0', 'label_delete'])"
                        @click="deleteHandle(scope.row.labelId)"
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
import AddOrUpdate from './label_add-or-update.vue';
export default {
	components: {
         AddOrUpdate
    },
	data: function() {
		return {
			typeList: [],
			dataForm: {
				labelName: null,
				typeId: null,
				order: null
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
		loadDataList: function() {
			let that = this;
            that.dataListLoading = true;
            let data = {
                labelName: that.dataForm.labelName == '' ? null : that.dataForm.labelName,
                typeId: that.dataForm.typeId == '' ? null : that.dataForm.typeId,
                order: that.dataForm.order,
                page: that.pageIndex,
                length: that.pageSize
            };
			that.$http('/label/list', 'POST', data, true, function(resp) {
				let result = resp.result;
				that.dataList = result.list;
				that.totalCount = result.totalCount;
				that.dataListLoading = false;
			});
		},
		loadTypeList: function() {
			let that = this;
			that.$http('/labelType/allLabelType', 'GET', null, true, function(resp) {
				let result = resp.result;
				that.typeList = result;
			});
		},
		searchHandle: function() {
			this.$refs['dataForm'].validate(valid => {
		        if (valid) {
		            this.$refs['dataForm'].clearValidate();
		            if (this.dataForm.labelName == '') {
		                this.dataForm.labelName = null;
		            }
		            if (this.dataForm.typeId== '') {
		                this.dataForm.typeId = null;
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
		deleteHandle: function(id) {
		    let that = this;
		    let ids = id
		        ? [id]
		        : that.dataListSelections.map(item => {
		              return item.labelId;
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
		            that.$http('/label', 'DELETE', { ids: ids }, true, function(resp) {
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
		updateHandle: function(labelId) {
		    this.$nextTick(() => {
		        this.$refs.addOrUpdate.init(labelId);
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