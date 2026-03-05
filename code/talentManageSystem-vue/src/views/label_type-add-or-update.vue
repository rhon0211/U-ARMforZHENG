<template>
	<el-dialog
		:title="!dataForm.labelId ? $t('Add') : $t('Modify')"
        v-if="isAuth(['0', 'label_update', 'label_add'])"
		:close-on-click-modal="false"
		v-model="visible"
		width="450px"
		:destroy-on-close="true"
	>
		<el-form :model="dataForm" ref="dataForm" :rules="dataRule" label-width="200px">
			<el-form-item :label="$t('Label Type in Chinese')" prop="typeName">
				<el-input v-model="dataForm.typeName" style="width:100%" clearable />
			</el-form-item>
			<el-form-item :label="$t('Label Type in Japanese')" prop="typeNameJap">
				<el-input v-model="dataForm.typeNameJap" style="width:100%" clearable />
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
				typeId: null,
				typeName: null,
				typeNameJap: null,
			},
			dataRule: {
				typeName: [],
				typeNameJap: [],
			}
		};
	},
	methods: {
		reset: function() {
		    let dataForm = {
		        typeId: null,
		        typeName: null,
				typeNameJap: null,
		    };
		    this.dataForm = dataForm;
		},
		init: function(typeId) {
		    let that = this;
			that.setFormRules();
		    that.reset();
		    that.dataForm.typeId = typeId || 0;
		    that.visible = true;
		    that.$nextTick(() => {
		        that.$refs['dataForm'].resetFields();
				if (typeId) {
					that.$http('/labelType/updateSearchById', 'POST', { typeId : typeId }, true, function(resp) {
						that.dataForm.typeName = resp.typeName;
						that.dataForm.typeNameJap = resp.typeNameJap;
						that.dataForm.typeId = typeId;
					});
				}
		    });
		},
		dataFormSubmit: function() {
		    let that = this;
		    this.$refs['dataForm'].validate(valid => {
                // 根据条件设置 operation 变量的值
                const operation = !that.dataForm.typeId ? 'insert' : 'update';
                // 根据 operation 的值设置 method 变量的值
                const method = operation === 'insert' ? 'POST' : 'PUT';
		        if (valid) {
		            that.$http('/labelType', method, that.dataForm, true, function(resp) {
		                    ElMessage({
		                        message: '操作が正常に完了しました',
		                        type: 'success'
		                    });
		                    that.visible = false;
		                    that.$emit('refreshDataList');
		                }
		            );
		        }
		    });
		},
		setFormRules: function() {
            this.dataRule.typeName = [
                { required: true, message: this.$t('Enter Label Type Name in Chinese') }
            ];
            this.dataRule.typeNameJap = [
                { required: true, message: this.$t('Enter Label Type Name in Japanese')}
            ];
            this.dataRule.typeId = [
                { required: true, message: this.$t('Select Label Type')}
            ]
        }
	}
}
</script>

<style lang="less" scoped="scoped"></style>