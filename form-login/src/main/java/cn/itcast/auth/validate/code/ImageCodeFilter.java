package cn.itcast.auth.validate.code;

import cn.itcast.auth.config.MyAuthenticationFailHandler;
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
public class ImageCodeFilter extends OncePerRequestFilter implements InitializingBean{

    //登录失败处理拦截器
    @Autowired
    private MyAuthenticationFailHandler authenticationFailHandler;

    //要拦截校验的url
    @Value("${image.code.url}")
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
        ImageCode imageCode = (ImageCode) session.getAttribute(CodeKey.IMAGE_CODE);
        String code = request.getParameter("imageCode");
        if (imageCode==null){
            throw new ValidateCodeException("验证码不存在");
        }
        if (StringUtils.isEmpty(code)){
            throw new ValidateCodeException("验证码不能为空");
        }
        if (!code.equals(imageCode.getCode())){
            throw new ValidateCodeException("验证码错误");
        }
        if (imageCode.isExpried()){
            throw new ValidateCodeException("验证码已过期");
        }
        session.removeAttribute(CodeKey.IMAGE_CODE);
    }
}
