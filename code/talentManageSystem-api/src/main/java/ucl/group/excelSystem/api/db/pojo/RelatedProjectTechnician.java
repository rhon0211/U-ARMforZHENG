package ucl.group.excelSystem.api.db.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ucl.group.talentManageSystem.api.db.pojo.BasicEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 项目-技术者关联表实体类
 */
@Data
public class RelatedProjectTechnician extends BasicEntity implements Cloneable {
    private Long projectTechnicianId;
    private Long projectId;
    private Long technicianId;
    private Long parentId;
    private LocalDate cRealBeginMonth;
    private LocalDate cRealEndMonth;
    private LocalDate realStopMonth;
    private LocalDate cBeginMonth;
    private LocalDate cEndMonth;
    private LocalDate hBeginMonth;
    private LocalDate hEndMonth;
    private LocalDate priceMonth;
    private LocalDate stopMonth;
    private BigDecimal cHigherHours;
    private BigDecimal cLowerHours;
    private BigDecimal cIncreasePrice;
    private BigDecimal cReductPrice;
    private BigDecimal hHigherHours;
    private BigDecimal hLowerHours;
    private BigDecimal hIncreasePrice;
    private BigDecimal hReductPrice;
    private BigDecimal hPrice;
    private BigDecimal standardHours;
    private BigDecimal cPrice;
    private int contractType;
    @JsonIgnore
    private String selected;
    private String contract;
    private String contractNum;

    @Override
    public RelatedProjectTechnician clone() {
        try {
            return (RelatedProjectTechnician) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
