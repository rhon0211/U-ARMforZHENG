package ucl.group.excelSystem.api.controller.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InsertProjectForm {
    @NotNull(message = "顧客Idは空欄にできません")
    @JsonProperty("customerId")
    private Long customerId;

    @NotNull(message = "プロジェクト名は空欄にできません")
    @JsonProperty("projectName")
    private String projectName;

    @JsonProperty("principal")
    private String principal;
    @JsonProperty("principalCompany")
    private String principalCompany;
    @JsonProperty("category")
    private String category;
    @JsonProperty("uclPrincipal")
    private String uclPrincipal;
    @JsonProperty("principalId")
    private String principalId;
    @JsonProperty("remark")
    private String remark;

}
