package cn.itcast.auth.pojo;

import lombok.Data;

@Data
public class AuthToken {

    private String access_token;

    private String refresh_token;

    private String token;
}
