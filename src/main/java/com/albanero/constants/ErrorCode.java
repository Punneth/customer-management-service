package com.albanero.constants;

import lombok.Getter;

public enum ErrorCode {
    CUSTOMER_VALIDATION_FAILED("1001", "Bad request, give customer details is empty"),
    COMPANY_VALIDATION_FAILED("1002", "Bad request, given company name is invalid or empty"),
    INDUSTRY_TYPE_VALIDATION_FAILED("1003", "Bad request, given industry type is invalid or empty"),
    PHONE_VALIDATION_FAILED("1004", "Bad request, given phone number is invalid or empty"),
    ACCOUNT_MANAGER_VALIDATION_FAILED("1005", "Bad request, given account manager is invalid or empty"),
    EMAIL_VALIDATION_FAILED("1006", "Bad Request, given email parameter is invalid or empty"),
    CUSTOMER_ID_NOT_EXITS("1007", "Bad request, customer id is invalid or empty"),
    CUSTOMER_NOT_EXITS("1008", "Customer does not exits with this id"),
    USER_VALIDATION_FAILED("1009", "Bad request, given user details is empty"),
    CUSTOMER_ALREADY_EXISTS("1010", "Bad request, given customer name or email is already exist"),
    NAME_VALIDATION_FAILED("1011", "Bad request, given name is invalid or empty"),
    CUSTOMER_ID_EMPTY("1012", "Bad Request, given customerId is empty"),
    GENERIC_EXCEPTION("1019", "Internal server error, please try again later");

    @Getter
    private String errorCode;
    @Getter
    private String errorMessage;

    private ErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
