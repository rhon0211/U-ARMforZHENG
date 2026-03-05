package ucl.group.excelSystem.api.db.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @projectName: code
 * @package: ucl.group.excelSystem.api.db.pojo.vo
 * @className: ProjectTechnicianVO
 * @author: he_jiale
 * @description: 包含row和column和monthDataList
 * @date: 2024/07/09 15:52
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTechnicianVO {
    private List<ProjectTechnicianRow> row;
    private List<ProjectTechnicianColumn> column;

}
