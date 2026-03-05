package ucl.group.talentManageSystem.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.R;
import ucl.group.talentManageSystem.api.controller.form.*;
import ucl.group.talentManageSystem.api.db.pojo.UserEntity;
import ucl.group.talentManageSystem.api.service.UserManageService;
import ucl.group.talentManageSystem.api.service.UserService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserManageController {

    @Resource
    private UserManageService userManageService;
    @Autowired
    private UserService userService;
    //注册用户
    @PostMapping
    @SaCheckLogin
    @SaCheckPermission(value = { "supadm_search", "ordadm_create", "user_create"}, mode = SaMode.OR)
    public R registerUser(@RequestBody @Valid UserManageRegisterForm form) {
        if (form.getStatus()==null || form.getStatus().equals("")) form.setStatus("0");
        UserEntity userEntity = BeanUtil.toBean(form, UserEntity.class);
        //userEntity.setCode(IdUtil.simpleUUID().toUpperCase());
        if(userService.judgeExist(form.getEmail(), form.getSystemCode())){
            return  R.error("该用户邮件已存在，不可重复创建");
        }

        userManageService.insertUser(userEntity);
        return R.ok();
    }

    @PostMapping("/list")
    @SaCheckLogin
    @SaCheckPermission(value = { "supadm_search", "ordadm_search", "user_search"}, mode = SaMode.OR)
    public R selectUser(@RequestBody @Valid UserManageSelectForm form) {
        if (form.getStatus()==null || form.getStatus().equals("")) form.setStatus("0");
        Map param = BeanUtil.beanToMap(form);
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        param.put("start", start);
        PageUtils pageUtils = userManageService.userSearchByPage(param);

        return R.ok().put("result", pageUtils);
    }
    //修改之前，根据id询用户信息
    @PostMapping("/updateSearchById")
    @SaCheckLogin
    @SaCheckPermission(value = { "supadm_search", "ordadm_search", "user_search"}, mode = SaMode.OR)
    public R updateSearchById(@RequestBody @Valid UserManageSearchByIdForm form) {
        HashMap map = userManageService.searchById(form.getUserId());
        return R.ok().put("result",map);
    }

    //修改用户
    @PutMapping
    @SaCheckLogin
    @SaCheckPermission(value = { "supadm_update", "ordadm_update", "ordadm_disable", "user_update", "user_disable"}, mode = SaMode.OR)
    public R modifyUser(@RequestBody @Valid UserManageModifyForm form) {
        UserEntity entity = BeanUtil.toBean(form, UserEntity.class);
        userManageService.updateUser(entity);
        return R.ok();
    }

    @DeleteMapping
    @SaCheckLogin
    @SaCheckPermission(value = { "supadm_delete", "ordadm_delete", "user_delete"}, mode = SaMode.OR)
    public R deleteUsers(@RequestBody @Valid UserManageDeleteForm form) {
        userManageService.userDeletes(form.getIds());
        return R.ok();
    }

    @PostMapping("/disable")
    @SaCheckLogin
    @SaCheckPermission(value = { "supadm_disable", "ordadm_disable", "user_disable"}, mode = SaMode.OR)
    public R disableUser(@RequestBody @Valid UserManageDisableForm form) {
        userManageService.userDisable(form.getIds());
        return R.ok();
    }
    @PostMapping("/enable")
    @SaCheckLogin
    @SaCheckPermission(value = { "supadm_disable", "ordadm_disable", "user_disable"}, mode = SaMode.OR)
    public R enableUser(@RequestBody @Valid UserManageDisableForm form) {
        userManageService.userEnable(form.getIds());
        return R.ok();
    }

    @GetMapping("/allUser")
    @SaCheckLogin
    public R allUser() {
        ArrayList<HashMap> map = userManageService.userSelect();
        return R.ok().put("result", map);
    }

    @GetMapping("allUserType")
    @SaCheckLogin
    public R allUserType() {
        ArrayList<HashMap> map = userManageService.userTypeSelect();
        return R.ok().put("result", map);
    }

    @GetMapping("allUserType/ex")
    @SaCheckLogin
    public R allexUserType() {
        ArrayList<HashMap> map = userManageService.exuserTypeSelect();
        return R.ok().put("result", map);
    }

    @PostMapping("/list/ex")
    @SaCheckLogin
    @SaCheckPermission(value = { "supadm_search", "ordadm_search", "user_search"}, mode = SaMode.OR)
    public R selectexUser(@RequestBody @Valid UserManageSelectForm form) {
        if (form.getStatus()==null || form.getStatus().equals("")) form.setStatus("0");
        Map param = BeanUtil.beanToMap(form);
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        param.put("start", start);
        PageUtils pageUtils = userManageService.exuserSearchByPage(param);

        return R.ok().put("result", pageUtils);
    }
}
