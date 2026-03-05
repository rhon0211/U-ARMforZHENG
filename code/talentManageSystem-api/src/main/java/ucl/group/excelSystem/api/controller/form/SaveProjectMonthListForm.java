package ucl.group.excelSystem.api.controller.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ucl.group.excelSystem.api.db.pojo.vo.DateListVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * projectName: code
 * package: ucl.group.excelSystem.api.controller.form
 * className: SaveProjectMonthListForm
 * author: he_jiale
 * description: TODO
 * date: 2024/07/16 15:41
 * version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveProjectMonthListForm {
    private String customerName;
    private String productNum;
    private String productName;
    private String summary;
    @JsonIgnore
    private String belongCompany;
    @JsonIgnore
    private String projectName;
    private String remark;
    private String principal;
    private String name;
    private BigDecimal cPrice;
    private BigDecimal personMonth;
    private BigDecimal orderAmountSum;
    private BigDecimal orderAmount;
    private Long monthId;
    private Long projectTechnicianId;
    private int countCustomer;
    private int countProject;
    private List<DateListVO> dateList;
}
