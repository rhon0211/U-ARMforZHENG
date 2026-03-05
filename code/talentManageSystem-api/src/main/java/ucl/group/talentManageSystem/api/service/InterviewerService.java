package ucl.group.talentManageSystem.api.service;

import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.controller.form.talentForm.InterviewerByPageForm;
import ucl.group.talentManageSystem.api.db.pojo.BasicInterviewerEntity;

import java.util.List;

public interface InterviewerService {

    /**
     * 查询所有面试官
     * @param interviewerByPageForm
     * @return
     */
    public PageUtils searchByPage(InterviewerByPageForm interviewerByPageForm);
    public BasicInterviewerEntity searchById(int interviewerId);
    public List<BasicInterviewerEntity> searchByType(String type);
   public int add(BasicInterviewerEntity basicInterviewerEntity);
    public int edit(BasicInterviewerEntity basicInterviewerEntity);
    public int remove(int[] interviewerIds);

}
