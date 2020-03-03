package cn.itcast.auth.service;

import cn.itcast.auth.exception.GlobalException;
import cn.itcast.auth.result.CodeMsg;
import cn.itcast.auth.vo.UserJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService{

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserJwt userJwt = userService.getUserByUsername(username);
        if (userJwt==null){
            throw  new GlobalException(CodeMsg.USER_NOT_EXIST);
        }
        return userJwt;
    }
}
