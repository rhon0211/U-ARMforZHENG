package ucl.group.excelSystem.api.controller.form;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class SelectProjectByMonthForm {
        @NotNull(message = "pageは空欄にできません")
        private Integer page;
        @NotNull(message = "lengthは空欄にできません")
        private Integer length;
        private Integer year;   // 查询年份，可为空
        private Integer month;  // 查询月份，可为空


}
