package cn.itcast.resource.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Configuration
@EnableResourceServer
//激活方法上的注解
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled =true)
@Slf4j
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //公钥
    public static final String PUBLIC_KEY="publickey.txt";

    //定义tokenStore
    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    //JwtAccessTokenConverter
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter=new JwtAccessTokenConverter();
        converter.setVerifierKey(getPublicKey());
        return converter;
    }

    //获取公钥
    private String getPublicKey(){
        Resource resource=new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader isr=new InputStreamReader(resource.getInputStream());
            BufferedReader br=new BufferedReader(isr);
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //所有请求必须认证通过
        http.authorizeRequests().anyRequest().authenticated();
    }

}
