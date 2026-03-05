package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;

@Data
public class StatisticsRequest {
    private String type;
    private String startTime;
    private String endTime;
}
