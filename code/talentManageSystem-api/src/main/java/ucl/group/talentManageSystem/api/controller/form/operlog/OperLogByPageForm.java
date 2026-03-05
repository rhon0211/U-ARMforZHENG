package ucl.group.talentManageSystem.api.controller.form.operlog;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OperLogByPageForm {
    @NotNull(message = "page不能为空")
    @Min(value = 1, message = "page不能小于1")
    private Integer page;

    @NotNull(message = "length不能为空")
    @Range(min = 10, max = 50, message = "length内容不正确")
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
    /** 操作模块 */
    private String title;

    /** 业务类型（0=其它 1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据） */
    private Integer businessType;


    /** 操作人员 */
    private String operName;
    /** 操作状态（0正常 1异常） */
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String startTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String endTime;
    /**
     * 分页查询的限制条件
     */
    private List<String> operNames;


}
