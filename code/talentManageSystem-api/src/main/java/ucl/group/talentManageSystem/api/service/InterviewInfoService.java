package ucl.group.talentManageSystem.api.service;

import java.util.List;

import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentInterviewInfoAddForm;
import ucl.group.talentManageSystem.api.db.pojo.BasicInterviewInfoEntity;
import ucl.group.talentManageSystem.api.db.pojo.RelatedTalentInterview;

public interface InterviewInfoService {

    public List<BasicInterviewInfoEntity> searchByTalentId(Integer talentId);

    public int add(TalentInterviewInfoAddForm talentInterviewInfoAddForm);

    public int edit(TalentInterviewInfoAddForm form);

    public int remove(int[] interIds);

    public int addRelated(RelatedTalentInterview relatedTalentInterview);

    public int countInterviewedNumByDate(String startDate, String endDate);

    // 统计时间内的采用回数
    public int countHiredNumByDate(String startDate, String endDate);
}
