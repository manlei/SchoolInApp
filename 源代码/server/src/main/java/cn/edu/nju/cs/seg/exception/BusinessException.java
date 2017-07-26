package cn.edu.nju.cs.seg.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by fwz on 2017/5/31.
 */
public class BusinessException extends RuntimeException {
    private String msg;
    private HttpStatus httpStatus;
    public BusinessException(String msg, HttpStatus httpStatus) {
        this.msg = msg;
        this.httpStatus = httpStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}

