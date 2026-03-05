package ucl.group.excelSystem.api.controller;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import ucl.group.excelSystem.api.controller.request.ReceivableAndPayablePageRequest;
import ucl.group.excelSystem.api.controller.request.UpdateReceivableAndPayableRequest;
import ucl.group.excelSystem.api.db.pojo.vo.ReceivableAndPayableVO;
import ucl.group.excelSystem.api.service.ReceivableAndPayableService;
import ucl.group.talentManageSystem.api.common.R;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

@Setter
@RestController
@RequestMapping("/api/v2/receivableAndPayable")
public class ReceivableAndPayableController {

    @Resource
    private ReceivableAndPayableService receivableAndPayableService;

    @GetMapping("/getReceivableByPageHelper")
    public R getReceivableByPage(@Valid ReceivableAndPayablePageRequest request) {
        // 将请求参数转换为Map对象
        Map<String, Object> param = BeanUtil.beanToMap(request);
        int page = request.getPage();
        int length = request.getLength();
//        int start = (page - 1) * length;
        param.put("length", length);
        param.put("page", page);
//        param.put("start", start);
        param.put("expectedDateOfPayment", request.getExpectedDateOfPayment());
        param.put("closingDate", request.getClosingDate());
        PageInfo<ReceivableAndPayableVO> receivableByPageHelper =
                receivableAndPayableService.getReceivableByPageHelper(param);
        return R.ok().put("result", receivableByPageHelper);
    }


    @GetMapping("/getPayableByPageHelper")
    public R getPayableByPage(@Valid ReceivableAndPayablePageRequest request) {
        // 将请求参数转换为Map对象
        Map<String, Object> param = BeanUtil.beanToMap(request);
        // 获取页码和每页长度
        int page = request.getPage();
        int length = request.getLength();
        // 计算起始位置
//        int start = (page - 1) * length;
        // 将起始位置添加到参数Map中
//        param.put("start", start);
        param.put("length", length);
        param.put("page", page);
        param.put("expectedDateOfPayment", request.getExpectedDateOfPayment());
        param.put("closingDate", request.getClosingDate());
        PageInfo<ReceivableAndPayableVO> payableByPageHelper =
                receivableAndPayableService.getPayableByPageHelper(param);
        return R.ok().put("result", payableByPageHelper);
    }


    @PostMapping("/updateReceivable")
    public R updateReceivable(@RequestBody UpdateReceivableAndPayableRequest request) {
        receivableAndPayableService.updateReceivable(request.getDataList());
        return R.ok();
    }

    @PostMapping("/updatePayable")
    public R updatePayable(@RequestBody UpdateReceivableAndPayableRequest request) {
        receivableAndPayableService.updatePayable(request.getDataList());
        return R.ok();
    }

}
