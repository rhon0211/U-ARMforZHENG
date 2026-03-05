package ucl.group.excelSystem.api.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ucl.group.excelSystem.api.db.pojo.BasicClearingBalanceEntity;
import ucl.group.excelSystem.api.db.pojo.BasicReceivableAndPayable;
import ucl.group.excelSystem.api.db.pojo.BasicTradingManagementEntity;
import ucl.group.excelSystem.api.db.pojo.BasicTransactionDetailEntity;
import ucl.group.excelSystem.api.db.pojo.vo.BasicClearBalance1Entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface TransactionDetailDao {
    //要员表 按月份和案件ID查询
    List<BasicTransactionDetailEntity> getDetailsByMonthAndProject(
            @Param("month") String month,
            @Param("projectId") Integer projectId
    );

    List<BasicTransactionDetailEntity> getDetailsByMonthAndProjectWithTotals(
            @Param("month") String month,
            @Param("projectId") Integer projectId
    );

    int updateTradingManagementTotals(
            Double totalOperatingTime,
            Double totalOverTime,
            BigDecimal totalSalesAmount,
            BigDecimal totalAmountRaised,
            BigDecimal theMonthOfActuarialAmount,
            BigDecimal theMonthOfResidualAmount,
            Integer tradingId
    );

    //要员update
    void updateTransactionDetail(BasicTransactionDetailEntity transactionDetail);

    //清算查找
    BasicClearBalance1Entity getClearBalanceData(@Param("month") String month, @Param("projectId") Integer projectId);

    // 更新 basic_v2_clearing_balance 的 theMonthActuarialAmount
    void updateTheMonthActuarialAmount(@Param("theMonthActuarialAmount") BigDecimal theMonthActuarialAmount,
                                       @Param("theMonthResidualAmount") BigDecimal theMonthResidualAmount,
                                       @Param("projectId") Integer projectId,
                                       @Param("month") String month
                                       );

    // 更新 basic_v2_trading_management 的 currentMonthBillingAmount
    void updateCurrentMonthBillingAmount(@Param("currentMonthBillingAmount") BigDecimal currentMonthBillingAmount,
                                         @Param("salesAmount") BigDecimal salesAmount,
                                         @Param("totalAmountRaised") BigDecimal totalAmountRaised,
                                         @Param("projectId") Integer projectId,
                                         @Param("month") String month);

    void updateTradingStatusByMonth(@Param("tradingStatus") Integer tradingStatus,
                                    @Param("projectId") Integer projectId,
                                    @Param("month") String month);

    BasicTradingManagementEntity searchOne(Integer projectId, String month);

    Integer getSaleOrProcurement(Integer companyId);

    String getClosingDate(Integer companyId);

    String getPaymentTerm(Integer companyId);

    String getTotalSettlementAmountThismonth(Integer companyId, Integer projectId, String month);

    void insertAccountsReceivableAndPayable(BasicReceivableAndPayable basicReceivableAndPayable);

    Boolean isExist(Integer projectId, String month);

    void insertTransactionDetail(BasicTransactionDetailEntity transactionDetail);

    Boolean isExistClearing(Integer projectId, String month);

    List<HashMap> getStaffsByProjectId(Integer projectId, String lastDayOfMonth, String month);

    void updateStatus(Integer status, Integer projectId, String month);

    //做数据
    void insertTradingManagement(BasicTradingManagementEntity basicTradingManagementEntity);

    void insertTransactionDetails(List<BasicTransactionDetailEntity> list);

    void insertClearingBalance(BasicClearingBalanceEntity basicClearingBalanceEntity);

    Boolean isExistTradingManage(Integer projectId, String month);
    List<Integer> checkProjectIdAndMonth(Integer projectId, String month, String lastDayOfMonth);
    void deleteTradingManagement(Integer tradingId);
    void deleteTransactionDetailsByTradingId(Integer tradingId);
//    void deleteClearingBalanceByTradingId(Integer tradingId);
    Integer getTradingIdByProjectId(Integer projectId, String month);
    List<Integer> getStaffIdsByTradingId(Integer tradingId);
    Integer checkStaffInService(@Param("staffId") Integer staffId,
                                @Param("month") String month,              // 2025-05-01
                                @Param("lastDayOfMonth") String lastDay); // 2025-05-31

    void deleteTsdByTradingIdAndStaffId(Integer tradingId, Integer staffId);
    void deletePendingTransactionDetails(Integer tradingId);
    Integer getSalesAmount(Integer projectId, Integer staffId);
    void deleteStaffSales(Integer tradingId, Integer salesAmount);

    Boolean isExistTransactionDetails(Integer tradingId);

    BigDecimal getAmountRaised(List<Integer> staffIds, Integer projectId);

    List<Integer> getStaffIdsByProjectId(Integer projectId);
    int countReceivable(@Param("salesCompanyId") Integer salesCompanyId,
                                  @Param("buyAndSellType") Integer buyAndSellType,
                                  @Param("targetYearAndMonth") String targetYearAndMonth);
    int countPayable(@Param("procurementCompanyId") Integer procurementCompanyId,
                        @Param("buyAndSellType") Integer buyAndSellType,
                        @Param("targetYearAndMonth") String targetYearAndMonth);

    BigDecimal getEstimatedSalesAmountByTradingId(Integer tradingId);

    void updateClearBalance(BigDecimal theMonthOfActuarialAmount, BigDecimal theMonthOfResidualAmount, Integer projectId, String month);

    Integer selectTradingStatus(Integer projectId, String month);

    BigDecimal getTheMonthOfResidualAmount(Integer projectId, String month);

    void updatePreviousMonthActuarialBalance(BigDecimal theMonthOfResidualAmount, Integer projectId, String month);

    List<Integer> getStaffIdsByProjectIdApplicableMonth(Integer projectId, String month);

    List<Integer> getProcurementCompanyIds(Integer projectId, String month);

    BigDecimal getTotalAmountRaised(Integer projectId, String month);
}

