<template>
	<el-form-item label="标签类型" prop="deptSub">
		<el-cascader v-model="dataForm.typeName" :options="labeltype" clearable />
	</el-form-item>
</template>

<script>
export default {
    data: function() {
        return {
            labeltype: [],

            dataForm: {
                typeName: null,
            },
        };
    },
    methods: {
		loadDeptAndSub: function() {
			let that = this;
			that.$http('/label/allLabel', 'GET', null, false, function(resp) {
				let result = resp.result;
				let labeltype = [];
				for (let one in result) {
					let array = [];
					for (let label of result[one]) {
						array.push({
							value: label.labelId,
							label: label.labelName
						});
					}
					labeltype.push({
						value: one,
						label: one,
						children: array
					});
				}
				that.labeltype = labeltype;
			});
		},
	},
	created: function() {
		this.loadDeptAndSub();
	}
};
</script>