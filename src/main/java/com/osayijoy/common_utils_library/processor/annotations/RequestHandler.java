package com.osayijoy.common_utils_library.processor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.osayijoy.common_utils_library.processor.enums.RequestHandlerType;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-12(Mon)-2022
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RequestHandler {
    String name() default "";

    RequestHandlerType type();
}
