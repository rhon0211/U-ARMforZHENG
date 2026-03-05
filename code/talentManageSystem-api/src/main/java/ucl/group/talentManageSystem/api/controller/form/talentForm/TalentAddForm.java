package ucl.group.talentManageSystem.api.controller.form.talentForm;

import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class TalentAddForm {
    private int talentId;
    @Pattern(regexp = "^[a-zA-Z0-9\u4e00-\u9fa5\u3040-\u309F\u30A0-\u30FF\\s]{1,50}$", message = "名称内容不正确")
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9\u4e00-\u9fa5\u3040-\u309F\u30A0-\u30FF\\s]{1,50}$", message = "日语假名内容不正确")
    private String pseudonym;
    @Pattern(regexp = "^[a-zA-Z0-9\u4e00-\u9fa5\\s]{1,50}$", message = "英文名称内容不正确")

    private String englishName;
    private String sex;
    private String birth;

    private int exprYear;
    private String nation;

    private int inJapanYear;
    private String japanLevel;
    private String station;
    private String school;
    private String major;
    private String picture;
    private String skillSheet;
    private String talentDescription;
    private String belongCompany;
    private String appointStatus;
    private String blacklistReason;
    private String blacklistTime;
    private String blacklistBy;
    private String deletedTime;
    private String deletedBy;
    private String status;
    private String remark;
    /**
     * 一级保密信息
     */
    private String email;
    private String phone;
    private String wechat;
    private String line;
    /**
     * 二级保密信息
     */
    private int price;
    /**
     * 工程id列表
     */
    private List<Integer> engineerIds;
    /**
     * 标签年份列表
     */
    private List<LabelYearForm> labelYears;
    /**
     * 行业年份列表
     */
    private List<BusinessYearForm>  businessYears;

    /**
     * 新增：找工作月份、公司名
     */
    private String predictMonth;
    private String companyName;
}
