package ucl.group.excelSystem.api.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class PersonnelRequest {
        private Integer staffId;
        @NotNull
        private Integer companyId;
        private Date entryDate;
        private Date plannedExitDate;
        private Date actualExitDate;
        private BigDecimal salesAmount;
        private BigDecimal salesIncrementUnitPriceHour;
        private BigDecimal salesDecrementUnitPriceHour;
        private BigDecimal amountRaised;
        private BigDecimal procurementIncrementUnitPriceHour;
        private BigDecimal procurementDecrementUnitPriceHour;
        public boolean hasValidStaffId() {
         return false;
    }
}
