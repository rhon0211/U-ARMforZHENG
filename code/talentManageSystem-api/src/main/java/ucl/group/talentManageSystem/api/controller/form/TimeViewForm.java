package ucl.group.talentManageSystem.api.controller.form;

import lombok.Data;

@Data
public class TimeViewForm {
    // 2024-02
    private String startTime;

    private String endTime;
    // 面试官类别
    private String type;
}
