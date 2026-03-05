package ucl.group.talentManageSystem.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucl.group.talentManageSystem.api.common.R;
import ucl.group.talentManageSystem.api.common.constants.CacheConstants;
import ucl.group.talentManageSystem.api.controller.form.EmailRequestForm;
import ucl.group.talentManageSystem.api.service.EmailService;
import ucl.group.talentManageSystem.api.service.RedisService;
import ucl.group.talentManageSystem.api.service.UserService;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    @Resource
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Resource
    private RedisService redisService;

    @PostMapping("/user/emailCode")
    public R sendVerificationCode(@Valid @RequestBody EmailRequestForm emailRequestForm) {
        String email = emailRequestForm.getEmail();
        int systemCode = emailRequestForm.getSystemCode();
        if (email == null || email.trim().isEmpty()) {
            // 如果电子邮件地址为 null 或空，则返回错误响应
            return R.error( "メールアドレスを空にすることはできません");
        }
        if (!userService.judgeExist(email, systemCode)) {
            //不存在则执行错误提示
            //邮件用户不存在
            return R.error("メールユーザーが存在しません");
        }
        String verificationCode = emailService.generateVerificationCode(email, 6);

        long expirationTime = 300;
        // 存储验证码到Redis
        redisService.setCacheObject(getCacheKey(email), verificationCode);
        //设置有效时间300s
        redisService.expire(getCacheKey(email), expirationTime);
        // 也存储语言设置到Redis，使用一个不同的key
        emailService.sendEmail(email, verificationCode);

        // 构造并返回响应
        return R.ok().put("code", HttpStatus.OK.value()).put("msg", "邮件发送成功");
    }

    private String getCacheKey(String configKey) {
        return CacheConstants.EMAIL_CODE_KEY + configKey;
    }

    private String getLangCacheKey(String configKey) {
        return CacheConstants.Language_user_KEY + configKey;
    }
}
