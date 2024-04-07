package com.midas.app.activities;

import com.midas.app.models.*;
import com.midas.app.providers.external.stripe.*;
import com.midas.app.providers.payment.*;
import com.midas.app.repositories.*;
import io.temporal.spring.boot.*;
import io.temporal.workflow.*;
import lombok.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

@Component
@RequiredArgsConstructor
@ActivityImpl(taskQueues = "update-account-workflow")
public class UpdateAccountActivityImpl implements UpdateAccountActivity {

  private final AccountRepository accountRepository;
  private final StripePaymentProvider stripePaymentProvider;
  private final Logger logger = Workflow.getLogger(UpdateAccountActivityImpl.class);

  @Override
  public void updatePaymentAccount(Account account) {
    logger.info("initiating payment account update for: {}", account.getEmail());

    var createAccount =
        CreateAccount.builder()
            .firstName(account.getFirstName())
            .lastName(account.getLastName())
            .email(account.getEmail())
            .userId(account.getProviderId())
            .build();

    stripePaymentProvider.updateAccount(createAccount);
    logger.info("completed payment account update for: {}", account.getEmail());
  }

  @Override
  public Account updateAccount(Account account) {
    logger.info("updating account: {}", account.getEmail());
    return accountRepository.save(account);
  }
}
