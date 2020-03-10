package cn.itcast.auth.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LoginController {

    @GetMapping("/userLogin.html")
    public String login(){
        return "login";
    }

    @GetMapping(value = {"/","/index.html"})
    public String index(){
        return "index";
    }

    //得到用户认证信息
    @GetMapping("/me")
    @ResponseBody
    public Object getCurrentUser(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
