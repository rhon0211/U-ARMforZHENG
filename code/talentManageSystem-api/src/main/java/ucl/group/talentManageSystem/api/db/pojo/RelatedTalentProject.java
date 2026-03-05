package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;

@Data
public class RelatedTalentProject {
    private int talentId;
    private int projectId;

    public RelatedTalentProject(int talentId, int projectId) {
        this.talentId = talentId;
        this.projectId = projectId;
    }
}
