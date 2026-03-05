package ucl.group.excelSystem.api.controller.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InsertUserForm {

    private String userName; // 用户名

    private String katakana;

    private String account; // 账户名

    private String password; // 密码

    @JsonProperty("role")
    private String type; // 角色

    @JsonProperty("active")
    private String status; // 活性状态

    @JsonProperty("email")
    private String email; // 邮箱

    @JsonProperty("phoneNumber")
    private String phoneNumber; // 电话号码

    @JsonProperty("employeeCode")
    private String employeeCode; // 员工编号

    @JsonProperty("remark")
    private String remark; // 备注

}
