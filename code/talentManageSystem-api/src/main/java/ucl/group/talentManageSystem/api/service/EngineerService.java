package ucl.group.talentManageSystem.api.service;

import ucl.group.talentManageSystem.api.db.pojo.BasicEngineerEntity;

import java.util.List;

public interface EngineerService {

    public List<BasicEngineerEntity> searchByTalentId(int talentId);
    public List<BasicEngineerEntity> searchAllEngineer();
    public List<String> searchNameByTalentId(int talentId);
}
