package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;

@Data
public class RelatedTalentInterview {
    private int talentId;
    private int interId;

    public RelatedTalentInterview(int talentId, int interId) {
        this.talentId = talentId;
        this.interId = interId;
    }
}
