<template>
	<el-form :inline="true" :model="dataForm" ref="dataForm">
		<el-form-item prop="techName">
			<el-select
				v-model="dataForm.techName"
				filterable
				clearable
				placeholder="技術者氏名"
				:filter-method="filterTechName"
				@visible-change="handleVisibleChange"
				style="width: 170px;"
			>
				<el-option
					v-for="item in filteredTechNames"
					:key="item.technicianId"
					:label="item.technicianName"
					:value="item.technicianName"
				/>
			</el-select>
		</el-form-item>
		<el-form-item>
			<el-button size="medium" type="primary" @click="searchHandle()">検索</el-button>
			<el-button size="medium" type="primary" @click="addHandle()">追加</el-button>
			<el-button size="medium" type="danger" @click="deleteHandle()">一括削除</el-button>
		</el-form-item>
	</el-form>
	<el-table
		ref="table"
		:data="dataList"
		border
		v-loading="dataListLoading"
		@selection-change="selectionChangeHandle"
		:row-key="getRowKeys"
		@sort-change="orderHandle"
		:cell-style="{ padding: '3px 0' }"
		style="width: 100%"
	>
		<el-table-column
			type="selection"
			:selectable="selectable"
			header-align="center"
			align="center"
			width="40"
		/>
		<el-table-column
			header-align="center"
			align="center"
			width="100"
			label="操作"
		>
			<template #default="scope">
				<el-button
					type="text"
					@click="updateHandle(scope.row.technicianId)"
				>
					変更
				</el-button>
				<el-button
					type="text"
					@click="deleteHandle(scope.row.technicianId)"
				>
					削除
				</el-button>
			</template>
		</el-table-column>
		<!--   序号  -->
		<el-table-column
			type="index"
			header-align="center"
			align="center"
			width="60"
			label="番号"
		>
			<template #default="scope">
				<span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
			</template>
		</el-table-column>
		<el-table-column
			header-align="center"
			align="center"
			width="150"
			label="写真"
		>
			<template #default="scope">
				<el-image
					:src="scope.row.picture"
					style="width: 90px; height: 100px; border: 1px solid #D3EDFA; border-radius: 5px;"
					fit="cover"
				>
					<template #error>
						<div class="error-img">
							<el-icon>
								<Picture />
							</el-icon>
						</div>
					</template>
				</el-image>
			</template>
		</el-table-column>    <!--   循环展示表格内容   -->
		<el-table-column
			v-for="(column, index) in visibleColumns"
			:label="column.label"
			:key="index"
			:prop="column.prop"
			header-align="center"
			align="center"
			min-width="150"
			:sortable="column.sortable"
			:show-overflow-tooltip="true"
		/>
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
	<add-or-update
		ref="addOrUpdate"
		@refreshDataList="loadTechnicianList"
	></add-or-update>
</template>

<script>

import AddOrUpdate from "./technician-add-or-update.vue";
import {ElMessage} from "element-plus";

export default {
	components: { AddOrUpdate },
	data: function () {
		return {
			token: localStorage.getItem("token"),
			action: `${this.$baseUrl}/file/upload`,
			selectedOption: null,
			// 技術者下拉列表
			techList: [],

			// 提交表单时的数据
			dataForm: {
				techName: null,
			},
			// 过滤技術者的姓名列表
			filteredTechNames: [],

			// 存放查询到的技術者、プロジェクト、顧客、プロジェクト-技術者数据
			dataList: [],
			pageIndex: 1,
			pageSize: 10,
			totalCount: 0,

			// 加载进度条
			dataListLoading: false,

			columns: [
				{
					key: 0,
					label: "技術者氏名",
					prop: "technicianName",
					visible: true,
					sortable: false,
				},
				{
					key: 1,
					label: "カタカナ",
					prop: "katakana",
					visible: true,
					sortable: false,
				},
				{
					key: 2,
					label: "ローマ字",
					prop: "roman",
					visible: true,
					sortable: false,
				},
				{
					key: 3,
					label: "生年月日",
					prop: "birthday",
					visible: true,
					sortable: false,
				},
				{
					key: 4,
					prop: "belongCompany",
					label: "所属（会社名）",
					visible: true,
					sortable: true,
				},
				{
					key: 5,
					prop: "representative",
					label: "営業担当者",
					visible: true,
					sortable: false,
				},
				{
					key: 6,
					prop: "remark",
					label: "備考",
					visible: true,
					sortable: false,
				},
			],

			dataListSelections: []
		}
	},
	computed: {
		visibleColumns() {
			return this.columns.filter( (column) => column.visible );
		},
	},
	methods: {
		loadTechnicianList() {
			let that = this;
			that.dataListLoading = true;
			let data = {
				page: that.pageIndex,
				length: that.pageSize,
				techName: that.dataForm.techName == null ? '' : that.dataForm.techName,
			};
			that.$httpV2('/technician', 'GET', data, true, function (resp) {
				let result = resp.result;
				that.dataList = result.list;
				that.totalCount = result.totalCount;
				that.dataListLoading = false;
			})
		},
		filterTechName(query) {
			if (query !== '') {
				this.filteredTechNames = this.techList.filter(item => {
					return item.technicianName.toLowerCase().includes(query.toLowerCase());
				});
			} else {
				this.filteredTechNames = this.techList;
			}
		},
		handleVisibleChange(visible) {
			if (visible) {
				this.filteredTechNames = this.techList;
			}
		},
		// 获取技術者全部信息
		loadTechList() {
			let that = this;
			that.$httpV2('/protech/technician', 'GET', null, true, function (resp) {
				let result = resp.result;
				that.techList = result;
			})
		},
		selectionChangeHandle: function (val) {
			this.dataListSelections = val;
		},
		getRowKeys(row) {
			return row.projectTechnicianId;
		},
		sizeChangeHandle(val) {
			this.pageSize = val;
			this.pageIndex = 1;
			this.loadTechnicianList()
		},
		currentChangeHandle(val) {
			this.pageIndex = val;
			this.loadTechnicianList()
		},

		searchHandle() {
			this.pageIndex = 1;
			this.loadTechnicianList();
		},
		addHandle() {
			this.$nextTick(() => {
				this.$refs.addOrUpdate.init();
			});
		},
		updateHandle: function(id) {
			const technicianInfo = this.dataList.find((technician) => technician.technicianId === id);
			this.$nextTick(() => {
				this.$refs.addOrUpdate.init(technicianInfo);
			});
		},
		deleteHandle(id) {
			console.log("id: " + id)
			let that = this;
			let ids = id
				? [id]
				: that.dataListSelections.map(item => {
					console.log(item)
					return item.technicianId;
				});
			console.log("ids: " + ids)
			if (ids.length == 0) {
				ElMessage({
					message: "レコードが選択されていません。",
					type: 'warning',
					duration: 1200
				});
			} else {
				ElMessageBox.confirm("レコード削除してよろしいでしょうか？", "警告", {
					confirmButtonText: "確定",
					cancelButtonText: "キャンセル",
					type: 'warning'
				}).then(() => {
					that.$httpV2('/technician', 'DELETE', { ids: ids }, true, function(resp) {
						ElMessage({
							message: '削除完了しました。',
							type: 'success',
							duration: 1200,
							onClose: () => {
								that.loadTechnicianList();
							}
						});
					});
				});
			}
		},
	},
	created() {
		this.loadTechnicianList();
		this.loadTechList();
	}
}
</script>