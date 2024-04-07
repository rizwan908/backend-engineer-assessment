package com.midas.app.providers.external.stripe;

import com.midas.app.enums.*;
import com.midas.app.exceptions.ApiException;
import com.midas.app.models.Account;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.stripe.*;
import com.stripe.exception.*;
import com.stripe.model.*;
import com.stripe.param.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.env.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);

  private final StripeConfiguration configuration;

  @Autowired private Environment environment;

  /** providerName is the name of the payment provider */
  @Override
  public String providerName() {
    return "stripe";
  }

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(CreateAccount details) {

    logger.info(
        "creating payment account in payment provider: {}", details.getEmail(), providerName());
    Stripe.apiKey = environment.getProperty("stripe.api-key");

    CustomerCreateParams params =
        CustomerCreateParams.builder()
            .setName(details.getFirstName() + " " + details.getLastName())
            .setEmail(details.getEmail())
            .build();

    try {
      Customer customer = Customer.create(params);
      String customerId = customer.getId();

      Account account =
          Account.builder()
              .email(details.getEmail())
              .firstName(details.getFirstName())
              .lastName(details.getLastName())
              .providerId(customerId)
              .providerType(ProviderType.STRIPE)
              .build();
      return account;
    } catch (StripeException e) {
      throw new ApiException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage());
    }
  }

  @Override
  public void updateAccount(CreateAccount details) {
    Stripe.apiKey = environment.getProperty("stripe.api-key");

    Customer resource = null;
    try {
      resource = Customer.retrieve(details.getUserId());

      CustomerUpdateParams params =
          CustomerUpdateParams.builder()
              .setName(details.getFirstName() + " " + details.getLastName())
              .setEmail(details.getEmail())
              .build();
      resource.update(params);
    } catch (StripeException e) {
      throw new ApiException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage());
    }
  }
}
