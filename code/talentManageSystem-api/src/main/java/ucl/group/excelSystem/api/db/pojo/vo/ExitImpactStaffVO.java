package ucl.group.excelSystem.api.db.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExitImpactStaffVO {
    private Integer projectId;
    private Integer staffId;
    private LocalDate actualExitDate;
    private LocalDate plannedEndDate;
    private BigDecimal salesAmount;
    private BigDecimal amountRaised;

}
