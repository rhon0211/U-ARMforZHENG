package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;

@Data
public class RelatedTalentBusiness {
    private int talentId;
    private int businessId;
    private Float businessYear;

    public RelatedTalentBusiness(int talentId, int businessId, Float businessYear) {
        this.talentId = talentId;
        this.businessId = businessId;
        this.businessYear = businessYear;
    }
}
