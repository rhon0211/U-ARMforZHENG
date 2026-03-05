package ucl.group.excelSystem.api.db.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class LaborManageVO {
    private Integer id;
    private Integer staffId;
    private Integer laborStatus;
    private String time;
    private String comment;
    private String staffNameKanji;
    private BigDecimal baseSalary;
    private BigDecimal positionAllowance;
    private BigDecimal overtimeAllowance;
    private BigDecimal otherAllowances;
    private BigDecimal advancePayment;
    private BigDecimal commutingExpense;
    private BigDecimal taxablePaymentTotal;
    private BigDecimal healthInsuranceFee;
    private BigDecimal nursingInsuranceFee;
    private BigDecimal welfarePensionInsurance;
    private BigDecimal employmentInsuranceFee;
    private BigDecimal otherLaborExpenses;
    private BigDecimal totalLaborExpenses;

}
