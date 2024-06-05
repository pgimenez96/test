package com.pgimenez.test.api.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidatorValidator implements ConstraintValidator<EnumValidator, Enum<?>> {
  private Class<? extends Enum<?>> enumClass;

  @Override
  public void initialize(EnumValidator constraintAnnotation) {
      enumClass = constraintAnnotation.enumClass();
  }

  @Override
  public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
      if (value == null) {
          return false; // Let @NotNull handle this
      }
      for (Enum<?> enumValue : enumClass.getEnumConstants()) {
          if (enumValue.name().equals(value.name())) {
              return true;
          }
      }
      return false;
  }
}
