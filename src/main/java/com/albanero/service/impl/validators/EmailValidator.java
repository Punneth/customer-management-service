package com.albanero.service.impl.validators;

import com.albanero.constants.ErrorCode;
import com.albanero.dto.CustomerRequest;
import com.albanero.exceptions.ValidationException;
import com.albanero.service.Validator;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class EmailValidator implements Validator {

    private static final String EMAIL_PATTERN = "(?i)[-a-zA-Z0-9+_][-a-zA-Z0-9+_.]*@[-a-zA-Z0-9][-a-zA-Z0-9.]*\\.[a-zA-Z]{2,30}";

    @Override
    public void doValidate(CustomerRequest customerRequest) {
        String email = customerRequest.getEmail();
        if (StringUtils.isBlank(email) || !email.matches(EMAIL_PATTERN)) {

            throw new ValidationException(ErrorCode.EMAIL_VALIDATION_FAILED.getErrorMessage(),
                    ErrorCode.EMAIL_VALIDATION_FAILED.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
    }
}
