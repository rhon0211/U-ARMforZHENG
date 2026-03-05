package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DictDataEntity extends BasicEntity {
    private int dictDataId;
    /**
     * 排序
     */
    private int dictSort;
    /**
     * 中文描述
     */
    private String dictLabel;
    /**
     * 日文描述
     */
    private String dictLabelJap;
    /**
     * 字典值
     */
    private String dictValue;
    /**
     * 字典类型，即前端查询的type字段
     */
    private String dictType;
    /**
     * 样式属性
     */
    private String cssClass;
    /**
     * 表格回显样式
     */
    private String listClass;
    /**
     * 是否默认 Y是 N否
     */
    private String isDefault;

}
