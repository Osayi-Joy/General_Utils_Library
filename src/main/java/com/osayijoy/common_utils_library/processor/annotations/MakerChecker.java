package com.osayijoy.common_utils_library.processor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Oct-28(Fri)-2022
 */

@Target({ElementType.METHOD,ElementType.TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MakerChecker {
    String makerPermission();
    String checkerPermission();
    String requestClassName() default "";
    boolean requestContainsFile() default false;



}
