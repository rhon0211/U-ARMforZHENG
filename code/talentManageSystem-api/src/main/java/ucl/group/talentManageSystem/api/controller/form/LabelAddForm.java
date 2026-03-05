package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class LabelAddForm extends BasicUnnecessaryForm {

    /**
     * 类型id
     */
    @NotNull(message = "typeId不能为空")
    private long typeId;

    /**
     * 标签中文名称
     */
    @NotBlank(message = "labelName不能为空")
    private String labelName;

    /**
     * 标签日语名称
     */
    @NotBlank(message = "labelNameJap不能为空")
    private String labelNameJap;

    /**
     * 备注
     */
    private String remark;
}
