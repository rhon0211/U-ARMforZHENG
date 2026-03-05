package ucl.group.talentManageSystem.api.service;


import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.controller.form.operlog.OperLogByPageForm;
import ucl.group.talentManageSystem.api.db.pojo.SysOperLog;

/**
 * 操作日志 服务层
 * 
 * @author hejiale
 */
public interface SysOperLogService {
    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     * @return 结果
     */
    public int insertOperlog(SysOperLog operLog);

    /**
     * 查询系统操作日志集合
     * 
     * @param form 操作日志对象
     * @return 操作日志集合
     */
    public PageUtils selectOperLogList(OperLogByPageForm form);

    /**
     * 批量删除系统操作日志
     * 
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    public int deleteOperLogByIds(Long[] operIds);

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    public SysOperLog selectOperLogById(Long operId);

    /**
     * 清空操作日志
     */
    public void cleanOperLog();
}
