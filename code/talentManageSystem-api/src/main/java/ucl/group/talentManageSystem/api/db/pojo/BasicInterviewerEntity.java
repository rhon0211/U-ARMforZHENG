package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BasicInterviewerEntity extends BasicEntity {
    private int interviewerId;
    private String name;
    private String pseudonym;
    private String type;
}
