package ucl.group.excelSystem.api.db.pojo;

import lombok.Data;

@Data
public class BasicStaffEntity {
    private int staffId;
    private String companyId;
    private String photoUrl;
    private String staffNameKanji;
    private String staffNameFurikana;
    private String staffNameRoma;
    private String staffBirthday;
    private String salesRepresentitive;
    private String operationStartDate;
    private String operationEndDate;
    private String latestEvaluation;
    private String activeFlg;
    private String commit;

}
