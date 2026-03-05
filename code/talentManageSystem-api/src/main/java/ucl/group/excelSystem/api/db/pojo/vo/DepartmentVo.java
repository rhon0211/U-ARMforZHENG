package ucl.group.excelSystem.api.db.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentVo {
    private Integer departmentId;
    private Integer activeFlg;
    private String departmentName;
    private String closingDate;
    private String departmentAbbreviation;
    private String principalName;

}
