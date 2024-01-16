package com.osayijoy.common_utils_library.registhentication.authentication.services;

import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.helper.response.ApiError;
import com.osayijoy.common_utils_library.registhentication.authentication.dtos.request.ResetPasswordSecondBaseRequestDTO;
import com.osayijoy.common_utils_library.registhentication.authentication.dtos.request.UpdatePasswordRequestDTO;

import static com.osayijoy.common_utils_library.registhentication.authentication.exceptions.messages.LoginErrorMessages.*;
import static com.osayijoy.common_utils_library.registhentication.exceptions.messages.ErrorMessages.IMPLEMENTATION_NOT_FOUND_MESSAGE;

public interface PasswordResetService {


    default void updateAccountPassword(ResetPasswordSecondBaseRequestDTO passwordFirstBaseRequestDTO){
        throw new ZeusRuntimeException(new ApiError(IMPLEMENTATION_NOT_FOUND_MESSAGE,IMPLEMENTATION_NOT_FOUND_UPDATE_ACCOUNT_PASSWORD_CODE));
    }

    default void updateAccountPasswordWithoutVerification(String email, String password){
        throw new ZeusRuntimeException(new ApiError(IMPLEMENTATION_NOT_FOUND_MESSAGE,IMPLEMENTATION_NOT_FOUND_UPDATE_ACCOUNT_PASSWORD_WITHOUT_VERIFICATION_CODE));
    }
    default void updateAccountPassword(UpdatePasswordRequestDTO updatePasswordRequestDTO){
        throw new ZeusRuntimeException(new ApiError(IMPLEMENTATION_NOT_FOUND_MESSAGE,IMPLEMENTATION_NOT_FOUND_UPDATE_MY_PASSWORD_CODE));
    }

}
