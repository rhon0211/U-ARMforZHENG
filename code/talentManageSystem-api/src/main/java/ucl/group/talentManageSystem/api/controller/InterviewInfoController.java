package ucl.group.talentManageSystem.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.group.talentManageSystem.api.common.R;
import ucl.group.talentManageSystem.api.common.annotation.Log;
import ucl.group.talentManageSystem.api.common.enums.BusinessType;
import ucl.group.talentManageSystem.api.controller.form.talentForm.InterviewInfoDeleteForm;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentInterviewInfoAddForm;
import ucl.group.talentManageSystem.api.service.InterviewInfoService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/interviewInfo")
public class InterviewInfoController {

    @Autowired
    private InterviewInfoService interviewInfoService;

    /**
     * 增加人才面试信息
     */
    @PostMapping
    @SaCheckLogin
    @Log(title = "人才管理", businessType = BusinessType.INSERT)
    public R add(@RequestBody @Valid TalentInterviewInfoAddForm talentInterviewInfoAddForm) {
        int row = interviewInfoService.add(talentInterviewInfoAddForm);
        if (row > 0) {
            return R.ok();
        } else {
            return R.error("插入面试信息失败");
        }
    }
    @PutMapping
    @SaCheckLogin
    @Log(title = "人才管理", businessType = BusinessType.UPDATE)
    public R edit(@RequestBody TalentInterviewInfoAddForm form){
        int row=interviewInfoService.edit(form);
        if(row>0) {
            return R.ok();
        }else{
            return R.error("修改面试信息失败");
        }
    }
    @DeleteMapping
    @SaCheckLogin
    @Log(title = "人才管理", businessType = BusinessType.DELETE)
    public R remove(@RequestBody InterviewInfoDeleteForm form){
        int row=interviewInfoService.remove(form.getInterIds());
        if(row>0) {
            return R.ok();
        }else{
            return R.error("删除面试信息失败");
        }
    }

}
