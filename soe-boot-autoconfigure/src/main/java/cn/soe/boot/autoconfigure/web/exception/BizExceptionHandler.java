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
                errorArr.add(fieldError.getField() + fieldError.getDefaultMessage());
            } else {
                errorArr.add(error.getObjectName() + error.getDefaultMessage());
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
            errorArr.add("字段:" + constraint.getPropertyPath() + ", 非法值:" + constraint.getInvalidValue() + "," + constraint.getMessage());
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
