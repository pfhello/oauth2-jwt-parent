package cn.itcast.auth.config;

import cn.itcast.auth.service.MyUserDetailService;
import cn.itcast.auth.vo.UserJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Autowired
    private MyUserDetailService userDetailService;

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String,Object> reponse=new LinkedHashMap<>();
        String name = authentication.getName();
        reponse.put("user_name",name);
        Object principal = authentication.getPrincipal();
        UserJwt userJwt;
        if (principal instanceof UserJwt){
            userJwt=(UserJwt)principal;
        }else {
            //refresh_token默认不去调用userdetailService获取用户信息，这里我们手动去调用，得到 UserJwt
             userJwt= (UserJwt) userDetailService.loadUserByUsername(name);
        }
        reponse.put("sex",userJwt.getSex());
        reponse.put("email",userJwt.getEmail());
        reponse.put("phone",userJwt.getPhone());
        if (authentication.getAuthorities()!=null&&!authentication.getAuthorities().isEmpty()){
            reponse.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return reponse;
    }
}
