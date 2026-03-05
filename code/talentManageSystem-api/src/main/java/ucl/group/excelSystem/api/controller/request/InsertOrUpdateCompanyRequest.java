package ucl.group.excelSystem.api.controller.request;

import lombok.Data;

@Data
public class InsertOrUpdateCompanyRequest {
    private Integer companyId;
    private String companyName;
    private String companyAbbreviation;
    // 企業郵便番号
    private String postalCode;
    private String companyCity;
    private String companyAddress;
    private String companyPhonenumber;
    // 請求先郵編
    private String billPostalcode;
    // 請求先所在地
    private String billCity;
    // 請求先住所
    private String billAddress;
    // 請求先電話
    private String billPhonenumber;
    // 代表者職稱
    private String representitivePosition;
    // 代表者名
    private String representitiveName;
    // インボイス番号 发票号
    private String invoiceCode;
    // 支払条件
    private String paymentTerm;
    // 公司類型
    private String saleOrProcurement;
    // 開始日
    private String contractStartDate;
    // 終了日
    private String contractEndDate;
    // 摘要
    private String comment;
    // 1:アクティブ。null：非アクティブ。默认为1
    private String activeFlg;
    private String closingDate;
}
