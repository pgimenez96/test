package com.pgimenez.test.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pgimenez.test.api.annotations.EnumValidator;
import com.pgimenez.test.api.constants.State;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest implements Serializable {

  @NotEmpty(message = "Ingrese código")
  @JsonProperty("code")
  String code;

  @NotEmpty(message = "Ingrese descripción")
  @JsonProperty("description")
  String description;

  @EnumValidator(enumClass = State.class, message = "Estado ingreso es incorrecto")
  State state;
  
}
