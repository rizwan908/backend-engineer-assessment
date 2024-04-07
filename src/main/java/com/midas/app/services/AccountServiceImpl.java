package com.midas.app.services;

import com.midas.app.exceptions.*;
import com.midas.app.models.Account;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.workflows.*;
import com.midas.generated.model.*;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Workflow;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final Logger logger = Workflow.getLogger(AccountServiceImpl.class);

  private final WorkflowClient workflowClient;

  private final AccountRepository accountRepository;

  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(Account details) {

    if (Objects.nonNull(accountRepository.findByEmail(details.getEmail()))) {
      throw new ResourceAlreadyExistsException();
    }
    var options = getWorkflowOptions(CreateAccountWorkflow.QUEUE_NAME, details.getEmail());

    logger.info("initiating workflow to create account for email: {}", details.getEmail());

    var workflow = workflowClient.newWorkflowStub(CreateAccountWorkflow.class, options);

    return workflow.createAccount(details);
  }

  @NotNull private static WorkflowOptions getWorkflowOptions(String queueName, String email) {
    var options = WorkflowOptions.newBuilder().setTaskQueue(queueName).setWorkflowId(email).build();
    return options;
  }

  @Override
  public Account updateAccount(UUID accountId, UpdateAccountDto updateAccountDto) {
    Account account = getAccount(accountId);
    account.setFirstName(updateAccountDto.getFirstName());
    account.setLastName(updateAccountDto.getLastName());
    account.setEmail(updateAccountDto.getEmail());

    var options = getWorkflowOptions(UpdateAccountWorkflow.QUEUE_NAME, account.getEmail());

    logger.info("initiating workflow to update account for email: {}", account.getEmail());

    var workflow = workflowClient.newWorkflowStub(UpdateAccountWorkflow.class, options);

    return workflow.updateAccount(account);
  }

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  @Override
  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }

  @Override
  public Account getAccount(UUID accountId) {
    Account byId = accountRepository.findById(accountId);
    if (Objects.isNull(byId)) {
      throw new ResourceNotFoundException("Account not found");
    }
    return byId;
  }
}
