package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Data
public class BasicNecessaryForm {

    /**
     * 状态，0停用 1启用
     */
    @NotBlank(message = "状态值不能为空")
    @Range(min = 0, max = 1, message = "status不能为空")
    private String status;

    /**
     * 删除标志位，0存在 2删除
     */
    @NotBlank(message = "删除标志位不能为空")
    @Range(min = 0, max = 2, message = "delFlag不能为空")
    private String delFlag;

}
