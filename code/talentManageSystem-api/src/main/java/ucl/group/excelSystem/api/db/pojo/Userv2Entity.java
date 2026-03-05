package ucl.group.excelSystem.api.db.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ucl.group.talentManageSystem.api.db.pojo.BasicEntity;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class Userv2Entity extends BasicEntity {
    private Long userId;
    private String name;
    private String katakana;
    private String email;
    private String type; // 用户类型 (4/5/6)
    private String code; // 员工编号
    private String phone;
    private String active; // 活性状态
    private String remark;
    private String account;
    private String password;
}
