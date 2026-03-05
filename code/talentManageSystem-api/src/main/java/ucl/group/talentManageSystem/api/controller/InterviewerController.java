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
import ucl.group.talentManageSystem.api.controller.form.talentForm.InterviewerByPageForm;
import ucl.group.talentManageSystem.api.controller.form.talentForm.InterviewerDeleteForm;
import ucl.group.talentManageSystem.api.controller.form.talentForm.InterviewerForm;
import ucl.group.talentManageSystem.api.db.pojo.BasicInterviewerEntity;
import ucl.group.talentManageSystem.api.service.InterviewInfoService;
import ucl.group.talentManageSystem.api.service.InterviewerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interviewer")
public class InterviewerController {

    @Autowired
    private InterviewInfoService interviewInfoService;
    @Autowired
    private InterviewerService interviewerService;

    @PostMapping("/list")
    @SaCheckLogin
    @SaCheckPermission(value = {"interviewer_search"})
    public R searchByPage(@RequestBody InterviewerByPageForm interviewerByPageForm) {
        int page = interviewerByPageForm.getPage();
        int length = interviewerByPageForm.getLength();
        int start = (page - 1) * length;
        interviewerByPageForm.setStart(start);
        PageUtils pageUtils = interviewerService.searchByPage(interviewerByPageForm);
        return R.ok().put("result", pageUtils);
    }

    /**
     * 根据id查询面试官
     */
    @PostMapping("/searchById")
    @SaCheckLogin
    @SaCheckPermission(value = {"interviewer_add","interviewer_del","interviewer_update"}, mode = SaMode.OR)
    public R searchById(@RequestBody InterviewerForm form) {
        BasicInterviewerEntity basicInterviewerEntity = interviewerService.searchById(form.getInterviewerId());
        return R.ok().put("result", basicInterviewerEntity);
    }
    /**
     * 根据type查询面试官
     */
    @PostMapping("/searchInterviewerByType")
    @SaCheckLogin
    @SaCheckPermission(value = {"interviewer_search"}, mode = SaMode.OR)
    public R searchInterviewerByType(@RequestBody InterviewerForm form) {
        List<BasicInterviewerEntity> basicInterviewerEntity = interviewerService.searchByType(form.getType());
        return R.ok().put("result", basicInterviewerEntity);
    }
    @PostMapping
    @SaCheckPermission(value = {"interviewer_add"}, mode = SaMode.AND)
    @SaCheckLogin
    @Log(title = "面试官管理", businessType = BusinessType.INSERT)
    public R add(@RequestBody BasicInterviewerEntity basicInterviewerEntity){
        int row=interviewerService.add(basicInterviewerEntity);
        if(row>0) {
            return R.ok();
        }else{
            return R.error("新增面试官失败");
        }
    }
    @PutMapping
    @SaCheckPermission(value = {"interviewer_update"}, mode = SaMode.AND)
    @SaCheckLogin
    @Log(title = "面试官管理", businessType = BusinessType.UPDATE)
    public R edit(@RequestBody BasicInterviewerEntity basicInterviewerEntity){
        int row=interviewerService.edit(basicInterviewerEntity);
        if(row>0) {
            return R.ok();
        }else{
            return R.error("修改面试官失败");
        }
    }
    @DeleteMapping
    @SaCheckPermission(value = {"interviewer_del"}, mode = SaMode.AND)
    @SaCheckLogin
    @Log(title = "面试官管理", businessType = BusinessType.DELETE)
    public R remove(@RequestBody InterviewerDeleteForm form){
        int row=interviewerService.remove(form.getInterviewerIds());
        if(row>0) {
            return R.ok();
        }else{
            return R.error("删除面试官失败");
        }
    }




}
