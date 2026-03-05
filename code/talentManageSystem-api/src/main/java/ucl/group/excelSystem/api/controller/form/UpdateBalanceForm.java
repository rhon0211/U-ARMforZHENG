package ucl.group.excelSystem.api.controller.form;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateBalanceForm {
    private BigDecimal theMonthActuarialAmount;
    private BigDecimal currentMonthBillingAmount;
    private BigDecimal theMonthResidualAmount;
    private BigDecimal salesAmount;
    private BigDecimal totalAmountRaised;
    private Integer projectId;
    private String month;

}
