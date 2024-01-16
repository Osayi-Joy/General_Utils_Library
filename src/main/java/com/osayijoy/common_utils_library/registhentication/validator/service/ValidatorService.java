package com.osayijoy.common_utils_library.registhentication.validator.service;


public interface ValidatorService<T,V> {

    T validatePhoneVerificationCode(V request);
    T validateEmailVerificationCode(V request);

    T validatePassword(V request);
}
