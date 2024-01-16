package com.osayijoy.common_utils_library.registhentication.registration.services;


import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.helper.response.ApiError;

import static com.osayijoy.common_utils_library.registhentication.exceptions.messages.ErrorMessages.IMPLEMENTATION_NOT_FOUND_MESSAGE;
import static com.osayijoy.common_utils_library.registhentication.registration.exceptions.messages.RegistrationErrorMessages.IMPLEMENTATION_NOT_FOUND_CREATE_USER_PROFILE_CODE;
import static com.osayijoy.common_utils_library.registhentication.registration.exceptions.messages.RegistrationErrorMessages.IMPLEMENTATION_NOT_FOUND_PROFILE_CHECKS_CODE;

public interface RegistrationService<T,V> {

    default T createProfile(V request){
         throw new ZeusRuntimeException(new ApiError(IMPLEMENTATION_NOT_FOUND_MESSAGE,IMPLEMENTATION_NOT_FOUND_CREATE_USER_PROFILE_CODE));
    }


    default boolean profileExistenceCheckByEmail(String email){
        throw new ZeusRuntimeException(new ApiError(IMPLEMENTATION_NOT_FOUND_MESSAGE,IMPLEMENTATION_NOT_FOUND_PROFILE_CHECKS_CODE));
    }
}
