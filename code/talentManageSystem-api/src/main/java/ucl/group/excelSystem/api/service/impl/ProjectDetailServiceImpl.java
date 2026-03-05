package ucl.group.excelSystem.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucl.group.excelSystem.api.controller.request.SaveProjectRequest;
import ucl.group.excelSystem.api.db.dao.ProjectDetailDao;
import ucl.group.excelSystem.api.db.dto.CompanyDTO;
import ucl.group.excelSystem.api.db.dto.DepartmentDTO;
import ucl.group.excelSystem.api.db.dto.ProjectDTO;
import ucl.group.excelSystem.api.db.pojo.ProjectEntity;
import ucl.group.excelSystem.api.service.ProjectDetailService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Service
public class ProjectDetailServiceImpl implements ProjectDetailService {
    @Autowired
    private ProjectDetailDao projectDetailDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean isProjectNameExist(Integer departmentId, Integer projectId, String projectName) {
        return projectDetailDao.existsByDepartmentIdAndProjectName(departmentId, projectId, projectName);
    }

    @Override
    public Map<String, Object> queryAllByMonth(LocalDate startDate, int page, int length) {
        int start = (page - 1) * length;

        List<CompanyDTO> companies = projectDetailDao.queryCompaniesByMonth(startDate, start, length);
        int companyCount = projectDetailDao.queryCompanyCount(startDate);
        List<DepartmentDTO> departments = projectDetailDao.queryDepartmentsByMonth(startDate);
        List<ProjectDTO> projects = projectDetailDao.queryProjectsByMonth(startDate);

        Map<Integer, List<ProjectDTO>> projectsByDepartment = projects.stream()
                .collect(Collectors.groupingBy(ProjectDTO::getDepartmentId));

        Map<Integer, List<DepartmentDTO>> departmentsByCompany = departments.stream()
                .peek(department -> department.setProjectList(
                        projectsByDepartment.getOrDefault(department.getDepartmentId(), new ArrayList<>())
                ))
                .collect(Collectors.groupingBy(DepartmentDTO::getCompanyId));

        companies.forEach(company -> company.setDepartmentList(
                departmentsByCompany.getOrDefault(company.getSalesCompanyId(), new ArrayList<>())
        ));

        Map<String, Object> result = new HashMap<>();
        result.put("companies", companies);
        result.put("companyCount", companyCount);
        return result;
    }

    @Override
    public int queryProjectsCount(Map<String, Object> param) {
        return 0;
    }

    @Override
    public Map<String, Object> newinitProjectPage(Integer salesCompanyId, Integer departmentId) {
        Map<String, Object> result = projectDetailDao.getInitializationData(salesCompanyId, departmentId);

        if (result.containsKey("laborCompaniesWithStaff")) {
            String laborCompaniesJson = (String) result.get("laborCompaniesWithStaff");
            try {
                List<Map<String, Object>> laborCompaniesWithStaff = objectMapper.readValue(
                        laborCompaniesJson,
                        new TypeReference<>() {}
                );
                result.put("laborCompaniesWithStaff", laborCompaniesWithStaff);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse laborCompaniesWithStaff JSON.", e);
            }
        }

        return result;
    }

    @Override
    public Map<String, Object> initProjectPage(String startDate, Integer projectId, Integer salesCompanyId, Integer departmentId) {
        String effectiveStartDate = startDate;
        if (effectiveStartDate == null || effectiveStartDate.trim().isEmpty()) {
            effectiveStartDate = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        Map<String, Object> rawResult = projectDetailDao.getExtendedInitializationData(
                effectiveStartDate, salesCompanyId, departmentId, projectId
        );

        Map<String, Object> result = new HashMap<>();

        if (rawResult.containsKey("projectInfo")) {
            String projectInfoJson = (String) rawResult.get("projectInfo");
            try {
                Map<String, Object> projectInfo = objectMapper.readValue(projectInfoJson, new TypeReference<>() {});
                result.put("projectInfo", projectInfo);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse projectInfo JSON.", e);
            }
        }

        if (rawResult.containsKey("projectDetails")) {
            String projectDetailsJson = (String) rawResult.get("projectDetails");
            try {
                List<Map<String, Object>> projectDetails = objectMapper.readValue(projectDetailsJson, new TypeReference<>() {});
                result.put("projectDetails", projectDetails);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse projectDetails JSON.", e);
            }
        }

        if (rawResult.containsKey("laborCompaniesWithStaff")) {
            String laborCompaniesJson = (String) rawResult.get("laborCompaniesWithStaff");
            try {
                List<Map<String, Object>> laborCompaniesWithStaff = objectMapper.readValue(
                        laborCompaniesJson,
                        new TypeReference<>() {}
                );
                result.put("laborCompaniesWithStaff", laborCompaniesWithStaff);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse laborCompaniesWithStaff JSON.", e);
            }
        }

        if (rawResult.containsKey("salesCompanyAbbreviation")) {
            result.put("salesCompanyAbbreviation", rawResult.get("salesCompanyAbbreviation"));
        }
        if (rawResult.containsKey("departmentAbbreviation")) {
            result.put("departmentAbbreviation", rawResult.get("departmentAbbreviation"));
        }
        if (rawResult.containsKey("principalName")) {
            result.put("principalName", rawResult.get("principalName"));
        }

        return result;
    }

    @Transactional
    @Override
    public void addProject(SaveProjectRequest projectRequest) {
        ProjectEntity projectInfo = projectRequest.getProjectInfo();
        List<Map<String, Object>> projectDetails = projectRequest.getProjectDetails();

        if (projectInfo == null) {
            throw new IllegalArgumentException("'projectInfo' must not be null");
        }
        if (projectDetails == null || projectDetails.isEmpty()) {
            return;
        }

        projectDetailDao.insertProject(projectInfo);
        Integer projectId = projectInfo.getProjectId();
        if (projectId == null) {
            throw new IllegalStateException("Failed to generate projectId.");
        }

        projectDetailDao.insertProjectDetails(projectId, projectDetails);
    }

    @Override
    @Transactional
    public void updateProjectDetails(Integer projectId, SaveProjectRequest projectRequest) {
        ProjectEntity projectInfo = projectRequest.getProjectInfo();
        List<Map<String, Object>> projectDetails = projectRequest.getProjectDetails();

        if (projectInfo == null) {
            throw new IllegalArgumentException("'projectInfo' must not be null.");
        }
        if (projectDetails == null || projectDetails.isEmpty()) {
            return;
        }

        projectDetailDao.updateProject(projectInfo);
        projectDetailDao.deleteProjectDetails(projectId);
        projectDetailDao.insertProjectDetails(projectId, projectDetails);
    }

    @Override
    public Map<String, Object> newinitAdditionalProjectPage() {
        Map<String, Object> result = projectDetailDao.getAdditionalInitializationData();
        if (result.containsKey("salesCompaniesWithDepartments")) {
            String salesCompaniesJson = (String) result.get("salesCompaniesWithDepartments");
            try {
                List<Map<String, Object>> salesCompaniesWithDepartments = objectMapper.readValue(
                        salesCompaniesJson,
                        new TypeReference<>() {}
                );
                result.put("salesCompaniesWithDepartments", salesCompaniesWithDepartments);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse salesCompaniesWithDepartments JSON.", e);
            }
        }
        if (result.containsKey("laborCompaniesWithStaff")) {
            String laborCompaniesJson = (String) result.get("laborCompaniesWithStaff");
            try {
                List<Map<String, Object>> laborCompaniesWithStaff = objectMapper.readValue(
                        laborCompaniesJson,
                        new TypeReference<>() {}
                );
                result.put("laborCompaniesWithStaff", laborCompaniesWithStaff);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse laborCompaniesWithStaff JSON.", e);
            }
        }
        return result;
    }
}