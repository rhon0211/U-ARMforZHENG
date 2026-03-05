package ucl.group.excelSystem.api.controller.request;

import lombok.Getter;
import lombok.Setter;
import ucl.group.excelSystem.api.db.pojo.ProjectEntity;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class SaveProjectRequest {
    private ProjectEntity projectInfo; // 项目基本信息
    private List<Map<String, Object>> projectDetails; // 项目详细信息

}
