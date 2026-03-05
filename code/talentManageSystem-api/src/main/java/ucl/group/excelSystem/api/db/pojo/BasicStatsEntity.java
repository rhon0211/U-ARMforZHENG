package ucl.group.excelSystem.api.db.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 统计表实体类
 */
@Data
public class BasicStatsEntity {

    private Long statsId;
    private BigDecimal uclMember;
    private BigDecimal bpMember;
    private BigDecimal totalNumber;
    private BigDecimal bpUclRate;
    private LocalDate yearMonth;

    public BasicStatsEntity(Long statsId, BigDecimal uclMember, BigDecimal bpMember, BigDecimal totalNumber, BigDecimal bpUclRate, LocalDate yearMonth) {
        this.statsId = statsId;
        this.uclMember = uclMember;
        this.bpMember = bpMember;
        this.totalNumber = totalNumber;
        this.bpUclRate = bpUclRate;
        this.yearMonth = yearMonth;
    }
}
