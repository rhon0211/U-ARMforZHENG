package ucl.group.excelSystem.api.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.group.excelSystem.api.db.pojo.vo.LaborManageVO;
import ucl.group.excelSystem.api.db.pojo.vo.PageResultVO;
import ucl.group.excelSystem.api.service.LaborManageService;
import ucl.group.talentManageSystem.api.common.R;

import java.util.List;

@Setter
@RestController
@RequestMapping("/api/v2/laborManagement")

public class LaborManageController {

    @Autowired
    private LaborManageService laborManageService;
    @GetMapping("/query")
    public R getLaborManageByPage(
            @RequestParam(required = false) String time,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "100") Integer pageSize) {
        PageResultVO<LaborManageVO> result = laborManageService.getLaborManageByPage(time, page, pageSize);
        return R.ok().put("list", result);
    }

    // 更新劳务数据
    @PostMapping("/update")
    public R updateLaborManage(@RequestBody List<LaborManageVO> request) {
        laborManageService.updateLaborManage(request);
        return R.ok();
    }


    // 更新劳务状态（确认/解除确认）
    @PostMapping("/confirm")
    public R updateLaborStatusBatch(@RequestBody List<LaborManageVO> request) {
        laborManageService.updateLaborStatusBatch(request);
        return R.ok();
    }


}