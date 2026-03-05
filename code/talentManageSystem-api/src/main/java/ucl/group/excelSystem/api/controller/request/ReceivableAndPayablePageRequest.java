package ucl.group.excelSystem.api.controller.request;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReceivableAndPayablePageRequest {
    @NotNull(message = "pageは空欄にできません")
    private int page;
    @NotNull(message = "lengthは空欄にできません")
    private int length;

    //出入金日预定日
    private String[] expectedDateOfPayment;
    //締日
    private String[] closingDate;
}
