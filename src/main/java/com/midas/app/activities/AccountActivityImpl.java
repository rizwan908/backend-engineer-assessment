package com.midas.app.activities;

import com.midas.app.exceptions.*;
import com.midas.app.models.*;
import com.midas.app.models.Account;
import com.midas.app.providers.external.stripe.*;
import com.midas.app.providers.payment.*;
import com.midas.app.repositories.*;
import com.stripe.*;
import com.stripe.exception.*;
import com.stripe.model.*;
import com.stripe.param.*;
import io.temporal.spring.boot.*;
import io.temporal.workflow.*;
import java.util.*;
import lombok.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
@RequiredArgsConstructor
@ActivityImpl(taskQueues = "create-account-workflow")
public class AccountActivityImpl implements AccountActivity {

  private final AccountRepository accountRepository;
  private final StripePaymentProvider stripePaymentProvider;
  private final Logger logger = Workflow.getLogger(AccountActivityImpl.class);

  @Override
  public Account saveAccount(Account account) {
    logger.info("saving account: {}", account.getEmail());
    return accountRepository.save(account);
  }

  @Override
  public Account createPaymentAccount(Account account) {
    logger.info("initiating payment account creation for: {}", account.getEmail());

    var createAccount =
        CreateAccount.builder()
            .firstName(account.getFirstName())
            .lastName(account.getLastName())
            .email(account.getEmail())
            .build();

    Account account1 = stripePaymentProvider.createAccount(createAccount);
    Account byEmail = accountRepository.findByEmail(account.getEmail());
    byEmail.setProviderId(account1.getProviderId());
    byEmail.setProviderType(account1.getProviderType());

    return accountRepository.save(byEmail);
  }
}
