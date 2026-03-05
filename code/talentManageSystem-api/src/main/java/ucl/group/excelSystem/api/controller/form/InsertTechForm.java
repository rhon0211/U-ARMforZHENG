package ucl.group.excelSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class InsertTechForm {

	@NotEmpty(message = "nameは空欄にできません")
	private String name;

	private String picture;

	private String katakana;

	private String roman;

	private String belongCompany;

	private String representative;

	private String belongCompanyId;

	private String remark;

	private LocalDate birthday;

}
