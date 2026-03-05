package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserManageRegisterForm extends BasicUnnecessaryForm {

    @NotBlank(message = "姓名不能为空")
    private String name;

    private String pseudonym;

    @NotBlank(message = "邮件不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;

    private String code;

    @NotBlank(message = "type不能为空")
    private String type;

    private String account;

    private String password;

    private int systemCode;

}
