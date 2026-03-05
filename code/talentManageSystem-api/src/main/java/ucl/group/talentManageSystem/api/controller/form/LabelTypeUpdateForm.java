package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class LabelTypeUpdateForm extends BasicUnnecessaryForm {

    @NotNull(message = "typeId的值不能为空")
    @Min(value = 1, message = "typeId的值不能小于1")
    private int typeId;

    @NotBlank(message = "typeName的值不能为空")
    private String typeName;

    @NotBlank(message = "typeNameJap的值不能为空")
    private String typeNameJap;

    private String remark;
}
