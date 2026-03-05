package ucl.group.excelSystem.api.controller.form;

import lombok.Data;
import ucl.group.excelSystem.api.db.pojo.BasicTransactionDetailEntity;

import java.util.List;

@Data
public class UpdateWorkForm {
    List<BasicTransactionDetailEntity> data;
}
