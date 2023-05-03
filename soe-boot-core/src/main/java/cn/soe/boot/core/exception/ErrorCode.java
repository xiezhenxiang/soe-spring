package cn.soe.boot.core.exception;

import javafx.util.Pair;

public class ErrorCode {

    public static final Pair<Integer, String> SUCCESS = new Pair<>(200, "请求成功");
    public static final Pair<Integer, String> BAD_REQUEST = new Pair<>(400, "无效的请求或参数有误");
    public static final Pair<Integer, String> ACCESS_DENY = new Pair<>(403, "重复提交");
    public static final Pair<Integer, String> NOT_FOUND = new Pair<>(404, "请求资源未找到");
    public static final Pair<Integer, String> FREQUENT_REQUEST = new Pair<>(405, "请求频率过高");
    public static final Pair<Integer, String> UNKNOWN_ERROR = new Pair<>(500, "未知错误");
}
