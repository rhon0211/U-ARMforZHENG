package ucl.group.talentManageSystem.api.db.dao;

import ucl.group.talentManageSystem.api.db.pojo.BasicProjectInfoEntity;
import ucl.group.talentManageSystem.api.db.pojo.RelatedTalentProject;

import java.util.List;

public interface ProjectInfoDao {
    /**
     * 根据人才id联合查询
     * 
     * @param talentId
     * @return
     */
    public List<BasicProjectInfoEntity> searchByTalentId(int talentId);

    /**
     * 增加项目信息
     */
    public int add(BasicProjectInfoEntity basicProjectInfoEntity);

    /**
     * 修改项目信息
     */
    public int edit(BasicProjectInfoEntity basicProjectInfoEntity);

    public int remove(int[] projectIds);

    /**
     * 增加关联
     */
    public int addRelated(RelatedTalentProject relatedTalentProject);

    /**
     * 删除关联
     */
    public int removeRelated(int proejctId);

}
