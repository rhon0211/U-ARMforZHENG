package ucl.group.excelSystem.api.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ucl.group.excelSystem.api.db.dto.CompanyDTO;
import ucl.group.excelSystem.api.db.dto.DepartmentDTO;
import ucl.group.excelSystem.api.db.dto.ProjectDTO;
import ucl.group.excelSystem.api.db.pojo.ProjectDetailEntity;
import ucl.group.excelSystem.api.db.pojo.ProjectEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper

public interface ProjectDetailDao {
        boolean existsByDepartmentIdAndProjectName(@Param("departmentId") int departmentId,
                                                   @Param("projectId") Integer  projectId,
                                                   @Param("projectName") String projectName);

        // 查询公司数据
        List<CompanyDTO> queryCompaniesByMonth(@Param("startDate") LocalDate startDate,
                                               //@Param("endDate") LocalDate endDate,
                                               @Param("start") int start,
                                               @Param("length") int length);

        // 查询部门数据

        List<DepartmentDTO> queryDepartmentsByMonth(@Param("startDate") LocalDate startDate);

        // 查询项目数据
        List<ProjectDTO> queryProjectsByMonth(@Param("startDate") LocalDate startDate);

        // 查询公司总数

        int queryCompanyCount(@Param("startDate") LocalDate startDate);

        // 插入项目基本信息
        void insertProject(ProjectEntity project);

        // 批量插入项目详细信息
        void insertProjectDetails(@Param("projectId") Integer projectId,
                                  @Param("details") List<Map<String, Object>> details);

        // 更新项目基本信息
        void updateProjectDetails(ProjectDetailEntity project);
        void updateProject(ProjectEntity project);
        void deleteProjectDetails(Integer projectId);

        // ========== 新增方法 ==========


        Map<String, Object> getInitializationData(@Param("salesCompanyId") Integer salesCompanyId,
                                                  @Param("departmentId") Integer departmentId);

        Map<String, Object> getExtendedInitializationData(@Param("startDate") String startDate,
                                                          @Param("salesCompanyId") Integer salesCompanyId,
                                                          @Param("departmentId") Integer departmentId,
                                                          @Param("projectId") Integer projectId);

        Map<String, Object> getAdditionalInitializationData();
}
