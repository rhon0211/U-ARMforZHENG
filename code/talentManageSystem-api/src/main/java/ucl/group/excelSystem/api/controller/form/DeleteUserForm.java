package ucl.group.excelSystem.api.controller.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Data
public class DeleteUserForm {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

}



