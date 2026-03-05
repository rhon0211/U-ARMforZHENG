package ucl.group.talentManageSystem.api.db.pojo.vo;

import lombok.Data;

/**
 * 查询人才记录
 * 返回前端
 */
@Data
public class LabelYear {
    private int labelId;
    private String labelName;
    private String labelNameJap;
    private Float labelYear;
}
