package cn.itcast.auth.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private String msg;
    private int code;
    private Object data;

    public Result(String msg, int code, Object data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public static Result success(){
        return new Result("success",0,null);
    }

    public static Result success(Object data){
        return new Result("success",0,data);
    }

    public static Result fail(CodeMsg codeMsg){
        return new Result(codeMsg.getMsg(),codeMsg.getCode(),null);
    }
}
