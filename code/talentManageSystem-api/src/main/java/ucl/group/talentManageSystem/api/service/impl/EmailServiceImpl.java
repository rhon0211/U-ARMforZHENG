package ucl.group.talentManageSystem.api.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import ucl.group.talentManageSystem.api.db.pojo.EmailRegisterEntity;
import ucl.group.talentManageSystem.api.service.EmailService;
import ucl.group.talentManageSystem.api.service.RedisService;

import javax.annotation.Resource;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private RedisService redisService;
    @Value("${spring.mail.username}")
    private String emailSender;
    @Override
    public String generateVerificationCode(String email,int length){
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }

@Override
    public void sendEmail(String to,String code)throws MessagingException{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSender);
        message.setTo(to);
        message.setSubject("您的验证码");
        message.setText("这是您的验证码: " + code);

        // 2. 【核心修改】注释掉会导致报错的发信动作，改为控制台打印
        // mailSender.send(message);

        System.out.println("========================================");
        System.out.println("【本地调试】正在向邮箱 [" + to + "] 发送验证码");
        System.out.println("【本地调试】当前生成的验证码为: " + code);
        System.out.println("========================================");
    }

    @Override
    public void sendRegisterEmail(String to, EmailRegisterEntity entity) throws MessagingException {

        String usertypename = null;
        if (entity.getUsertype().equals("1")) usertypename = "スーパー管理者";
        else if (entity.getUsertype().equals("2")) usertypename = "普通の管理者";
        else usertypename = "ユーザー";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSender);
        message.setTo(to);
        message.setSubject("人材管理-新規ユーザー作成通知");

        if (entity.getSearchname().equals(entity.getCurrentname()))
            message.setText(entity.getSearchname() + "様：\n"
                          + "現在、あなたはシステムユーザーを作った。\n"
                          + "名前は：" + entity.getUsername() + "；\n"
                          + "権限は：" + usertypename + "。\n"
                          + "以上です、どうぞよろしくお願いいたします。");
        else
            message.setText(entity.getSearchname() + "様：\n"
                          + "現在、"  + entity.getCurrentname() + "様はシステムユーザーを作った。"
                          + "名前は：" + entity.getUsername() + "；\n"
                          + "権限は：" + usertypename + "。\n"
                          + "以上です、どうぞよろしくお願いいたします。");
        mailSender.send(message);
    }
    @Override
    public void sendRegisterUserEmail(String to, EmailRegisterEntity entity) throws MessagingException {

        String usertypename = null;
        if (entity.getUsertype().equals("1")) usertypename = "スーパー管理者";
        else if (entity.getUsertype().equals("2")) usertypename = "普通の管理者";
        else usertypename = "ユーザー";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSender);
        message.setTo(to);
        message.setSubject("人材管理-新規ユーザー作成通知");
        message.setText(entity.getUsername() + "様：\n"
                + "人材管理システムユーザーを作った。\n"
                + "権限は：" + usertypename + "。\n"
                + "以上です、どうぞよろしくお願いいたします。");
        mailSender.send(message);
        }
}
