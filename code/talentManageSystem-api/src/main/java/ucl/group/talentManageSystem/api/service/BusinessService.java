package ucl.group.talentManageSystem.api.service;

import ucl.group.talentManageSystem.api.db.pojo.BusinessEntity;

import java.util.ArrayList;
import java.util.HashMap;

public interface BusinessService {

    public ArrayList<HashMap> BusinessSelect();
    public void BusinessInsert (BusinessEntity entity);
    public void BusinessUpdate (BusinessEntity entity);
}
