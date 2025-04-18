package ru.practicum.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<EventDateConstraint, LocalDateTime> {

    private int hoursOffset;

    @Override
    public void initialize(EventDateConstraint constraintAnnotation) {
        this.hoursOffset = constraintAnnotation.amountInHours();
    }

    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext context) {
        if (eventDate == null) return true;

        LocalDateTime minValidTime = LocalDateTime.now().plusHours(hoursOffset);

        if (eventDate.isBefore(minValidTime) && eventDate.isAfter(LocalDateTime.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Event date must be at least " + hoursOffset + " hours from now"
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}