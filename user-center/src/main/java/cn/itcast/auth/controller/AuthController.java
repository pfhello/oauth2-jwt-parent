package cn.itcast.auth.controller;

import cn.itcast.auth.commons.UserKey;
import cn.itcast.auth.exception.GlobalException;
import cn.itcast.auth.pojo.AuthToken;
import cn.itcast.auth.result.CodeMsg;
import cn.itcast.auth.result.Result;
import cn.itcast.auth.service.AuthService;
import cn.itcast.auth.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class AuthController {

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    @Autowired
    private AuthService authService;

    @PostMapping("/userLogin")
    public Result login(String username, String password, HttpServletResponse response){
        if (username==null){
            throw new GlobalException(CodeMsg.USERNAME_NOT_EMPTY);
        }
        if (password==null){
            throw new GlobalException(CodeMsg.PASSWORD_NOT_EMPTY);
        }
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);
        String token=authToken.getToken();
        CookieUtil.addCookie(response,cookieDomain,"/", UserKey.USER_TOKEN,token,-1,false);
        return Result.success(token);
    }

    @GetMapping("/userLogout")
    public Result logout(HttpServletResponse response, @CookieValue(UserKey.USER_TOKEN) String token){
        CookieUtil.addCookie(response,cookieDomain,"/",UserKey.USER_TOKEN,"",0,false);
        boolean logout = authService.logout(token);
        if (logout){
            return Result.success();
        }
        return Result.fail(CodeMsg.LOGOUT_FAIL);
    }

    @GetMapping("/userJwt")
    public Result jwt(HttpServletRequest request){
        String token = CookieUtil.getCookieValue(request, UserKey.USER_TOKEN);
        AuthToken authToken = authService.getToken(token);
        return Result.success(authToken.getAccess_token());
    }
}
