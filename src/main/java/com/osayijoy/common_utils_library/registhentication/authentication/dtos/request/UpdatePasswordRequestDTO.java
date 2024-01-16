package com.osayijoy.common_utils_library.registhentication.authentication.dtos.request;

import com.osayijoy.common_utils_library.commonUtils.validator.RequestBodyChecker;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.osayijoy.common_utils_library.registhentication.common.constants.RequestConstants.OLD_PASSWORD_IS_REQUIRED_MESSAGE;
import static com.osayijoy.common_utils_library.registhentication.common.constants.RequestConstants.UPDATE_PASSWORD_IS_REQUIRED_MESSAGE;

/**
 * @author Joy Osayi
 * @createdOn Sep-08(Fri)-2023
 */
@Setter
@Getter
@ToString
public class UpdatePasswordRequestDTO {
    @RequestBodyChecker(message = OLD_PASSWORD_IS_REQUIRED_MESSAGE)
    private String oldPassword;
    @RequestBodyChecker(message = UPDATE_PASSWORD_IS_REQUIRED_MESSAGE)
    private String newPassword;
}
