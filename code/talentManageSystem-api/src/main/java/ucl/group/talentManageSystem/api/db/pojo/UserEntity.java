package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode; // 新增这一行导入

@Data
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BasicEntity {

    private int userId;

    private String name;

    private String pseudonym;

    private String account;

    private String password;

    private String email;

    private String phone;

    private String code;

    private String type;

}
