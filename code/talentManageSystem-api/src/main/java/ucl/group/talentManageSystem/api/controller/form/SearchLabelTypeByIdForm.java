package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SearchLabelTypeByIdForm {
    @NotNull(message = "typeId不能为空")
    @Min(value = 1, message = "typeId不能小于1")
    private Integer typeId;
}
