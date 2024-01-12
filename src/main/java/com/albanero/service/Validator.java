package com.albanero.service;

import com.albanero.dto.CustomerRequest;

public interface Validator {
    void doValidate(CustomerRequest customerRequest);
}
