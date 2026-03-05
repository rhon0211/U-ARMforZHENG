package ucl.group.excelSystem.api.db.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @projectName: code
 * @package: ucl.group.excelSystem.api.db.pojo.vo
 * @className: MonthDataListVO
 * @author: he_jiale
 * @date: 2024/07/09 15:56
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthDataListVO {
    @NonNull
    private Long monthId;
    private BigDecimal presumedTime;
    private BigDecimal expectedPrice;
    private BigDecimal actualHours;
    private BigDecimal actualPrice;
    private BigDecimal subcontractPrice;
    private BigDecimal from;
    private Boolean fromEdit;
    private Boolean actualPriceEdit;

    @JsonIgnore
    private LocalDate yearMonth;
    // 0显示 1青色（价格变动显示） 2空(契约结束显示) 3灰色（退场显示）
    private int colorFlag;


}
