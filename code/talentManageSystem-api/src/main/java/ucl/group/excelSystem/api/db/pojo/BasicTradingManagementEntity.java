package ucl.group.excelSystem.api.db.pojo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BasicTradingManagementEntity {

    private Integer tradingId;
    private String applicableMonth;
    private Integer tradingStatus;
    private Integer salesCompanyId;
    private Integer departmentId;
    private Integer projectId;
    private Float totalOperatingTime;
    private Float totalOverTime;
    private BigDecimal totalSalesAmount;
    private BigDecimal totalAmountRaised;
    private BigDecimal estimatedSalesAmount;
    private BigDecimal totalSettlementAmount;
    private BigDecimal currentMonthBillingAmount;
    //private BigDecimal unsettledProcurementAmount;
    private BigDecimal theMonthOfResidualAmount;


    public BasicTradingManagementEntity(Integer tradingId,String applicableMonth , Integer tradingStatus, Integer salesCompanyId, Integer departmentId, Integer projectId, Float totalOperatingTime, Float totalOverTime, BigDecimal totalSalesAmount, BigDecimal totalAmountRaised, BigDecimal estimatedSalesAmount, BigDecimal totalSettlementAmount, BigDecimal currentMonthBillingAmount, BigDecimal theMonthOfResidualAmount) {
        this.tradingId = tradingId;
        this.applicableMonth = applicableMonth;
        this.tradingStatus = tradingStatus;
        this.salesCompanyId = salesCompanyId;
        this.departmentId = departmentId;
        this.projectId = projectId;
        this.totalOperatingTime = totalOperatingTime;
        this.totalOverTime = totalOverTime;
        this.totalSalesAmount = totalSalesAmount;
        this.totalAmountRaised = totalAmountRaised;
        this.estimatedSalesAmount = estimatedSalesAmount;
        this.totalSettlementAmount = totalSettlementAmount;
        this.currentMonthBillingAmount = currentMonthBillingAmount;
        this.theMonthOfResidualAmount = theMonthOfResidualAmount;
    }
}
