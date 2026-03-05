package ucl.group.talentManageSystem.api.controller.form.talentForm;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BusinessYearForm {
    @NotNull(message = "行业id不能为空")
    private int businessId;
    @NotNull(message = "行业年份不能为空")
    private Float businessYear;
    @NotNull(message = "行业名称不能为空")
    private String businessName;
}
