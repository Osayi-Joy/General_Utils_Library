package com.osayijoy.common_utils_library.registhentication.registration.services;


import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.helper.response.ApiError;

import static com.osayijoy.common_utils_library.registhentication.exceptions.messages.ErrorMessages.IMPLEMENTATION_NOT_FOUND_MESSAGE;
import static com.osayijoy.common_utils_library.registhentication.registration.exceptions.messages.NotificationErrorMessages.IMPLEMENTATION_NOT_FOUND_SEND_VERIFICATION_CODE_TO_EMAIL_CODE;
import static com.osayijoy.common_utils_library.registhentication.registration.exceptions.messages.NotificationErrorMessages.IMPLEMENTATION_NOT_FOUND_SEND_VERIFICATION_CODE_TO_PHONE_CODE;

public interface NotificationService<T,V>  {

   default T sendVerificationCodeToPhone(V request){
       throw new ZeusRuntimeException(new ApiError(IMPLEMENTATION_NOT_FOUND_MESSAGE,IMPLEMENTATION_NOT_FOUND_SEND_VERIFICATION_CODE_TO_PHONE_CODE));
   }
    default T sendVerificationCodeToEmail(V request){
        throw new ZeusRuntimeException(new ApiError(IMPLEMENTATION_NOT_FOUND_MESSAGE,IMPLEMENTATION_NOT_FOUND_SEND_VERIFICATION_CODE_TO_EMAIL_CODE));
    }
}
