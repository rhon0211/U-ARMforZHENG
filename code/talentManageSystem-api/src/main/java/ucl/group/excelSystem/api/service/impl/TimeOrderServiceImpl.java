package ucl.group.excelSystem.api.service.impl;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucl.group.excelSystem.api.db.dao.TimeOrderDao;
import ucl.group.excelSystem.api.db.pojo.TimeOrderEntity;
import ucl.group.excelSystem.api.service.TimeOrderService;

import java.time.LocalDate;
import java.util.List;

@Setter
@Service
public class TimeOrderServiceImpl implements TimeOrderService {

    @Autowired
    private TimeOrderDao timeOrderDao;

    @Override
    @Transactional
    public void updateExpiredProjects() {
        List<TimeOrderEntity> projects = timeOrderDao.findProjectsForStatusUpdate();

        LocalDate now = LocalDate.now();
        for (TimeOrderEntity project : projects) {
            LocalDate endDate = project.getProjectEndDate();
            LocalDate scheduledDate = project.getProjectScheduledEndDate();

            // 决定哪个时间生效
            LocalDate validEndDate = (endDate != null) ? endDate : scheduledDate;
            if (validEndDate != null && validEndDate.isBefore(now.minusDays(1))) {
                // 已超过一天以上
                timeOrderDao.deactivateProjectById(project.getProjectId());
                timeOrderDao.deactivateProjectDetailById(project.getProjectId());
            }
        }
    }

}
