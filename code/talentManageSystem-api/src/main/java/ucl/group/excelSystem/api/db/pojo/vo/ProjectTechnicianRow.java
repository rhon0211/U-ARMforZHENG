package ucl.group.excelSystem.api.db.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @projectName: code
 * @package: ucl.group.excelSystem.api.db.pojo.vo
 * @className: ProjectTechnicianRow
 * @author: he_jiale
 * @description: 包含MonthDataListVO
 * @date: 2024/07/09 15:53
 * @version: 1.0
 */
@Data
public class ProjectTechnicianRow {
    private Long projectTechnicianId;
    @JsonIgnore
    private Long technicianId;
    private String customerName;
    private String projectName;
    private String name;
    private String status;
    private String belongCompany;
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
    private List<MonthDataListVO> monthDataList;
    private List<MonthClassArrVO> monthClassArr;
    //以下仅限1-6
    @JsonIgnore
    private int startMonthNum;
    @JsonIgnore
    private int endMonthNum;
    @JsonIgnore
    private int stopMonthNum;
    //以下是用于判断
    @JsonIgnore
    private LocalDate startMonth;
    @JsonIgnore
    private LocalDate endMonth;
    @JsonIgnore
    private LocalDate stopMonth;
    //仅限0和1
    private int colorLeft;
    @JsonIgnore
    private Date createTime;




}
