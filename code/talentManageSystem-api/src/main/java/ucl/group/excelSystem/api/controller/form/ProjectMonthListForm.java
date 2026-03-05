package ucl.group.excelSystem.api.controller.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * projectName: code
 * package: ucl.group.excelSystem.api.controller.form
 * className: ProjectMonthListForm
 * author: he_jiale
 * description: TODO
 * date: 2024/07/11 18:19
 * version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMonthListForm {
    @JsonFormat(pattern = "yyyy-MM")
    @NotEmpty(message = "dateMonthは空欄にできません")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "dateMonth内容が正しくありません")
    private String dateMonth;
    private Long customerId;

}
