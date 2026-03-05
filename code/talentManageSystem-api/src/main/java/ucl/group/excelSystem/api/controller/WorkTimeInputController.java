package ucl.group.excelSystem.api.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.group.excelSystem.api.controller.form.UpdateBalanceForm;
import ucl.group.excelSystem.api.controller.form.UpdateWorkForm;
import ucl.group.excelSystem.api.db.pojo.BasicTransactionDetailEntity;
import ucl.group.excelSystem.api.db.pojo.vo.BasicClearBalance1Entity;
import ucl.group.excelSystem.api.service.TransactionDetailService;
import ucl.group.talentManageSystem.api.common.R;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Setter
@RestController
@RequestMapping("/api/v2/worktimeinput")

public class WorkTimeInputController {
    @Autowired
    private TransactionDetailService transactionDetailService;

    @GetMapping("/doData")
    public R doData(@RequestParam Integer projectId, @RequestParam String month) {
        transactionDetailService.doData(projectId, month);
        return R.ok();
    }

    @PostMapping("update")
    public R updateTransactionDetail(@RequestBody UpdateWorkForm updateWorkForm) {
        List<BasicTransactionDetailEntity> list = updateWorkForm.getData();
        for (BasicTransactionDetailEntity basicTransactionDetailEntity : list) {
            transactionDetailService.updateTransactionDetail(basicTransactionDetailEntity);
        }
        return R.ok();
    }

    //查要员
    @GetMapping("/queryAndUpdate")
    public R queryAndUpdate(
            @RequestParam String month,
            @RequestParam Integer projectId) {

        List<BasicTransactionDetailEntity> result = transactionDetailService.queryAndUpdateTradingManagement(month, projectId);
        return R.ok().put("result", result);
    }


    //查清算
    @GetMapping("/getBalanceByProjectAndMonth")
    public R getClearBalanceData(
            @RequestParam String month,
            @RequestParam Integer projectId
            ) {

        BasicClearBalance1Entity result = transactionDetailService.getClearBalanceData(month, projectId);
        return R.ok().put("result", result);
    }

    //保存清算
    @PostMapping("/balanceUpdate")
    public R updateBalances(@RequestBody UpdateBalanceForm request) {
        try {
            transactionDetailService.updateBalances(
                    request.getTheMonthActuarialAmount(),
                    request.getCurrentMonthBillingAmount(),
                    request.getTheMonthResidualAmount(),
                    request.getSalesAmount(),
                    request.getTotalAmountRaised(),
                    request.getProjectId(),
                    request.getMonth()
            );
            return R.ok("Update successful");
        } catch (Exception e) {
            return R.error("Update failed: " + e.getMessage());
        }
    }

    //营业确认
    @GetMapping("/updateStatus")
    public R updateStatus(@RequestParam Integer projectId, @RequestParam String month) {
        transactionDetailService.updateTradingStatus(projectId, month);
        return R.ok();
    }

    @GetMapping("/updateTradingStatus")
    public R updateTradingStatus(@RequestParam Integer projectId, @RequestParam String month) {
        transactionDetailService.updateTradingStatus(projectId, month);
        return R.ok();
    }

    @GetMapping("/getSettlementUpperAndLowerLimit")
    public R getSettlementUpperAndLowerLimit(@RequestParam Integer projectId) {
        HashMap<String, BigDecimal> settlementUpperAndLowerLimit = transactionDetailService.getSettlementUpperAndLowerLimit(projectId);
        return R.ok().put("result", settlementUpperAndLowerLimit);
    }


    @GetMapping("/updateConfirmInput")
    public R updateConfirmInput(@RequestParam Integer status, @RequestParam Integer projectId, @RequestParam String month) {
        transactionDetailService.updateConfirmInput(status, projectId, month);
        return R.ok();
    }

}
