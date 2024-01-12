package com.albanero.dao.impl;

import com.albanero.constants.CustomerSaveResult;
import com.albanero.constants.ErrorCode;
import com.albanero.dao.CustomerManagementDao;
import com.albanero.exceptions.ValidationException;
import com.albanero.pojo.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CustomerManagementDaoImpl implements CustomerManagementDao {

    public static final Logger LOGGER = LogManager.getLogger(CustomerManagementDaoImpl.class);
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public Integer addCustomer(Customer customer) {
        Integer customerId = null;
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedJdbcTemplate.update(addCustomer(), new BeanPropertySqlParameterSource(customer),
                    keyHolder, new String[]{"customerId"});
            customerId = keyHolder.getKey().intValue();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ValidationException(ErrorCode.GENERIC_EXCEPTION.getErrorMessage(),
                    ErrorCode.GENERIC_EXCEPTION.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return customerId;
    }

    @Override
    public Customer getCustomer(Integer customerId) {
        Customer customer = null;
        try {

            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("customerId", customerId);
            customer = namedJdbcTemplate.queryForObject(getCustomer(),
                    new BeanPropertySqlParameterSource(Customer.builder().customerId(customerId).build()),
                    new BeanPropertyRowMapper<>(Customer.class));
        } catch (Exception exception) {
            LOGGER.info("Customer Doesn't exists with this is {}", customerId);
        }
        return customer;
    }

    @Override
    public void updateCustomer(Customer customer) {

        try {
            int update = namedJdbcTemplate.update(updateCustomer(), new BeanPropertySqlParameterSource(customer));
            System.out.println(update);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ValidationException(ErrorCode.GENERIC_EXCEPTION.getErrorMessage(),
                    ErrorCode.GENERIC_EXCEPTION.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        try {

            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("customerId", customerId);
            namedJdbcTemplate.update(deleteCustomer(), parameters);
        } catch (Exception exception) {
            LOGGER.info("Customer Doesn't exists with this is {} to delete", customerId);
        }
    }

    @Override
    public CustomerSaveResult addBulkCustomers(List<Customer> customers) {
        try {
            SqlParameterSource[] batchParams = SqlParameterSourceUtils.createBatch(customers.toArray());
            namedJdbcTemplate.batchUpdate(addBulkCustomer(), batchParams);
            return CustomerSaveResult.IS_SAVED;
        } catch (DuplicateKeyException e) {
            LOGGER.info("Duplicate while saving");
            return CustomerSaveResult.IS_DUPLICATE;
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.info("Internal Server Error need to check");
            return CustomerSaveResult.IS_ERROR;
        }
    }

    @Override
    public Boolean isCustomerExistByNameOrEmail(String name, String email) {
        boolean isExist = false;
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("name", name)
                    .addValue("email", email);
            isExist = namedJdbcTemplate.queryForObject(isCustomerExistByNameOrEmail(), parameters, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            isExist = false;
        }
        return isExist;
    }

    //    @Transactional(rollbackFor = {DuplicateKeyException.class, Exception.class})
    @Override
    public CustomerSaveResult bulkUpdate(List<Customer> customers) {
        try {
            List<Map<String, Object>> batchParams = customers.stream()
                    .map(customer -> {
                        Map<String, Object> paramMap = new HashMap<>();
                        paramMap.put("customerId", customer.getCustomerId());
                        paramMap.put("name", customer.getName());
                        paramMap.put("email", customer.getEmail());
                        paramMap.put("phone", customer.getPhone());
                        paramMap.put("address", customer.getAddress());
                        paramMap.put("companyName", customer.getCompanyName());
                        paramMap.put("industryType", customer.getIndustryType());
                        paramMap.put("customerStatus", "active");
                        paramMap.put("accountManager", customer.getAccountManager());
                        return paramMap;
                    })
                    .collect(Collectors.toList());

            namedJdbcTemplate.batchUpdate(updateCustomer(), batchParams.toArray(new Map[0]));
            return CustomerSaveResult.IS_SAVED;
        } catch (DuplicateKeyException e) {
            LOGGER.info("Duplicate while saving");
            return CustomerSaveResult.IS_DUPLICATE;
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.info("Internal Server Error need to check");
            return CustomerSaveResult.IS_ERROR;
        }
    }

    @Override
    public List<Customer> getAllCustomers(List<Integer> customerIdList) {
        List<Customer> customers = null;
        try {
            Map<String, Object> paramMap = Collections.singletonMap("customerIdList", customerIdList);
            RowMapper<Customer> rowMapper = new BeanPropertyRowMapper<>(Customer.class);
            customers = namedJdbcTemplate.query(getAllCustomers(), paramMap, rowMapper);
        } catch (Exception exception) {
            LOGGER.info("Internal Server Error need to check");
        }
        return customers;
    }

    private String addCustomer() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO CUSTOMER(name, email, phone, address, companyName, industryType,customerStatus, accountManager)");
        queryBuilder.append(" VALUES(:name, :email, :phone, :address, :companyName, :industryType, :customerStatus, :accountManager)");
        return queryBuilder.toString();
    }

    private String getCustomer() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM CUSTOMER WHERE customerId =:customerId");
        return queryBuilder.toString();
    }

    private String updateCustomer() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE CUSTOMER ");
        queryBuilder.append("SET name =:name, email =:email, phone =:phone, address =:address," +
                " companyName =:companyName, industryType =:industryType, customerStatus =:customerStatus, accountManager =:accountManager ");
        queryBuilder.append("WHERE customerId =:customerId");
        return queryBuilder.toString();
    }

    private String deleteCustomer() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("DELETE FROM CUSTOMER ");
        queryBuilder.append("WHERE customerId =:customerId");
        return queryBuilder.toString();
    }

    private String addBulkCustomer() {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO CUSTOMER ");
        queryBuilder.append("(name, email, phone, address, companyName, industryType, customerStatus, accountManager) ");
        queryBuilder.append("VALUES (:name, :email, :phone, :address, :companyName, :industryType, :customerStatus, :accountManager)");
        return queryBuilder.toString();
    }

    private String isCustomerExistByNameOrEmail() {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) > 0 FROM CUSTOMER ");
        queryBuilder.append("WHERE name =:name OR email =:email");
        return queryBuilder.toString();
    }

    private String getAllCustomers() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM CUSTOMER WHERE customerId IN(:customerIdList)");
        return queryBuilder.toString();
    }

}
