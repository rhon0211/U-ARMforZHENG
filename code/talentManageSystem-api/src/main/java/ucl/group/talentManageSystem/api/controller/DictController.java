package ucl.group.talentManageSystem.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucl.group.talentManageSystem.api.common.R;
import ucl.group.talentManageSystem.api.service.DictService;

@RestController
@RequestMapping("/api/v1/dict")
public class DictController {

    @Autowired
    private DictService dictService;
    @GetMapping("/{type}")
    @SaCheckLogin
    public R getDictByType(@PathVariable("type") String type) {
        return R.ok().put("result", dictService.searchByType(type));
    }
}
