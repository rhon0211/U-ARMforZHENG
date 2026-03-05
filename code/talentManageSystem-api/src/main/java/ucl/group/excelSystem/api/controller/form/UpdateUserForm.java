package ucl.group.excelSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateUserForm {

    @NotNull(message = "ユーザIDは空欄にできません")
    private Long userId; // 用户ID

    @Size(max = 50, message = "ユーザー名の長さは50文字を超えてはいけません")
    private String userName; // 用户名

    @Size(max = 50, message = "カタカナの長さは50文字を超えてはいけません")
    private String katakana; // 片假名

    @Size(max = 30, message = "アカウントの長さは30文字を超えてはいけません")
    private String account; // 账户名

    @Email(message = "メールアドレスの形式が正しくありません")
    @Size(max = 100, message = "メールアドレスの長さは100文字を超えてはいけません")
    private String email; // 邮箱

    @Pattern(regexp = "^[0-9]{10,15}$", message = "電話番号は10～15桁の数字でなければなりません")
    private String phoneNumber; // 电话号码

    @Size(max = 20, message = "社員番号の長さは20文字を超えてはいけません")
    private String employeeCode; // 员工编号

    @Size(max = 30, message = "役割の長さは30文字を超えてはいけません")
    private String role; // 角色

    private String active; // 活性状态

    @Size(max = 200, message = "備考の長さは200文字を超えてはいけません")
    private String remark; // 备注
}
