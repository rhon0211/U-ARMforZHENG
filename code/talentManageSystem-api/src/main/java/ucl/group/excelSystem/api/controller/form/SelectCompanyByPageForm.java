package ucl.group.excelSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data

public class SelectCompanyByPageForm {
    @NotNull(message = "pageは空欄にできません")
    private int page;
    @NotNull(message = "lengthは空欄にできません")
    private int length;
    private String partnerCompanyName;
}
