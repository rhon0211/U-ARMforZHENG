package ucl.group.excelSystem.api.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.group.excelSystem.api.controller.form.*;
import ucl.group.excelSystem.api.db.pojo.Userv2Entity;
import ucl.group.excelSystem.api.db.pojo.vo.Userv2VO;
import ucl.group.excelSystem.api.service.Userv2Service;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.R;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@RestController
@RequestMapping("/api/v2/user")
public class Userv2Controller {

    @Autowired
    private Userv2Service userv2Service;

    // 新增用户
    @PostMapping("/add")
    public R addUser(@RequestBody @Valid InsertUserForm form) {
        Userv2Entity userv2Entity = Userv2Entity.builder()
                .name(form.getUserName())
                .katakana(form.getKatakana())
                .email(form.getEmail())
                .type(form.getType())
                .code(form.getEmployeeCode())
                .phone(form.getPhoneNumber())
                .active(form.getStatus())
                .remark(form.getRemark())
                .account(form.getAccount())
                .password(form.getPassword())
                .build();
        userv2Service.insertUser(userv2Entity);
        return R.ok();
    }

    // 更新用户
    @PutMapping("/update")
    public R updateUser(@RequestBody @Valid UpdateUserForm form) {
        Userv2Entity userv2Entity = Userv2Entity.builder()
                .userId(form.getUserId())
                .name(form.getUserName())
                .katakana(form.getKatakana())
                .email(form.getEmail())
                .type(form.getRole())
                .code(form.getEmployeeCode())
                .phone(form.getPhoneNumber())
                .active(form.getActive())
                .remark(form.getRemark())
                .build();
        userv2Service.updateUser(userv2Entity);
        return R.ok();
    }

    // 删除用户
    @DeleteMapping("/delete/{id}")
    public R deleteUser(@RequestBody @Valid DeleteUserForm form) {
        userv2Service.deleteUser(form.getUserId());
        return R.ok();
    }

    // 修改密码
    @PutMapping("/updatePassword")
    public R updatePassword(@RequestBody @Valid UpdatePasswordForm form) {
        boolean isVerified = userv2Service.verifyPassword(form.getUserId(), form.getOldPassword());
        if (!isVerified) {
            return R.error("旧パスワードが間違っています");
        }
        userv2Service.updatePassword(form.getUserId(), form.getPassword());
        return R.ok();
    }

    // 按 ID 查询用户详情
    @GetMapping("/getUserById")
    public R getUserById(@PathVariable Long userId) {
        Userv2VO userDetail = userv2Service.getUserById(userId);
        return R.ok().put("result", userDetail);
    }

    // 校验密码
    @GetMapping("/verify-password")
    public R verifyPassword(@RequestParam Long userId, @RequestParam String password) {
        boolean isValid = userv2Service.verifyPassword(userId, password);
        return R.ok().put("result", isValid);
    }


    // 分页查询用户列表
    @GetMapping("/list")
    public R queryUsersByPage(@Valid SelectUserByPageForm form) {
        Map<String, Object> param = new HashMap<>();
        param.put("type", form.getType());
        param.put("name", form.getName());
        param.put("start", (form.getPage() - 1) * form.getLength());
        param.put("length", form.getLength());
        List<Userv2VO> userv2VOS = userv2Service.queryUsersByPage(param);
        long l = userv2Service.queryUsersByPageCount(param);

        PageUtils pageUtils = new PageUtils(
                userv2VOS,
                l,
                form.getPage(),
                form.getLength()
        );
        return R.ok().put("result", pageUtils);
    }

}
