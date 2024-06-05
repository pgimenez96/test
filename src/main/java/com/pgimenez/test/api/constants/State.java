package com.pgimenez.test.api.constants;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum State {

  @JsonProperty(value = "A")
  ACTIVE("A"),

  @JsonProperty(value = "I")
  INACTIVE("I");

  private final String value;

  State(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
