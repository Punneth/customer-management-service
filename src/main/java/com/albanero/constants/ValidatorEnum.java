package com.albanero.constants;

import com.albanero.service.Validator;
import com.albanero.service.impl.validators.*;
import lombok.Getter;

public enum ValidatorEnum {
    EMAIL_VALIDATOR("EMAIL_VALIDATOR", EmailValidator.class),
    PHONE_VALIDATOR("PHONE_VALIDATOR", PhoneValidator.class),
    COMPANY_NAME_VALIDATOR("COMPANY_NAME_VALIDATOR", CompanyNameValidator.class),
    INDUSTRY_TYPE_VALIDATOR("INDUSTRY_TYPE_VALIDATOR", IndustryTypeValidator.class),
    ACCOUNT_MANAGER_VALIDATOR("ACCOUNT_MANAGER_VALIDATOR", AccountManagerValidator.class),
    NAME_VALIDATION_FAILED("NAME_VALIDATION_FAILED", NameValidator.class);

    @Getter
    private String validatorName;

    private Class<? extends Validator> validatorClass;

    private ValidatorEnum(String validatorName, Class<? extends Validator> validatorClass) {
        this.validatorName = validatorName;
        this.validatorClass = validatorClass;
    }

    public static ValidatorEnum getEnumByString(String name) {
        for (ValidatorEnum validatorEnum : ValidatorEnum.values()) {
            if (validatorEnum.validatorName.equals(name)) {
                return validatorEnum;
            }
        }
        return null;
    }

    public Class<? extends Validator> getValidatorClass() {
        return this.validatorClass;
    }

}
