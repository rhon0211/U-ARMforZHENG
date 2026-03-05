package ucl.group.talentManageSystem.api.service;

import java.util.Map;

public interface StatisticsService {
    Map<String, Object> getStatistics(String type, String startTime, String endTime);
}
