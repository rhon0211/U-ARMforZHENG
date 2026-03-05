package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserManageDisableForm {

	@NotEmpty(message = "ids不能为空")
	private Integer[] ids;

}
