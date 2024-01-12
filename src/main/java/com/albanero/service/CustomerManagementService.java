package com.albanero.service;

import com.albanero.dto.CustomerRequest;
import com.albanero.dto.CustomerResponse;

import java.util.List;

public interface CustomerManagementService {
    CustomerResponse createCustomer(CustomerRequest customerRequest);

    CustomerResponse getCustomer(Integer customerId);

    CustomerResponse updateCustomer(CustomerRequest customerRequest, Integer customerId);

    void deleteCustomer(Integer customerId);

    void bulkLoadCustomers(List<CustomerRequest> customerRequests);

    void bulkUpdate(List<Integer> customerIdList, List<CustomerRequest> customerRequests);
}
