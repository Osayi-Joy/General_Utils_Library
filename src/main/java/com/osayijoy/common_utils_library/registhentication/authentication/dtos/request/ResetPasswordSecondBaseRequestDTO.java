package com.osayijoy.common_utils_library.registhentication.authentication.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osayijoy.common_utils_library.commonUtils.validator.RequestBodyChecker;
import lombok.*;

import static com.osayijoy.common_utils_library.registhentication.common.constants.RequestConstants.UPDATE_PASSWORD_IS_REQUIRED_MESSAGE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ResetPasswordSecondBaseRequestDTO extends ResetPasswordFirstBaseRequestDTO {
 @RequestBodyChecker(message = UPDATE_PASSWORD_IS_REQUIRED_MESSAGE)
 private String newPassword;
}
