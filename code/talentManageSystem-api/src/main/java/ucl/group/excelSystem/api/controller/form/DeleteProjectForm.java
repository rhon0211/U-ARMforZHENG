package ucl.group.excelSystem.api.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteProjectForm {

    @NotEmpty(message = "プロジェクトIdは空欄にできません")
    private Long[] projectIds;
}
