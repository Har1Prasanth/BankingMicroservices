package com.irah.accounts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountsDto {

    /* Not Empty & Pattern validators
       not applicable for Long Type */
    //   @NotEmpty(message = "AccountNumber can not be a null or empty")
    //   @Pattern(regexp="(^$|[0-9]{10})",message = "AccountNumber must be 10 digits")
    @NotNull(message = "AccountNumber can not be a null or empty")
    private Long accountNumber;

    @NotEmpty(message = "AccountType can not be a null or empty")
    private String accountType;

    @NotEmpty(message = "BranchAddress can not be a null or empty")
    private String branchAddress;
}
