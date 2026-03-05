package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SearchLabelByIdForm {
    @NotNull(message = "labelId不能为空")
    @Min(value = 1, message = "labelId不能小于1")
    private Integer labelId;
}
