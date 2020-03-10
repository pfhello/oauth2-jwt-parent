package cn.itcast.auth.service;

import cn.itcast.auth.vo.UserJwt;

public interface UserService {

    UserJwt getUserByUsername(String username);

    UserJwt getUserByMobile(String mobile);
}
