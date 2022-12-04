package com.kqinfo.universal.common.exception;


import com.kqinfo.universal.common.response.ResultCode;

/**
 * @author Zijian Liao
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 8355761362309363832L;
    private final Integer code;

    public Integer getCode() {
        return code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = -1;
    }

    public BusinessException(String message) {
        this(-1, message);
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode status) {
        super(status.message());
        this.code = status.code();
    }

}
