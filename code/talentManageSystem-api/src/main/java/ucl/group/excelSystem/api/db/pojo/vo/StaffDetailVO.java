package ucl.group.excelSystem.api.db.pojo.vo;


import lombok.Data;

@Data

public class StaffDetailVO {
    private Integer staffId;
    private Integer companyId;
    private String companyName;
    private String staffName;
    private String entryDate;
    private String plannedExitDate;
    private String actualExitDate;
    private Double salesAmount;
    private Double salesIncrementUnitPriceHour;
    private Double salesDecrementUnitPriceHour;
    private Double amountRaised;
    private Double procurementIncrementUnitPriceHour;
    private Double procurementDecrementUnitPriceHour;
}


