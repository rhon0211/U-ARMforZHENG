package ucl.group.excelSystem.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InsertOrUpdateDepartmentRequest {
    private Integer companyId;
    private Integer departmentId;
    private String departmentName;
    // 部署略称
    private String departmentAbbreviation;
    // 部门責任者
    @JsonProperty("principalJob")
    private String principalJob;
    // 部门責任者姓名
    private String principalName;
    // 開始日期
    private String departmentStartdate;
    // 結束日期
    private String departmentEnddate;
    // 摘要
    private String comment;
    // 1:アクティブ。Null：非アクティブ。
    private String activeFlg;
    private String closingDate;
}
