package ucl.group.talentManageSystem.api.db.dao;

import ucl.group.talentManageSystem.api.db.pojo.BasicEngineerEntity;
import ucl.group.talentManageSystem.api.db.pojo.RelatedTalentEngineer;

import java.util.List;

public interface EngineerDao {
    /**
     * 根据人才id联合查询
     * @param talentId
     * @return
     */
    public List<BasicEngineerEntity> searchByTalentId(int talentId);

    /**
     * 查询所有工程
     */
    public List<BasicEngineerEntity> searchAllEngineer();
    /**
     * 插入工程表
     */
    public int add(RelatedTalentEngineer relatedTalentEngineer);
    /**
     * 删除工程表
     */
    public int removeRelated(int talentId);

}
