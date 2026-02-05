package org.example.techstore.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final StatusCode statusCode;
    private final Object data;

    public AppException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
        this.data = null;
    }

    public AppException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.data = null;
    }

    public AppException(StatusCode statusCode, String message, Object data) {
        super(message);
        this.statusCode = statusCode;
        this.data = data;
    }
}