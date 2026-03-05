package ucl.group.excelSystem.api.db.dao;

import ucl.group.excelSystem.api.db.pojo.TimeOrderEntity;

import java.util.List;

public interface TimeOrderDao {
    List<TimeOrderEntity> findProjectsForStatusUpdate();

    void deactivateProjectById(Integer projectId);         // 项目管理表
    void deactivateProjectDetailById(Integer projectId);    // 项目明细表
}
