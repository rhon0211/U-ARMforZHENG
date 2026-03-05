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
import ucl.group.talentManageSystem.api.controller.form.operlog.OperLogByPageForm;
import ucl.group.talentManageSystem.api.controller.form.operlog.OperLogDeleteForm;
import ucl.group.talentManageSystem.api.service.SysOperLogService;

import javax.validation.Valid;


/**
 * 操作日志记录
 * 
 * @author hejiale
 */
@RestController
@RequestMapping("/api/v1/operlog")
public class SysOperlogController
{
    @Autowired
    private SysOperLogService operLogService;


    @PostMapping("/list")
    @SaCheckLogin
    @SaCheckPermission(value = {"log"}, mode = SaMode.AND)
    public R list(@RequestBody @Valid OperLogByPageForm form)
    {
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        form.setStart(start);

        PageUtils pageUtils = operLogService.selectOperLogList(form);
        return R.ok().put("result",pageUtils);
    }


    @Log(title = "日志管理", businessType = BusinessType.DELETE)
    @SaCheckPermission(value = {"log"}, mode = SaMode.AND)
    @DeleteMapping
    @SaCheckLogin
    public R remove(@RequestBody OperLogDeleteForm form)
    {
        int row =operLogService.deleteOperLogByIds(form.getOperIds());
        if(row>0){
            return R.ok();
        }
        else{
            return R.error("删除日志记录失败");
        }
    }

    @Log(title = "日志管理", businessType = BusinessType.CLEAN)
    @SaCheckPermission(value = {"log"}, mode = SaMode.AND)
    @DeleteMapping("/clean")
    @SaCheckLogin
    public R clean()
    {
        operLogService.cleanOperLog();
        return R.ok();
    }



}
