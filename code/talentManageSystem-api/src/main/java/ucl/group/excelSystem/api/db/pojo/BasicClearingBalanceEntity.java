package ucl.group.excelSystem.api.db.pojo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BasicClearingBalanceEntity {

    private Integer projectId;

    private String targetMonth;

    private BigDecimal previousMonthActuarialBalance;

    private BigDecimal theMonthOfActuarialAmount;

    private BigDecimal theMonthOfResidualAmount;


    public BasicClearingBalanceEntity(Integer projectId, String targetMonth, BigDecimal previousMonthActuarialBalance, BigDecimal theMonthOfActuarialAmount, BigDecimal theMonthOfResidualAmount) {
        this.projectId = projectId;
        this.targetMonth = targetMonth;
        this.previousMonthActuarialBalance = previousMonthActuarialBalance;
        this.theMonthOfActuarialAmount = theMonthOfActuarialAmount;
        this.theMonthOfResidualAmount = theMonthOfResidualAmount;
    }
}
