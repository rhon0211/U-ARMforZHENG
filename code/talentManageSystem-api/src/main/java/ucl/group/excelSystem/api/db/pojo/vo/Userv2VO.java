package ucl.group.excelSystem.api.db.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data

public class Userv2VO {
    @JsonProperty("userId")
    private Long userId;
    private String name;
    private String katakana;
    private String email;
    private String type;
    private String code;
    private String phone;
    private String active;
    private String remark;
    private String account;
    private String role;

    // 手动添加带参构造函数
    public Userv2VO(Long userId, String userName, String katakana, String email, String type,
                  String code, String phone, String active, String remark,
                  String account) {
        this.userId = userId;
        this.name = userName;
        this.katakana = katakana;
        this.email = email;
        this.type = type;
        this.code = code;
        this.phone = phone;
        this.active = active;
        this.remark = remark;
        this.account = account;
    }

    // 无参构造函数
    public Userv2VO() {}
}
