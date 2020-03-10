package cn.itcast.auth.mapper;

import cn.itcast.auth.vo.UserJwt;

import java.util.List;

public interface UserMapper {

    List<UserJwt> getUserByUsername(String username);

    List<UserJwt> getUserByMobile(String mobile);

}