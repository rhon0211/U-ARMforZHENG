package ucl.group.talentManageSystem.api.db.dao;

import ucl.group.talentManageSystem.api.db.pojo.BusinessEntity;
import ucl.group.talentManageSystem.api.db.pojo.vo.BusinessYear;

import java.util.ArrayList;
import java.util.HashMap;

public interface BusinessDao {
    @SuppressWarnings({"rawtypes"})
    public ArrayList<HashMap> BusinessSelect();

    public void BusinessInsert(BusinessEntity entity);

    public void BusinessUpdate(BusinessEntity entity);

    public ArrayList<BusinessYear> searchBusinessByTalentId(int talentId);
}
