package ucl.group.excelSystem.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import ucl.group.excelSystem.api.controller.form.DeleteProjectForm;
import ucl.group.excelSystem.api.controller.form.InsertProjectForm;
import ucl.group.excelSystem.api.controller.form.SelectProjectByPageForm;
import ucl.group.excelSystem.api.controller.form.UpdateProjectForm;
import ucl.group.excelSystem.api.db.pojo.BasicProjectEntity;
import ucl.group.excelSystem.api.service.ProjectV2Service;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.R;
import ucl.group.talentManageSystem.api.common.annotation.Log;
import ucl.group.talentManageSystem.api.common.enums.BusinessType;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

import static cn.hutool.core.bean.BeanUtil.toBean;

@Setter
@RestController
@RequestMapping("api/v2/project")
@SuppressWarnings({"unchecked", "rawtypes",})
public class ProjectV2Controller {
    @Resource
    private ProjectV2Service projectService;
    @PostMapping
    @SaCheckLogin
    @Log(title = "管理表格系统v2-项目管理", businessType = BusinessType.INSERT)
    public R insertProject(@RequestBody @Valid InsertProjectForm form){
        BasicProjectEntity basicProjectEntity= toBean(form,BasicProjectEntity.class);
        projectService.insertProject(basicProjectEntity);
        return R.ok();
    }
    @PutMapping
    @Log(title = "管理表格系统v2-项目管理", businessType = BusinessType.UPDATE)
    @SaCheckLogin
    public R updateProject(@RequestBody @Valid UpdateProjectForm form){
        projectService.updateProject(toBean(form, BasicProjectEntity.class));
        return R.ok();
    }
    @DeleteMapping
    @Log(title = "管理表格系统v2-项目管理", businessType = BusinessType.DELETE)
    @SaCheckLogin
    public R deleteProject(@RequestBody @Valid DeleteProjectForm form){
        projectService.deleteProject(form.getProjectIds());
        return R.ok();
    }
    @GetMapping
    @SaCheckLogin
    public R selectProjectByPage(@Valid SelectProjectByPageForm form){
        Map param=BeanUtil.beanToMap(form);
        int page=form.getPage();
        int length=form.getLength();
        int start = (page - 1) * length;
        param.put("start", start);
        PageUtils pageUtils =projectService.selectProjectByPage(param);

        return R.ok().put("result", pageUtils);
    }


}
