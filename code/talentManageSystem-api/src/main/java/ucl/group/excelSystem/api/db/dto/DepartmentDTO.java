package ucl.group.excelSystem.api.db.dto;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentDTO {
    private Integer departmentId;
    private Integer departmentProjectCount;
    private Integer companyId;
    private String departmentAbbreviation;
    private String principalName;
    private List<ProjectDTO> projectList;

}
