package cn.itcast.auth.service;

import cn.itcast.auth.commons.ServerList;
import cn.itcast.auth.commons.UserKey;
import cn.itcast.auth.exception.GlobalException;
import cn.itcast.auth.pojo.AuthToken;
import cn.itcast.auth.result.CodeMsg;
import cn.itcast.auth.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    public AuthToken login(String username,String password,String clientId,String clientSecret){
        AuthToken authToken = applyToken(username, password, clientId, clientSecret);
        boolean saveToken = saveToken(authToken);
        if (!saveToken){
            throw new GlobalException(CodeMsg.SAVE_TOKEN_ERROR);
        }
        return authToken;
    }

    private AuthToken applyToken(String username,String password,String clientId,String clientSecret){
        String authUrl="http://"+ ServerList.USER_CENTER+"/oauth/token";
        //1.header
        MultiValueMap<String,String> headers=new LinkedMultiValueMap<>();
        String httpBasic = httpBasic(clientId, clientSecret);
        headers.add("Authorization",httpBasic);
        //2.body
        MultiValueMap<String,String> body=new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);
        HttpEntity<MultiValueMap<String,String>> httpEntity=new HttpEntity<>(body,headers);
        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401时候也要正常响应，不要抛出异常
                if (response.getRawStatusCode()!=400&&response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
        Map map = exchange.getBody();
        if (map==null||map.get("access_token")==null
                ||map.get("refresh_token")==null
                || map.get("jti")==null){
            throw new GlobalException(CodeMsg.APPLY_TOKEN_ERROR);
        }
        AuthToken authToken=new AuthToken();
        authToken.setAccess_token((String) map.get("access_token"));
        authToken.setRefresh_token((String) map.get("refresh_token"));
        authToken.setToken((String) map.get("jti"));
        return authToken;
    }

    private boolean saveToken(AuthToken authToken){
        String token= UserKey.USER_TOKEN+":"+authToken.getToken();
        String content= JsonUtils.objectToJson(authToken);
        redisTemplate.opsForValue().set(token,content,UserKey.USER_TOKEN_TIMEOUT, TimeUnit.SECONDS);
        //得到过期时间
        Long expire = redisTemplate.getExpire(token);
        return expire>0;
    }

    private String httpBasic(String clientId,String clientSecret){
        //将客户端id和客户端密码拼接
        String str=clientId+":"+clientSecret;
        //进行base64编码
        byte[] encode = Base64.encode(str.getBytes());
        return "Basic "+new String(encode);
    }

    public boolean logout(String token){
        String authToken=UserKey.USER_TOKEN+":"+token;
        return redisTemplate.delete(authToken);
    }

    public AuthToken getToken(String token){
        String authToken=UserKey.USER_TOKEN+":"+token;
        String json = redisTemplate.opsForValue().get(authToken);
        if (json==null){
            throw new GlobalException(CodeMsg.TOKEN_TIMEOUT);
        }
        redisTemplate.expire(authToken,UserKey.USER_TOKEN_TIMEOUT,TimeUnit.SECONDS);
        return JsonUtils.jsonToPojo(json,AuthToken.class);
    }
}
