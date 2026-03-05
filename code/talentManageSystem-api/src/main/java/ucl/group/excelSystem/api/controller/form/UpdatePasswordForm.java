package ucl.group.excelSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdatePasswordForm {
    @NotNull(message = "ユーザIDは空欄にできません")
    private Long userId; // 用户ID
    private String oldPassword;
    private String password; // 新密码
}
