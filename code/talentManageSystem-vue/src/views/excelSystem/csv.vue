<template>
  <div class="sales-csv-export">
    <h3>売上管理CSV</h3>
    <el-form :model="form" class="form-container" label-position="top">
      <!-- 年度选择框 -->
      <el-form-item label="対象年度" class="year-container">
        <el-select v-model="form.selectedYear" placeholder="年を選択してください" @change="handleYearChange">
          <el-option v-for="year in yearOptions" :key="year" :label="year+ '年度'" :value="year" />
        </el-select>
      </el-form-item>

      <!-- CSV出力按钮 -->
      <el-form-item>
        <el-button type="primary" @click="exportData" class="button-container">CSV出力</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

export default {
  data() {
    return {
      form: {
        selectedYear: new Date().getFullYear()
      },
      yearOptions: []
    };
  },
  created() {
        this.generateYearList();
    },
  methods: {
    generateYearList() {
      const currentYear = new Date().getFullYear();
      const startYear = currentYear - 10; // 可调整起始年份
      const endYear = currentYear + 10; // 可调整结束年份

      // 生成年份范围数组
      this.yearOptions = [];
      for (let year = startYear; year <= endYear; year++) {
        this.yearOptions.push(year);
      }
      //默认选中当前年份
      this.selectedYear = currentYear;
    },
    handleYearChange(newYear) {
      this.selectedYear = newYear;
    },
    exportData() {
      let that = this;
      let year = this.form.selectedYear;
      let url = `/sales/getExcel/${year}`;
      that.$httpV2(url, 'GET', null, true, function (response) {
        // 成功回调不会被调用，因为我们改用了blob处理方式
      }, {
        dataType: undefined,  // 不要尝试解析响应为JSON
        xhrFields: {
          responseType: 'blob'
        },
        success: function (response) {
          try {
            const blob = new Blob([response], {
              type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
            });
            const objectUrl = URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = objectUrl;
            a.download = "csv.xlsx";

            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(objectUrl);
          } catch (error) {
            console.error('ファイルのダウンロードに失敗しました', error);
            that.$message.error('ファイルのダウンロードに失敗しました');
          }
        }
      });
    }
  },


};
</script>

<style lang="less" scoped>
.sales-csv-export {
  margin: 20px;
}

.year-container {
  margin-left: 60px;
}

.el-select {
  margin-left: 80px;
}

.form-container {
  align-items: center;
  gap: 30px;
  display: block;
}

.button-container {
  margin-left: 350px;
}
</style>