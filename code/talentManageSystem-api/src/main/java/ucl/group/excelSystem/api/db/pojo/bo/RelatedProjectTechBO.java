package ucl.group.excelSystem.api.db.pojo.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * projectName: code
 * package: ucl.group.excelSystem.api.db.pojo.bo
 * className: RelatedProjectTechBO
 * author: he_jiale
 * date: 2024/07/10 15:41
 * version: 1.0
 */
@Data
public class RelatedProjectTechBO {

    private Long technicianId;
    private Long projectTechnicianId;

    private String name;

    private String customerName;

    private String  projectName;

    @JsonProperty("cBeginMonth")
    private String cBeginMonth;
    @JsonProperty("cEndMonth")
    private String cEndMonth;
    @JsonProperty("stopMonth")
    private String stopMonth;
    private String status;
    @JsonProperty("cPrice")
    private BigDecimal cPrice;
    @JsonProperty("cLowerHours")
    private BigDecimal cLowerHours;
    @JsonProperty("cHigherHours")
    private BigDecimal cHigherHours;
    @JsonProperty("cReductPrice")
    private BigDecimal cReductPrice;
    @JsonProperty("cIncreasePrice")
    private BigDecimal cIncreasePrice;
    @JsonProperty("hPrice")
    private BigDecimal hPrice;
    private BigDecimal standardHours;

    private int colorLeft;
    //以下是用于排序字段

    private Date createTime;

    private String belongCompany;

}
