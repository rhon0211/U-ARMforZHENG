package ucl.group.excelSystem.api.db.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("rawtypes")
public interface ProjectManagementDao {
    List<HashMap> selectProjectsByDepartmentId(Integer departmentId);

    Integer getStaffNumByProjectId(Integer projectId, LocalDate queryMonth, LocalDate lastDayOfMonth);

    List<Integer> getStaffIdsByProjectId(Integer projectId);

    List<HashMap> getProjects(List<Integer> projectIds);

    List<Integer> getProjectIds(LocalDate date, LocalDate lastDayOfMonth);

    Integer getSalesCompanyId(Integer projectId);

    Integer getDepartmentId(Integer projectId);

    BigDecimal getSumSalesAmount(Integer projectId);

    Double getDailyOperatingHoursByProjectId(Integer projectId);

    String getWorkDaysByMonth(String month);

    BigDecimal getSalesAmountByProjectIdAndStaffId(Integer projectId, Integer staffId);

    BigDecimal getAmountRaisedByProjectIdAndStaffId(Integer projectId, Integer staffId);

    BigDecimal getTotalSalesAmountByProjectId(Integer projectId);

    BigDecimal getTheMonthOfResidualAmount(Integer projectId, String preMonth);

    HashMap<String, BigDecimal> getUpperAndLowerLimit(Integer projectId);

    String getClosingDate(Integer salesCompanyId);

    HashMap<String, BigDecimal> getSalesIncrementAndDecrementUnitPriceHour(Integer projectId,
            Integer staffId);

    HashMap<String, String> getAmountRaisedUpperAndLowerLimit(Integer projectId, Integer staffId);

    HashMap<String, BigDecimal> getProcurementIncrementAndDecrementUnitPriceHour(Integer projectId,
            Integer staffId);

    BigDecimal getPreviousMonthActuarialBalance(Integer projectId, LocalDate queryMonth);
}
