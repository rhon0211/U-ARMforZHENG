package ucl.group.excelSystem.api.db.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: code
 * @package: ucl.group.excelSystem.api.db.pojo.vo
 * @className: DateListVO
 * @author: he_jiale
 * @description: 作业管理表的 子属性
 * @date: 2024/07/11 18:42
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateListVO {
    private String name;
    private String monthDate;
}
