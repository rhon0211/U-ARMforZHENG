package ucl.group.excelSystem.api.service;

import org.springframework.stereotype.Service;
import ucl.group.excelSystem.api.controller.request.SaveProjectRequest;

import java.time.LocalDate;
import java.util.Map;

@Service
public interface ProjectDetailService {
    boolean isProjectNameExist(Integer departmentId, Integer projectId,String projectName);

    Map<String, Object> newinitAdditionalProjectPage();

    /**
     * 查询按月份的项目列表
     */
    //Map<String, Object> queryAllByMonth(LocalDate startDate, LocalDate endDate, int page, int length);
    Map<String, Object> queryAllByMonth(LocalDate startDate, int page, int length);
    /**
     * 查询项目总数
     */
    int queryProjectsCount(Map<String, Object> param);

    /**
     * 初始化项目页面
     * @param projectId 项目ID
     * @param salesCompanyId 销售公司ID
     * @param departmentId 部门ID
     * @return 包含项目基本信息、详细信息、劳动公司员工信息的初始化数据
     */
    Map<String, Object> initProjectPage(String startDate, Integer projectId, Integer salesCompanyId, Integer departmentId);

    /**
     * 新增初始化页面
     * @param salesCompanyId 销售公司ID
     * @param departmentId 部门ID
     * @return 包含销售公司、部门信息及劳动公司员工信息
     */
    Map<String, Object> newinitProjectPage(Integer salesCompanyId, Integer departmentId);

    /**
     * 添加新项目
     * @param projectRequest 项目保存请求
     */
    void addProject(SaveProjectRequest projectRequest);

    /**
     * 更新项目
     * @param projectId 项目ID
     * @param projectRequest 项目更新请求
     */
    void updateProjectDetails(Integer projectId, SaveProjectRequest projectRequest);
}
