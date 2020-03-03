package cn.itcast.auth.service.impl;

import cn.itcast.auth.mapper.UserMapper;
import cn.itcast.auth.service.UserService;
import cn.itcast.auth.vo.UserJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserJwt getUserByUsername(String username) {
        List<UserJwt> userList = userMapper.getUserByUsername(username);
        if (userList!=null&&userList.size()>0){
            return userList.get(0);
        }
        return null;
    }

}
