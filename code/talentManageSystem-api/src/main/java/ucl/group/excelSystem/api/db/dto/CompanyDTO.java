package ucl.group.excelSystem.api.db.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompanyDTO {
    private Integer salesCompanyId;
    private Integer projectCount;
    private String companyAbbreviation;
    private List<DepartmentDTO> departmentList;
}
