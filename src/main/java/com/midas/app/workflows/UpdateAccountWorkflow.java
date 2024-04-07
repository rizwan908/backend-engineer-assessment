package com.midas.app.workflows;

import com.midas.app.models.*;
import io.temporal.workflow.*;

@WorkflowInterface
public interface UpdateAccountWorkflow {
  String QUEUE_NAME = "update-account-workflow";

  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @WorkflowMethod
  Account updateAccount(Account details);
}
