package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class LabelTypeSearchByPageForm extends BasicUnnecessaryForm {

    @NotNull(message = "page不能为空")
    private int page;

    @NotNull(message = "length不能为空")
    private int length;

    private String typeName;
}
