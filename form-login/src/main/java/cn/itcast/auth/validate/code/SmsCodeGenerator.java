package cn.itcast.auth.validate.code;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Value("${sms.code.expireTime}")
    private int expireTime;

    @Override
    public ValidateCode generate(HttpServletRequest request) {
        String code = RandomStringUtils.randomNumeric(6);
        return new ValidateCode(code,expireTime);
    }
}
