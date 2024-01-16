package com.osayijoy.common_utils_library.registhentication.common.dto.request;

import com.osayijoy.common_utils_library.commonUtils.validator.RequestBodyChecker;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.osayijoy.common_utils_library.registhentication.common.constants.RequestConstants.EMAIL_IS_REQUIRED_MESSAGE;
import static com.osayijoy.common_utils_library.registhentication.common.constants.RequestConstants.EMAIL_PATTERN;


@Setter
@Getter
@ToString
public class SixthBaseRequestDTO {
 @RequestBodyChecker(message = EMAIL_IS_REQUIRED_MESSAGE,pattern = EMAIL_PATTERN)
 private String email;

}
