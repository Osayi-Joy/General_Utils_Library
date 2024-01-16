package com.osayijoy.common_utils_library.processor.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-12(Mon)-2022
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogActivity {
    String activity();
    String auditType() default "N/A";

    String auditDescription();
}
