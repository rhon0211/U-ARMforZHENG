package ucl.group.excelSystem.api.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * projectName: code
 * package: ucl.group.excelSystem.api.controller.form
 * className: SaveTechnicianListForm
 * author: he_jiale
 * description: TODO
 * date: 2024/07/08 11:21
 * version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveTechnicianListForm {
    private Long technicianId;
    private String remark;
    private String updateBy;

}
