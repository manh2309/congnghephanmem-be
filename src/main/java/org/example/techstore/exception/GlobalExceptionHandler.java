package org.example.techstore.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.techstore.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        log.warn(ex.getMessage(), ex);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(StatusCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message("Đã xảy ra lỗi")
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn(ex.getMessage(), ex);
        var errors = new LinkedHashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(StatusCode.INVALID_KEY.getCode())
                .message("Đã xảy ra lỗi")
                .errors(errors)
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Object>> handlingAppException(AppException exception) {
        log.warn(exception.getMessage(), exception);
        StatusCode statusCode = exception.getStatusCode();
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(statusCode.getCode())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(statusCode.getHttpStatusCode()).body(apiResponse);
    }
}
