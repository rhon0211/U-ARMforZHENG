package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LabelEntity extends BasicEntity {

    private int labelId;

    private String labelName;

    private String labelNameJap;

    /**
     * 标签类型ID 为了方便使用一个Entity类就可以接收前端请求数据
     */
    private int typeId;

}
