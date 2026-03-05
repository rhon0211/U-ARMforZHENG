<template>
  <div class="grid" v-if="showMenu">
    <div class="grid-item">
      <div class="top">
        <SvgIcon name="zhedie" class="icon-svg" />
        <span class="title">稼働状况</span>
      </div>
      <div class="bottom">
        <SvgIcon
          name="arr-left"
          class="icon-svg"
          @click="timeChange('user', 'back')"
        />
        <span @click="gotoUserTable(time.userYear)"
          >{{ time.userYear }}年3月~{{ time.userYear + 1 }}年2月</span
        >
        <SvgIcon
          name="arr-right"
          class="icon-svg"
          @click="timeChange('user', 'forward')"
        />
      </div>
    </div>
    <div class="grid-item">
      <div class="top">
        <SvgIcon name="zhedie" class="icon-svg" />
        <span class="title">売上管理表</span>
      </div>
      <div class="bottom">
        <SvgIcon
          name="arr-left"
          class="icon-svg"
          @click="timeChange('project', 'back')"
        />
        <span
          style="margin-right: 15px"
          @click="gotoProjectTable(time.projectYear, '上期')"
          >{{ time.projectYear }}年上期</span
        >
        <span @click="gotoProjectTable(time.projectYear, '下期')"
          >{{ time.projectYear }}年下期</span
        >
        <SvgIcon
          name="arr-right"
          class="icon-svg"
          @click="timeChange('project', 'forward')"
        />
      </div>
    </div>
    <div class="grid-item">
      <div class="top">
        <SvgIcon name="zhedie" class="icon-svg" />
        <span class="title">作業管理表</span>
      </div>
      <div class="bottom">
        <SvgIcon
          name="arr-left"
          class="icon-svg"
          @click="timeChange('work', 'back')"
        />
        <span
          style="margin-right: 10px"
          @click="gotoWorkTable(workTime3.workYear, workTime3.workMonth)"
          >{{ workTime3.workYear }}年{{ workTime3.workMonth }}月</span
        >
        <span
          style="margin-right: 10px"
          @click="gotoWorkTable(workTime2.workYear, workTime2.workMonth)"
          >{{ workTime2.workYear }}年{{ workTime2.workMonth }}月</span
        >
        <span @click="gotoWorkTable(time.workYear, time.workMonth)"
          >{{ time.workYear }}年{{ time.workMonth }}月</span
        >
        <SvgIcon
          name="arr-right"
          class="icon-svg"
          @click="timeChange('work', 'forward')"
        />
      </div>
    </div>
    <div class="grid-item">
      <div class="top">
        <SvgIcon name="zhedie" class="icon-svg" />
        <span class="title">進捗管理</span>
      </div>
      <div class="bottom"></div>
    </div>
    <div class="grid-item">
      <div class="top">
        <SvgIcon name="zhedie" class="icon-svg" />
        <span class="title">フォーマット関連</span>
      </div>
      <div class="bottom"></div>
    </div>
    <div class="grid-item">
      <div class="top">
        <SvgIcon name="zhedie" class="icon-svg" />
        <span class="title">計画表</span>
      </div>
      <div class="bottom"></div>
    </div>
  </div>
  <!-- 三个页面组件 -->
  <UserTable
    v-if="show.userTable"
    ref="userTable"
    @backToMenu="backToMenu()"
  ></UserTable>
  <ProjectTable
    v-if="show.projectTable"
    ref="projectTable"
    @backToMenu="backToMenu()"
  ></ProjectTable>
  <WorkTable
    v-if="show.workTable"
    ref="workTable"
    @backToMenu="backToMenu()"
  ></WorkTable>
</template>

<script>
import UserTable from "./user_table.vue";
import ProjectTable from "./project_table.vue";
import WorkTable from "./work_table.vue";

export default {
  components: {
    UserTable,
    ProjectTable,
    WorkTable,
  },
  data() {
    return {
      time: {
        // 初始的时间，设为当前年月
        userYear: new Date().getFullYear(),
        userMonth: new Date().getMonth() + 1,
        projectYear: new Date().getFullYear(),
        projectMonth: new Date().getMonth() + 1,
        workYear: new Date().getFullYear(),
        workMonth: new Date().getMonth() + 1,
      },
      show: {
        userTable: false,
        projectTable: false,
        workTable: false,
      },
    };
  },
  computed: {
    showMenu() {
      return !(
        this.show.userTable ||
        this.show.projectTable ||
        this.show.workTable
      );
    },
    workTime2() {
      let time = {
        workYear: this.time.workYear,
        workMonth: this.time.workMonth,
      };
      if (time.workMonth == 1) {
        time.workMonth = 12;
        time.workYear--;
      } else {
        time.workMonth--;
      }
      return time;
    },
    workTime3() {
      let time = {
        workYear: this.time.workYear,
        workMonth: this.time.workMonth,
      };
      if (time.workMonth == 2) {
        time.workMonth = 12;
        time.workYear--;
      } else if (time.workMonth == 1) {
        time.workMonth = 11;
        time.workYear--;
      } else {
        time.workMonth = time.workMonth - 2;
      }
      return time;
    },
  },
  methods: {
    // 时间变动
    timeChange: function (tableName, dirc) {
      // user表
      if (tableName == "user") {
        if (dirc == "back") {
          this.time.userYear--;
        } else if (dirc == "forward") {
          this.time.userYear++;
        }
      }
      // project表
      else if (tableName == "project") {
        if (dirc == "back") {
          this.time.projectYear--;
        } else if (dirc == "forward") {
          this.time.projectYear++;
        }
      }
      // work表
      else if (tableName == "work") {
        if (dirc == "back") {
          if (this.time.workMonth == 1) {
            this.time.workMonth = 12;
            this.time.workYear--;
          } else {
            this.time.workMonth--;
          }
        } else if (dirc == "forward") {
          if (this.time.workMonth == 12) {
            this.time.workMonth = 1;
            this.time.workYear++;
          } else {
            this.time.workMonth++;
          }
        }
      }
    },
    // 跳转
    gotoUserTable: function (year) {
      // 处理年月
      let startTime = year + "-03";
      let endTime = year + 1 + "-02";

      // 显隐
      this.show.userTable = true;

      // 传参
      this.$nextTick(() => {
        this.$refs.userTable.init(startTime, endTime);
      });
    },
    gotoProjectTable: function (year, time) {

      // 处理年月
      let startTime = "";
      let endTime = "";
      if (time == "上期") {
        startTime = year + "-03";
        endTime = year + "-08";
      } else if (time == "下期") {
        startTime = year + "-09";
        endTime = year + 1 + "-02";
      }

      // 显隐
      this.show.projectTable = true;

      // 传参
      this.$nextTick(() => {
        this.$refs.projectTable.init(startTime, endTime);
      });
    },
    gotoWorkTable: function (year, month) {
      // 处理年月
      month = month < 10 ? `0${month}` : `${month}`;
      let time = year + "-" + month;

      // 显隐
      this.show.workTable = true;

      // 传参
      this.$nextTick(() => {
        this.$refs.workTable.init(time);
      });
    },
    backToMenu() {
      this.show.userTable = false;
      this.show.projectTable = false;
      this.show.workTable = false;
    },
  },
};
</script>

<style scoped>
body,
html {
  justify-content: center;
  align-items: center;
}

.grid {
  display: grid;
  grid-template-rows: repeat(3, 120px);
  grid-template-columns: repeat(2, 420px);
  gap: 20px; /* 你可以根据需要调整网格之间的间距 */
  justify-content: center;
  margin-top: 80px;
}

.grid-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border: 1px solid #000; /* 你可以根据需要调整边框 */
  height: 100%; /* 确保父组件有高度 */
}

.top {
  margin-top: 20px;
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  position: relative; /* 使 .title 的绝对定位相对于 .top */
  width: 100%;
}

.top .icon-svg {
  transform: scale(1.1); /* 图片放大1.3倍 */
  margin-left: 30px; /* 左间距30px */
}

.top .title {
  font-size: 1.3em; /* 字体放大1.5倍 */
  font-weight: bold; /* 字体加粗 */
  position: absolute; /* 绝对定位 */
  left: 50%; /* 水平居中 */
  transform: translateX(-50%); /* 调整偏移量以实现真正的居中 */
}

.bottom {
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1; /* 使 top 和 bottom 占据相同的空间 */
  width: 100%;
  margin-bottom: 20px;
}

.bottom .icon-svg + span,
.bottom span + .icon-svg {
  margin-left: 9px; /* 图片和 span 之间的间距 */
}
</style>