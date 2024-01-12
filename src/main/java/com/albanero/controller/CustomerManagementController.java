package com.albanero.controller;

import com.albanero.constants.Endpoint;
import com.albanero.dto.CustomerRequest;
import com.albanero.dto.CustomerResponse;
import com.albanero.service.CustomerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = Endpoint.CUSTOMER_MAPPING)
public class CustomerManagementController {

    @Autowired
    private CustomerManagementService customerManagementService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse customerResponse = customerManagementService.createCustomer(customerRequest);
        return new ResponseEntity<>(customerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Integer customerId) {
        return new ResponseEntity<>(customerManagementService.getCustomer(customerId), HttpStatus.OK);
    }

    @PutMapping("/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Integer customerId,
                                                           @RequestBody CustomerRequest customerRequest) {
        return new ResponseEntity<>(customerManagementService.updateCustomer(customerRequest, customerId), HttpStatus.OK);
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER') or hasAuthority('ROLE_ADMIN')")
    public void deleteCustomer(@PathVariable Integer customerId) {
        customerManagementService.deleteCustomer(customerId);
    }

    @PostMapping(Endpoint.BULK_LOAD)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void bulkUpdateCustomers(@RequestBody List<CustomerRequest> customerRequests) {
        customerManagementService.bulkLoadCustomers(customerRequests);
    }

    @PutMapping(Endpoint.BULK_UPDATE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> getAllCustomers(@RequestParam("customerId") List<Integer> customerIdList,
                                                  @RequestBody List<CustomerRequest> customerRequests) {
        customerManagementService.bulkUpdate(customerIdList, customerRequests);
        return new ResponseEntity<>("Bulk Update Successful", HttpStatus.OK);
    }
}
