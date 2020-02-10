package com.nabucco.petshop.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.restexpress.exception.BadRequestException;

public abstract class AbastractEntity<T extends AbastractEntity> {
  public final void isValid() {
    Locale.setDefault(Locale.US);
    Configuration config = Validation.byDefaultProvider().configure();
    ValidatorFactory factory = config.buildValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<AbastractEntity>> violations = validator.validate(this);
    List<String> messages = new ArrayList<>();

    for (ConstraintViolation<AbastractEntity> constraintViolation : violations) {
      String propertyPath = constraintViolation.getPropertyPath().toString();
      String message = constraintViolation.getMessage();
      messages.add(propertyPath.concat(": ").concat(message));
    }

    if (!messages.isEmpty()) {
      throw new BadRequestException(String.join(" \n ", messages));
    }
  }
}
