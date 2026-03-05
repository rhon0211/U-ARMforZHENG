package ucl.group.excelSystem.api.db.pojo.vo;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesVO {

    @ExcelIgnore
    private Integer projectId;

    @ExcelProperty("対象年月")
    private String applicableMonth;

    @ExcelProperty("企業名")
    private String companyAbbreviation;

    @ExcelProperty("部署名")
    private String departmentAbbreviation;

//    @ExcelProperty("担当者名")
    @ExcelIgnore
    private String principalName;

    @ExcelProperty("案件名")
    private String projectNameAbbreviation;

    @ExcelProperty("売買管理ステータス")
    private String tradingStatus;

    @ExcelProperty("販売価格合計")
    private BigDecimal totalSalesAmount;

    @ExcelProperty("調達金額合計")
    private BigDecimal totalAmountRaised;

    @ExcelProperty("労務費合計")
    private BigDecimal totalLaborExpenses;
}
