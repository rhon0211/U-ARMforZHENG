package ucl.group.excelSystem.api.db.pojo.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BasicClearBalance1Entity {

    //贩卖金额
    BigDecimal salesAmount;
    //当月精算
    BigDecimal theMonthActuarialAmount;
    //前月残精算
    BigDecimal previousMonthActuarialBalance;
    //当月请求
    BigDecimal currentMonthBillingAmount;
    // 当月残
    BigDecimal theMonthResidualAmount;
    //调达金额合计
    BigDecimal totalAmountRaised;

    public BasicClearBalance1Entity() {
    }

    public BasicClearBalance1Entity( BigDecimal salesAmount, BigDecimal theMonthActuarialAmount, BigDecimal previousMonthActuarialBalance, BigDecimal currentMonthBillingAmount, BigDecimal theMonthResidualAmount, BigDecimal totalAmountRaised) {
        this.salesAmount = salesAmount;
        this.theMonthActuarialAmount = theMonthActuarialAmount;
        this.previousMonthActuarialBalance = previousMonthActuarialBalance;
        this.currentMonthBillingAmount = currentMonthBillingAmount;
        this.theMonthResidualAmount = theMonthResidualAmount;
        this.totalAmountRaised = totalAmountRaised;
    }
}
