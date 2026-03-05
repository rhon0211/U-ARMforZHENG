package ucl.group.talentManageSystem.api.common.service;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ucl.group.talentManageSystem.api.db.pojo.SysOperLog;
import ucl.group.talentManageSystem.api.service.SysOperLogService;

/** 异步调用日志服务
 * @author hejiale
 */
@Setter
@Service
public class AsyncLogService
{
    @Autowired
    private SysOperLogService operLogService;

    /** 保存系统日志记录*/
    @Async
    public void saveSysLog(SysOperLog sysOperLog) {
        operLogService.insertOperlog(sysOperLog);
    }

}
