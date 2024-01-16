package com.osayijoy.common_utils_library.registhentication.validator.service.implementations;

import com.osayijoy.common_utils_library.commonUtils.validator.RequestBodyChecker;
import com.osayijoy.common_utils_library.commonUtils.validator.enums.Currency;
import com.osayijoy.common_utils_library.registhentication.properties_configuration.ValidatorConfigurations;
import com.osayijoy.common_utils_library.registhentication.util.AESUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestBodyCheckerValidator implements ConstraintValidator<RequestBodyChecker, String> {

   private final ValidatorConfigurations validatorConfigurations;
    private String pattern;

    private boolean dateOfBirthCheck;
    private boolean exportFormatCheck;
    private boolean currencyCheck;
    private boolean requestSourceCheck;
    private boolean passwordCheck;



    public void initialize(RequestBodyChecker constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
        this.dateOfBirthCheck = constraintAnnotation.dateOfBirthCheck();
        this.exportFormatCheck = constraintAnnotation.exportFormatCheck();
        this.currencyCheck = constraintAnnotation.currencyCheck();
        this.requestSourceCheck = constraintAnnotation.requestSourceCheck();
        this.passwordCheck = constraintAnnotation.passwordCheck();
    }


    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null && !value.isBlank() && !value.equalsIgnoreCase("string")) {
            if (this.passwordCheck && validatorConfigurations.isShouldValidatePassword()){
                return !this.pattern.isEmpty() || value.matches(this.pattern);
            }
            if (!currencyIsValid(value)) return false;
            if (this.dateOfBirthCheck) {
                return LocalDate.parse(value).isBefore(LocalDate.now().minusYears(18L));
            } else if (this.exportFormatCheck) {
                return value.equalsIgnoreCase("CSV") || value.equalsIgnoreCase("PDF");
            } else {
                return this.pattern.isEmpty() || value.matches(this.pattern);
            }
        } else {
            return false;
        }
    }

    private boolean currencyIsValid(String value) {
        try {
            if (this.currencyCheck) {
                Currency.valueOf(value);
            }
        } catch (IllegalArgumentException var4) {
            return false;
        }
        return true;
    }

    private boolean requestSourceIsValid(String value) {
        if (requestSourceCheck && validatorConfigurations.isShouldValidateRequestLocation()){
          String[] requestPlatform =  AESUtil.decrypt(value).split("_");
            switch (requestPlatform[0]) {
                case "3MNT001" -> {
                    return isSecretValid(validatorConfigurations.getWebPlatformSecret(),requestPlatform[1]);
                }
                case "4MNT001" -> {
                   return isSecretValid(validatorConfigurations.getMobilePlatformSecret(),requestPlatform[1]);
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isSecretValid(String secret, String passedSecret) {
        return secret.equals(passedSecret);
    }
}
