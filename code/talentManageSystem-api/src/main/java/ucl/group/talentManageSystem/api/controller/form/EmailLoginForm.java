package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EmailLoginForm {
    @NotBlank(message="邮箱不能为空")
    private String email;

    @NotBlank(message="验证码不能为空")
    private String code;

    @NotNull(message="语言设置不能为空")
    private String lang;

    @NotNull(message = "系统选择不能为空")
    private int systemCode;

    @NotBlank(message = "密码不能为空")
    private String password;
}
