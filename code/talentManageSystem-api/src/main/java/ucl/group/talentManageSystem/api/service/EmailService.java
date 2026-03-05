package ucl.group.talentManageSystem.api.service;

import org.springframework.messaging.MessagingException;
import ucl.group.talentManageSystem.api.db.pojo.EmailRegisterEntity;

public interface EmailService {
    String generateVerificationCode(String email,int length);
    void sendEmail(String to,String code) throws MessagingException;
    //给系统管理员和超级管理员发送邮件
    void sendRegisterEmail(String to, EmailRegisterEntity entity) throws MessagingException;
    //给注册的用户发送邮件
    void sendRegisterUserEmail(String to, EmailRegisterEntity entity) throws MessagingException;
}
