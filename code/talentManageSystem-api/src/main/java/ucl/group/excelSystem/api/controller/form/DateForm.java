package ucl.group.excelSystem.api.controller.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Setter
@Getter
public class DateForm {
    @JsonFormat(pattern = "yyyy-MM")
    @NotEmpty(message = "dateStartは空欄にできません")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "dateStart内容が正しくありません")
    private String dateStart;

    @JsonFormat(pattern = "yyyy-MM")
    @NotEmpty(message = "dateEndは空欄にできません")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "dateEnd内容が正しくありません")
    private String dateEnd;

}
