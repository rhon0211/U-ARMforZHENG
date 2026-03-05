package ucl.group.excelSystem.api.service;

import ucl.group.excelSystem.api.db.pojo.vo.LaborManageVO;
import ucl.group.excelSystem.api.db.pojo.vo.PageResultVO;

import java.util.List;

public interface LaborManageService {
    // 分页查询
    PageResultVO<LaborManageVO> getLaborManageByPage(String time, Integer page, Integer pageSize);

    // 更新劳务数据（检查状态）
    void updateLaborManage(List<LaborManageVO> laborManageVOList);

    // 更新劳务状态（确认/解除确认）
    void updateLaborStatusBatch(List<LaborManageVO> laborManageVOList);

    List<Integer> getLaborStatusByStaffIds(List<Integer> staffIds, String time);
}
