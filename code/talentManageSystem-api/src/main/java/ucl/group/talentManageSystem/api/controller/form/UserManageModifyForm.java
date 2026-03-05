package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserManageModifyForm extends BasicUnnecessaryForm {

    private int userId;

    @NotBlank(message = "name不能为空")
    private String name;

    private String pseudonym;

    private String account;

    private String password;

    @NotBlank(message = "email不能为空")
    private String email;

    private String phone;

    private String code;

    @NotBlank(message = "type不能为空")
    private String type;

}
