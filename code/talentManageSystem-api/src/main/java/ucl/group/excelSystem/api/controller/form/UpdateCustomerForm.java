package ucl.group.excelSystem.api.controller.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class UpdateCustomerForm {
    @NotNull(message = "ユーザIDは空欄にできません")
    private Long customerId;
    @NotNull(message = "ユーザ名は空欄にできません")
    @JsonProperty("customerName")
    private String customerName;
    @JsonProperty("remark")
    private String remark;

}
