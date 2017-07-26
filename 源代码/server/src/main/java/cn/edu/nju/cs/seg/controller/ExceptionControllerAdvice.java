package cn.edu.nju.cs.seg.controller;

import cn.edu.nju.cs.seg.exception.BusinessException;
import cn.edu.nju.cs.seg.json.ErrorResponseMapBuilder;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Created by fwz on 2017/5/31.
 */

@RestControllerAdvice
public class ExceptionControllerAdvice {
    private Logger logger = Logger.getLogger(this.getClass());
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException e) {
        Map<String, Object> map = ErrorResponseMapBuilder.build(e.getMsg());
        System.out.println("ERROR MESSAGE: " + e.getMsg());
        logger.error("ERROR MESSAGE: " + e.getMsg());
        return new ResponseEntity<Map<String, Object>>(map, e.getHttpStatus());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> map = ErrorResponseMapBuilder.build(e.getMessage());
        System.out.println("ERROR MESSAGE: " + e.getMessage());
        logger.error("ERROR MESSAGE: " + e.getMessage());
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.SERVICE_UNAVAILABLE);
    }
}

