package ucl.group.excelSystem.api.db.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectDTO {
    private Integer projectId;
    private Integer departmentId;
    private Integer staffCount;
    private LocalDate projectStartDate;
    private LocalDate projectScheduledEndDate;
    private LocalDate projectEndDate;
    private String projectName;
    private String projectNameAbbreviation;
}
