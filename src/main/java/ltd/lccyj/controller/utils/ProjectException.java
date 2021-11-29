package ltd.lccyj.controller.utils;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * SpringMVC异常处理器
 * @author 刘淳
 */
@RestControllerAdvice
public class ProjectException {

    //异常处理
    @ExceptionHandler
    public Result doException(Exception ex){
        ex.printStackTrace();
        return new Result(false,null,"服务器出现故障，请稍后再试");
    }
}
