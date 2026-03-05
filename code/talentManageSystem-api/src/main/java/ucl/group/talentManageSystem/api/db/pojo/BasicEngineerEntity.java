package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BasicEngineerEntity extends BasicEntity {
    private int engineerId;
    private String engineerName;
    private String engineerNameJap;
}
