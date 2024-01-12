package com.albanero.dao;

import com.albanero.constants.CustomerSaveResult;
import com.albanero.pojo.Customer;

import java.util.List;

public interface CustomerManagementDao {
    Integer addCustomer(Customer customer);

    Customer getCustomer(Integer customerId);

    void updateCustomer(Customer customer);

    void deleteCustomer(Integer customerId);

    CustomerSaveResult addBulkCustomers(List<Customer> customers);

    Boolean isCustomerExistByNameOrEmail(String name, String email);

    CustomerSaveResult bulkUpdate(List<Customer> customers);

    List<Customer> getAllCustomers(List<Integer> customerIdList);

}
