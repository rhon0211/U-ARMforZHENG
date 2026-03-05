package ucl.group.excelSystem.api.service;
import ucl.group.excelSystem.api.db.pojo.BasicProjectEntity;
import ucl.group.talentManageSystem.api.common.PageUtils;

import java.util.Map;
@SuppressWarnings({ "rawtypes",})
public interface ProjectV2Service {

     void insertProject(BasicProjectEntity basicProjectEntity);

     void updateProject(BasicProjectEntity basicProjectEntity);

     void deleteProject(Long[] projectIds);

     PageUtils selectProjectByPage(Map param);
}
