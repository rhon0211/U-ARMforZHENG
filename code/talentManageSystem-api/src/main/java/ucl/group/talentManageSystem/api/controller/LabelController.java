package ucl.group.talentManageSystem.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.web.bind.annotation.*;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.R;
import ucl.group.talentManageSystem.api.common.annotation.Log;
import ucl.group.talentManageSystem.api.common.enums.BusinessType;
import ucl.group.talentManageSystem.api.controller.form.*;
import ucl.group.talentManageSystem.api.db.pojo.LabelEntity;
import ucl.group.talentManageSystem.api.service.LabelService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/label")
public class LabelController {

    @Resource
    private LabelService labelService;

    @GetMapping("/allLabel")
    @SaCheckLogin
    public R allLabel() {
        HashMap map = labelService.labelSelect();
        return R.ok().put("result", map);
    }

    @PostMapping
    @SaCheckLogin
    @Log(title = "标签管理", businessType = BusinessType.INSERT)
    @SaCheckPermission(value = { "label_add"}, mode = SaMode.OR)
    public R addLabel(@RequestBody @Valid LabelAddForm form) {
        LabelEntity entity = BeanUtil.toBean(form, LabelEntity.class);
        labelService.LabelInsert(entity);
        return R.ok();
    }

    @PutMapping
    @SaCheckLogin
    @Log(title = "标签管理", businessType = BusinessType.UPDATE)
    @SaCheckPermission(value = { "label_update"}, mode = SaMode.OR)
    public R modifyLabel(@RequestBody @Valid LabelUpdateForm form) {
        LabelEntity entity = BeanUtil.toBean(form, LabelEntity.class);
        labelService.LabelUpdate(entity);
        return R.ok();
    }

    @PostMapping("/updateSearchById")
    @SaCheckLogin
    public R updateSearchById(@RequestBody @Valid SearchLabelByIdForm form) {
        HashMap map = labelService.searchById(form.getLabelId());
        return R.ok(map);
    }

    @DeleteMapping
    @SaCheckLogin
    @Log(title = "标签管理", businessType = BusinessType.DELETE)
    @SaCheckPermission(value = { "label_delete"}, mode = SaMode.OR)
    public R deleteLabels(@RequestBody @Valid LabelDeleteForm form) {
        labelService.LabelDeletes(form.getIds());
        return R.ok();
    }

    @PostMapping("/list")
    @SaCheckLogin
    public R listLabel(@RequestBody @Valid LabelSearchByPageForm form) {
        if (form.getStatus()==null || form.getStatus().equals("")) {
            form.setStatus("0");
        }
        Map param = BeanUtil.beanToMap(form);
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        param.put("start", start);
        PageUtils pageUtils = labelService.LabelSearchByPage(param);
        return R.ok().put("result", pageUtils);
    }

}
