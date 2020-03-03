package cn.itcast.auth;

import cn.itcast.auth.commons.ServerList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientTest {


    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void clientTest(){
        String authUrl="http://"+ServerList.USER_CENTER+"/oauth/token";
        //URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType
        // url就是 申请令牌的url /oauth/token
        //method http的方法类型
        //requestEntity请求内容
        //responseType，将响应的结果生成的类型
        //请求的内容分两部分
        //1、header信息，包括了http basic认证信息
        MultiValueMap<String,String> headers=new LinkedMultiValueMap<>();
        String httpBasic = httpBasic("XcWebApp", "XcWebApp");
        headers.add("Authorization",httpBasic);
        //2、包括：grant_type、username、passowrd
        MultiValueMap<String,String> body=new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username","admin");
        body.add("password","1234");
        HttpEntity<MultiValueMap<String,String>> multiValueMapHttpEntity=new HttpEntity<>(body,headers);
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
        //远程调用申请令牌
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        Map body1 = exchange.getBody();
        System.out.println(body1);


    }

    private String httpBasic(String clientId,String clientSecret){
        //将客户端id和客户端密码拼接
        String str=clientId+":"+clientSecret;
        //进行base64编码
        byte[] encode = Base64.encode(str.getBytes());
        return "Basic "+new String(encode);
    }
}
