package org.example.techstore.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.techstore.dto.response.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateKey(DataIntegrityViolationException e) {
        // Log lỗi để mình biết là có trùng thật
        log.error("Duplicate Key: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.builder()
                        .code(400)
                        .message("Mã hệ thống bị trùng lặp hoặc dữ liệu đã tồn tại, vui lòng thử lại!")
                        .build()
        );
    }
}
