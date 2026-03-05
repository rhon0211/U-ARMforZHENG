package ucl.group.talentManageSystem.api.db.dao;

import ucl.group.talentManageSystem.api.db.pojo.LabelTypeEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes"})
public interface LabelTypeDao {

    public ArrayList<HashMap> LabelTypeSelect();

    public void LabelTypeInsert(LabelTypeEntity entity);

    public void LabelTypeUpdate(LabelTypeEntity entity);

    public Integer LabelTypeCheckDelete(Integer[] ids);

    public void LabelTypeDeletes(Integer[] ids);

    public long LabelTypeSearchCount(Map param);

    public ArrayList<HashMap> LabelTypeSearchByPage(Map param);

    public HashMap searchById(Integer typeId);

}
