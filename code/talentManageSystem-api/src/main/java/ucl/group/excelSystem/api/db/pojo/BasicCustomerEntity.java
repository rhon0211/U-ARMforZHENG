package ucl.group.excelSystem.api.db.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ucl.group.talentManageSystem.api.db.pojo.BasicEntity;

/**
 * 顾客表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicCustomerEntity extends BasicEntity {

    private Long customerId;
    private String customerName;

}
