<template>
    <el-dialog
        :title="!dataForm.labelId ? $t('Add') : $t('Modify')"
        v-if="isAuth(['0', 'label_update', 'label_add'])"
        :close-on-click-modal="false"
        v-model="visible"
        width="450px"
    >
        <el-form :model="dataForm" ref="dataForm" :rules="dataRule" label-width="150px">
            <el-form-item :label="$t('Label Name in Chinese')" prop="labelName">
                <el-input v-model="dataForm.labelName" style="width:100%" clearable />
            </el-form-item>
            <el-form-item :label="$t('Label Name in Japanese')" prop="labelNameJap">
                <el-input v-model="dataForm.labelNameJap" style="width:100%" clearable />
            </el-form-item>
            <el-form-item :label="$t('Label Type')" prop="typeId">
                <el-select v-model="dataForm.typeId" class="input" :placeholder="$t('Select Label Type')" clearable="clearable">
                    <el-option v-for="one in typeList" :label="one.name" :value="one.id" />
                </el-select>
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
            typeList: [],
            dataForm: {
                labelId: null,
                labelName: null,
                labelNameJap: null,
                typeId: null,
                typeName: null,
            },
            dataRule: {
                labelName: [],
                labelNameJap: [],
                typeId: [],
            }
        };
    },

    methods: {
        reset: function() {
            let dataForm = {
                labelId: null,
                labelName: null,
                labelNameJap: null,
                typeId: null,
                typeName: null,
            };
            this.dataForm = dataForm;
        },
        loadTypeList: function() {
		    let that = this;
		    that.$http('/labelType/allLabelType', 'GET', null, true, function(resp) {
				let result = resp.result;
				that.typeList = result;
			});
		},
        init: function(labelId) {
            let that = this;
            that.setFormRules();
            that.reset();
            that.dataForm.labelId = labelId || 0;
            that.visible = true;
            that.$nextTick(() => {
                that.$refs['dataForm'].resetFields();
                that.loadTypeList();
                if (labelId) {
                    that.$http('/label/updateSearchById', 'POST', { labelId: labelId }, true, function(resp) {
                        that.dataForm.labelName = resp.labelName;
                        that.dataForm.labelNameJap = resp.labelNameJap;
                        that.dataForm.typeId = resp.typeId;
                        that.dataForm.typeName = resp.typeName;
                    });
                }
            });
        },
        dataFormSubmit: function() {
		    let that = this;
		    this.$refs['dataForm'].validate(valid => {
                // 根据条件设置 operation 变量的值
                const operation = !that.dataForm.labelId ? 'insert' : 'update';
                // 根据 operation 的值设置 method 变量的值
                const method = operation === 'insert' ? 'POST' : 'PUT';
		        if (valid) {
		            that.$http(
		                `/label/`,
		                method,
		                that.dataForm,
		                true,
		                function(resp) {
		                    ElMessage({
		                        message: '操作が正常に完了しました',
		                        type: 'success'
		                    });
		                    that.visible = false;
		                    that.$emit('refreshDataList');
                            that.$emit('addNewLabelCallback');
		                }
		            );
		        }
		    });
		},
        setFormRules: function() {
            this.dataRule.labelName = [
                { required: true, message: this.$t('Enter Label Name in Chinese') }
            ];
            this.dataRule.labelNameJap = [
                { required: true, message: this.$t('Enter Label Name in Japanese')}
            ];
            this.dataRule.typeId = [
                { required: true, message: this.$t('Select Label Type')}
            ]
        }
    },
}
</script>

<style lang="less" scoped="scoped"></style>
