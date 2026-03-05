package ucl.group.excelSystem.api.service;

import com.github.pagehelper.PageInfo;
import ucl.group.excelSystem.api.db.pojo.vo.ReceivableAndPayableVO;
import java.util.List;
import java.util.Map;

public interface ReceivableAndPayableService {
    PageInfo<ReceivableAndPayableVO> getReceivableByPageHelper(Map<String, Object> param);

    PageInfo<ReceivableAndPayableVO> getPayableByPageHelper(Map<String, Object> param);

    void updateReceivable(List<ReceivableAndPayableVO> dataList);

    void updatePayable(List<ReceivableAndPayableVO> dataList);
}
