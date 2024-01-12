package com.albanero.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    private int customerId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String companyName;
    private String industryType;
    private String customerStatus;
    private String accountManager;

}
