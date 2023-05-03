package cn.soe.boot.core.exception;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BizException extends RuntimeException {

    protected int code;

    public BizException(String msg) {
        super(msg);
        this.code = 500;
    }

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(Pair<Integer, String> codeMsg) {
        super(codeMsg.getValue());
        this.code = codeMsg.getKey();
    }

    public BizException(int errCode, String message, Throwable cause) {
        super(message, cause);
        this.code = errCode;
    }

    public BizException(Pair<Integer, String> codeMsg, Throwable cause) {
        super(codeMsg.getValue(), cause);
        this.code = codeMsg.getKey();
    }

    public static BizException of(Pair<Integer, String> codeMsg) {
        return new BizException(codeMsg);
    }
}