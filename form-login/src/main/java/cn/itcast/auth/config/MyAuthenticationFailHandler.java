package cn.itcast.auth.config;

import cn.itcast.auth.util.JsonUtils;
import cn.itcast.auth.validate.code.ValidateCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MyAuthenticationFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.info("登录失败");
        log.info(e.getMessage(),e);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String,Object> map = new HashMap<>();
        if(e instanceof LockedException){
            map.put("message","账户被锁定，登录失败!");
        }else if(e instanceof BadCredentialsException){
            map.put("message","账户名或密码输入错误，登录失败!");
        }else if(e instanceof DisabledException){
            map.put("message","账户被禁用，登录失败!");
        }else if(e instanceof AccountExpiredException){
            map.put("message","账户已过期，登录失败!");
        }else if(e instanceof CredentialsExpiredException){
            map.put("message","密码已过期，登录失败!");
        }else if (e instanceof ValidateCodeException){
            map.put("message",e.getMessage());
        } else{
            map.put("message","登录失败!");
        }
        response.getWriter().write(JsonUtils.objectToJson(map));
    }
}
