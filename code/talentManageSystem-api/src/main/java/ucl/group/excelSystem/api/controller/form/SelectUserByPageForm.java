package ucl.group.excelSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
public class SelectUserByPageForm {
    private String type;
    private String name;

    /**
     * 当前是第几页，必须 >= 1
     */
    @NotNull(message = "該当ページは空欄にできません")
    @Min(value = 1, message = "該当ページは1以上でなければなりません")
    private Integer page;

    /**
     * 每页条数，必须 >= 1
     */
    @NotNull(message = "各ページの項目数は空にできません")
    @Min(value = 1, message = "各ページの項目数は1以上でなければなりません")
    private Integer length;
}
