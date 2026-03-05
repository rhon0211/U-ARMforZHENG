<template>
  <!-- 搜索面板 -->
  <el-form
    v-if="searchPanelShow"
    :model="queryParams"
    ref="queryForm"
    size="small"
    :inline="true"
  >
    <!-- label-width="68px" -->
    <!-- 三个姓名框 -->
    <el-form-item
      :label="$t('Talent Name')"
      prop="userName"
    >
      <el-input
        v-model="queryParams.name"
        :placeholder="$t('JapaneseName')"
        clearable
        style="width: 150px"
      />
    </el-form-item>
    <el-form-item>
      <el-input
        v-model="queryParams.pseudonym"
        :placeholder="$t('pseudonym')"
        clearable
        style="width: 150px"
      /></el-form-item>
    <el-form-item>
      <el-input
        v-model="queryParams.englishName"
        :placeholder="$t('englishName')"
        clearable
        style="width: 150px"
      />
    </el-form-item>
    <br />

    <!-- 国籍 -->
    <el-form-item
      :label="$t('nation')"
      prop="nation"
    >
      <el-select
        v-model="queryParams.nation"
        :placeholder="$t('nation')"
        clearable
        style="width: 150px"
      >
        <el-option
          v-for="dict in dicts.nation"
          :key="dict.dictValue"
          :label="dict.dictLabel"
          :value="dict.dictValue"
        />
      </el-select>
    </el-form-item>
    <!-- 日语等级 -->
    <el-form-item
      :label="$t('japanLevel')"
      prop="japanLevel"
    >
      <el-select
        v-model="queryParams.japanLevel"
        :placeholder="$t('japanLevel')"
        clearable
        style="width: 150px"
      >
        <el-option
          v-for="dict in dicts.japaneseLevel"
          :key="dict.dictValue"
          :label="
            dict.dictLabel.startsWith('N')
              ? dict.dictLabel + $t('and above')
              : dict.dictLabel
          "
          :value="dict.dictValue"
        />
      </el-select>
    </el-form-item>
    <br />

    <!-- 年龄 -->
    <el-form-item
      :label="$t('age')"
      prop="ageStart"
    >
      <el-input
        type="number"
        :controls="false"
        :min="1"
        :max="100"
        :placeholder="$t('Minimum age')"
        v-model="queryParams.ageStart"
        :value="queryParams.ageStart === '' ? null : queryParams.ageStart"
        clearable
        style="width: 64px"
      />
      &nbsp;&nbsp;~&nbsp;&nbsp;
      <el-input
        type="number"
        :controls="false"
        :min="1"
        :max="100"
        :placeholder="$t('Maximum age')"
        v-model="queryParams.ageEnd"
        :value="queryParams.ageEnd === '' ? null : queryParams.ageEnd"
        clearable
        style="width: 64px"
      />
    </el-form-item>
    <!-- 入行年数 -->
    <el-form-item
      :label="$t('exprYear')"
      prop="exprYear"
    >
      <el-select
        v-model="queryParams.exprYear"
        :placeholder="$t('exprYear')"
        clearable
        style="width: 150px"
      >
        <el-option
          value="0"
          :label="$t('No experience')"
        />
        <el-option
          v-for="year in 50"
          :key="year"
          :value="year"
          :label="year + $t('Year above')"
        />
      </el-select>
    </el-form-item>
    <br />

    <!-- 学科 -->
    <el-form-item
      :label="$t('major')"
      prop="major"
    >
      <el-select
        v-model="queryParams.major"
        :placeholder="$t('major')"
        clearable
        style="width: 150px"
      >
        <el-option
          v-for="dict in dicts.major"
          :key="dict.dictValue"
          :label="dict.dictLabel"
          :value="dict.dictValue"
        />
      </el-select>
    </el-form-item>
    <!-- 任用状态 -->
    <el-form-item
      :label="$t('appointStatus')"
      prop="appointStatus"
    >
      <el-select
        v-model="queryParams.appointStatus"
        :placeholder="$t('appointStatus')"
        clearable
        style="width: 150px"
      >
        <el-option
          v-for="dict in dicts.appointStatus"
          :key="dict.dictValue"
          :label="dict.dictLabel"
          :value="dict.dictValue"
        />
      </el-select>
    </el-form-item>
    <br />

    <!-- 技能 -->
    <el-form-item
      :label="$t('Label')"
      prop="labelAndType"
    >
      <el-select
        v-model="newLabel.id"
        :placeholder="$t('Label')"
        clearable
        filterable
        :filter-method="filterHandle"
        @visible-change="visibleChangeHandle"
      >
        <el-option
          v-for="label in filteredLabelAndType"
          :key="label.labelId"
          :label="label.labelName"
          :value="label.labelId"
        />
      </el-select>
    </el-form-item>

    <el-form-item>
      <el-select
        ref="inputYear"
        v-model="newLabel.year"
        type="number"
        :placeholder="$t('Year')"
        style="width: 100px"
        clearable
      >
        <el-option
          v-for="(one, index) in years"
          :key="index"
          :value="one.value"
          :label="one.label"
        />
      </el-select>
    </el-form-item>

    <el-form-item>
      <el-button
        size="medium"
        type="primary"
        @click="addLabelHandle()"
        style="height: 25px"
      >{{ $t("add") }}</el-button>
    </el-form-item>

    <el-form-item>
      <p>
        <el-tag
          v-for="(one, index) in queryParams.labelYears"
          closable
          :key="index"
          :disable-transitions="false"
          @close="closeTagHandle(one)"
        >
          <span v-if="one.year === 0">{{ one.label }}：{{ $t("No limit") }}</span>
          <span v-else>{{ one.label }}：{{ one.year }}年以上</span>
        </el-tag>
      </p>
    </el-form-item>
    <br />

    <!-- 行业信息 -->
    <el-form-item :label="$t('Industry Classification')">
      <el-select
        v-model="queryParams.business"
        clearable
        style="width: 150px;"
      >
        <el-option
          v-for="one in businessList"
          :label="one.businessName"
          :key="one.businessId"
          :value="one.businessId"
        />
      </el-select>&nbsp;&nbsp;
      <el-select
        v-model="queryParams.businessYear"
        placeholder="年"
        clearable
        style="width: 100px;"
      >
        <el-option
          v-for="(one, index) in years"
          :key="index"
          :value="one.value"
          :label="one.label"
        />
      </el-select>
    </el-form-item><br>

    <!-- 公司名 -->
    <el-form-item label="会社名">
      <el-input
        v-model="queryParams.companyName"
        clearable
        style="width: 150px;"
      ></el-input>
    </el-form-item>

    <!-- 稼动时期 -->
    <el-form-item :label="$t('predictMonth')">
      <el-date-picker
        type="month"
        format="YYYY-MM"
        value-format="YYYY-MM"
        :default-value="new Date()"
        v-model="queryParams.predictMonth"
      ></el-date-picker>
    </el-form-item>

    <!-- 备注 -->
    <el-form-item :label="$t('Introduction information')">
      <el-input
        v-model="queryParams.talentDescription"
        style="width: 300px;"
        clearable
      ></el-input>
    </el-form-item>

    <!-- 对应工程 -->
    <el-form-item
      :label="$t('engineer')"
      prop="engineer"
    >
      <el-checkbox-group v-model="queryParams.engineerIds">
        <el-checkbox
          v-for="one in engineerNameList"
          :key="one.engineerId"
          :label="one.engineerId"
        >{{ one.engineerName }}</el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <br />
    <el-form-item>
      <el-button
        size="medium"
        type="primary"
        @click="searchHandle()"
      >{{
        $t("Query")
      }}</el-button>
      <el-button
        size="medium"
        type="primary"
        @click="resetHandle()"
      >{{
        $t("Reset")
      }}</el-button>
    </el-form-item>
  </el-form>

  <!-- 显示/隐藏搜索面板  追加、批量删除人才 -->
  <el-form-item>
    <el-button
      size="medium"
      type="primary"
      @click="searchChangeHandle"
    >
      <div v-if="searchPanelShow">{{ $t("Hind Query") }}</div>
      <div v-else>{{ $t("Show Query") }}</div>
    </el-button>
    <el-button
      size="medium"
      type="primary"
      @click="searchAdd()"
    >{{
      $t("Add Talent")
    }}</el-button>
    <el-button
      size="medium"
      type="primary"
      @click="deleteHandle()"
      :disabled="!isAuth(['talent_delete'])"
    >{{ $t("Batch Delete") }}</el-button>
    <el-button
      size="medium"
      type="primary"
      @click="exportHandle()"
      :disabled="!isAuth(['talent_export'])"
    >{{ $t("export as Excel") }}</el-button>
    &nbsp;&nbsp;&nbsp;
    <el-select :placeholder="$t('increase or decrease column')">
      <el-option
        v-for="item in columns"
        :key="item.key"
        :label="item.label"
        :value="item.key"
      >
        <el-checkbox
          :label="item.label"
          @change="visibleColumnsChangeHandle(item.key)"
          :checked="item.visible"
        >
          {{ item.label }}
        </el-checkbox>
      </el-option>
    </el-select>
    &nbsp;&nbsp;&nbsp;
    <div>
      <span :style="{ color: '#70ad47' }">任用中</span>&nbsp;
      <span :style="{ color: '#f35346' }">任用过</span>
    </div>
  </el-form-item>

  <!-- 表格 -->
  <el-table
    :data="dataList"
    border
    v-loading="dataListLoading"
    @selection-change="selectionChangeHandle"
    :cell-style="{ padding: '3px 0' }"
    style="width: 100%"
    :row-key="getRowKeys"
    @expand-change="expand"
    :expand-row-keys="expands"
    @sort-change="columnSortMethod"
    :row-class-name="rowClassName"
  >
    <!-- 详情 -->
    <el-table-column
      type="expand"
      fixed="left"
    >
      <template #default="scope">
        <div>
          <table class="content">
            <tr>
              <th width="140">{{ $t("school") }}</th>
              <td width="320">{{ dataList[scope.$index].school }}</td>
              <th width="140">{{ $t("major") }}</th>
              <td width="320">
                {{
                  dicts.major.find(
                    (one) => one.dictValue === dataList[scope.$index].major
                  ).dictLabel
                }}
              </td>

              <td
                width="110"
                rowspan="3"
                align="center"
              >
                <el-image
                  style="width: 130px; height: 130px"
                  :src="dataList[scope.$index].picture"
                  :fit="fit"
                >
                  <template #error>
                    <div class="error-img">
                      <el-icon>
                        <Picture />
                      </el-icon>
                    </div>
                  </template>
                </el-image>
              </td>
            </tr>
            <tr>
              <th>{{ $t("inJapanYear") }}</th>
              <td v-if="dataList[scope.$index].inJapanYear === 100">
                {{ $t("Japanese") }}
              </td>
              <td v-else>{{ dataList[scope.$index].inJapanYear }}</td>
              <th>{{ $t("exprYear") }}</th>
              <td>{{ dataList[scope.$index].exprYear }}</td>
            </tr>

            <tr>
              <th>{{ $t("skill sheet") }}</th>
              <td v-if="isAuth(['talent_skill_sheet'])">
                <el-button
                  type="text"
                  @click="downloadHandle(scope.row.skillSheet)"
                  v-if="
                    scope.row.skillSheet != null && scope.row.skillSheet != ''
                  "
                >
                  {{ $t("Click to download") }}
                </el-button>
                <div v-else>{{ $t("Resume not uploaded") }}</div>
              </td>
              <td v-else>{{ $t("You do not have permission to download") }}</td>
              <th>{{ $t("japanLevel") }}</th>
              <td>{{ dataList[scope.$index].japanLevel }}</td>
            </tr>
            <tr v-if="isAuth(['talent_search_lev1secret_info'])">
              <th>{{ $t("phone") }}</th>
              <td>{{ dataList[scope.$index].phone }}</td>
              <th>{{ $t("email") }}</th>
              <td colspan="2">{{ dataList[scope.$index].email }}</td>
            </tr>
            <tr v-if="isAuth(['talent_search_lev1secret_info'])">
              <th>{{ $t("wechat") }}</th>
              <td>{{ dataList[scope.$index].wechat }}</td>
              <th>{{ $t("line") }}</th>
              <td colspan="2">{{ dataList[scope.$index].line }}</td>
            </tr>
            <tr>
              <th>{{ $t("engineer") }}</th>
              <td colspan="4">
                <el-tag
                  v-for="(one, index) in dataList[scope.$index]
                    .engineerNameList"
                  :key="index"
                  :disable-transitions="false"
                >{{ one.engineerName }}
                </el-tag>
              </td>
            </tr>
            <!-- 行业信息 -->
            <tr>
              <th>{{ $t("Industry Information") }}</th>
              <td colspan="4">
                <el-tag
                  v-for="(one, index) in dataList[scope.$index]
                    .talentBusinessList"
                  :key="index"
                  :disable-transitions="false"
                >
                  {{ one.businessName }}:{{ years.find(year => year.value === one.businessYear).label }}
                </el-tag>
              </td>
            </tr>

            <tr>
              <th>{{ $t("Label") }}</th>
              <td colspan="4">
                <el-tag
                  v-for="(one, index) in dataList[scope.$index].talentLabelList"
                  :key="index"
                  :disable-transitions="false"
                >{{ one.labelName }}：{{ one.labelYear }}{{ $t("Year") }}
                </el-tag>
              </td>
            </tr>
            <tr>
              <th>{{ $t("Introduction information") }}</th>
              <td colspan="4">
                {{ dataList[scope.$index].talentDescription }}
              </td>
            </tr>
          </table>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      type="selection"
      :selectable="selectable"
      header-align="center"
      align="center"
      width="50"
      fixed="left"
    />
    <el-table-column
      type="index"
      header-align="center"
      align="center"
      width="100"
      :label="$t('Serial Number')"
      fixed="left"
    >
      <template #default="scope">
        <span>{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</span>
      </template>
    </el-table-column>

    <!-- v-for简化table -->
    <el-table-column
      v-for="(column, index) in visibleColumns"
      :key="index"
      :prop="column.prop"
      header-align="center"
      align="center"
      :label="column.label"
      min-width="120"
      :sortable="column.sortable"
      :fixed="column.fixed"
    />
    <el-table-column
      prop="interviewInfo"
      header-align="center"
      align="center"
      width="200"
      :label="$t('Interview information')"
      sortable
    >
      <template #default="scope">
        <el-button
          type="text"
          @click="interviewHandle(scope.row.talentId)"
        >
          <!-- 如果最新面试日期是空，说明没有被面试过，显示 点击添加 -->
          <div v-if="scope.row.latestDate == '' || scope.row.latestDate == null">未 {{ $t('Add') }}</div>
          <!-- 不为空，显示日期 和 【点击查看】 -->
          <div v-else>{{ scope.row.latestDate }}&nbsp;&nbsp;{{ $t("Click to Check") }}</div>

        </el-button>
      </template>
    </el-table-column>
    <el-table-column
      header-align="center"
      align="center"
      width="200"
      :label="$t('Project experience')"
    >
      <template #default="scope">
        <el-button
          type="text"
          @click="projectHandle(scope.row.talentId)"
        >
          {{ $t("Click to Check") }}
        </el-button>
      </template>
    </el-table-column>
    <el-table-column
      header-align="center"
      align="center"
      width="200"
      :label="$t('operate')"
    >
      <template #default="scope">
        <el-button
          type="text"
          :disabled="!isAuth(['talent_update'])"
          @click="updateHandle(scope.row.talentId)"
        >
          {{ $t("Modify") }}
        </el-button>
        <el-button
          type="text"
          :disabled="!isAuth(['talent_add_blacklist'])"
          @click="blacklistHandle(scope.row.talentId)"
        >
          {{ $t("Add To Blacklist") }}
        </el-button>
        <el-button
          type="text"
          :disabled="!isAuth(['talent_delete'])"
          @click="deleteHandle(scope.row.talentId)"
        >
          {{ $t("Delete") }}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 翻页 -->
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
    @refreshDataList="loadDataList"
  ></add-or-update>
  <interview-info
    ref="InterviewInfo"
    @refreshDataList="loadDataList"
  ></interview-info>
  <project-info
    ref="ProjectInfo"
    @refreshDataList="loadDataList"
  ></project-info>
  <add-to-blacklist
    ref="AddToBlacklist"
    @refreshDataList="loadDataList"
  ></add-to-blacklist>
</template>

<script>
import AddOrUpdate from "./talent-add-or-update.vue";
import InterviewInfo from "./talent-interview-info.vue";
import ProjectInfo from "./talent-project-info.vue";
import AddToBlacklist from "./talent-add-to-blacklist.vue";
import * as XLSX from "xlsx";

export default {
  components: { AddOrUpdate, InterviewInfo, ProjectInfo, AddToBlacklist },
  data() {
    return {
      businessList: [],
      years: [
        { value: 0.1, label: "小于半年" },
        { value: 0.5, label: "半年以上" },
        { value: 1, label: "一年以上" },
        { value: 1.5, label: "一年半以上" },
        { value: 2, label: "两年以上" },
        { value: 2.5, label: "两年半以上" },
        { value: 3, label: "三年以上" },
        { value: 4, label: "四年以上" },
        { value: 5, label: "五年以上" },
        { value: 6, label: "六年以上" },
        { value: 7, label: "七年以上" },
        { value: 8, label: "八年以上" },
        { value: 9, label: "九年以上" },
        { value: 10, label: "十年以上" },
        { value: 12, label: "十二年以上" },
        { value: 15, label: "十五年以上" },
        { value: 18, label: "十八年以上" },
        { value: 20, label: "二十年以上" },
        { value: 25, label: "二十五年以上" },
        { value: 30, label: "三十年以上" },
        { value: 35, label: "三十五年以上" },
        { value: 40, label: "四十年以上" },
      ],
      filteredLabelAndType: [],
      token: localStorage.getItem("token"),
      action: `${this.$baseUrl}/file/upload`,
      blacklistSelectedOne: "",
      dialogVisible: false,
      searchPanelShow: "",
      labelColumns: [],
      newLabelInfo: "",
      pageIndex: 1,
      pageSize: 10,
      totalCount: 0,
      dataListLoading: false,
      dataListSelections: [],
      // 新标签信息
      newLabel: {
        id: "",
        label: "",
        year: "",
      },
      getRowKeys(row) {
        return row.talentId;
      },
      // 表格data
      dataList: [],
      // 搜索条件
      queryParams: {
        labelYears: [],
        name: "", //日语名
        pseudonym: "", //假名
        englishName: "", //英文名
        exprYear: "", //入行年数
        nation: "", //国籍 0日本 1中国 2其他
        inJapanYear: "", //100-日本人
        japanLevel: "", // 日语等级 0-母语者 1~5-N1~N5 6-未考级
        major: "", //学科 0-理科 1-文科
        appointStatus: "", //是否任用过 0-任用中 1-任用过 2-未任用过
        engineerIds: [], // 对应工程
        ageStart: "",
        ageEnd: "",
        orderColumn: "",
        orderSeq: "",
        status: 0,
        delFlag: 0,
        business: "",
        businessYear: "",
        companyName: "",
        predictMonth: "",
      },
      // 字典
      dicts: {
        japaneseLevel: [],
        sex: [],
        company: [],
        interviewType: [],
        nation: [],
        major: [],
        appointStatus: [],
      },
      tableLabelInfo: [],
      // 表格信息
      columns: [
        {
          key: 0,
          label: this.$t("JapaneseName"),
          prop: "name",
          visible: true,
          sortable: false,
          style: "width: 200px",
          fixed: "left",
        },
        {
          key: 1,
          label: this.$t("pseudonym"),
          prop: "pseudonym",
          visible: true,
          sortable: false,
        },
        {
          key: 2,
          label: this.$t("englishName"),
          prop: "englishName",
          visible: true,
          sortable: false,
        },
        {
          key: 3,
          label: this.$t("sex"),
          prop: "sex",
          visible: true,
          sortable: false,
        },
        {
          key: 4,
          label: this.$t("nation"),
          prop: "nation",
          visible: true,
          sortable: false,
        },
        {
          key: 5,
          label: this.$t("age"),
          prop: "age",
          visible: true,
          sortable: true,
        },
        {
          key: 14,
          label: this.$t("predictMonth"),
          prop: "predictMonth",
          visible: true,
          sortable: true,
        },
        {
          key: 6,
          label: this.$t("exprYear"),
          prop: "exprYear",
          visible: true,
          sortable: true,
        },
        {
          key: 7,
          label: this.$t("japanLevel"),
          prop: "japanLevel",
          visible: true,
          sortable: true,
        },
        {
          key: 8,
          label: this.$t("station"),
          prop: "station",
          visible: true,
          sortable: false,
        },
        {
          key: 9,
          label: this.$t("school"),
          prop: "school",
          visible: true,
          sortable: false,
        },
        {
          key: 10,
          label: this.$t("price"),
          prop: "price",
          visible: true,
          sortable: true,
        },
        {
          key: 11,
          label: this.$t("belongCompany"),
          prop: "belongCompany",
          visible: true,
          sortable: false,
        },
        {
          key: 13,
          label: "会社名",
          prop: "companyName",
          visible: true,
          sortable: false,
        },
        {
          key: 12,
          label: this.$t("appointStatus"),
          prop: "appointStatus",
          visible: true,
          sortable: false,
        },
      ],
      // 对应工程信息
      engineerNameList: [],
      // 技能类型和技能
      labelAndType: [],
    };
  },
  computed: {
    visibleColumns() {
      if (this.isAuth(["talent_search_lev2secret_info"])) {
        return this.columns.filter((column) => column.visible);
      } else {
        return this.columns.filter(
          (column) => column.visible && column.key !== 10
        );
      }
    },
  },

  methods: {
    filterHandle(query) {
      let that = this;
      if (query == "" || query == null) {
        that.filteredLabelAndType = that.labelAndType;
      } else {
        that.filteredLabelAndType = that.labelAndType.filter((label) => {
          return label.labelName.toLowerCase().includes(query.toLowerCase());
        });
      }
    },
    visibleChangeHandle(visible) {
      if (visible) {
        this.filteredLabelAndType = this.labelAndType;
      }
    },
    exportHandle() {
      let that = this;
      if (this.dataListSelections.length == 0) {
        ElMessage({
          message: that.$t("No Records Selected"),
          type: "warning",
          duration: 1200,
        });
        return;
      } else {
        // 将数据转换为工作表
        const exportFile = [];
        that.dataListSelections.forEach((one, index) => {
          let temp = {
            日文名: one.name,
            假名: one.pseudonym,
            英文名: one.englishName,
            性别: one.sex,
            生日: one.birth,
            国籍: one.nation,
            来日年数:
              one.inJapanYear == 100 ? "日本人" : `${one.inJapanYear}年`,
            日语等级: one.japanLevel,
            最近车站: one.station,
            毕业学校: one.school,
            学科: one.major == 0 ? "理科" : "文科",
            描述信息: one.talentDescription,
            所属公司: one.belongCompany,
          };
          exportFile.push(temp);
        });
        // 创建一个新的工作簿
        const ws = XLSX.utils.json_to_sheet(exportFile);
        const wb = XLSX.utils.book_new();
        // 将工作表添加到工作簿中
        XLSX.utils.book_append_sheet(wb, ws, "Sheet1");

        // 生成时间戳作为文件名的一部分
        const timestamp = new Date().toISOString().replace(/[-T:.Z]/g, "");
        const filename = `${timestamp}导出文件.xlsx`;

        // 生成Excel文件并触发下载
        XLSX.writeFile(wb, filename);
      }
    },
    showColumn() {
      this.open = true;
    },
    visibleColumnsChangeHandle(key, checked) {
      const selectedOne = this.columns.find((one) => one.key == key);
      selectedOne.visible = !selectedOne.visible;
    },
    downloadFile: function (blob, filename) {
      // 创建一个链接元素
      var link = document.createElement("a");
      // 创建一个指向 Blob 对象的 URL
      link.href = window.URL.createObjectURL(blob);
      // 设置下载属性，包括文件名
      link.download = filename;
      // 隐藏该元素
      link.style.display = "none";
      // 将其添加到 DOM 中
      document.body.appendChild(link);
      // 模拟点击
      link.click();
      // 从 DOM 中移除元素
      document.body.removeChild(link);
      // 清理 blob URL
      window.URL.revokeObjectURL(link.href);
    },
    getFileName: function (url) {
      const match = url.match(/\/([^\/?#]+)$/);
      const fileName = match ? match[1] : null;
      return fileName;
    },
    downloadHandle: function (url) {
      let that = this;
      var xhr = new XMLHttpRequest();
      var requestUrl =
        "http://192.168.1.2:8092/talentManageSystem-api/api/v1/file/download?name=" +
        encodeURIComponent(url);
      console.log("requestUrl:" + requestUrl);
      xhr.open("GET", requestUrl, true);
      xhr.responseType = "blob"; // 重要：设置响应类型为 Blob
      xhr.setRequestHeader("token", localStorage.getItem("token"));
      xhr.send();
      xhr.onload = function () {
        if (xhr.status === 200) {
          // 响应成功，处理下载
          that.downloadFile(xhr.response, that.getFileName(url)); // 假设你知道文件类型和扩展名
        } else {
          console.error("Request failed. Status:", xhr.status);
        }
      };
    },
    updatePhotoSuccess: function (resp) {},
    // 拉黑
    blacklistHandle: function (id) {
      this.$nextTick(() => {
        this.$refs.AddToBlacklist.init(id);
      });
    },
    rowClassName: function ({ row }) {
      let className = "";
      if (row.appointStatus == "任用中") {
        className = "appointingRow";
      } else if (
        row.appointStatus == "任用过" ||
        row.appointStatus == "任用経験あり"
      ) {
        className = "appointedRow";
      } else {
        className = "normalRow";
      }
      return className;
    },
    // 计算年龄
    calculateAge: function (birthday) {
      const birthDate = new Date(birthday); // 将生日字符串转换为日期对象
      const currentDate = new Date(); // 获取当前日期

      // 计算年龄差
      let age = currentDate.getFullYear() - birthDate.getFullYear();

      // 检查是否已经过了生日
      const currentMonth = currentDate.getMonth();
      const birthMonth = birthDate.getMonth();
      if (
        currentMonth < birthMonth ||
        (currentMonth === birthMonth &&
          currentDate.getDate() < birthDate.getDate())
      ) {
        age--; // 如果当前月份小于生日月份，或者当前月份等于生日月份但日期还没到，年龄减一
      }

      return age;
    },
    // 修改
    updateHandle: function (id) {
      const talentInfo = this.dataList.find((talent) => talent.talentId === id);
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(talentInfo);
      });
    },
    // 删除
    deleteHandle: function (id) {
      let that = this;
      let ids = id
        ? [id]
        : that.dataListSelections.map((item) => {
            return item.talentId;
          });
      if (ids.length == 0) {
        ElMessage({
          message: that.$t("No Records Selected"),
          type: "warning",
          duration: 1200,
        });
      } else {
        that
          .$confirm(
            that.$t("Confirm to delete selected records?"),
            that.$t("tip"),
            {
              confirmButtonText: that.$t("Confirm"),
              cancelButtonText: that.$t("Cancel"),
              type: "warning",
            }
          )
          .then(() => {
            that.$http(
              "/talent",
              "DELETE",
              {
                talentIds: ids,
              },
              true,
              function (resp) {
                ElMessage({
                  message: that.$t("Successful operation"),
                  type: "success",
                  duration: 1200,
                  onClose: () => {
                    that.loadDataList();
                  },
                });
              }
            );
          });
      }
    },
    // 清空搜索面板
    resetHandle: function () {
      Object.keys(this.queryParams).forEach((key) => {
        this.queryParams[key] = "";
      });
      this.queryParams.labelYears = [];
      this.queryParams.engineerIds = [];
      this.queryParams.status = 0;
      this.queryParams.delFlag = 0;
      (this.newLabelInfo = ""), (this.newLabel.year = "");
    },
    // 点击隐藏/显示搜索面板
    searchChangeHandle: function () {
      if (this.searchPanelShow == true) {
        this.searchPanelShow = false;
      } else {
        this.searchPanelShow = true;
      }
    },
    // 新增人才
    searchAdd: function () {
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init();
      });
    },
    // 点击已选择的技能标签进行删除
    closeTagHandle: function (tag) {
      let i = this.queryParams.labelYears.indexOf(tag);
      this.queryParams.labelYears.splice(i, 1);
    },
    // 点击添加技能标签和年份
    addLabelHandle: function () {
      let that = this;

      // 判断有没有选择技能
      if (this.newLabel.id == null || this.newLabel.id == "") {
        ElMessage({
          message: that.$t("Please select a skill"),
          type: "warning",
          duration: 1200,
        });
        return;
      }

      // 选择的技能
      let chosenLabel = that.labelAndType.find((label) => {
        return label.labelId === that.newLabel.id;
      });
      this.newLabel.label = chosenLabel.labelName;

      // 判断该技能是不是已经被选择过了
      let tmp = this.queryParams.labelYears.find(
        (labelYear) => labelYear.id === this.newLabel.id
      );

      // 已经选过了
      if (tmp != null) {
        ElMessage({
          message: that.$t("You have already added this skill"),
          type: "warning",
          duration: 1200,
        });
        return;
      }

      // 如果年份没有填，默认为无限制
      if (this.newLabel.year == null || this.newLabel.year == "") {
        this.newLabel.year = 0;
      }

      this.queryParams.labelYears.push({
        id: this.newLabel.id,
        year: this.newLabel.year,
        label: this.newLabel.label,
      });
      this.newLabel.id = "";
      this.newLabel.year = "";
      this.newLabel.label = "";
    },
    columnSortMethod: function (param) {
      if (param.order == "ascending") {
        this.queryParams.orderSeq = "ASC";
      } else if (param.order == "descending") {
        this.queryParams.orderSeq = "DESC";
      } else {
        return;
      }
      if (param.prop === "age") {
        this.queryParams.orderColumn = "birth";
      } else if (param.prop === "japanLevel") {
        this.queryParams.orderColumn = "japan_level";
      } else if (param.prop === "exprYear") {
        this.queryParams.orderColumn = "expr_year";
      } else if (param.prop === "price") {
        this.queryParams.orderColumn = "price";
      } else if (param.prop === "predictMonth") {
        this.queryParams.orderColumn = "predict_month";
      } else if (param.prop === 'interviewInfo'){
        this.queryParams.orderColumn = "interview_info";
      }
      this.dataList = [];
      this.loadDataList();
    },
    interviewHandle: function (id) {
      const talentInfo = this.dataList.find((talent) => talent.talentId === id);
      const interviewInfoList = talentInfo.interviewInfoList;
      this.$nextTick(() => {
        this.$refs.InterviewInfo.init(id, interviewInfoList);
      });
    },
    projectHandle: function (id) {
      const talentInfo = this.dataList.find((talent) => talent.talentId === id);
      const projectInfoList = talentInfo.projectInfoList;
      this.$nextTick(() => {
        this.$refs.ProjectInfo.init(id, projectInfoList);
      });
    },
    loadDataList: function () {
      let that = this;
      // that.dataListLoading = true;
      let url = "";
      // 根据权限决定路径
      if (that.isAuth(["talent_search_lev1secret_info"])) {
        url = "/talent/searchLev1secretInfo";
      } else if (that.isAuth(["talent_search_lev2secret_info"])) {
        url = "/talent/searchLev2secretInfo";
      } else {
        url = "/talent/searchBasicInfo";
      }
      let data = {
        name: that.queryParams.name, //日语名
        pseudonym: that.queryParams.pseudonym, //假名
        englishName: that.queryParams.englishName, //英文名
        exprYear: that.queryParams.exprYear, //入行年数
        nation: that.queryParams.nation, //国籍 0日本 1中国 2其他
        inJapanYear: that.queryParams.inJapanYear, //100-日本人
        japanLevel: that.queryParams.japanLevel, // 日语等级 0-母语者 1~5-N1~N5 6-未考级
        major: that.queryParams.major, //学科 0-理科 1-文科
        appointStatus: that.queryParams.appointStatus, //是否任用过 0-任用中 1-任用过 2-未任用过
        engineerIds: that.queryParams.engineerIds, // 对应工程
        ageStart: that.queryParams.ageStart,
        ageEnd: that.queryParams.ageEnd,
        labelYears: that.queryParams.labelYears.map((item) => ({
          labelId: item.id,
          labelYear: item.year,
        })),
        orderColumn: that.queryParams.orderColumn,
        orderSeq: that.queryParams.orderSeq,
        status: that.queryParams.status,
        delFlag: that.queryParams.delFlag,
        business: that.queryParams.business,
        businessYear:
          that.queryParams.businessYear == "" ||
          that.queryParams.businessYear == null
            ? 0
            : that.queryParams.businessYear,
        page: that.pageIndex,
        length: that.pageSize,
        predictMonth:
          that.queryParams.predictMonth == null
            ? ""
            : that.queryParams.predictMonth,
        companyName:
          that.queryParams.companyName == null
            ? ""
            : that.queryParams.companyName,
        talentDescription:
          that.queryParams.talentDescription == null
            ? ""
            : that.queryParams.talentDescription,
      };
      that.$http(url, "POST", data, false, function (resp) {
        let result = resp.result;
        that.dataList = result.list;
        that.dataList.forEach((talent) => {
          talent.age = that.calculateAge(talent.birth);
          if (talent.predictMonth != "" && talent.predictMonth != null) {
            talent.predictMonth = talent.predictMonth.substring(0, 7);
          }
          if (talent.latestDate != null && talent.latestDate != ''){
            talent.latestDate = talent.latestDate.substring(0,10)
          }
        });
        // 筛选
        that.totalCount = result.totalCount;
        that.dataListLoading = false;
      });
    },
    searchHandle: function () {
      let that = this;
      this.pageIndex = 1;
      this.loadDataList();
      this.labelColumns = this.queryParams.labelYears;
      that.tableLabelInfo;
    },
    loadEngineerNameList: function () {
      let that = this;
      that.$http("/engineer", "GET", null, false, function (resp) {
        that.engineerNameList = resp.result;
      });
    },
    loadDict: function () {
      let that = this;
      that.dicts.japaneseLevel = that.$searchDict("dict_japanese_level");
      that.dicts.sex = that.$searchDict("dict_sex");
      that.dicts.nation = that.$searchDict("dict_nation");
      that.dicts.major = that.$searchDict("dict_major");
      that.dicts.company = that.$searchDict("dict_company");
      that.dicts.appointStatus = that.$searchDict("dict_appoint_status");
      that.dicts.interviewType = that.$searchDict("dict_interviewer_type");
    },
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.loadDataList();
    },
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.loadDataList();
    },
    // 返回人才的主键值
    getRowKeys(row) {
      return row.talentId;
    },
    // 选择行变化
    selectionChangeHandle: function (val) {
      // val：被选中的行的全部信息
      this.dataListSelections = val;
    },
    loadLabelAndType: function () {
      let that = this;
      that.$http("/label/allLabel", "GET", null, false, function (resp) {
        for (let type in resp.result) {
          for (let label in resp.result[type]) {
            that.labelAndType.push({
              labelId: resp.result[type][label].labelId,
              labelName: resp.result[type][label].labelName,
            });
          }
        }
      });
    },
    refresh: function () {
      this.loadDict();
      this.loadEngineerNameList();
      this.loadLabelAndType();
      this.loadDataList();
    },
    loadBusinessList() {
      let that = this;
      that.$http("/business", "GET", null, false, function (resp) {
        that.businessList = resp.result;
      });
    },
  },
  created: function () {
    this.searchPanelShow = true;
    this.loadDict();
    this.loadEngineerNameList();
    this.loadLabelAndType();
    this.loadDataList();
    this.loadBusinessList();
    this.$bus.on("refreshTabs", this.refresh);
  },
};
</script>

<style lang="less" scoped="scoped">
@import url(talent.less);
</style>