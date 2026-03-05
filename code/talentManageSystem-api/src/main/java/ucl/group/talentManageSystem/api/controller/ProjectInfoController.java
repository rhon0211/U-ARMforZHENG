package ucl.group.talentManageSystem.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaCheckLogin;
import ucl.group.talentManageSystem.api.common.R;
import ucl.group.talentManageSystem.api.common.annotation.Log;
import ucl.group.talentManageSystem.api.common.enums.BusinessType;
import ucl.group.talentManageSystem.api.controller.form.talentForm.ProjectDeleteForm;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentPorjectAddForm;
import ucl.group.talentManageSystem.api.service.ProjectInfoService;

@RestController
@RequestMapping("/api/v1/projectInfo")
public class ProjectInfoController {

    @Autowired
    private ProjectInfoService projectInfoService;

    /**
     * 增加人才项目信息
     */
    @PostMapping
    @SaCheckLogin
    @Log(title = "人才管理", businessType = BusinessType.INSERT)
    public R add(@RequestBody @Valid TalentPorjectAddForm talentInterviewInfoAddForm) {
        int row = projectInfoService.add(talentInterviewInfoAddForm);
        if (row > 0) {
            return R.ok();
        } else {
            return R.error("插入项目信息失败");
        }

    }

    /**
     * 修改人才项目信息
     */
    @PutMapping
    @SaCheckLogin
    @Log(title = "人才管理", businessType = BusinessType.UPDATE)
    public R edit(@RequestBody @Valid TalentPorjectAddForm form) {
        int row = projectInfoService.edit(form);
        if (row > 0) {
            return R.ok();
        } else {
            return R.error("修改项目信息失败");
        }

    }

    @DeleteMapping
    @SaCheckLogin
    @Log(title = "人才管理", businessType = BusinessType.DELETE)
    public R remove(@RequestBody ProjectDeleteForm form) {
        int row = projectInfoService.remove(form.getProjectIds());
        if (row > 0) {
            return R.ok();
        } else {
            return R.error("删除项目信息失败");
        }
    }
}
