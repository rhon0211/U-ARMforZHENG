package ucl.group.excelSystem.api.db.pojo;

import lombok.Data;

@Data
public class BasicDepartmentEntity {
    private Integer departmentId;
    private Integer companyId;
    private Integer activeFlg;
    private String departmentName;
    private String departmentAbbreviation;
    private String principalJob;
    private String principalName;
    private String closingDate;
    private String departmentStartdate;
    private String departmentEnddate;
    private String comment;

}
