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
import ucl.group.talentManageSystem.api.db.pojo.LabelTypeEntity;
import ucl.group.talentManageSystem.api.service.LabelTypeService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/labelType")
@SuppressWarnings({"rawtypes", "unchecked"})
public class LabelTypeController {

    @Resource
    LabelTypeService labelTypeService;

    @GetMapping("/allLabelType")
    @SaCheckLogin
    public R searchAll() {
        ArrayList<HashMap> list = labelTypeService.LabelTypeSelect();
        return R.ok().put("result", list);
    }

    @PostMapping
    @SaCheckLogin
    @Log(title = "标签类别管理", businessType = BusinessType.INSERT)
    @SaCheckPermission(value = {"label_add"}, mode = SaMode.OR)
    public R addLabelType(@RequestBody @Valid LabelTypeAddForm form) {
        LabelTypeEntity entity = BeanUtil.toBean(form, LabelTypeEntity.class);
        labelTypeService.LabelTypeInsert(entity);
        return R.ok();
    }

    @PutMapping
    @SaCheckLogin
    @Log(title = "标签类别管理", businessType = BusinessType.UPDATE)
    @SaCheckPermission(value = {"label_update"}, mode = SaMode.OR)
    public R modifyLabelType(@RequestBody @Valid LabelTypeUpdateForm form) {
        LabelTypeEntity entity = BeanUtil.toBean(form, LabelTypeEntity.class);
        labelTypeService.LabelTypeUpdate(entity);
        return R.ok();
    }

    @DeleteMapping
    @SaCheckLogin
    @Log(title = "标签类别管理", businessType = BusinessType.DELETE)
    @SaCheckPermission(value = {"label_delete"}, mode = SaMode.OR)
    public R deleteLabelTypes(@RequestBody @Valid LabelTypeDeleteForm form) {
        String result = labelTypeService.LabelTypeDeletes(form.getIds());
        return R.ok().put("result", result);
    }

    @PostMapping("/list")
    @SaCheckLogin
    public R listLabelType(@RequestBody @Valid LabelTypeSearchByPageForm form) {
        if (form.getStatus() == null || form.getStatus().equals("")) {
            form.setStatus("0");
        }
        if (form.getTypeName() == null) {
            form.setTypeName(""); // Defaulting to empty string if typeName is null
        }
        Map param = BeanUtil.beanToMap(form);
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        param.put("start", start);
        PageUtils pageUtils = labelTypeService.LabelTypeSearchByPage(param);
        return R.ok().put("result", pageUtils);
    }

    @PostMapping("/updateSearchById")
    @SaCheckLogin
    public R updateSearchById(@RequestBody @Valid SearchLabelTypeByIdForm form) {
        HashMap map = labelTypeService.searchById(form.getTypeId());
        return R.ok(map);
    }

}
