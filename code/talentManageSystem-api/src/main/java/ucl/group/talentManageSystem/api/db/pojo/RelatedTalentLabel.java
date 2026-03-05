package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;

@Data
public class RelatedTalentLabel {
    private int talentId;
    private int labelId;
    private Float labelYear;

    public RelatedTalentLabel(int talentId, int labelId, Float labelYear) {
        this.talentId = talentId;
        this.labelId = labelId;
        this.labelYear = labelYear;
    }
}
