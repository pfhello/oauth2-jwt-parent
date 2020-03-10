package cn.itcast.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@MapperScan(basePackages = "cn.itcast.auth.mapper")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class FormLoginApp {
    public static void main(String[] args) {
        SpringApplication.run(FormLoginApp.class);
    }
}
