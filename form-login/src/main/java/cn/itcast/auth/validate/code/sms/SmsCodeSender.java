package cn.itcast.auth.validate.code.sms;

public interface SmsCodeSender {

    void send(String mobile,String code);
}
