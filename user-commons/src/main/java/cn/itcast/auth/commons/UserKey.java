package cn.itcast.auth.commons;

public interface UserKey {

    String USER_TOKEN="user_token";
    //token保存在redis中的过期时间
    long USER_TOKEN_TIMEOUT=1200;
}
