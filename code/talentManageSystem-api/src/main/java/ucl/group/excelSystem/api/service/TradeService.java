package ucl.group.excelSystem.api.service;

import ucl.group.talentManageSystem.api.common.PageUtils;

import java.util.Map;

public interface TradeService {
    PageUtils getByPage(Map<String, Object> param);
}
