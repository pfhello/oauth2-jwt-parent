package cn.itcast.auth.exception;

import cn.itcast.auth.result.CodeMsg;
import cn.itcast.auth.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handler(Exception e){
        if (e instanceof GlobalException){
            GlobalException ge=(GlobalException) e;
            log.error(ge.getCodeMsg().getMsg(),ge);
            return Result.fail(ge.getCodeMsg());
        }
        log.error(e.getMessage(),e);
        return Result.fail(CodeMsg.SERVER_ERROR);
    }
}
