package ucl.group.excelSystem.api.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ucl.group.excelSystem.api.db.pojo.vo.MonthDataListVO;

import java.util.List;

/**
 * projectName: code
 * package: ucl.group.excelSystem.api.controller.form
 * className: SaveProjectTechnicianRow
 * author: he_jiale
 * description: TODO
 * date: 2024/07/11 17:28
 * version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveProjectTechnicianRow {
    private Long projectTechnicianId;
    private List<MonthDataListVO> monthDataList;
}
