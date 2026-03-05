package ucl.group.talentManageSystem.api.controller.form.talentForm;

import lombok.Data;

@Data
public class EngineerForm {

    private int engineerId;
    /**
     * 中文名或日文名
     */
    private String engineerName;

    public EngineerForm(int engineerId, String engineerName) {
        this.engineerId = engineerId;
        this.engineerName = engineerName;
    }
}
