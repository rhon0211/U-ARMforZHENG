package ucl.group.talentManageSystem.api.controller.form.talentForm;

import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class TalentPorjectAddForm {
    @Pattern(regexp = "^[a-zA-Z0-9\u4e00-\u9fa5\u3040-\u309F\u30A0-\u30FF]{1,50}$"
            , message = "name内容不正确")
    private String name;

    private Date appointStartTime;

    private Date appointEndTime;
    private String appointEvalution;
    private int talentId;
    private int projectId;
    private String  remark;


}
