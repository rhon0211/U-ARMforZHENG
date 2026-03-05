package ucl.group.talentManageSystem.api.service.impl;

import org.springframework.stereotype.Service;
import ucl.group.talentManageSystem.api.db.dao.InterviewInfoDao;
import ucl.group.talentManageSystem.api.db.dao.InterviewerDao;
import ucl.group.talentManageSystem.api.db.pojo.BasicInterviewerEntity;
import ucl.group.talentManageSystem.api.service.StatisticsService;

import javax.annotation.Resource;
import java.util.*;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private InterviewerDao interviewerDao;
    @Resource
    private InterviewInfoDao interviewInfoDao;

    @Override
    public Map<String, Object> getStatistics(String type, String startTime, String endTime) {
        // 1.获取横轴数据（名称）  获取符合类型的面试官名称
        List<BasicInterviewerEntity> interviewers = interviewerDao.findInterviewersByType(type);
        List<String> names = new ArrayList<>();
        for (BasicInterviewerEntity interviewer : interviewers) {
            names.add(interviewer.getName()); // 获取面试官的名称
        }

        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", names);

        List<Map<String, Object>> series = Arrays.asList(
                new HashMap<String, Object>() {{
                    put("type", "面试回数");
                    put("data", calculateInterviewCounts(interviewers, type, startTime, endTime));
                }},
                new HashMap<String, Object>() {{
                    put("type", "采用回数");
                    put("data", calculateAdoptCounts(interviewers, type, startTime, endTime));
                }}
        );

        result.put("series", series);

        return result;
    }

    private List<Integer> calculateInterviewCounts(List<BasicInterviewerEntity> interviewers, String type, String startTime, String endTime) {
        List<Integer> counts = new ArrayList<>();
        for (BasicInterviewerEntity interviewer : interviewers) {
            int count = interviewInfoDao.countInterviewedNum(interviewer.getInterviewerId(), type, startTime, endTime);
            counts.add(count);
        }
        return counts;
    }

    private List<Integer> calculateAdoptCounts(List<BasicInterviewerEntity> interviewers, String type, String startTime, String endTime) {
        List<Integer> counts = new ArrayList<>();
        for (BasicInterviewerEntity interviewer : interviewers) {
            int count = interviewInfoDao.countHiredNum(interviewer.getInterviewerId(), type, startTime, endTime);
            counts.add(count);
        }
        return counts;
    }
}