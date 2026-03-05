<template>
	<el-dialog
		:title="!dataForm.businessId ? $t('Add') : $t('Modify')"
		:close-on-click-modal="false"
		v-model="visible"
		width="450px"
		:destroy-on-close="true"
	>
		<el-form :model="dataForm" ref="dataForm" :rules="dataRule" label-width="100px">
			<el-form-item :label="$t('Industry Information')" prop="businessName">
				<el-input v-model="dataForm.businessName" style="width:100%" clearable />
			</el-form-item>
			<el-form-item :label="$t('remark')" prop="remark">
				<el-input v-model="dataForm.remark" style="width:100%" clearable />
			</el-form-item>
		</el-form>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="visible = false">{{ $t('Cancel') }}</el-button>
				<el-button type="primary" @click="dataFormSubmit">{{ $t('Confirm') }}</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script>
export default {
	data: function() {
		return {
			visible: false,
			dataForm: {
				businessId: null,
				businessName: null,
				remark: null,
			},
			dataRule: {
				businessName: [],
			}
		};
	},
	methods: {
		reset: function() {
		    let dataForm = {
		        businessId: null,
		        businessName: null,
		    };
		    this.dataForm = dataForm;
		},
		init: function(businessId,businessName,remark) {
		    let that = this;
			that.setFormRules();
		    that.reset();
		    that.dataForm.businessId = businessId || 0;
		    that.dataForm.businessName = businessName;
			that.dataForm.remark = remark;
		    that.visible = true;
		},
		dataFormSubmit: function() {
		    let that = this;
		    this.$refs['dataForm'].validate(valid => {
                // 根据条件设置 operation 变量的值
                const operation = !that.dataForm.businessId ? 'insert' : 'update';
                // 根据 operation 的值设置 method 变量的值
                const method = operation === 'insert' ? 'POST' : 'PUT';
		        if (valid) {
		            that.$http('/business', method, that.dataForm, true, function(resp) {
		                    ElMessage({
		                        message: '操作が正常に完了しました',
		                        type: 'success'
		                    });
		                    that.visible = false;
		                    that.$emit('refreshDataList');
							that.$emit('addNewBusinessCallback');
		                }
		            );
		        }
		    });
		},
		setFormRules: function() {
            let that = this;
            this.dataRule.businessName = [
                { required: true, message: that.$t('Can not be empty') }
            ];
        }
	}
}
</script>

<style lang="less" scoped="scoped"></style>