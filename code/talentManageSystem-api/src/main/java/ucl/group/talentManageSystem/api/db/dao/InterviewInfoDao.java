package ucl.group.talentManageSystem.api.db.dao;


import org.apache.ibatis.annotations.Param;
import ucl.group.talentManageSystem.api.db.pojo.BasicInterviewInfoEntity;
import ucl.group.talentManageSystem.api.db.pojo.RelatedTalentInterview;

import java.util.List;

public interface InterviewInfoDao {
    /**
     * 根据人才id联合查询
     *  SELECT i.bus_date,i.bus_interviewer,i.bus_evaluation,i.tech_date,i.tech_interviewer, i.tech_evaluation,
     *     i.status, i.created_by, i.created_time, i.remark
     *     FROM basic_interview_info i
     * @param talentId
     * @return
     */
    public List<BasicInterviewInfoEntity> searchByTalentId(@Param("talentId") Integer talentId);
//统计面试回数
    public int countInterviewedNum(@Param("interviewerId") int interviewerId,@Param("type")String type,@Param("startDate") String startDate, @Param("endDate") String endDate);
//统计采用回数
    public int countHiredNum(@Param("interviewerId") int interviewerId,@Param("type")String type,@Param("startDate") String startDate,@Param("endDate") String endDate);
//统计时间内的面试回数
    public int countInterviewedNumByDate(@Param("startDate") String startDate,@Param("endDate") String endDate);
    //统计时间内的采用回数
    public int countHiredNumByDate(@Param("startDate") String startDate,@Param("endDate") String endDate);
    /**
     * 插入面试信息
     * status: 1采用  2未采用 默认2
     */
    public int add(BasicInterviewInfoEntity basicInterviewInfoEntity);
    /**
     * 修改面试信息
     * status: 1采用  2未采用 3待定 默认3
     */
    public int edit(BasicInterviewInfoEntity basicInterviewInfoEntity);
    public int remove(int[] interIds);
    /**
     * 增加关联
     * @param relatedTalentInterview
     * @return
     */
    public int addRelated(RelatedTalentInterview relatedTalentInterview);
    /**
     * 删除关联
     */
    public int removeRelated(int interId);
}
