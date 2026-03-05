package ucl.group.excelSystem.api.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Mapper
@SuppressWarnings("rawtypes")
public interface TradeDao {
    BigDecimal getTotalSalesAmountByProjectId(Integer projectId, LocalDate queryMonth);

    BigDecimal getPreviousMonthActuarialBalanceByProjectId(Integer projectId, LocalDate queryMonth);

    Integer getTradingStatus(Integer projectId, LocalDate queryMonth);

    List<Integer> getProjectIds(LocalDate date);

    ArrayList<HashMap> selectSaleCompanyByProjectId(List<Integer> projectIds);

    List<HashMap> getDepartmentsByProjectIds(List<Integer> projectIds);

    Boolean isExist(Integer projectId, LocalDate queryMonth);
}
