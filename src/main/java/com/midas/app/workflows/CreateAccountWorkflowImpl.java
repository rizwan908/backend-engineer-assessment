/*
 *  Copyright (c) 2020 Temporal Technologies, Inc. All Rights Reserved
 *
 *  Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *  Modifications copyright (C) 2017 Uber Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"). You may not
 *  use this file except in compliance with the License. A copy of the License is
 *  located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 *  or in the "license" file accompanying this file. This file is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package com.midas.app.workflows;

import com.midas.app.activities.*;
import com.midas.app.models.*;
import io.temporal.activity.*;
import io.temporal.common.*;
import io.temporal.spring.boot.*;
import io.temporal.workflow.*;
import java.time.*;

@WorkflowImpl(taskQueues = "create-account-workflow")
public class CreateAccountWorkflowImpl implements CreateAccountWorkflow {

  private AccountActivity activity =
      Workflow.newActivityStub(
          AccountActivity.class,
          ActivityOptions.newBuilder()
              .setStartToCloseTimeout(Duration.ofSeconds(20))
              .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(1).build())
              .build());

  @Override
  public Account createAccount(Account person) {
    Account account = activity.saveAccount(person);
    return activity.createPaymentAccount(account);
  }
}
