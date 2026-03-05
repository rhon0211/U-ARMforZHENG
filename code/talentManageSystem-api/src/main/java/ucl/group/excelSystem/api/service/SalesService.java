package ucl.group.excelSystem.api.service;

import ucl.group.excelSystem.api.db.pojo.vo.SalesVO;

import java.util.List;

public interface SalesService {
    List<SalesVO> getData(String fiscalYear);
}
