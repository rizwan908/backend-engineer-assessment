package com.midas.app.activities;

import com.midas.app.models.*;
import io.temporal.activity.*;

@ActivityInterface
public interface UpdateAccountActivity {

  /**
   * updatePaymentAccount update a payment account in the system or provider.
   *
   * @param account is the account to be created
   */
  @ActivityMethod
  void updatePaymentAccount(Account account);

  @ActivityMethod
  Account updateAccount(Account person);
}
