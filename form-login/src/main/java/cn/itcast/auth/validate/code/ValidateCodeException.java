package cn.itcast.auth.validate.code;

//在认证过程中的异常
public class ValidateCodeException extends org.springframework.security.core.AuthenticationException {

    public ValidateCodeException(String detail) {
        super(detail);
    }
}
