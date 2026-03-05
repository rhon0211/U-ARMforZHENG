package ucl.group.talentManageSystem.api.service;

import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.db.pojo.LabelTypeEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface LabelTypeService {

    public ArrayList<HashMap> LabelTypeSelect();

    public void LabelTypeInsert(LabelTypeEntity entity);

    public void LabelTypeUpdate(LabelTypeEntity entity);

    public String LabelTypeDeletes(Integer[] ids);

    public PageUtils LabelTypeSearchByPage(Map param);

    public HashMap searchById(Integer typeId);
}
