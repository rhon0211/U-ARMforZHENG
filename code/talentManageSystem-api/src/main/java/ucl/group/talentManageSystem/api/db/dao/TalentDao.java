package ucl.group.talentManageSystem.api.db.dao;

import org.apache.ibatis.annotations.Param;
import ucl.group.talentManageSystem.api.controller.form.talentForm.LabelYearForm;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentInfoByPageForm;
import ucl.group.talentManageSystem.api.db.pojo.BasicTalentEntity;
import ucl.group.talentManageSystem.api.db.pojo.RelatedTalentBusiness;
import ucl.group.talentManageSystem.api.db.pojo.RelatedTalentLabel;
import ucl.group.talentManageSystem.api.db.pojo.vo.LabelYear;

import java.util.List;

public interface TalentDao {
    /**
     * 使用子查询，统计结果数
     * 
     * @param talentInfoByPageForm
     * @return
     */
    public int searchCount(TalentInfoByPageForm talentInfoByPageForm);

    /**
     * 分页查询人才数据，需要搭配其他查询使用
     * 
     * @param talentInfoByPageForm
     * @return
     */
    public List<BasicTalentEntity> searchByPage(TalentInfoByPageForm talentInfoByPageForm);

    /**
     * 增加人才信息（一二级信息+基本信息）
     */
    public int add(BasicTalentEntity basicTalentEntity);

    /**
     * 修改人才信息（一二级信息+基本信息）
     */
    public int edit(BasicTalentEntity basicTalentEntity);

    /**
     * 删除人才信息（一二级信息+基本信息）
     */
    public int remove(int[] talentIds);

    /**
     * 恢复删除人才信息（一二级信息+基本信息）
     */
    public int undeletion(int talentId);

    /**
     * 查询用户相关的标签
     */
    public List<LabelYear> searchLabelByTalentId(int talentId);

    /**
     * 删除用户相关标签再新增 此为删除
     */
    public int removeTalentLabelByTalentId(int talentId);

    /**
     * 删除人才的行业信息
     */
    public int removeTalentBusinessByTalentId(int talentId);

    /**
     * 插入标签年份
     */
    public int addTalentLabel(RelatedTalentLabel relatedTalentLabel);

    /**
     * 插入行业年份
     */
    public int addTalentBusiness(RelatedTalentBusiness relatedTalentBusiness);

    /**
     * 根据标签id和年份查询talent_id
     */
    public List<Integer> selectTalentIdByLabelYear(
            @Param("labelYears") List<LabelYearForm> labelYears);

}
