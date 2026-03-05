<template>
  <el-dialog
    :title="
      dataForm.talentId == '' || dataForm.talentId == null
        ? $t('add')
        : $t('update')
    "
    :close-on-click-modal="false"
    v-model="visible"
    width="520px"
  >
    <el-scrollbar height="500px">
      <el-form
        :model="dataForm"
        ref="dataForm"
        :rules="dataRule"
        label-width="120px"
      >
        <el-form-item
          :label="$t('JapaneseName')"
          prop="name"
          style="width: 400px"
        ><el-input
            v-model="dataForm.name"
            clearable
          /></el-form-item>
        <el-form-item
          :label="$t('pseudonym')"
          prop="pseudonym"
          style="width: 400px"
        ><el-input
            v-model="dataForm.pseudonym"
            clearable
          /></el-form-item>
        <el-form-item
          :label="$t('englishName')"
          prop="englishName"
          style="width: 400px"
        ><el-input
            v-model="dataForm.englishName"
            clearable
          /></el-form-item>
        <!-- 照片 -->
        <el-form-item
          :label="$t('picture')"
          prop="picture"
        >
          <el-upload
            :action="action"
            :headers="{ token: token }"
            with-credentials="true"
            :on-success="updatePhotoSuccess"
            :on-error="updatePhotoError"
            :show-file-list="false"
          >
            <el-image
              style="width: 100px; height: 100px"
              :src="dataForm.picture"
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
          </el-upload>
        </el-form-item>
        <!-- skillsheet -->
        <el-form-item
          :label="$t('skillSheet')"
          prop="skillSheet"
        >
          <el-upload
            :action="action"
            :headers="{ token: token }"
            with-credentials="true"
            :on-success="updateFileSuccess"
            :on-error="updateFileError"
            :show-file-list="false"
          >
            <el-button
              type="text"
              v-if="
                this.dataForm.skillSheet != null &&
                this.dataForm.skillSheet != ''
              "
            >
              {{ getFileName(dataForm.skillSheet) }}
            </el-button>
            <el-button
              v-else
              type="text"
            >
              {{ $t("Click to upload") }}
            </el-button>
          </el-upload>
        </el-form-item>
        <el-form-item
          :label="$t('sex')"
          prop="sex"
        >
          <el-select
            v-model="dataForm.sex"
            style="width: 200px"
          >
            <el-option
              v-for="one in dicts.sex"
              :key="one.dictValue"
              :label="one.dictLabel"
              :value="one.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="$t('birth')"
          prop="birth"
        >
          <el-date-picker
            v-model="dataForm.birth"
            type="date"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            default-value="2000-01-01"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item
          :label="$t('exprYear')"
          prop="exprYear"
        >
          <el-input
            v-model="dataForm.exprYear"
            type="number"
            style="width: 200px"
            clearable
          ></el-input>
        </el-form-item>
        <!-- 国籍 -->
        <el-form-item
          :label="$t('nation')"
          prop="nation"
        >
          <el-select
            v-model="dataForm.nation"
            style="width: 200px"
            @change="nationChangeHandle"
          >
            <el-option
              v-for="dict in dicts.nation"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <!-- 来日年数 -->
        <el-form-item
          :label="$t('inJapanYear')"
          prop="inJapanYear"
        >
          <el-input
            ref="inputInJapanYear"
            :disabled="isJapanese"
            v-model="displayInJapanYear"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <!-- 日语等级 -->
        <el-form-item
          :label="$t('japanLevel')"
          prop="japanLevel"
        >
          <el-select
            ref="selectJapanLevel"
            :disabled="isJapanese"
            v-model="dataForm.japanLevel"
            style="width: 200px"
          >
            <el-option
              v-for="dict in dicts.japaneseLevel"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="$t('station')"
          prop="station"
        ><el-input
            v-model="dataForm.station"
            style="width: 200px;"
            clearable
          /></el-form-item>
        <el-form-item
          :label="$t('school')"
          prop="school"
        >
          <el-input
            v-model="dataForm.school"
            style="width: 200px;"
            maxlength="50"
            clearable
          />
        </el-form-item>
        <el-form-item
          :label="$t('major')"
          prop="major"
        >
          <el-select
            v-model="dataForm.major"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="dict in dicts.major"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>

        <el-form-item
          :label="$t('price')"
          prop="price"
          :v-if="isAuth([talent_search_lev2secret_info])"
        >
          <el-input
            v-model="dataForm.price"
            type="number"
            style="width: 200px"
            clearable
          ></el-input>
        </el-form-item>

        <!-- 联系方式 -->
        <div :v-if="isAuth([talent_search_lev1secret_info])">
          <el-form-item
            :label="$t('email')"
            prop="email"
          >
            <el-input
              v-model="dataForm.email"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item
            :label="$t('phone')"
            prop="phone"
          >
            <el-input
              v-model="dataForm.phone"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item
            :label="$t('wechat')"
            prop="wechat"
          >
            <el-input
              v-model="dataForm.wechat"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item
            :label="$t('line')"
            prop="line"
          >
            <el-input
              v-model="dataForm.line"
              clearable
            ></el-input>
          </el-form-item>
        </div>
        <el-form-item
          :label="$t('appointStatus')"
          prop="appointStatus"
        >
          <el-select
            v-model="dataForm.appointStatus"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="dict in dicts.appointStatus"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="$t('belongCompany')"
          prop="belongCompany"
        >
          <el-select
            v-model="dataForm.belongCompany"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="dict in dicts.belongCompany"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <!-- 公司名字 -->
        <el-form-item label="会社名">
          <el-input
            v-model="dataForm.companyName"
            style="width: 200px;"
            clearable
          ></el-input>
        </el-form-item>
        <!-- 预计找工作月份 -->
        <el-form-item :label="$t('predictMonth')">
          <el-date-picker
            type="month"
            :default-value="new Date()"
            v-model="dataForm.predictMonth"
            style="width: 200px;"
            format="YYYY-MM"
            value-format="YYYY-MM"
          ></el-date-picker>
        </el-form-item>
        <!-- 对应工程 -->
        <el-form-item
          :label="$t('engineer')"
          prop="engineer"
        >
          <el-select
            @change="selectOneEngineer"
            clearable
            style="width: 300px"
          >
            <el-option
              v-for="one in engineerNameList"
              :key="one.engineerId"
              :value="one.engineerId"
              :label="one.engineerName"
            /></el-select>&nbsp;&nbsp;
          <p>
            <el-tag
              v-for="one in dataForm.engineerNameList"
              :key="one.engineerId"
              closable
              :disable-transitions="false"
              @close="closeEngineerTagHandle(one)"
            >
              {{ one.engineerName }}
            </el-tag>
          </p>
        </el-form-item>
        <!-- 行业信息 -->
        <el-form-item :label="$t('Industry Information')">
          <!-- 行业 -->
          <el-select
            v-model="newBusiness.id"
            :placeholder="$t('Industry Information')"
            clearable
            style="width: 160px;"
          >
            <el-option
              v-for="one in businessNameList"
              :key="one.businessId"
              :value="one.businessId"
              :label="one.businessName"
              style="width: 190px;"
            />
          </el-select>
          &nbsp;&nbsp;
          <!-- 增加按钮 -->
          <el-button
            size="medium"
            type="primary"
            @click="addBusinessHandle()"
            style="width: 120px;"
          >
            {{ $t('add') }}
          </el-button> <br />
          <!-- 年份 -->
          <el-select
            v-model="newBusiness.year"
            :placeholder="$t('Year')"
            style="width: 160px;margin-top: 5px; "
            clearable
          >
            <el-option
              v-for="(one, index) in years"
              :key="index"
              :value="one.value"
              :label="one.label"
            />
          </el-select>
          &nbsp;&nbsp;
          <!-- 新增行业信息按钮 -->
          <el-button
            size="medium"
            type="info"
            @click="addNewBusinessHandle()"
            style="margin-top: 5px; width: 120px;"
          >
            {{ $t('add a new business') }}
          </el-button>&nbsp;&nbsp;
          <!-- 展示标签 -->
          <p>
            <el-tag
              v-for="one in dataForm.talentBusinessList"
              :key="one.businessId"
              closable
              @close="closeBusinessTagHandle(one)"
            >
              {{ one.businessName }}:{{ years.find(year => year.value === one.businessYear).label }}
            </el-tag>
          </p>

        </el-form-item>

        <!-- 技能标签 -->
        <el-form-item
          :label="$t('Label')"
          prop="labelAndType"
        >
          <el-select
            v-model="newLabel.id"
            :placeholder="$t('Label')"
            clearable
            :filter-method="filterHandle"
            filterable
            @visible-change="visibleChangeHandle"
            style="width: 160px;"
          >
            <el-option
              v-for="label in filteredLabelList"
              :key="label.labelId"
              :label="label.labelName"
              :value="label.labelId"
              style="width: 190px"
            />
          </el-select>
          &nbsp;&nbsp;
          <el-button
            size="medium"
            type="primary"
            @click="addLabelHandle()"
            style="width: 120px;"
          >{{ $t("add") }}
          </el-button>
          <br />
          <el-select
            ref="inputYear"
            v-model="newLabel.year"
            type="number"
            :placeholder="$t('Year')"
            style="width: 160px;margin-top: 5px; "
            clearable
          >
            <el-option
              v-for="(one, index) in years"
              :key="index"
              :value="one.value"
              :label="one.label"
            />
          </el-select>
          &nbsp;&nbsp;
          <el-button
            size="medium"
            type="info"
            @click="addNewLabelHandle()"
            style="margin-top: 5px; width:120px"
          >{{ $t("add a new label") }}</el-button>&nbsp;&nbsp;
          <p>
            <el-tag
              v-for="one in dataForm.talentLabelList"
              closable
              :key="one.labelId"
              :disable-transitions="false"
              @close="closeTagHandle(one)"
            >
              {{ one.labelName }}：{{
                one.labelYear == 0 ? "小于半" : one.labelYear
              }}{{ $t("Year") }}
            </el-tag>
          </p>
        </el-form-item>
        <el-form-item
          :label="$t('Introduction information')"
          prop="talentDescription"
        >
          <el-input
            v-model="dataForm.talentDescription"
            type="textarea"
            :rows="6"
            style="width: 100%"
            maxlength="350"
            show-word-limit
            clearable
          />
        </el-form-item>
      </el-form>
    </el-scrollbar>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">{{ $t("Cancel") }}</el-button>
        <el-button
          type="primary"
          @click="dataFormSubmit"
        >{{
          $t("Confirm")
        }}</el-button>
      </span>
    </template>
  </el-dialog>
  <addNewLabel
    ref="addNewLabel"
    @addNewLabelCallback="loadLabelAndType"
  ></addNewLabel>
  <addNewBusiness
    ref="addNewBusiness"
    @addNewBusinessCallback="loadBusinessNameList"
  ></addNewBusiness>
</template>

<script>
import { ElMessage } from "element-plus";
import addNewLabel from "./label_add-or-update.vue";
import addNewBusiness from "./business-add-or-update.vue";
export default {
  components: { addNewLabel, addNewBusiness },
  data: function () {
    return {
      businessNameList: [],
      years: [
        { value: 0.1, label: "小于半年" },
        { value: 0.5, label: "半年" },
        { value: 1, label: "一年" },
        { value: 1.5, label: "一年半" },
        { value: 2, label: "两年" },
        { value: 2.5, label: "两年半" },
        { value: 3, label: "三年" },
        { value: 3.5, label: "三年半" },
        { value: 4, label: "四年" },
        { value: 4.5, label: "四年半" },
        { value: 5, label: "五年" },
        { value: 5.5, label: "五年半" },
        { value: 6, label: "六年" },
        { value: 6.5, label: "六年半" },
        { value: 7, label: "七年" },
        { value: 7.5, label: "七年半" },
        { value: 8, label: "八年" },
        { value: 8.5, label: "八年半" },
        { value: 9, label: "九年" },
        { value: 9.5, label: "九年半" },
        { value: 10, label: "十年" },
        { value: 11, label: "十一年" },
        { value: 12, label: "十二年" },
        { value: 13, label: "十三年" },
        { value: 14, label: "十四年" },
        { value: 15, label: "十五年" },
        { value: 16, label: "十六年" },
        { value: 17, label: "十七年" },
        { value: 18, label: "十八年" },
        { value: 19, label: "十九年" },
        { value: 20, label: "二十年" },
        { value: 21, label: "二十一年" },
        { value: 22, label: "二十二年" },
        { value: 23, label: "二十三年" },
        { value: 24, label: "二十四年" },
        { value: 25, label: "二十五年" },
        { value: 26, label: "二十六年" },
        { value: 27, label: "二十七年" },
        { value: 28, label: "二十八年" },
        { value: 29, label: "二十九年" },
        { value: 30, label: "三十年" },
        { value: 31, label: "三十一年" },
        { value: 32, label: "三十二年" },
        { value: 33, label: "三十三年" },
        { value: 34, label: "三十四年" },
        { value: 35, label: "三十五年" },
        { value: 36, label: "三十六年" },
        { value: 37, label: "三十七年" },
        { value: 38, label: "三十八年" },
        { value: 39, label: "三十九年" },
        { value: 40, label: "四十年" },
        { value: 41, label: "四十一年" },
        { value: 42, label: "四十二年" },
        { value: 43, label: "四十三年" },
        { value: 44, label: "四十四年" },
        { value: 45, label: "四十五年" },
        { value: 46, label: "四十六年" },
        { value: 47, label: "四十七年" },
        { value: 48, label: "四十八年" },
        { value: 49, label: "四十九年" },
        { value: 50, label: "五十年" },
      ],
      token: localStorage.getItem("token"),
      action: `${this.$baseUrl}/file/upload`,
      isJapanese: false,
      // 新标签信息
      newLabel: {
        id: "",
        label: "",
        year: "",
      },
      // 新选择label信息
      newLabelInfo: "",
      // 新行业信息
      newBusiness: {
        id: "",
        year: "",
      },
      // 表单数据，修改显示初始数据+点击确定往后端提交
      dataForm: {
        talentId: "",
        name: "",
        pseudonym: "",
        englishName: "",
        sex: "",
        birth: "",
        age: "",
        exprYear: "",
        nation: "",
        inJapanYear: "",
        japanLevel: "",
        station: "",
        school: "",
        major: "",
        picture: "",
        price: "",
        skillSheet: "",
        talentDescription: "",
        email: "",
        phone: "",
        wechat: "",
        line: "",
        belongCompany: "",
        appointStatus: "",
        status: "",
        remark: "",
        createTime: "",
        createBy: "",
        talentLabelList: [],
        engineerNameList: [],
        interviewInfoList: [],
        projectInfoList: [],
        delFlag: "",
        blacklistReason: "",
        talentBusinessList: [],
        companyName: "",
        predictMonth: "",
      },

      dataRule: {
        name: [
          {
            required: true,
            message: this.$t("Name required"),
          },
        ],
        pseudonym: [
          {
            required: true,
            message: this.$t("Pseudonym required"),
          },
        ],
        englishName: [
          {
            required: true,
            message: this.$t("English name required"),
          },
        ],
        sex: [
          {
            required: true,
            message: this.$t("Sex required"),
          },
        ],
        birth: [
          {
            required: true,
            message: this.$t("Birth required"),
          },
        ],
        exprYear: [
          {
            required: true,
            message: this.$t("Expr year required"),
          },
        ],
        nation: [
          {
            required: true,
            message: this.$t("Nation required"),
          },
        ],
        inJapanYear: [
          {
            required: true,
            message: this.$t("In Japan year required"),
          },
        ],
        japanLevel: [
          {
            required: true,
            message: this.$t("Japan level required"),
          },
        ],
        station: [
          {
            required: true,
            message: this.$t("Station required"),
          },
        ],
        school: [
          {
            required: true,
            message: this.$t("School required"),
          },
        ],
        major: [
          {
            required: true,
            message: this.$t("Major required"),
          },
        ],
        skillSheet: [
          {
            required: true,
            message: this.$t("Skill sheet required"),
          },
        ],
        belongCompany: [
          {
            required: true,
            message: this.$t("Belong company required"),
          },
        ],
        appointStatus: [
          {
            required: true,
            message: this.$t("Appoint status required"),
          },
        ],
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
        belongCompany: [],
      },
      // 表单显示隐藏
      visible: false,
      // 对应工程
      engineerNameList: [],
      // 所有标签类型和所有标签
      labelAndType: [],
      inJapanYearCopy: "",
      japanLevelCopy: "",
      filteredLabelList: [],
    };
  },
  computed: {
    displayInJapanYear: {
      get() {
        return this.dataForm.inJapanYear === 100
          ? this.$t("Japanese")
          : this.dataForm.inJapanYear;
      },
      set(value) {
        if (value === this.$t("Japanese")) {
          this.dataForm.inJapanYear = 100;
        } else {
          this.dataForm.inJapanYear = value;
        }
      },
    },
  },
  watch: {
    visible(newVal) {
      if (!newVal) {
        this.reset();
      }
    },
  },
  methods: {
    closeBusinessTagHandle(tag) {
      let i = this.dataForm.talentBusinessList.indexOf(tag);
      this.dataForm.talentBusinessList.splice(i, 1);
    },
    addBusinessHandle() {
      let that = this;

      // 确认行业和年份都是选择了的
      console.log(that.newBusiness.id);
      console.log(that.newBusiness.year);

      if (
        that.newBusiness.id == null ||
        that.newBusiness.id == "" ||
        that.newBusiness.year == null ||
        that.newBusiness.year == ""
      ) {
        ElMessage({
          message: that.$t("Can not be empty"),
          type: "warning",
          duration: 1200,
        });
        return;
      }

      // 确认该行业未被选择过
      let chosenBusiness = that.dataForm.talentBusinessList.find(
        (one) => one.businessId === that.newBusiness.id
      );
      if (chosenBusiness != null) {
        ElMessage({
          message: that.$t("You have already added this one"),
          type: "warning",
          duration: 1200,
        });
        return;
      }

      // 添加
      that.dataForm.talentBusinessList.push({
        businessId: that.newBusiness.id,
        businessName: that.businessNameList.find(
          (one) => one.businessId === that.newBusiness.id
        ).businessName,
        businessYear: that.newBusiness.year,
      });
    },
    addNewLabelHandle: function () {
      let that = this;
      that.$nextTick(() => {
        that.$refs.addNewLabel.init();
      });
    },
    addNewBusinessHandle: function () {
      let that = this;
      that.$nextTick(() => {
        that.$refs.addNewBusiness.init();
      });
    },
    filterHandle: function (query) {
      let that = this;
      if (query == "" || query == null) {
        that.filteredLabelList = that.labelAndType;
      } else {
        that.filteredLabelList = that.labelAndType.filter((label) => {
          return label.labelName.toLowerCase().includes(query.toLowerCase());
        });
        console.log(that.filteredLabelList);
      }
    },
    visibleChangeHandle: function (visible) {
      if (visible) {
        this.filteredLabelList = this.labelAndType;
      }
    },
    getFileName: function (url) {
      const match = url.match(/\/([^\/?#]+)$/);
      const fileName = match ? match[1] : null;
      return fileName;
    },
    // 点击已选择的技能标签进行删除
    closeTagHandle: function (tag) {
      let i = this.dataForm.talentLabelList.indexOf(tag);
      this.dataForm.talentLabelList.splice(i, 1);
    },
    updateFileSuccess: function (resp) {
      let that = this;
      if (resp && resp.file && resp.file.url) {
        that.dataForm.skillSheet = resp.file.url;
      }
    },

    updatePhotoSuccess: function (resp) {
      let that = this;
      that.dataForm.picture = resp.file.url;
    },
    // 初始化
    init: function (talentInfo) {
      let that = this;
      that.reset();
      that.loadDict();
      that.loadEngineerNameList();
      that.loadBusinessNameList();
      that.loadLabelAndType();
      that.$nextTick(() => {
        that.$refs["dataForm"].resetFields();
        // 如果是修改
        if (talentInfo != null && talentInfo != "") {
          // 深拷贝talentInfo对象到dataForm对象
          that.dataForm = {
            ...JSON.parse(JSON.stringify(talentInfo)),
          };
          that.dataForm.sex = that.dicts.sex.find(
            (one) => one.dictLabel == that.dataForm.sex
          ).dictValue;
          that.dataForm.nation = that.dicts.nation.find(
            (one) => one.dictLabel == that.dataForm.nation
          ).dictValue;
          that.dataForm.appointStatus = that.dicts.appointStatus.find(
            (one) => one.dictLabel == that.dataForm.appointStatus
          ).dictValue;
          that.dataForm.belongCompany = that.dicts.belongCompany.find(
            (one) => one.dictLabel == that.dataForm.belongCompany
          ).dictValue;
          that.inJapanYearCopy = that.dataForm.inJapanYear;
          that.japanLevelCopy = that.dataForm.japanLevel;
          that.nationChangeHandle();
        }
      });
      that.dataForm.japanLevel;
      that.visible = true;
    },
    // 点击添加技能标签和年份
    addLabelHandle: function () {
      let that = this;

      // 判断有没有选择技能
      if (this.newLabel.id == null || this.newLabel.id == "") {
        ElMessage({
          message: this.$t("Please select a skill"),
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
      let tmp = that.dataForm.talentLabelList.find(
        (labelYear) => labelYear.labelId === this.newLabel.id
      );

      // 已经选过了
      if (tmp != null) {
        ElMessage({
          message: this.$t("You have already added this skill"),
          type: "warning",
          duration: 1200,
        });
        return;
      }

      // 如果年份没有填，默认为无限制
      if (this.newLabel.year == null || this.newLabel.year == "") {
        this.newLabel.year = 0;
      }

      that.dataForm.talentLabelList.push({
        labelId: this.newLabel.id,
        labelYear: this.newLabel.year,
        labelName: this.newLabel.label,
      });
      this.newLabel.id = "";
      this.newLabel.year = "";
      this.newLabel.label = "";
    },
    // 加载所有标签和类型
    loadLabelAndType: function () {
      let that = this;
      that.$http("/label/allLabel", "GET", null, false, function (resp) {
        that.labelAndType = [];
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
    // 选中了一个对应工程，加入到dataForm的engineerNameList，有engineerId和engineerName
    selectOneEngineer: function (engineerId) {
      let that = this;
      // 判断选中的在dataForm的engineerNameList是否已经存在
      if (that.dataForm.engineerNameList == null) {
        that.dataForm.engineerNameList = [];
      }
      let tmp = that.dataForm.engineerNameList.find(
        (engineer) => engineer.engineerId === engineerId
      );
      if (tmp != null && tmp != "") {
        ElMessage({
          message: that.$t("You have already added this one"),
          type: "warning",
          duration: 1200,
        });
      } else {
        that.dataForm.engineerNameList.push({
          engineerId: engineerId,
          engineerName: that.engineerNameList.find(
            (one) => one.engineerId == engineerId
          ).engineerName,
        });
      }
    },
    // 选择国籍判断是否是日本人
    nationChangeHandle: function () {
      if (this.dataForm.nation == 0) {
        this.dataForm.inJapanYear = 100;
        this.dataForm.japanLevel = "0";
        this.isJapanese = true;
      } else {
        // this.dataForm.japanLevel = this.dicts.japaneseLevel.find((one) => one.dictLabel == this.dataForm.japanLevel).dictValue;
        this.dataForm.inJapanYear = "";
        this.dataForm.japanLevel = "";
        // 如果是修改
        if (this.dataForm.talentId != "" && this.dataForm.talentId != null) {
          this.dataForm.inJapanYear = this.inJapanYearCopy;
          this.dataForm.japanLevel = this.dicts.japaneseLevel.find(
            (one) => one.dictLabel == this.japanLevelCopy
          ).dictValue;
        }
        this.isJapanese = false;
      }
    },
    // 点击关掉标签和年份
    closeLabelTagHandle: function (tag) {
      let i = this.dataForm.talentLabelList.indexOf(tag);
      this.dataForm.talentLabelList.splice(i, 1);
    },
    // 点击关掉对应工程
    closeEngineerTagHandle: function (tag) {
      let i = this.dataForm.engineerNameList.indexOf(tag);
      this.dataForm.engineerNameList.splice(i, 1);
    },
    loadEngineerNameList: function () {
      let that = this;
      that.$http("/engineer", "GET", null, false, function (resp) {
        that.engineerNameList = resp.result;
      });
    },
    loadBusinessNameList: function () {
      let that = this;
      that.$http("/business", "GET", null, false, function (resp) {
        that.businessNameList = resp.result;
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
      that.dicts.belongCompany = that.$searchDict("dict_company");
    },
    reset: function () {
      let dataForm = {
        name: null,
        pseudonym: null,
        englishName: null,
        sex: null,
        birth: null,
        exprYear: null,
        nation: null,
        inJapanYear: null,
        japanLevel: null,
        station: null,
        school: null,
        major: null,
        picture: null,
        price: null,
        skillSheet: null,
        talentDescription: null,
        email: null,
        phone: null,
        wechat: null,
        line: null,
        belongCompany: null,
        appointStatus: null,
        engineerIds: null,
        talentLabelList: [],
        talentBusinessList: [],
        predictMonth: null,
        companyName: null,
      };
      this.dataForm = dataForm;
      let newLabel = {
        id: "",
        label: "",
        year: "",
      };
      this.newLabel = newLabel;
      this.newLabelInfo = "";
    },
    dataFormSubmit: function () {
      let that = this;
      if (
        that.dataForm.engineerNameList == null ||
        that.dataForm.engineerNameList == ""
      ) {
        that.dataForm.engineerNameList = [];
      }
      that.$refs["dataForm"].validate(function (valid) {
        if (valid) {
          let data = {
            name: that.dataForm.name,
            pseudonym: that.dataForm.pseudonym,
            englishName: that.dataForm.englishName,
            sex: that.dataForm.sex,
            birth: that.dataForm.birth,
            exprYear: that.dataForm.exprYear,
            nation: that.dataForm.nation,
            inJapanYear: that.dataForm.inJapanYear,
            japanLevel: that.dataForm.japanLevel,
            station: that.dataForm.station,
            school: that.dataForm.school,
            picture: that.dataForm.picture == null ? "" : that.dataForm.picture,
            price: that.dataForm.price == null ? "" : that.dataForm.price,
            skillSheet: that.dataForm.skillSheet,
            talentDescription:
              that.dataForm.talentDescription == null
                ? ""
                : that.dataForm.talentDescription,
            email: that.dataForm.email == null ? "" : that.dataForm.email,
            phone: that.dataForm.phone == null ? "" : that.dataForm.phone,
            wechat: that.dataForm.wechat == null ? "" : that.dataForm.wechat,
            line: that.dataForm.line == null ? "" : that.dataForm.line,
            belongCompany: that.dataForm.belongCompany,
            appointStatus: that.dataForm.appointStatus,
            status: that.dataForm.status,
            blacklistReason: that.dataForm.blacklistReason,
            engineerIds: [],
            major: that.dataForm.major,
            labelYears:
              that.dataForm.talentLabelList == null
                ? []
                : that.dataForm.talentLabelList,
            businessYears:
              that.dataForm.talentBusinessList == null
                ? []
                : that.dataForm.talentBusinessList,
            blacklistReason: "",
            companyName:
              that.dataForm.companyName == null
                ? ""
                : that.dataForm.companyName,
            predictMonth:
              that.dataForm.predictMonth == null
                ? null
                : that.dataForm.predictMonth + "-01",
          };
          that.dataForm.engineerNameList.forEach((one, index) => {
            data.engineerIds[index] = one.engineerId;
          });
          let method = "";
          if (that.dataForm.talentId == "" || that.dataForm.talentId == null) {
            method = "POST";
          } else {
            method = "PUT";
            data.talentId = that.dataForm.talentId;
          }
          that.$http("/talent", method, data, false, function (resp) {
            ElMessage({
              message: that.$t("Successful operation"),
              type: "success",
            });
            that.visible = false;
            that.$emit("refreshDataList");
          });
        }
      });
    },
  },
};
</script>

<style lang="less" scoped="scoped"></style>