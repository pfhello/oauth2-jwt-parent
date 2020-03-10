package cn.itcast.auth.service.impl;

import cn.itcast.auth.service.UserService;
import cn.itcast.auth.vo.UserJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserJwt user = userService.getUserByUsername(username);
        return user;
    }

    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        UserJwt user = userService.getUserByMobile(mobile);
        return user;
    }

}
