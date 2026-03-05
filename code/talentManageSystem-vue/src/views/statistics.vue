<template>
  <!-- 选择视图按钮 -->
  <div style="float: right">
    <el-radio-group v-model="view" @change="viewChangeHandle">
      <el-radio-button label="1">{{ $t("Interviewer") }}</el-radio-button>
      <el-radio-button label="2">{{ $t("Month") }}</el-radio-button>
    </el-radio-group>
  </div>

  <!-- 按面试官统计 -->
  <div v-if="view == 1">
    <!-- 搜索面板 -->
    <el-form>
      <el-form-item style="width: 240px; float: left">
        <el-date-picker
          v-model="view1date"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="~"
          :start-placeholder="$t('Begin Date')"
          :end-placeholder="$t('End Date')"
        ></el-date-picker>
      </el-form-item>
      <el-button
        size="medium"
        @click="loadView1Datalist()"
        style="background-color: #409eff; color: white"
      >
        {{ $t("generate") }}
      </el-button>
    </el-form>
    <!-- 柱状图 -->
    <div id="bie1" style="width: 1000px; height: 500px"></div>
    <div id="bie2" style="width: 1000px; height: 500px"></div>
  </div>

  <!-- 按月份统计 -->
  <div v-else>
    <!-- 搜索面板 -->
    <el-form>
      <el-form-item style="width: 240px; float: left">
        <el-date-picker
          v-model="view2Month"
          value-format="YYYY-MM"
          type="monthrange"
          range-separator="~"
          :start-placeholder="$t('Begin Month')"
          :end-placeholder="$t('End Month')"
        ></el-date-picker>
      </el-form-item>
      <el-button
        size="medium"
        @click="loadView2Datalist()"
        style="background-color: #409eff; color: white"
      >
        {{ $t("generate") }}
      </el-button>
    </el-form>
    <div id="bie3" style="width: 1000px; height: 500px"></div>
  </div>
</template>
 
 <script>
import * as echarts from "echarts";
export default {
  data() {
    return {
      view: 1,
      view1date: [],
      view2Month: [],
      chartDate1: null,
      chartDate2: null,
      chartDate3: null,
    };
  },
  methods: {
    viewChangeHandle: function () {
      if (this.view == 1) {
        this.loadView1Datalist();
      } else if (this.view == 2) {
        this.loadView2Datalist();
      }
    },
    loadView1Datalist: function () {
      let startDate = "";
      let endDate = "";
      if (this.view1date != null && this.view1date.length != 0) {
        startDate = this.view1date[0];
        endDate = this.view1date[1];
      }
      // 发请求
      let that = this;
      that.$http(
        "/count/byName",
        "POST",
        {
          type: 0,
          startTime: startDate,
          endTime: endDate,
        },
        false,
        function (resp) {
          that.chartDate1 = resp.result;
        }
      );
      that.$http(
        "/count/byName",
        "POST",
        {
          type: 1,
          startTime: startDate,
          endTime: endDate,
        },
        false,
        function (resp) {
          that.chartDate2 = resp.result;
        }
      );
      this.initBie1();
      this.initBie2();
    },
    loadView2Datalist: function () {
      let startMonth = "";
      let endMonth = "";
      if (this.view2Month != null && this.view2Month.length != 0) {
        startMonth = this.view2Month[0];
        endMonth = this.view2Month[1];
      }
      // 发请求
      let that = this;
      that.$http(
        "/count/byMonth",
        "POST",
        {
          startTime: startMonth,
          endTime: endMonth,
        },
        false,
        function (resp) {
          that.chartDate3 = resp.result;
        }
      );
      this.initBie3();
    },
    initBie1() {
      let that = this;
      let chartDom = document.getElementById("bie1");
      let myChart = echarts.init(chartDom);
      let option = {
        title: {
          text: that.$t("Data statistics (bar chart)"),
          subtext: that.$t("Business interviewer"),
          left: "center",
        },
        xAxis: {
          type: "category",
          data: that.chartDate1.xAxis,
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: that.$t("Interview number count"),
            data: that.chartDate1.series[0].data,
            type: "bar",
            label: {
              show: true,
              position: "top", // 在柱子顶部显示标签
              formatter: function (params) {
                return that.$t("Interview number count") + "：" + params.value;
              },
            },
          },
          {
            name: that.$t("appointed number count"),
            data: that.chartDate1.series[1].data,
            type: "bar",
            label: {
              show: true,
              position: "top", // 在柱子顶部显示标签
              formatter: function (params) {
                return that.$t("appointed number count") + "：" + params.value;
              },
            },
          },
        ],
      };
      option && myChart.setOption(option);
    },
    initBie2() {
      let that = this;
      let chartDom = document.getElementById("bie2");
      let myChart = echarts.init(chartDom);
      let option;
      option = {
        title: {
          text: that.$t("Data statistics (bar chart)"),
          subtext: that.$t("Technology interviewer"),
          left: "center",
        },
        xAxis: {
          type: "category",
          data: that.chartDate2.xAxis,
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: that.$t("Interview number count"),
            data: that.chartDate2.series[0].data,
            type: "bar",
            label: {
              show: true,
              position: "top", // 在柱子顶部显示标签
              formatter: function (params) {
                return that.$t("Interview number count") + "：" + params.value;
              },
            },
          },
          {
            name: that.$t("appointed number count"),
            data: that.chartDate2.series[1].data,
            type: "bar",
            label: {
              show: true,
              position: "top", // 在柱子顶部显示标签
              formatter: function (params) {
                return that.$t("appointed number count") + "：" + params.value;
              },
            },
          },
        ],
      };
      option && myChart.setOption(option);
    },
    initBie3() {
      let that = this;
      let chartDom = document.getElementById("bie3");
      let myChart = echarts.init(chartDom);
      let option;
      option = {
        title: {
          text: that.$t("Data statistics (bar chart)"),
          subtext: that.$t("time statistics"),
          left: "center",
        },
        xAxis: {
          type: "category",
          data: that.chartDate3.xAxis,
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: that.$t("Interview count"),
            data: that.chartDate3.series[0].data,
            type: "bar",
            label: {
              show: true,
              position: "top", // 在柱子顶部显示标签
              formatter: function (params) {
                return that.$t("Interview") + "：" + params.value;
              },
            },
          },
          {
            name: that.$t("appointed count"),
            data: that.chartDate3.series[1].data,
            type: "bar",
            label: {
              show: true,
              position: "top", // 在柱子顶部显示标签
              formatter: function (params) {
                return that.$t("appointed people") + "：" + params.value;
              },
            },
          },
        ],
      };
      option && myChart.setOption(option);
    },
  },
  mounted() {
    this.view = 1;
    this.loadView1Datalist();
  },
};
</script>