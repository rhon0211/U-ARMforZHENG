<template>
    <!-- 検索 追加 削除 -->
    <el-form :inline="true" :model="dataForm" ref="dataForm">
        <el-form-item prop="omerName">
            <el-input v-model="input" placeholder="技術者氏名"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button size="medium" type="primary" @click="searchHandle()">検索</el-button>
            <el-button size="medium" type="primary" @click="addHandle()">追加</el-button>
        </el-form-item>
    </el-form>

    <!-- 表格 -->
     <!-- todo 可以做懒加载 -->
    <el-table :data="tableData" style="width: 100%;margin-bottom: 20px;" row-key="id" border
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" @expand-change="handleExpandChange">
        <el-table-column prop="id" label="番号" width="180">
        </el-table-column>
        <el-table-column
        header-align="center"
        align="center"
        width="100"
        label="操作"
    >
      <template #default="scope">
        <el-button
            type="text"
            @click="updateHandle(scope.row.id)"
        >
          変更
        </el-button>
        <el-button
            type="text"
            @click="deleteHandle(scope.row.id)"
        >
          削除
        </el-button>
      </template>
    </el-table-column>
        <el-table-column prop="companyName" label="会社略称" width="180">
        </el-table-column>
        <el-table-column prop="" label="稼働人数" width="180">
        </el-table-column>
        <el-table-column prop="" label="売上合計">
        </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50]"
      :page-size="pageSize"
      :total="totalCount"
      layout="total, sizes, prev, pager, next, jumper"
  ></el-pagination>

</template>

<script>
export default {
    data() {
        return {
            // 示例表格数据
            tableData: [{
                id: 1,
                companyName: "1", 
                children: [{
                    id: 31,
                    companyName: "2"
                }]
            }, {
                id: 2,
                companyName: "2",
                children: [{
                    id: 31,
                    companyName: "3"
                }]
            }, {
                id: 3,
                companyName: "3",
                children: [{
                    id: 32,
                    companyName: "4"
                }]
            }, {
                id: 4,
                companyName: "4",
                children: [{
                    id: 31,
                    companyName: "5"
                }]
            }],
            pageIndex: 1,
            pageSize: 10,
            totalCount: 0,
        }
    },
    methods: {
        handleExpandChange(row, expanded) { 
            if (expanded) { 
                this.expandedRows.push(row.id); 
            } else { 
                this.expandedRows = this.expandedRows.filter(id => id !== row.id); 
            } 
        },
        // 懒加载
        load(tree, treeNode, resolve) {
        setTimeout(() => {
            resolve([
            {
                id: 32,
                companyName: "4"
            }
            ])
        }, 1000)
        },
        searchHandle() {

        },
        addHandle() {

        },
        deleteHandle(id) {

        },
        getRowKeys(row) {
            return row.id;
        },
        created() {
        },
        sizeChangeHandle(val) {
            this.pageSize = val;
            this.pageIndex = 1;
            this.loadComtomerList()
        },
        currentChangeHandle(val) {
            this.pageIndex = val;
            this.loadComtomerList()
        },
        // 加载企业列表
        loadComtList() {
            let that = this;
            that.$httpV2('', 'GET', null, true, function (resp) {
            let result = resp.result;
            that.custList = result;
         })
        },
        handleUpdate(row) { 
            // 更新操作的逻辑 
            this.$message.success(`操作が正常に完了しました: ${row.companyName}`); 
        }, 
        handleDelete(row) { 
            // 删除操作的逻辑 
            this.$message.success(`正常に削除されました: ${row.companyName}`); 
        }
    }
}
</script>
