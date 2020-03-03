package cn.itcast.zuul.service;

import cn.itcast.auth.commons.UserKey;
import cn.itcast.auth.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String getToken(HttpServletRequest request){
        return CookieUtil.getCookieValue(request, UserKey.USER_TOKEN);
    }

    public long getExplire(String token){
        String authToken=UserKey.USER_TOKEN+":"+token;
        return stringRedisTemplate.getExpire(authToken, TimeUnit.SECONDS);
    }

    public String getJwtFromHeader(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(authorization)&&authorization.startsWith("Bearer")){
            return authorization;
        }
        return null;
    }
}
