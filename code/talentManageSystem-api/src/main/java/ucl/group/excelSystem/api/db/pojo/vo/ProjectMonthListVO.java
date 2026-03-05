package ucl.group.excelSystem.api.db.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @projectName: code
 * @package: ucl.group.excelSystem.api.db.pojo.vo
 * @className: ProjectMonthListVO
 * @author: he_jiale
 * @description: TODO
 * @date: 2024/07/11 18:22
 * @version: 1.0
 */
@Data
public class ProjectMonthListVO {
    private BigDecimal orderAmountSum;
    private BigDecimal orderAmount;
    @JsonProperty("cPrice")
    private BigDecimal cPrice;
    private BigDecimal personMonth;
    private String customerName;
    private String name;
    private String productNum;
    private String productName;
    private String summary;
    private String remark;
    private String principal;
    @JsonIgnore
    private String belongCompany;
    @JsonIgnore
    private String projectName;
    private Long projectTechnicianId;
    private Long monthId;
    //一个客户的总技术者
    private int countCustomer;
    //一个项目的总技术者
    private int countProject;
    /**
     * @param null:
     * @return null
     * @author he_jiale
     * @description 日期信息，monthId从technicianList里获取
     * @date 2024/07/12 18:45
     */
    private List<DateListVO> dateList;

}
