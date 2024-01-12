package com.albanero.service.impl;

import com.albanero.constants.CustomerSaveResult;
import com.albanero.constants.ErrorCode;
import com.albanero.constants.ValidatorEnum;
import com.albanero.dao.CustomerManagementDao;
import com.albanero.dto.CustomerRequest;
import com.albanero.dto.CustomerResponse;
import com.albanero.exceptions.ValidationException;
import com.albanero.pojo.Customer;
import com.albanero.service.CustomerManagementService;
import com.albanero.service.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomerManagementServiceImpl implements CustomerManagementService {

    @Autowired
    private CustomerManagementDao customerManagementDao;

    @Value("${validation.rules}")
    private String validatorRules;

    @Autowired
    private ApplicationContext context;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        if (null == customerRequest) {

            throw new ValidationException(ErrorCode.CUSTOMER_VALIDATION_FAILED.getErrorMessage(),
                    ErrorCode.CUSTOMER_VALIDATION_FAILED.getErrorCode(), HttpStatus.BAD_REQUEST);
        }

        validateRules(customerRequest);
        Customer customer = retrieveCustomer(customerRequest);
        Boolean isExists = customerManagementDao.isCustomerExistByNameOrEmail(customer.getName(), customer.getEmail());

        if (isExists) {
            throw new ValidationException(ErrorCode.CUSTOMER_ALREADY_EXISTS.getErrorMessage(),
                    ErrorCode.CUSTOMER_ALREADY_EXISTS.getErrorCode(), HttpStatus.BAD_REQUEST);
        }

        Integer customerId = customerManagementDao.addCustomer(customer);
        customer.setCustomerId(customerId);
        return retrieveCustomerResponse(customer);
    }

    @Override
    public CustomerResponse getCustomer(Integer customerId) {

        if (null == customerId) {
            throw new ValidationException(ErrorCode.CUSTOMER_ID_NOT_EXITS.getErrorMessage(),
                    ErrorCode.CUSTOMER_ID_NOT_EXITS.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerManagementDao.getCustomer(customerId);
        if (customer == null) {
            throw new ValidationException(ErrorCode.CUSTOMER_NOT_EXITS.getErrorMessage(),
                    ErrorCode.CUSTOMER_NOT_EXITS.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
        return retrieveCustomerResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(CustomerRequest customerRequest, Integer customerId) {
        if (null == customerRequest) {

            throw new ValidationException(ErrorCode.CUSTOMER_VALIDATION_FAILED.getErrorMessage(),
                    ErrorCode.CUSTOMER_VALIDATION_FAILED.getErrorCode(), HttpStatus.BAD_REQUEST);
        } else if (null == customerId) {
            throw new ValidationException(ErrorCode.CUSTOMER_ID_NOT_EXITS.getErrorMessage(),
                    ErrorCode.CUSTOMER_ID_NOT_EXITS.getErrorCode(), HttpStatus.BAD_REQUEST);

        }

        validateRules(customerRequest);
        Customer customer = customerManagementDao.getCustomer(customerId);

        if (null == customer) {
            throw new ValidationException(ErrorCode.CUSTOMER_NOT_EXITS.getErrorMessage(),
                    ErrorCode.CUSTOMER_NOT_EXITS.getErrorCode(), HttpStatus.BAD_REQUEST);
        }

        customer.setName(customerRequest.getName());
        customer.setEmail(customerRequest.getEmail());
        customer.setAddress(customerRequest.getAddress());
        customer.setAccountManager(customerRequest.getAccountManager());
        customer.setCompanyName(customerRequest.getCompanyName());
        customer.setIndustryType(customerRequest.getIndustryType());
        customer.setPhone(customerRequest.getPhone());

        customerManagementDao.updateCustomer(customer);
        return retrieveCustomerResponse(customer);
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        if (null == customerId) {
            throw new ValidationException(ErrorCode.CUSTOMER_ID_NOT_EXITS.getErrorMessage(),
                    ErrorCode.CUSTOMER_ID_NOT_EXITS.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerManagementDao.getCustomer(customerId);

        if (null == customer) {
            throw new ValidationException(ErrorCode.CUSTOMER_NOT_EXITS.getErrorMessage(),
                    ErrorCode.CUSTOMER_NOT_EXITS.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
        customerManagementDao.deleteCustomer(customerId);

    }

    @Override
    public void bulkLoadCustomers(List<CustomerRequest> customerRequests) {
        List<String> validatorList = Stream.of(validatorRules.split(",")).toList();

        customerRequests.forEach(customerRequest -> {
                    validatorList.forEach(rule -> {
                                ValidatorEnum validatorEnum = ValidatorEnum.getEnumByString(rule);
                                Validator validatorBean = context.getBean(validatorEnum.getValidatorClass());
                                validatorBean.doValidate(customerRequest);
                            }
                    );
                }
        );

        List<Customer> customers = customerRequests.stream().map(customerRequest -> {
            return retrieveCustomer(customerRequest);
        }).collect(Collectors.toList());

        CustomerSaveResult response = customerManagementDao.addBulkCustomers(customers);
        if (response == CustomerSaveResult.IS_DUPLICATE) {
            throw new ValidationException(ErrorCode.CUSTOMER_ALREADY_EXISTS.getErrorMessage(),
                    ErrorCode.CUSTOMER_ID_NOT_EXITS.getErrorCode(), HttpStatus.BAD_REQUEST);
        }

        if (response == CustomerSaveResult.IS_ERROR) {
            throw new ValidationException(ErrorCode.GENERIC_EXCEPTION.getErrorMessage(),
                    ErrorCode.GENERIC_EXCEPTION.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public void bulkUpdate(List<Integer> customerIdList, List<CustomerRequest> customerRequests) {
        boolean result = customerIdList.stream().allMatch(customerId -> customerId != null);
        if (!result) {
            throw new ValidationException(ErrorCode.CUSTOMER_ID_EMPTY.getErrorMessage(),
                    ErrorCode.CUSTOMER_ID_EMPTY.getErrorCode(), HttpStatus.BAD_REQUEST);
        }

        customerRequests.stream().forEach(this::validateRules);
        List<Customer> customers = customerManagementDao.getAllCustomers(customerIdList);

        if (customers.isEmpty() || customers.size() != customerRequests.size()) {
            throw new ValidationException(ErrorCode.CUSTOMER_NOT_EXITS.getErrorMessage(),
                    ErrorCode.CUSTOMER_NOT_EXITS.getErrorCode(), HttpStatus.BAD_REQUEST);
        }

        List<Customer> customers1 = customerRequests.stream().map(customerRequest ->
                retrieveCustomer(customerRequest)).collect(Collectors.toList());
        CustomerSaveResult response = customerManagementDao.bulkUpdate(customers1);
        if (response == CustomerSaveResult.IS_DUPLICATE) {
            throw new ValidationException(ErrorCode.CUSTOMER_ALREADY_EXISTS.getErrorMessage(),
                    ErrorCode.CUSTOMER_ID_NOT_EXITS.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
        if (response == CustomerSaveResult.IS_ERROR) {
            throw new ValidationException(ErrorCode.GENERIC_EXCEPTION.getErrorMessage(),
                    ErrorCode.GENERIC_EXCEPTION.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* For this conversion I tried to user ModelMapper but I
    got some dependency issue, I'll resolve that one and I'll upload later
     */
    private CustomerResponse retrieveCustomerResponse(Customer customer) {
        return CustomerResponse.builder().name(customer.getName()).customerId(customer.getCustomerId())
                .companyName(customer.getCompanyName()).address(customer.getAddress()).email(customer.getEmail())
                .industryType(customer.getIndustryType()).phone(customer.getPhone())
                .accountManager(customer.getAccountManager()).build();
    }

    // For this conversion I tried to user ModelMapper but I
    // got some dependency issue, I'll resolve that one and I'll upload later
    private Customer retrieveCustomer(CustomerRequest customerRequest) {
        return Customer.builder().customerId(customerRequest.getCustomerId()).name(customerRequest.getName()).
                email(customerRequest.getEmail()).phone(customerRequest.getPhone()).address(customerRequest.getAddress())
                .companyName(customerRequest.getCompanyName()).industryType(customerRequest.getIndustryType())
                .accountManager(customerRequest.getAccountManager()).customerStatus("active").build();
    }

    private void validateRules(CustomerRequest customerRequest) {
        List<String> validatorList = Stream.of(validatorRules.split(",")).toList();
        validatorList.forEach(rule -> {
                    ValidatorEnum validatorEnum = ValidatorEnum.getEnumByString(rule);
                    Validator validatorBean = context.getBean(validatorEnum.getValidatorClass());
                    validatorBean.doValidate(customerRequest);
                }
        );
    }
}
