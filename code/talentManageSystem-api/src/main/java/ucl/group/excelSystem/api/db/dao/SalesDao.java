package ucl.group.excelSystem.api.db.dao;

import org.apache.ibatis.annotations.Param;
import ucl.group.excelSystem.api.db.pojo.vo.SalesVO;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface SalesDao {

    //  从tradingManagement表获取销售数据主表
    List<SalesVO> getData(@Param("list") List<Integer> projectIds,
                          @Param("startDate") LocalDate startDate,
                          @Param("endDate") LocalDate endDate);

    //  获取项目参与员工ID
    List<Integer> getStaffIds(@Param("projectId") Integer projectId);

    //  计算人工成本合计
    BigDecimal getTotalLaborExpenses(@Param("staffIds") List<Integer> staffIds);

    //  获取项目销售金额
    BigDecimal getSalesAmount(@Param("projectId") Integer projectId);

    //  获取项目调达金额
    BigDecimal getAmountRaised(@Param("projectId") Integer projectId);

    //  获取所有项目基础数据（用于Java过滤）
    List<HashMap<String, Object>> getProjects();

    //  获取指定月前已退场员工
    List<String> getRetiredStaff(@Param("projectId") Integer projectId,
                                 @Param("monthStart") LocalDate monthStart);

    //  获取某项目下所有员工的销售/调达金额映射
    List<HashMap<String, Object>> getStaffFinancials(@Param("projectId") Integer projectId,
                                                     @Param("monthStart") LocalDate monthStart);
    Date getProjectEndDate(@Param("projectId") Integer projectId);

    Date getProjectScheduledEndDate(@Param("projectId") Integer projectId);


    //  获取项目ID（项目结束日在范围内）
    List<Integer> getProjectIdByEndDate(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    //  获取项目ID（预定结束日在范围内）
    List<Integer> getProjectIdByScheduledEndDate(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);
}
