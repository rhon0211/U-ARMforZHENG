package ucl.group.talentManageSystem.api.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.controller.form.operlog.OperLogByPageForm;
import ucl.group.talentManageSystem.api.db.dao.SysOperLogDao;
import ucl.group.talentManageSystem.api.db.pojo.SysOperLog;
import ucl.group.talentManageSystem.api.service.SysOperLogService;
import ucl.group.talentManageSystem.api.service.UserManageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 操作日志 服务层处理
 * 
 * @author hejiale
 */
@Service
@SuppressWarnings("rawtypes")
public class SysOperLogServiceImpl implements SysOperLogService {
    @Autowired
    private SysOperLogDao operLogDao;
    @Autowired
    private UserManageService userManageService;

    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     * @return 结果
     */
    @Override
    public int insertOperlog(SysOperLog operLog) {
        return operLogDao.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     * 
     * @param form 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public PageUtils selectOperLogList(OperLogByPageForm form) {
        int total = operLogDao.searchCount(form);
        form.setOperNames(convertToSysOperLogList(userManageService.userSelect()));
        List<SysOperLog> sysOperLogs = operLogDao.selectOperLogList(form);
        PageUtils pageUtils = new PageUtils(sysOperLogs, total, form.getPage(), form.getLength());
        return pageUtils;
    }

    @SuppressWarnings("unchecked")
    private List<String> convertToSysOperLogList(ArrayList<HashMap> hashMapList) {
        List<String> nameList = new ArrayList<>();

        for (HashMap<String, Object> map : hashMapList) {
            String user = null;

            // 假设 map 的 key 对应 SysOperLog 的字段名
            if (map.containsKey("name")) {
                user = (String) map.get("name");
            }
            nameList.add(user);
        }

        return nameList;
    }

    /**
     * 批量删除系统操作日志
     * 
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds) {
        return operLogDao.deleteOperLogByIds(operIds);
    }

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId) {
        return operLogDao.selectOperLogById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        operLogDao.cleanOperLog();
    }
}
