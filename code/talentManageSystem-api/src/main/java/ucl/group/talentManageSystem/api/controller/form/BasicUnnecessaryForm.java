package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class BasicUnnecessaryForm {
    /**
     * 状态，0启用 1停用
     */
    @Range(min = 0, max = 2, message = "status不正确")
    private String status;

    /**
     * 删除标志位，0存在 2删除
     */
    @Range(min = 0, max = 2, message = "delFlag不正确")
    private String delFlag;

}
