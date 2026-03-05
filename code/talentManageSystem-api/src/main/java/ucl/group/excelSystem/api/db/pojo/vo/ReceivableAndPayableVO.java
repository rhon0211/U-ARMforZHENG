package ucl.group.excelSystem.api.db.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("rawtypes")
@Data
public class ReceivableAndPayableVO {
    private Integer accountsReceivableAndPayableId;
    private Integer companyId;
    private String companyAbbreviation;
    private String closingDate;
    private String expectedDateOfPayment;
    //目标月和trading_management表的适用月一样
    private String targetYearAndMonth;
    //出入金日
    private String depositDate;
    //明细合计
    private BigDecimal lineTotal;
    //
    private BigDecimal amountCharged;
    //出入金额
    private BigDecimal depositAmount;
    private BigDecimal commission;

    private ArrayList<HashMap> list;
    private List<HashMap> uniqueList;

    public void setList(List<HashMap> uniqueList) {
        this.uniqueList = uniqueList;
    }
}
