package ucl.group.talentManageSystem.api.controller.form.talentForm;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LabelYearForm {
    @NotNull(message = "标签id不能为空")
    private int labelId;
    @NotNull(message = "标签年份不能为空")
    private Float labelYear;
}
