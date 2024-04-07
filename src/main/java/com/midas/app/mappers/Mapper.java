package com.midas.app.mappers;

import com.midas.app.models.Account;
import com.midas.app.providers.payment.*;
import com.midas.generated.model.AccountDto;
import lombok.NonNull;

public class Mapper {
  // Prevent instantiation
  private Mapper() {}

  /**
   * toAccountDto maps an account to an account dto.
   *
   * @param account is the account to be mapped
   * @return AccountDto
   */
  public static AccountDto toAccountDto(@NonNull Account account) {
    var accountDto = new AccountDto();

    accountDto
        .id(account.getId())
        .firstName(account.getFirstName())
        .lastName(account.getLastName())
        .email(account.getEmail())
        .providerId(account.getProviderId())
        .providerType(account.getProviderType().name())
        .createdAt(account.getCreatedAt())
        .updatedAt(account.getUpdatedAt());

    return accountDto;
  }

  public static Account toAccount(@NonNull CreateAccount account) {
    var accountDto = new Account();
    return Account.builder()
        .firstName(account.getFirstName())
        .lastName(account.getLastName())
        .email(account.getEmail())
        .build();
  }
}
