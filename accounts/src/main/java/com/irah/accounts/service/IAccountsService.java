package com.irah.accounts.service;

import com.irah.accounts.dto.CustomerDto;

import java.util.List;

public interface IAccountsService {

    void CreateAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);

    List<CustomerDto> fetchAllAccounts();
}
