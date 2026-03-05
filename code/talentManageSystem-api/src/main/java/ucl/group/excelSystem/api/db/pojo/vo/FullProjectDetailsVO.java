package ucl.group.excelSystem.api.db.pojo.vo;

import lombok.Getter;
import lombok.Setter;
import ucl.group.excelSystem.api.db.pojo.ProjectEntity;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class FullProjectDetailsVO {
    // Getters and Setters
    private ProjectEntity projectInfo;
    private List<Map<String, Object>> projectDetails;

}
