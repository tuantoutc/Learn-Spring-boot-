package com.springmatter.relearnspringboot.exception;


import com.springmatter.relearnspringboot.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public GlobalExceptionHandler() {
        super();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiResponse<Map<String, String>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error
                -> errors.put(error.getField(), error.getDefaultMessage()));
        return ApiResponse.errorListMessages(400, "Invalid input data", errors);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ApiResponse<Objects> handlerHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ApiResponse.error(400, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Objects>> handlerRuntimeException(RuntimeException ex) {
        ApiResponse<Objects> apiResponse = ApiResponse.error(400, ex.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Objects>> handlerMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ApiResponse<Objects> apiResponse = ApiResponse.error(400, ex.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

}
