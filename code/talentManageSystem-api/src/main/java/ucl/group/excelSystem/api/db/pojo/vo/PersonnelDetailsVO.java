package ucl.group.excelSystem.api.db.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PersonnelDetailsVO {
    private int staffId;
    private int projectId;
    private int companyId;
    private String companyName;
    private String staffName;
    private Date entryDate;
    private Date plannedExitDate;
    private Date actualExitDate;
    private BigDecimal salesAmount;
    private BigDecimal salesIncrementUnitPriceHour;
    private BigDecimal salesDecrementUnitPriceHour;
    private BigDecimal amountRaised;
    private BigDecimal procurementIncrementUnitPriceHour;
    private BigDecimal procurementDecrementUnitPriceHour;
}
