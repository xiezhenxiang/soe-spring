package cn.soe.boot.core.bean.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * api return model
 * @since  xxl-job 1.0
 * @author xiezhenxiang 2020/1/7
 **/
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReturnT<T> implements Serializable  {

    private static final long serialVersionUID = 112L;
    public static final int SUCCESS_CODE = 200;
    private static final String SUCCESS_MSG = "ok";
    private static final int DEFAULT_ERROR_CODE = 500;
    private static final String DEFAULT_ERROR_MSG = "Undefined Error";

    private int code;
    private String msg;
    private T data;

    private ReturnT(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ReturnT<T> success() {
        return new ReturnT<>(SUCCESS_CODE, SUCCESS_MSG, null);
    }

    public static <T> ReturnT<T> success(T data) {
        return new ReturnT<>(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static <T> ReturnT<ReturnPage<T>> success(List<T> pageContent, long totalSize) {
        return success(ReturnPage.of(pageContent, totalSize));
    }

    public static <T> ReturnT<T> error() {
        return error(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MSG, null);
    }

    public static <T> ReturnT<T> error(String msg) {
        return error(DEFAULT_ERROR_CODE, msg, null);
    }

    public static <T> ReturnT<T> error(int code, String msg) {
        return error(code, msg, null);
    }

    public static <T> ReturnT<T> error(String msg, T data) {
        return error(DEFAULT_ERROR_CODE, msg, data);
    }

    public static <T> ReturnT<T> error(int code, String msg, T data) {
        return new ReturnT<>(code, msg, data);
    }
}
