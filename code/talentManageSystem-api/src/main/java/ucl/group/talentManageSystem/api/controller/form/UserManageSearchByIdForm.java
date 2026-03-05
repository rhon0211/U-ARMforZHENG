package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserManageSearchByIdForm {
    @NotNull(message = "userId不能为空")
    private int userId;
}
