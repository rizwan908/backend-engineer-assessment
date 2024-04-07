package com.midas.app.enums;

public enum ProviderType {
  STRIPE("stripe");

  private final String providerName;

  ProviderType(String provider) {
    this.providerName = provider;
  }
}
