package ucl.group.talentManageSystem.api.controller.form.talentForm;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import ucl.group.talentManageSystem.api.controller.form.BasicUnnecessaryForm;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class TalentInfoByPageForm extends BasicUnnecessaryForm {
    /**
     * \u4e00-\u9fa5 - 包括所有常用的中文汉字，部分也在日语中使用。
     * \u3040-\u309F - 包括所有平假名字符，这是日语特有的表达音节的字符集。
     * \u30A0-\u30FF - 包括所有片假名字符，也是日语中用来表达音节的字符集，常用于外来语和强调。
     */
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{1,50}$", message = "name内容不正确")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9\u4e00-\u9fa5\u3040-\u309F\u30A0-\u30FF]{1,50}$"
            , message = "pseudonym内容不正确")
    private String pseudonym;
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{1,50}$", message = "englishName内容不正确")
    private String englishName;

    @Range(min = 0, max = 100, message = "经验年数在0-100")
    private Integer exprYear;

    /**
     * 国籍
     */
    private String nation;
    /**
     * 日语等级
     */
    private String japanLevel;
    /**
     * 任用状态
     */
    private String appointStatus;
    /**
     * 对应工程编号
     */
    private List<Integer> engineerIds;
    /**
     * 年龄选择范围
     */
    @Range(min = 1, max = 100, message = "范围在1-100")
    private Integer ageStart;
    @Range(min = 1, max = 100, message = "范围在1-100")
    private Integer ageEnd;
    /**
     * 学科
     */
    private String major;
    /**
     * 标签年份
     */
    private List<LabelYearForm> labelYears;
    /**
     * 行业年份
     */
    private String business;
    private Float businessYear;



    @NotNull(message = "page不能为空")
    @Min(value = 1, message = "page不能小于1")
    private Integer page;

    @NotNull(message = "length不能为空")
    @Range(min = 10, max = 50, message = "length内容在10-50")
    private Integer length;
    /**
     * 排序的列
     */
    private String orderColumn;
    /**
     * 升序 asc
     * 降序 desc
     */
    private String orderSeq;
    /**
     * (page - 1) * length
     */
    private int start;
    @Range(min = 0, max = 1, message = "状态只有0和1")
    private String status;
    @Range(min = 0, max = 2, message = "删除状态只有0和2")
    private String delFlag;

    private int engineerSize;
    private String companyName;
    private String predictMonth;
    private List<Integer> talentIds;
    private String talentDescription;
}
