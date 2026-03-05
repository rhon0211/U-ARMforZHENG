package ucl.group.excelSystem.api.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateOrganizationRequest {
    @NotNull(message="organizationIdは空欄にできません")
    private Long organizationId;

    private String organizationName;

    private String belong;

    private Long preOrganizationId;

    private String relatedId;
}
