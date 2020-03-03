package cn.itcast.auth.exception;

import cn.itcast.auth.result.CodeMsg;

public class GlobalException extends RuntimeException {

    private CodeMsg codeMsg;

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }

    public GlobalException(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }
}
