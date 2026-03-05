package ucl.group.excelSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteCustomerForm {

    @NotEmpty(message = "ユーザのcustomerIdsは空欄にできません")
    private Long[] customerIds;
}
