package ucl.group.excelSystem.api.service;

import org.springframework.stereotype.Service;
import ucl.group.excelSystem.api.db.pojo.BasicTransactionDetailEntity;
import ucl.group.excelSystem.api.db.pojo.vo.BasicClearBalance1Entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Service
public interface TransactionDetailService {
    //要员
    //public List<BasicTransactionDetailEntity> getDetailsByMonthAndProject(String month, Integer projectId);
     void updateTransactionDetail(BasicTransactionDetailEntity transactionDetail);
     List<BasicTransactionDetailEntity> queryAndUpdateTradingManagement(String month, Integer projectId);
    //清算
     BasicClearBalance1Entity getClearBalanceData(String month, Integer projectId);
     void updateBalances(BigDecimal theMonthActuarialAmount,
                               BigDecimal currentMonthBillingAmount,
                               BigDecimal theMonthResidualAmount,
                               BigDecimal salesAmount,
                               BigDecimal totalAmountRaised,
                               Integer projectId,
                               String month);

     void updateTradingStatus(Integer projectId, String month);

    void doData(Integer projectId, String month);
     void updateConfirmInput(Integer status,Integer projectId, String month);
    HashMap<String, BigDecimal> getSettlementUpperAndLowerLimit(Integer projectId);
}
