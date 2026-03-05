package ucl.group.excelSystem.api.db.pojo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BasicReceivableAndPayable {
    private Integer accountsReceivableAndPayableId;
    private Integer buyAndSellType;
    private Integer companyId;
    private String targetYearAndMonth;
    private String closingDate;
    private String expectedDateOfPayment;
    private String depositDate;
    private BigDecimal lineTotal;
    private BigDecimal amountCharged;
    private BigDecimal depositAmount;
    private BigDecimal commission;

}
