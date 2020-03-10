package cn.itcast.auth.config;

import cn.itcast.auth.service.impl.UserDetailServiceImpl;
import cn.itcast.auth.validate.code.ImageCodeFilter;
import cn.itcast.auth.validate.code.sms.SmsCodeAuthenticationFilter;
import cn.itcast.auth.validate.code.sms.SmsCodeAuthenticationSecurityConfig;
import cn.itcast.auth.validate.code.sms.SmsCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailHandler authenticationFailHandler;

    @Autowired
    private ImageCodeFilter imageCodeFilter;

    @Autowired
    private SmsCodeFilter smsCodeFilter;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //将验证码校验过滤器加到用户名密码过滤器之前
            http.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(imageCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/userLogin.html")
                .loginProcessingUrl("/userLogin")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .rememberMe().rememberMeParameter("rememberMe")
                .and()
                .authorizeRequests().antMatchers("/userLogin.html","/code/*","/smsLogin").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().ignoringAntMatchers("/druid/*")
                .and()
                //加入sms配置类
                .apply(smsCodeAuthenticationSecurityConfig);
    }
}
