package cn.itcast.auth.validate.code.sms;

import cn.itcast.auth.service.impl.UserDetailServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
//实现AuthenticationProvider
public class SmsCodeAuthenticationProvider implements AuthenticationProvider{

    private UserDetailServiceImpl userDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken=(SmsCodeAuthenticationToken) authentication;
        UserDetails user = userDetailService.loadUserByMobile((String) authentication.getPrincipal());
        if (user==null){
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        SmsCodeAuthenticationToken authenticationResult=new SmsCodeAuthenticationToken(user,user.getAuthorities());
        //将未认证的authenticationToken的Details设置到已认证的token中
        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    //支持的认证类型token
    @Override
    public boolean supports(Class<?> aClass) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

    public UserDetailServiceImpl getUserDetailService() {
        return userDetailService;
    }

    public void setUserDetailService(UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }
}