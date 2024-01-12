package com.albanero.service.impl.validators;

import com.albanero.constants.ErrorCode;
import com.albanero.dto.CustomerRequest;
import com.albanero.exceptions.ValidationException;
import com.albanero.service.Validator;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class IndustryTypeValidator implements Validator {
    @Override
    public void doValidate(CustomerRequest customerRequest) {
        if (StringUtils.isBlank(customerRequest.getIndustryType())) {

            throw new ValidationException(ErrorCode.INDUSTRY_TYPE_VALIDATION_FAILED.getErrorMessage(),
                    ErrorCode.ACCOUNT_MANAGER_VALIDATION_FAILED.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
    }
}
