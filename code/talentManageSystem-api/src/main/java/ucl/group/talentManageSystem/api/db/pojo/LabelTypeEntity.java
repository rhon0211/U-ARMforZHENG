package ucl.group.talentManageSystem.api.db.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LabelTypeEntity extends BasicEntity {

    private int typeId;

    private String typeName;

    private String typeNameJap;

    private int labelId;
}
