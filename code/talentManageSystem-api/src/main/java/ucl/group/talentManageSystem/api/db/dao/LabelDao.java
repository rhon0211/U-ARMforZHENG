package ucl.group.talentManageSystem.api.db.dao;

import ucl.group.talentManageSystem.api.db.pojo.LabelEntity;
import ucl.group.talentManageSystem.api.db.pojo.RelatedLabelTypeEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface LabelDao {
    public ArrayList<HashMap> LabelSearchByPage(Map param);
    public long LabelSearchCount(Map param);
    public void LabelInsert(LabelEntity entity);
    public void RelatedLabelInsert(RelatedLabelTypeEntity entity);
    public void LabelUpdate(LabelEntity entity);
    public void RelatedLabelUpdate(RelatedLabelTypeEntity entity);
    public void LabelDeletes(Integer[] ids);
    public ArrayList<HashMap> labelSelect();
    public HashMap searchById(Integer labelId);
}
