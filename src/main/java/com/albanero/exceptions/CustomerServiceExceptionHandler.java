package com.albanero.exceptions;

import com.albanero.constants.ErrorCode;
import com.albanero.pojo.CustomerError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomerServiceExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CustomerError> handleValidationException(ValidationException validationException) {
        CustomerError customerError = CustomerError.builder().errorCode(validationException.getErrorCode())
                .errorMessage(validationException.getErrorMessage()).build();
        return new ResponseEntity<>(customerError, validationException.getHttpStatus());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<CustomerError> handleException(AccessDeniedException accessDeniedException) {
        CustomerError customerError = CustomerError.builder().errorCode(ErrorCode.GENERIC_EXCEPTION.getErrorCode())
                .errorMessage(accessDeniedException.getMessage()).build();
        return new ResponseEntity<>(customerError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomerError> handleGenericException(Exception ex) {
        CustomerError customerError = CustomerError.builder()
                .errorCode(ErrorCode.GENERIC_EXCEPTION.getErrorCode())
                .errorMessage(ErrorCode.GENERIC_EXCEPTION.getErrorMessage())
                .build();
        return new ResponseEntity<>(customerError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
