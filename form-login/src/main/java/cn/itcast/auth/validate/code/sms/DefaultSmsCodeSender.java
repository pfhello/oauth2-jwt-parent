package cn.itcast.auth.validate.code.sms;

import org.springframework.stereotype.Component;

@Component
public class DefaultSmsCodeSender implements SmsCodeSender {

    @Override
    public void send(String mobile, String code) {
        System.out.println("已向"+mobile+"发送了验证码:"+code);
    }
}
