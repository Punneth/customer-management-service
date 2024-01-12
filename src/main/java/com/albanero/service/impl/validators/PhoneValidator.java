package com.albanero.service.impl.validators;

import com.albanero.constants.ErrorCode;
import com.albanero.dto.CustomerRequest;
import com.albanero.exceptions.ValidationException;
import com.albanero.service.Validator;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class PhoneValidator implements Validator {

    private static final String PHONE_PATTERN = "^(\\+91[\\-\\s]?)?[789]\\d{9}$";

    @Override
    public void doValidate(CustomerRequest customerRequest) {
        String phone = customerRequest.getPhone();
        if (StringUtils.isBlank(phone) || !phone.matches(PHONE_PATTERN)) {

            throw new ValidationException(ErrorCode.PHONE_VALIDATION_FAILED.getErrorMessage(),
                    ErrorCode.PHONE_VALIDATION_FAILED.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
    }
}
