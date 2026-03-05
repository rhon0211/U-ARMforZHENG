package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BusinessAddForm {
    @NotBlank(message = "businessName的值不能为空")
    private String businessName;

    private String remark;
}
