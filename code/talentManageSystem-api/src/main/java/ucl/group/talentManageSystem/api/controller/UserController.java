package ucl.group.talentManageSystem.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.web.bind.annotation.*;
import ucl.group.talentManageSystem.api.common.R;
import ucl.group.talentManageSystem.api.common.constants.CacheConstants;
import ucl.group.talentManageSystem.api.controller.form.EmailLoginForm;
import ucl.group.talentManageSystem.api.db.pojo.UserEntity;
import ucl.group.talentManageSystem.api.service.RedisService;
import ucl.group.talentManageSystem.api.service.UserService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Resource
    private RedisService redisService;
    @Resource
    private UserService userService;

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public R login(@RequestBody @Valid EmailLoginForm loginForm) {
        Map param = BeanUtil.beanToMap(loginForm);
        UserEntity userEntity = userService.login(param);
        if (userEntity != null && (Integer) userEntity.getUserId() != null) {
            StpUtil.getSession().set("userName", userEntity.getName());
            StpUtil.getSession().set("userId", userEntity.getUserId());
            // 根据application.yml文件，token有效期是timeout: 2592000 即30天
            // 设置userId在token中
            StpUtil.login(userEntity.getUserId());
            String token = StpUtil.getTokenValue();
            List<String> permissions = StpUtil.getPermissionList();
            String lang = loginForm.getLang();
            redisService.setCacheObject(getLangCacheKey(String.valueOf(userEntity.getUserId())),
                    lang);
            return R.ok("登录成功").put("result", "true").put("token", token).put("permissions",
                    permissions);
        } else {
            // 如果登录失败，返回错误的R对象
            return R.error("ログインに失敗しました。メールアドレスまたは確認コードが間違っています").put("result", false);
        }
    }

    @GetMapping("/login/logout")
    @SaCheckLogin
    public R logout() {
        StpUtil.logout();
        return R.ok();
    }

    private String getLangCacheKey(String configKey) {
        return CacheConstants.Language_user_KEY + configKey;
    }
}
