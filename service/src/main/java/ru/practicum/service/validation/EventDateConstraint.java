package ru.practicum.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDateConstraint {
    String message() default "Event date must be at least 2 hours from now";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int amountInHours() default 2;
}
