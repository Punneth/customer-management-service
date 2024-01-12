package com.albanero.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {

    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMessage;

    public ValidationException(String errorMessage, String errorCode, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
