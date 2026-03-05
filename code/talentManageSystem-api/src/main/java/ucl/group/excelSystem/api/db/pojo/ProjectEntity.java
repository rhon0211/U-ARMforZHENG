package ucl.group.excelSystem.api.db.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProjectEntity {
    private Integer projectId;
    private Integer salesCompanyId;
    private Integer departmentId;
    private LocalDate projectStartDate;
    private LocalDate projectScheduledEndDate;
    private LocalDate projectEndDate;
    private String projectName;
    private String projectNameAbbreviation;
    private String siteAddress;
    private String startTime;
    private String endTime;
    private String comment;
    private BigDecimal dailyOperatingHours;
    private BigDecimal settlementUpperLimit;
    private BigDecimal settlementLowerLimit;

}
