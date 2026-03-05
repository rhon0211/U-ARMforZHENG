package ucl.group.excelSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SelectStaffCompanyByPageForm {
    @NotNull(message = "ページはからにできません")
    private int page;
    @NotNull(message = "ながさはからにできません")
    private int length;
    private String staffNameKanji;
}
