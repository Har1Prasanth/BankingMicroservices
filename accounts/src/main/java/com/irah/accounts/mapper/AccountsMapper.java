package com.irah.accounts.mapper;

import com.irah.accounts.dto.AccountsDto;
import com.irah.accounts.entity.Accounts;

public class AccountsMapper {

  public static AccountsDto mapToAccountsDto(Accounts accounts,AccountsDto accountsDto){
      accountsDto.setAccountNumber(accounts.getAccountNumber());
      accountsDto.setAccountType(accounts.getAccountType());
      accountsDto.setBranchAddress(accounts.getBranchAddress());
      return accountsDto;
  }

    public static Accounts mapToAccounts(AccountsDto accountsDto,Accounts accounts){
        accounts.setAccountNumber(accountsDto.getAccountNumber());
        accounts.setAccountType(accountsDto.getAccountType());
        accounts.setBranchAddress(accountsDto.getBranchAddress());
        return accounts;
    }
}
