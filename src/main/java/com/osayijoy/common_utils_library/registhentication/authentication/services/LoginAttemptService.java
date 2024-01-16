package com.osayijoy.common_utils_library.registhentication.authentication.services;


import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.helper.response.ApiError;

import static com.osayijoy.common_utils_library.registhentication.authentication.exceptions.messages.LoginErrorMessages.IMPLEMENTATION_NOT_FOUND_VERIFY_ATTEMPT_CODE;
import static com.osayijoy.common_utils_library.registhentication.exceptions.messages.ErrorMessages.IMPLEMENTATION_NOT_FOUND_MESSAGE;

public interface LoginAttemptService {
 default void verifyLoginAccess(String username, boolean credentialMatches){
  throw new ZeusRuntimeException(new ApiError(IMPLEMENTATION_NOT_FOUND_MESSAGE,IMPLEMENTATION_NOT_FOUND_VERIFY_ATTEMPT_CODE));
 }
}
