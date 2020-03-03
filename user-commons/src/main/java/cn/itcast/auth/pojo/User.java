package cn.itcast.auth.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable{

    private String id;

    private String username;

    private String password;

    private String salt;

    private String name;

    private String userpic;

    private String utype;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String sex;

    private String email;

    private String phone;

    private String qq;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
