package cn.itcast.auth.validate.code.sms;

import cn.itcast.auth.config.MyAuthenticationFailHandler;
import cn.itcast.auth.config.MyAuthenticationSuccessHandler;
import cn.itcast.auth.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
//将编写的sms配置到security中
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity>{

    @Autowired
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailHandler authenticationFailHandler;

    @Autowired
    private UserDetailServiceImpl userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeAuthenticationFilter authenticationFilter=new SmsCodeAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailHandler);
        SmsCodeAuthenticationProvider authenticationProvider=new SmsCodeAuthenticationProvider();
        authenticationProvider.setUserDetailService(userDetailsService);
        http.authenticationProvider(authenticationProvider)
            .addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
