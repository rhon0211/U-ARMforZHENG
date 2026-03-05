package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;

@Data
public class EmailRegisterEntity {
	// 查询用户
	String searchname;
	String username;
	String usertype;
	// 当前用户
	String currentname;
}
