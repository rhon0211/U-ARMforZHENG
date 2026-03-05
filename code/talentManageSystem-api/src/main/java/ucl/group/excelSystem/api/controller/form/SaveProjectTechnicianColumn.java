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
 * className: SaveProjectTechnicianColumn
 * author: he_jiale
 * description: TODO
 * date: 2024/07/11 17:28
 * version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveProjectTechnicianColumn {
    private int totalNumber;
    private int monthDays;

    @JsonFormat(pattern = "yyyy-MM")
    @NotEmpty(message = "yearMonthは空欄にできません")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "yearMonth内容が正しくありません")
    private String yearMonth;
}
