package ucl.group.excelSystem.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InsertOrganizationRequest {

    @NotNull(message = "組織１名は空欄にできません")
    @JsonProperty("organizationName")
    private String organizationName;

    @NotNull(message = "belongは空欄にできません")
    private String belong;


    private Long preOrganizationId;
}
