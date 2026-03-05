package ucl.group.excelSystem.api.controller.request;


import lombok.Data;
import ucl.group.excelSystem.api.db.pojo.vo.ReceivableAndPayableVO;

import java.util.List;

@Data
public class UpdateReceivableAndPayableRequest {

    private List<ReceivableAndPayableVO> dataList;
}
