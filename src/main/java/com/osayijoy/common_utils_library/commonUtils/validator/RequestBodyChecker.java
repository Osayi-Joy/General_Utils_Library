package com.osayijoy.common_utils_library.commonUtils.validator;



import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Oluwatobi Ogunwuyi
 * @since May-06(Sat)-2023
 */

@Documented
@Constraint(
        validatedBy = {RequestBodyCheckerValidator.class}
)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestBodyChecker {

    String message() default "Invalid field";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String pattern() default "";

    boolean dateOfBirthCheck() default false;

    boolean exportFormatCheck() default false;
    boolean currencyCheck() default false;
    boolean amountCheck() default false;
    boolean requestSourceCheck() default false;

    boolean passwordCheck() default  false;
}
