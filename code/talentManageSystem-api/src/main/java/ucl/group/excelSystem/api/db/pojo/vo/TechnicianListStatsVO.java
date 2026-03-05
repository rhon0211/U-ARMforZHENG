package ucl.group.excelSystem.api.db.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

/**
 * @projectName: code
 * @package: ucl.group.excelSystem.api.db.pojo.vo
 * @className: TechnicianListStatsVO
 * @author: he_jiale
 * @description: 技术者一览的统计信息
 * @date: 2024/07/08 11:55
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianListStatsVO {
    private Long statsId;
    private BigDecimal uclMember;
    private BigDecimal bpMember;
    private BigDecimal totalNumber;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal bpUclRate;

    private String yearMonth;


}
