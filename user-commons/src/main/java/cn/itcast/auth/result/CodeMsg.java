package cn.itcast.auth.result;

public enum CodeMsg {

    //通用错误
    SERVER_ERROR(5000100,"服务端异常"),

    //用户登录模块
    USERNAME_NOT_EMPTY(500200,"用户名不能为空"),
    PASSWORD_NOT_EMPTY(500201,"密码不能为空"),
    PASSWORD_ERROR(500202,"密码错误"),
    APPLY_TOKEN_ERROR(500203,"申请令牌错误"),
    SAVE_TOKEN_ERROR(500204,"令牌保存异常"),
    LOGOUT_FAIL(500205,"退出失败"),
    USER_NOT_EXIST(500206,"用户不存在"),
    TOKEN_TIMEOUT(500207,"令牌已失效"),
    USER_NOT_LOGIN(500208,"您还没有登录")
    ;

    private String msg;
    private int code;

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    CodeMsg(int code,String msg) {
        this.msg = msg;
        this.code = code;
    }
}
