package com.patrykzdral.musicalworldcore.validation;

import com.google.common.base.Joiner;
import com.patrykzdral.musicalworldcore.validation.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@Slf4j
public class PasswordConstraintValidator
        implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(final ValidPassword arg0) {

    }

    @Override
    public boolean isValid(final String password,
                           final ConstraintValidatorContext context) {
        final PasswordValidator validator =
                new PasswordValidator(Arrays.asList(
                new LengthRule(6, 30),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new SpecialCharacterRule(1)));
        final RuleResult result = validator
                .validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(Joiner.on(",")
                .join(validator.getMessages(result))).addConstraintViolation();
        throw new ApplicationException("Registration error",
                context.getDefaultConstraintMessageTemplate());
    }
}
