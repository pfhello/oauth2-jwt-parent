package cn.itcast.auth;

import cn.itcast.auth.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;

import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserTest {

    //创建jwt令牌
    @Test
    public void createJwtTest(){
        //证书文件
        String key_location="xc.keystore";
        //密匙库密码
        String keyStore_password="xuecheng";
        //访问证书路径
        ClassPathResource resource=new ClassPathResource(key_location);
        //密匙工厂
        KeyStoreKeyFactory keyStoreKeyFactory=new KeyStoreKeyFactory(resource,keyStore_password.toCharArray());
        //密匙的密码,此密码要与别名匹配
        String key_password="xuecheng";
        //密匙别名
        String alias="xckey";
        //密匙对(公匙和私匙)
        KeyPair keyPair=keyStoreKeyFactory.getKeyPair(alias,key_password.toCharArray());
        //私匙
        RSAPrivateKey privateKey= (RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String,Object> info=new HashMap<>();
        info.put("name","貂蝉");
        info.put("age",17);
        info.put("id","10086");
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(JsonUtils.objectToJson(info), new RsaSigner(privateKey));
        //取出jwt令牌
        String token = jwt.getEncoded();
        System.out.println(token);
    }

    //解析jwt令牌
    @Test
    public void verifyTest(){
        String publickey ="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmIl3ncvgEVL7bfTgl3bVhymk3byCLkefDuVm/ZMGPJqPwu5xVXwtyn7AGpfg7JBlITMEzUqMt6cinKYEnnyJUM4jL9A1QxW8BtQMXRdFNVibNw5d1c4jT1lfVsS2PucSZqi8VRJ3lMdeiKkNc4rQ5z/g4ekaCzSJCxhh9jA5kAutGGacGsgrYDRf26Iw/QyPm2K2jmJoDsTd9lItY4vEDpQR3YS/wpuwPS8oA0X55O9x1qk4OG28DugmsYnLOV/VZ5elun4GlfDS2q9EmZMKyZJ5CtYnMOnC6c+j7P44YVKPi8PJWH2M0J2l3sQfex7kjMmLQsqkqjN8JUk5mgBKQQIDAQAB-----END PUBLIC KEY-----";
        String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJwaG9uZSI6bnVsbCwidXNlcl9uYW1lIjoiYWRtaW4iLCJzZXgiOiIxIiwic2NvcGUiOlsiYXBwIl0sImV4cCI6MTU4MzAzNDExOSwiZW1haWwiOm51bGwsImp0aSI6ImY1ZGQyOTI0LTBjOWEtNDRjNy04ZmNmLTY5MDhiMzhjNWFmYiIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.Vo9BtA_KcGpD45Gx07LtuvSFPZ3rc0OSyWUp5lQL3WU6hPcoptk2RBJqNyNDlilBwyZ5XKM7kyNZP9AUire5mhZyx8i8hi8tYkCa41uk2ANQ6TzUYfFtChZR3F7GX5xQXLgMCmqHYiIWrDmbX2EAR1F3UX3ao0haOQKHXCTGzvYFCeQaqQco3tqxw-KHY41O_UlfAxqx2z-2UgI2vitUk6VCD3RH14azcDJ7C7--uq1bXEhYpHCYNNqR24Zzqif5V3yuTdkHmjUj5_Lqyh7tvcOnfMY20xt1YabwGZJU5uIuHPHm_3YCNj_BhSPCsjIlDINgD0QOfoczMP7AzNn6Fg";
        //校验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));
        //获取jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
    }

    @Test
    public void pwTest(){
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("1234"));
    }
}
