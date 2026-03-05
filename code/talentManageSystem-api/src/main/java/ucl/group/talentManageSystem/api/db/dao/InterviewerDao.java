package ucl.group.talentManageSystem.api.db.dao;

import org.apache.ibatis.annotations.Param;
import ucl.group.talentManageSystem.api.controller.form.talentForm.InterviewerByPageForm;
import ucl.group.talentManageSystem.api.db.pojo.BasicInterviewerEntity;

import java.util.List;

public interface InterviewerDao {
    public int searchCount(InterviewerByPageForm interviewerByPageForm);
    public List<BasicInterviewerEntity> searchByPage(InterviewerByPageForm interviewerByPageForm);

    public BasicInterviewerEntity searchById(int interviewerId);
    public List<BasicInterviewerEntity> searchByType(String type);

    public int add(BasicInterviewerEntity basicInterviewerEntity);

    public int edit(BasicInterviewerEntity basicInterviewerEntity);
    public int remove(int[] interviewerIds);

    public List<BasicInterviewerEntity>findInterviewersByType(@Param("type") String type);
    public List<BasicInterviewerEntity> findInterviewerIdsByTypeAndDate(
            @Param("type") String type,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );

}
