package ucl.group.excelSystem.api.service.impl;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ucl.group.excelSystem.api.db.dao.ProjectManagementDao;
import ucl.group.excelSystem.api.service.ProjectService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Setter
@Service
@Slf4j
@SuppressWarnings({"rawtypes",})
public class ProjectV3ServiceImpl implements ProjectService {

    @Resource
    private ProjectManagementDao projectManagementDao;

    @Override
    public List<HashMap> selectProjectsByDepartmentId(Integer departmentId) {
        return projectManagementDao.selectProjectsByDepartmentId(departmentId);
    }

    @Override
    public Integer getStaffNumByProjectId(Integer projectId, LocalDate queryMonth, LocalDate lastDayOfMonth) {
        return projectManagementDao.getStaffNumByProjectId(projectId, queryMonth, lastDayOfMonth);
    }

    @Override
    public List<Integer> getStaffIdsByProjectId(Integer projectId) {
        return projectManagementDao.getStaffIdsByProjectId(projectId);
    }

    @Override
    public List<HashMap> getProjects(List<Integer> projectIds) {
        return projectManagementDao.getProjects(projectIds);
    }

    @Override
    public List<Integer> getProjectIds(LocalDate date, LocalDate lastDayOfMonth) {
        return projectManagementDao.getProjectIds(date, lastDayOfMonth);
    }

    @Override
    public Integer getSalesCompanyId(Integer projectId) {
        return projectManagementDao.getSalesCompanyId(projectId);
    }

    @Override
    public Integer getDepartmentId(Integer projectId) {
        return projectManagementDao.getDepartmentId(projectId);
    }

    @Override
    public BigDecimal getSumSalesAmount(Integer projectId) {
        return projectManagementDao.getSumSalesAmount(projectId);
    }

    @Override
    public Double getDailyOperatingHoursByProjectId(Integer projectId) {
        return projectManagementDao.getDailyOperatingHoursByProjectId(projectId);
    }

    @Override
    public String getWorkDaysByMonth(String month) {
        return projectManagementDao.getWorkDaysByMonth(month);
    }

    @Override
    public BigDecimal getSalesAmountByProjectIdAndStaffId(Integer projectId, Integer staffId) {
        return projectManagementDao.getSalesAmountByProjectIdAndStaffId(projectId, staffId);
    }

    @Override
    public BigDecimal getAmountRaisedByProjectIdAndStaffId(Integer projectId, Integer staffId) {
        return projectManagementDao.getAmountRaisedByProjectIdAndStaffId(projectId, staffId);
    }

    @Override
    public BigDecimal getTotalSalesAmountByProjectId(Integer projectId) {
        return projectManagementDao.getTotalSalesAmountByProjectId(projectId);
    }

    @Override
    public BigDecimal getTheMonthOfResidualAmount(Integer projectId, String preMonth) {
        return projectManagementDao.getTheMonthOfResidualAmount(projectId, preMonth);
    }

    @Override
    public HashMap<String, BigDecimal> getUpperAndLowerLimit(Integer projectId) {
        return projectManagementDao.getUpperAndLowerLimit(projectId);
    }

    @Override
    public String getClosingDate(Integer salesCompanyId) {
        return projectManagementDao.getClosingDate(salesCompanyId);
    }

    @Override
    public HashMap<String, BigDecimal> getSalesIncrementAndDecrementUnitPriceHour(Integer projectId, Integer staffId) {
        return projectManagementDao.getSalesIncrementAndDecrementUnitPriceHour(projectId, staffId);
    }

    @Override
    public HashMap<String, String> getAmountRaisedUpperAndLowerLimit(Integer projectId, Integer staffId) {
        return projectManagementDao.getAmountRaisedUpperAndLowerLimit(projectId, staffId);
    }

    @Override
    public HashMap<String, BigDecimal> getProcurementIncrementAndDecrementUnitPriceHour(Integer projectId, Integer staffId) {
        return projectManagementDao.getProcurementIncrementAndDecrementUnitPriceHour(projectId, staffId);
    }

    @Override
    public BigDecimal getPreviousMonthActuarialBalance(Integer projectId, LocalDate queryMonth) {
        return projectManagementDao.getPreviousMonthActuarialBalance(projectId, queryMonth);
    }

}
