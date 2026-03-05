package ucl.group.talentManageSystem.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.R;
import ucl.group.talentManageSystem.api.common.annotation.Log;
import ucl.group.talentManageSystem.api.common.enums.BusinessType;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentAddForm;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentDeleteForm;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentIdForm;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentInfoByPageForm;
import ucl.group.talentManageSystem.api.service.TalentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/talent")
public class TalentController {

    @Autowired
    private TalentService talentService;

    /**
     * @Description: 超管，系统管理员
     * @Param: [form]
     * @return: ucl.group.talentManageSystem.api.common.R
     * @Author: he_jiale
     * @Date: 18:03 2024/09/12
     */
    @PostMapping("/searchLev1secretInfo")
    @SaCheckLogin
    @SaCheckPermission(value = {"talent_search_lev1secret_info", "talent_search_blacklist",
            "talent_search_deletelist"}, mode = SaMode.OR)
    public R listLabel(@RequestBody @Valid TalentInfoByPageForm form) {
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        form.setStart(start);
        PageUtils pageUtils = talentService.searchByPage(form);
        return R.ok().put("result", pageUtils);
    }

    /**
     * @Description: 普通管理员
     * @Param: [form]
     * @return: ucl.group.talentManageSystem.api.common.R
     * @Author: he_jiale
     * @Date: 18:03 2024/09/12
     */
    @PostMapping("/searchLev2secretInfo")
    @SaCheckLogin
    @SaCheckPermission(value = {"talent_search_lev2secret_info", "talent_search_blacklist",
            "talent_search_deletelist"}, mode = SaMode.OR)
    public R listLabel2(@RequestBody @Valid TalentInfoByPageForm form) {

        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        form.setStart(start);
        PageUtils pageUtils = talentService.searchByPage2(form);
        return R.ok().put("result", pageUtils);
    }

    /**
     * @Description: 普通用户
     * @Param: [form]
     * @return: ucl.group.talentManageSystem.api.common.R
     * @Author: he_jiale
     * @Date: 18:03 2024/09/12
     */
    @PostMapping("/searchBasicInfo")
    @SaCheckLogin
    @SaCheckPermission(value = {"talent_search_basic_info", "talent_search_blacklist",
            "talent_search_deletelist"}, mode = SaMode.OR)
    public R listLabel3(@RequestBody @Valid TalentInfoByPageForm form) {
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        form.setStart(start);
        PageUtils pageUtils = talentService.searchByPage3(form);
        return R.ok().put("result", pageUtils);
    }

    @PostMapping
    @SaCheckLogin
    @Log(title = "人才管理", businessType = BusinessType.INSERT)
    @SaCheckPermission(value = {"talent_add"}, mode = SaMode.AND)
    public R add(@RequestBody @Valid TalentAddForm form) {
        int row = talentService.add(form);
        if (row > 0) {
            return R.ok();
        } else {
            return R.error("插入人才记录失败");
        }
    }

    @PutMapping
    @SaCheckLogin
    @Log(title = "人才管理", businessType = BusinessType.UPDATE)
    @SaCheckPermission(value = {"talent_update"}, mode = SaMode.OR)
    public R edit(@RequestBody @Valid TalentAddForm form) {
        int row = talentService.edit(form);
        if (row > 0) {
            return R.ok();
        } else {
            return R.error("修改人才记录失败");
        }
    }

    @DeleteMapping
    @SaCheckLogin
    @Log(title = "人才管理", businessType = BusinessType.DELETE)
    @SaCheckPermission(value = {"talent_delete"}, mode = SaMode.AND)
    public R remove(@RequestBody TalentDeleteForm talentDeleteForm) {
        int row = talentService.remove(talentDeleteForm.getTalentIds());
        if (row > 0) {
            return R.ok();
        } else {
            return R.error("修改人才记录失败");
        }
    }

    /**
     * 恢复删除
     */
    @PutMapping("/undeletion")
    @SaCheckLogin
    @Log(title = "人才管理", businessType = BusinessType.UPDATE)
    @SaCheckPermission(value = {"talent_recover_deletelist"}, mode = SaMode.AND)
    public R undeletion(@RequestBody TalentIdForm undeletionForm) {
        int row = talentService.undeletion(undeletionForm.getTalentId());
        if (row > 0) {
            return R.ok();
        } else {
            return R.error("恢复删除人才记录失败");
        }
    }

    /**
     * 根据人才id查询面试信息和项目信息
     * 
     * @param form
     * @return
     */
    @PostMapping("/searchInfoByTalentId")
    @SaCheckLogin
    @SaCheckPermission(value = {"talent_search_basic_info", "talent_search_lev1secret_info",
            "talent_search_lev2secret_info"}, mode = SaMode.OR)
    public R searchInfoByTalentId(@RequestBody @Valid TalentIdForm form) {
        return R.ok().put("result", talentService.searchInfoByTalentId(form.getTalentId()));

    }

    /**
     * 拉黑 和恢复拉黑
     * 
     * @param form
     * @return
     */
    @PutMapping("/blacklist")
    @SaCheckLogin
    @Log(title = "人才管理", businessType = BusinessType.UPDATE)
    @SaCheckPermission(value = {"talent_add_blacklist", "talent_update_blacklist"},
            mode = SaMode.OR)
    public R editBlacklist(@RequestBody TalentAddForm form) {
        int row = talentService.editBlacklist(form);
        if (row > 0) {
            return R.ok();
        } else {
            return R.error("拉黑或取消拉黑人才记录失败");
        }
    }

}
