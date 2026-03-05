package ucl.group.excelSystem.api.db.pojo;


import lombok.Data;

@Data
public class BasicCompanyEntity {
    private Integer saleOrProcurement;
    private Integer companyId;
    private Integer activeFlg;
    private String companyName;
    private String companyAbbreviation;
    private String postalCode;
    private String companyCity;
    private String companyAddress;
    private String companyPhonenumber;
    private String billsiteCheck;
    private String billPostalcode;
    private String closingDate;
    private String billCity;
    private String billAddress;
    private String billPhonenumber;
    private String representitivePosition;
    private String representitiveName;
    private String invoiceCode;
    private String paymentTerm;
    private String contractStartDate;
    private String contractEndDate;
    private String comment;

}
