package cn.itcast.auth.validate.code.sms;

import cn.itcast.auth.config.MyAuthenticationFailHandler;
import cn.itcast.auth.validate.code.CodeKey;
import cn.itcast.auth.validate.code.ImageCode;
import cn.itcast.auth.validate.code.ValidateCode;
import cn.itcast.auth.validate.code.ValidateCodeException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
//在spring中，filter都默认继承OncePerRequestFilter
//OncePerRequestFilter顾名思义，他能够确保在一次请求只通过一次filter，而不需要重复执行。
public class SmsCodeFilter extends OncePerRequestFilter implements InitializingBean{

    //登录失败处理拦截器
    @Autowired
    private MyAuthenticationFailHandler authenticationFailHandler;

    //要拦截校验的url
    @Value("${sms.code.url}")
    private String urls;

    private Set<String> configUrls=new HashSet<>();

    //匹配字符串如/user/*
    private AntPathMatcher pathMatcher=new AntPathMatcher();

    //初始化configUrls
    @Override
    public void afterPropertiesSet() throws ServletException {
        String[] split = urls.split(",");
        if (!StringUtils.isEmpty(split)){
            Arrays.stream(split).forEach(s->{
                configUrls.add(s);
            });
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean action=false;
        for (String url:configUrls){
            if (pathMatcher.match(url,request.getRequestURI())){
                action=true;
                break;
            }
        }
        if (action){
            try {
                validate(request);
            } catch (ValidateCodeException e) {
                authenticationFailHandler.onAuthenticationFailure(request,response,e);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

    private void validate(HttpServletRequest request){
        HttpSession session = request.getSession();
        ValidateCode smsCode = (ValidateCode) session.getAttribute(CodeKey.SMS_CODE);
        String code = request.getParameter("smsCode");
        if (smsCode==null){
            throw new ValidateCodeException("验证码不存在");
        }
        if (StringUtils.isEmpty(code)){
            throw new ValidateCodeException("验证码不能为空");
        }
        if (!code.equals(smsCode.getCode())){
            throw new ValidateCodeException("验证码错误");
        }
        if (smsCode.isExpried()){
            throw new ValidateCodeException("验证码已过期");
        }
        session.removeAttribute(CodeKey.SMS_CODE);
    }
}
