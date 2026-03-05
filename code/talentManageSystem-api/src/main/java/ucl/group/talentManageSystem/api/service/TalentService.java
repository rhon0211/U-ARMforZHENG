package ucl.group.talentManageSystem.api.service;

import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentAddForm;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentInfoByPageForm;
import ucl.group.talentManageSystem.api.db.pojo.vo.InterAndProInfoVO;
import ucl.group.talentManageSystem.api.db.pojo.vo.LabelYear;

import java.util.List;

public interface TalentService {

    public PageUtils searchByPage(TalentInfoByPageForm form);

    public PageUtils searchByPage2(TalentInfoByPageForm form);

    public PageUtils searchByPage3(TalentInfoByPageForm form);

    /**
     * 新增人才一二级信息+基本信息
     */
    public int add(TalentAddForm talentAddForm);

    /**
     * 修改人才一二级信息+基本信息
     */
    public int edit(TalentAddForm talentAddForm);

    /**
     * 拉黑
     */
    public int editBlacklist(TalentAddForm talentAddForm);

    /**
     * 删除
     */
    public int remove(int[] talentIds);

    /**
     * 恢复删除
     */
    public int undeletion(int talentId);

    public InterAndProInfoVO searchInfoByTalentId(int talentId);



    /**
     * 先写在这里 查询人才拥有的所有标签
     */
    public List<LabelYear> searchLabelByTalentId(int talentId);
}
