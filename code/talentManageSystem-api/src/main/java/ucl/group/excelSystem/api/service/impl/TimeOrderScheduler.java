package ucl.group.excelSystem.api.service.impl;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ucl.group.excelSystem.api.service.TimeOrderService;

import java.time.LocalDateTime;

@Setter
@Component
public class TimeOrderScheduler {

    @Autowired
    private TimeOrderService timeOrderService;

    // 每天14点整执行，2分钟执行一次
    @Scheduled(cron = "0 0/2 14 ? * *")
    public void runProjectStatusCheck() {
        System.out.println("[定时任务] 项目状态检查开始：" + LocalDateTime.now());
        timeOrderService.updateExpiredProjects();
        System.out.println("[定时任务] 项目状态检查完成：" + LocalDateTime.now());
    }

}

