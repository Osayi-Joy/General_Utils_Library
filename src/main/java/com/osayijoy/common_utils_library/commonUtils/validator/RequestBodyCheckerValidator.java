package com.osayijoy.common_utils_library.commonUtils.validator;

import com.osayijoy.common_utils_library.commonUtils.util.MoneyUtil;
import com.osayijoy.common_utils_library.commonUtils.validator.enums.Currency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * @author Oluwatobi Ogunwuyi
 * @since May-06(Sat)-2023
 */
public class RequestBodyCheckerValidator implements ConstraintValidator<RequestBodyChecker, String> {
    private String pattern;

    private boolean dateOfBirthCheck;
    private boolean exportFormatCheck;
    private boolean currencyCheck;
    private boolean amountCheck;

    public RequestBodyCheckerValidator() {
    }

    public void initialize(RequestBodyChecker constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
        this.dateOfBirthCheck = constraintAnnotation.dateOfBirthCheck();
        this.exportFormatCheck = constraintAnnotation.exportFormatCheck();
        this.currencyCheck = constraintAnnotation.currencyCheck();
        this.amountCheck = constraintAnnotation.amountCheck();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null && !value.isBlank()) {
            try {
                if (this.amountCheck) {
                    MoneyUtil.convertMinorToMajor(Long.valueOf(value));
                }
            } catch (Exception var4) {
                return false;
            }
            try {
                if (this.currencyCheck) {
                    Currency.valueOf(value);
                }
            } catch (IllegalArgumentException var4) {
                return false;
            }
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
}
