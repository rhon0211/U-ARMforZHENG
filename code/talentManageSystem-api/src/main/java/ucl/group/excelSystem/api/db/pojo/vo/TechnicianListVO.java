package ucl.group.excelSystem.api.db.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author he_jiale
 * @description 不可增加@Data注解，有局限性
 * @date 2024/07/08 15:52
 */

public class TechnicianListVO {
    @Getter
    @Setter
    private Long technicianId;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String belongCompany;
    @Getter
    @Setter
    private String remark;
    @Getter
    @Setter
    private String customerName;
    @Getter
    @Setter
    private String projectName;
    @Getter
    @Setter
    private String principal;
    @Getter
    @Setter
    private String principalCompany;

    private String cBeginMonth;

    private String cEndMonth;
    @Getter
    @Setter
    private String stopMonth;

    public String getcBeginMonth() {
        return cBeginMonth;
    }

    public void setcBeginMonth(String cBeginMonth) {
        this.cBeginMonth = cBeginMonth;
    }

    public String getcEndMonth() {
        return cEndMonth;
    }

    public void setcEndMonth(String cEndMonth) {
        this.cEndMonth = cEndMonth;
    }


}
