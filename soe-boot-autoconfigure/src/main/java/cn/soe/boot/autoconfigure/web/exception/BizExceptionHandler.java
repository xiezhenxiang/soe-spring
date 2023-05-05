package cn.soe.boot.autoconfigure.web.exception;

import cn.soe.boot.core.bean.rest.ReturnT;
import cn.soe.boot.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiezhenxiang 2021/9/10
 */
@Slf4j
@RestControllerAdvice
public class BizExceptionHandler {

    /**
     * 自定义业务异常
     **/
    @ExceptionHandler(value = BizException.class)
    public ReturnT<Object> errorHandler(BizException ex) {
        printException(ex);
        return ReturnT.error(ex.getCode(), ex.getMessage(), ex.getClass());
    }

    /*@ExceptionHandler(value = AccessDeniedException.class)
    public ReturnT<Object> accessDeniedHandler(AccessDeniedException ex) {
        throw new BizException(HttpStatus.FORBIDDEN.value(), "无权限访问");
    }*/

    /**
     * 参数校验未通过异常 @RequestBody参数校验失败
     **/
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ReturnT<Object> errorHandler(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        List<String> errorArr = new ArrayList<>();
        for (ObjectError error : errors) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError)error;
                errorArr.add(String.format("%s,字段%s传入非法值:%s", fieldError.getDefaultMessage(), fieldError.getField(), fieldError.getRejectedValue()));
            } else {
                errorArr.add(String.format("%s,字段%s传入非法值", error.getDefaultMessage(), error.getObjectName()));
            }
        }
        String errMsg= String.join( ";", errorArr);
        printException(ex);
        return ReturnT.error(errMsg, MethodArgumentNotValidException.class);
    }

    /**
     * 参数校验未通过异常 @RequestParam 参数校验失败
     **/
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ReturnT<Object> errorHandler(ConstraintViolationException ex) {
        List<String> errorArr = new ArrayList<>();
        for (ConstraintViolation<?> constraint : ex.getConstraintViolations()) {
            errorArr.add(String.format("%s,字段%s传入非法值:%s", constraint.getMessage(), constraint.getPropertyPath(), constraint.getInvalidValue()));
        }
        String errMsg= String.join( ";", errorArr);
        printException(ex);
        return ReturnT.error(errMsg, ConstraintViolationException.class);
    }

    /**
     * 其他异常
     **/
    @ExceptionHandler(value = Throwable.class)
    public ReturnT<Object> errorHandler(Throwable ex) {
        printException(ex);
        return ReturnT.error(ex.getMessage(), ex.getClass());
    }

    private void printException(Throwable ex) {
        log.error("there is an exception interrupt", ex);
    }
}
/*
异常处理可以分为三种。
第一种是进入Controller方法前产生的异常，例如404。这种异常需要通过实现ErrorController来处理。
第二种是进入Controller但还未进行逻辑处理时产生的异常，例如参数数据类型错误。这种异常需要用@ControllerAdvice处理，建议继承ResponseEntityExceptionHandler来处理，该父类包括了很多已经被@ExceptionHandler 注解标识的方法，包括一些参数转换，请求方法不支持等类型等等。
第三种是进入Controller并进行逻辑处理时产生的异常，例如NullPointerException异常等。这种异常处理也可以用@ControllerAdvice来标识并进行处理，也建议继承ResponseEntityExceptionHandle 处理，可以用@ExceptionHandler自定义捕获的异常并处理。
以上三种情况都是restful的情况，结果会返回一个Json，如果希望返回跳转页面，则需要实现HandlerExceptionResolver类来进行异常处理并跳转。
*/