package com.patrykzdral.musicalworldcore.validation;


import com.patrykzdral.musicalworldcore.services.user.exception.InternalException;
import com.patrykzdral.musicalworldcore.services.user.model.RegisterUserRequestDTO;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    private String message;

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final RegisterUserRequestDTO user = (RegisterUserRequestDTO) obj;
        if (!user.getPassword().equals(user.getMatchingPassword()))
            throw new InternalException("Registration error", message);
        return user.getPassword().equals(user.getMatchingPassword());
    }

}
