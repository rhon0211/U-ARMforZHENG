package ucl.group.excelSystem.api.db.pojo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BasicTransactionDetailEntity {
    private String upperLimit;
    private String lowerLimit;
    private String staffNameKanji;
    private String procurementCompanyName; // 新增字段，用于存储公司名
    private Integer id;
    private Integer tradingId;
    private Integer procurementCompanyId;
    private Integer staffId;
    private Double operatingTime;
    private Double overTime;
    private Double totalOperatingTime;
    private Double totalOverTime;
    private BigDecimal salesAmount;
    private BigDecimal amountRaised;
    private BigDecimal totalSalesAmount;
    private BigDecimal totalAmountRaised;
    private BigDecimal settlementUpperLimit;
    private BigDecimal settlementLowerLimit;
    private BigDecimal salesIncrementUnitPriceHour;
    private BigDecimal salesDecrementUnitPriceHour;
    private BigDecimal procurementIncrementUnitPriceHour;
    private BigDecimal procurementDecrementUnitPriceHour;
    private BigDecimal baseSalesAmount;
    private BigDecimal baseAmountRaised;

    public BasicTransactionDetailEntity() {
    }

    public BasicTransactionDetailEntity(String upperLimit, String lowerLimit, String staffNameKanji, String procurementCompanyName,Integer id, Integer tradingId, Integer procurementCompanyId, Integer staffId,  Double operatingTime, Double overTime, Double totalOperatingTime, Double totalOverTime,BigDecimal salesAmount, BigDecimal amountRaised, BigDecimal totalSalesAmount, BigDecimal totalAmountRaised, BigDecimal settlementUpperLimit, BigDecimal settlementLowerLimit, BigDecimal salesIncrementUnitPriceHour, BigDecimal salesDecrementUnitPriceHour, BigDecimal procurementIncrementUnitPriceHour, BigDecimal procurementDecrementUnitPriceHour, BigDecimal baseSalesAmount, BigDecimal baseAmountRaised) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.staffNameKanji = staffNameKanji;
        this.procurementCompanyName = procurementCompanyName;
        this.id = id;
        this.tradingId = tradingId;
        this.procurementCompanyId = procurementCompanyId;
        this.staffId = staffId;
        this.operatingTime = operatingTime;
        this.overTime = overTime;
        this.totalOperatingTime = totalOperatingTime;
        this.totalOverTime = totalOverTime;
        this.salesAmount = salesAmount;
        this.amountRaised = amountRaised;
        this.totalSalesAmount = totalSalesAmount;
        this.totalAmountRaised = totalAmountRaised;
        this.settlementUpperLimit = settlementUpperLimit;
        this.settlementLowerLimit = settlementLowerLimit;
        this.salesIncrementUnitPriceHour = salesIncrementUnitPriceHour;
        this.salesDecrementUnitPriceHour = salesDecrementUnitPriceHour;
        this.procurementIncrementUnitPriceHour = procurementIncrementUnitPriceHour;
        this.procurementDecrementUnitPriceHour = procurementDecrementUnitPriceHour;
        this.baseSalesAmount = baseSalesAmount;
        this.baseAmountRaised = baseAmountRaised;
    }
}
