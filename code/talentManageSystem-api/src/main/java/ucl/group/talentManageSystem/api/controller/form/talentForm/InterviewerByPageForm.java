package ucl.group.talentManageSystem.api.controller.form.talentForm;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class InterviewerByPageForm {
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
     * 升序 asc 降序 desc
     */
    private String orderSeq;
    /**
     * (page - 1) * length
     */
    private int start;
    private String status;
    private String delFlag;

    private int interviewerId;
    @Pattern(regexp = "^[a-zA-Z0-9\u4e00-\u9fa5\u3040-\u309F\u30A0-\u30FF]{1,50}$",
            message = "name内容不正确")
    private String name;
    private String type;
    /**
     * 面试信息表中面试开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String startDate;
    /**
     * 面试信息表中面试结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String endDate;
}
