package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;

@Data
public class RelatedTalentEngineer {
    private int talentId;
    private int engineerId;

    public RelatedTalentEngineer(int talentId, int engineerId) {
        this.talentId = talentId;
        this.engineerId = engineerId;
    }
}
