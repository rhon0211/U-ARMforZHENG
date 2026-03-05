package ucl.group.excelSystem.api.db.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProjectDetailEntity {
    private Integer staffId;
    private Integer projectId;
    private Integer companyId;
    private String staffName;
    private String entryDate;
    private String plannedExitDate;
    private String actualExitDate;
    private BigDecimal salesAmount;
    private BigDecimal salesIncrementUnitPriceHour;
    private BigDecimal salesDecrementUnitPriceHour;
    private BigDecimal amountRaised;
    private BigDecimal procurementIncrementUnitPriceHour;
    private BigDecimal procurementDecrementUnitPriceHour;
    private String upperLimit;
    private String lowerLimit;
}
