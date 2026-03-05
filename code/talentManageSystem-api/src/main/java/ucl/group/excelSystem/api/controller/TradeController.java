package ucl.group.excelSystem.api.controller;


import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.group.excelSystem.api.controller.request.TradePageRequest;
import ucl.group.excelSystem.api.controller.request.TradeRequest;
import ucl.group.excelSystem.api.service.TradeService;
import ucl.group.excelSystem.api.service.TransactionDetailService;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.R;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/trade")
public class TradeController {


    private final TradeService tradeService;

    private final TransactionDetailService transactionDetailService;

    //构造器注入
    @Autowired
    public TradeController(TradeService tradeService, TransactionDetailService transactionDetailService) {
        this.tradeService = tradeService;
        this.transactionDetailService = transactionDetailService;
    }

    @GetMapping("/getByPage")
    public R getByPage(@Valid TradePageRequest request) {
        // 将请求参数转换为Map对象
        Map<String, Object> param = BeanUtil.beanToMap(request);

        // 获取页码和每页长度
        int page = request.getPage();
        int length = request.getLength();

        // 计算起始位置
        int start = (page - 1) * length;

        // 将起始位置添加到参数Map中
        param.put("start", start);
        param.put("date", request.getDate());
        // 调用服务方法获取分页数据
        PageUtils pageUtils = tradeService.getByPage(param);

        // 返回包含分页结果的响应对象
        return R.ok().put("result", pageUtils);
    }

    @PostMapping("/doData")
    public R doData(@RequestBody TradeRequest request) {
        request.getProjectIds().forEach(projectId -> transactionDetailService.doData(projectId, request.getMonth()));
        return R.ok();
    }
}
