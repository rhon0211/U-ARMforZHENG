package ucl.group.talentManageSystem.api.service;

import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.db.pojo.LabelEntity;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface LabelService {
    public PageUtils LabelSearchByPage(Map param);

    public void LabelInsert(LabelEntity entity);

    public void LabelUpdate(LabelEntity entity);

    public void LabelDeletes(Integer[] ids);

    public HashMap labelSelect();

    public HashMap searchById(Integer labelId);
}
