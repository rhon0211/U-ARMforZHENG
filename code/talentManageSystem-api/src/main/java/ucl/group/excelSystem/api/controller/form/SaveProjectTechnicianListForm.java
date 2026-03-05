package ucl.group.excelSystem.api.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * projectName: code
 * package: ucl.group.excelSystem.api.controller.form
 * className: SaveProjectTechnicianListForm
 * author: he_jiale
 * description: 全体项目管理表的保存
 * date: 2024/07/11 16:53
 * version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveProjectTechnicianListForm {
    private List<SaveProjectTechnicianRow> row;
    private List<SaveProjectTechnicianColumn> column;
    /**
     * 1 工作日
     * 2 想定单价
     * 3 实时间
     * 4 乖离
     * 5 实际单价
     */
    @Deprecated
    private int flag;
}
