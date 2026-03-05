package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class LabelUpdateForm extends BasicUnnecessaryForm {

    @NotNull(message = "labelId的值不能为空")
    @Min(value = 1, message = "labelId的值不能小于1")
    private int labelId;

    @Min(value = 1, message = "typeId的值不能小于1")
    private int typeId;

    @NotBlank(message = "labelName不能为空")
    private String labelName;

    @NotBlank(message = "labelNameJap不能为空")
    private String labelNameJap;

    private String remark;
}
