package com.midas.app.services;

import com.midas.app.models.Account;
import com.midas.generated.model.*;
import java.util.*;

public interface AccountService {
  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  Account createAccount(Account details);

  /**
   * updateAccount updates account.
   *
   * @param accountId
   * @param updateAccountDto
   * @return Account //
   */
  Account updateAccount(UUID accountId, UpdateAccountDto updateAccountDto);

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  List<Account> getAccounts();

  Account getAccount(UUID accountId);
}
