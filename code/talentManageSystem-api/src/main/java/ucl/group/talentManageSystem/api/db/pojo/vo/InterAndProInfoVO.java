package ucl.group.talentManageSystem.api.db.pojo.vo;

import lombok.Data;
import ucl.group.talentManageSystem.api.db.pojo.BasicInterviewInfoEntity;
import ucl.group.talentManageSystem.api.db.pojo.BasicProjectInfoEntity;

import java.util.List;
@Data
public class InterAndProInfoVO {
    private List<BasicInterviewInfoEntity> interviewInfoList;
    private List<BasicProjectInfoEntity> projectInfoList;

    public InterAndProInfoVO(List<BasicInterviewInfoEntity> interviewInfoList, List<BasicProjectInfoEntity> projectInfoList) {
        this.interviewInfoList = interviewInfoList;
        this.projectInfoList = projectInfoList;
    }
}
