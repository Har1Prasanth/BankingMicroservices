package com.irah.accounts.service.impl;

import com.irah.accounts.constants.AccountsConstants;
import com.irah.accounts.dto.AccountsDto;
import com.irah.accounts.dto.CustomerDto;
import com.irah.accounts.entity.Accounts;
import com.irah.accounts.entity.Customer;
import com.irah.accounts.exception.CustomerAlreadyExistException;
import com.irah.accounts.exception.ResourceNotFoundException;
import com.irah.accounts.mapper.AccountsMapper;
import com.irah.accounts.mapper.CustomerMapper;
import com.irah.accounts.repository.AccountsRepository;
import com.irah.accounts.repository.CustomerRepository;
import com.irah.accounts.service.IAccountsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;


    @Override
    public void CreateAccount(CustomerDto customerDto) {

        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());

        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistException("Customer Already Registered with Given Mobile Number " +
                    customerDto.getMobileNumber());
        }
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));


    }

    private Accounts createNewAccount(Customer customer) {

        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 100000000L + new Random().nextInt(90000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;

    }

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
                );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
                );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        return customerDto;
    }

    /**
     * @param customerDto
     * @return
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;

        AccountsDto accountsDto = customerDto.getAccountsDto();


        if (accountsDto != null) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString()
                            ));

            AccountsMapper.mapToAccounts(accountsDto,accounts);
            accounts= accountsRepository.save(accounts);

            Long customerId=accounts.getCustomerId();
            Customer customer=customerRepository.findById(customerId)
                    .orElseThrow(
                            ()->new ResourceNotFoundException("Customer","CustomerId",customerId.toString())
                    );

            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated=true;
        }
        return isUpdated;
    }

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {

        boolean isDeleted=false;
        Customer customer=customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(
                        ()->new ResourceNotFoundException("Customer","MobileNumber",mobileNumber)
                );

        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());

        isDeleted=true;

        return isDeleted;
    }

    /**
     * @return
     */
    @Override
    public List<CustomerDto> fetchAllAccounts() {
        List<Customer> customers=customerRepository.findAll();
        List<CustomerDto> customerDtos= customers
                .stream()
                .map(this::accountFetch)
                .collect(Collectors.toList());



        return customerDtos;
    }

    private CustomerDto accountFetch(Customer customer) {
        CustomerDto customerDto=CustomerMapper.mapToCustomerDto(customer,new CustomerDto());
        Optional<Accounts> accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts.get(), new AccountsDto()));
        return customerDto;
    }

}
