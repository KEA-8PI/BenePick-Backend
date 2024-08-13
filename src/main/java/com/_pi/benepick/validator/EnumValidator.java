package com._pi.benepick.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<EnumValid,String> {
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
       //null일 경우 @NotBlank 어노테이션이 처리하게끔 처리
        if (value == null) {
            return true;
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equals(value));
    }
}
