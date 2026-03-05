package ucl.group.talentManageSystem.api.service;

import ucl.group.talentManageSystem.api.db.pojo.DictDataEntity;

import java.util.List;

public interface DictService {

    public List<DictDataEntity> searchByType(String type);
    public void loadingDictCache();
    public void clearDictCache();
    public void resetDictCache();
    /**
     * 获取名称
     */
    public String  getDictName(String  type,String lang,String value);
}
