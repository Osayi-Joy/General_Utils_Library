package com.osayijoy.common_utils_library.registhentication.common.dto.request;

import com.osayijoy.common_utils_library.commonUtils.validator.RequestBodyChecker;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.osayijoy.common_utils_library.registhentication.common.constants.RequestConstants.*;

@Setter
@Getter
@ToString
public class FirstBaseRequestDTO {
    @RequestBodyChecker(message = USERNAME_IS_REQUIRED_MESSAGE)
    private String username;
    @RequestBodyChecker(message = EMAIL_IS_REQUIRED_MESSAGE,pattern = EMAIL_PATTERN)
    private String email;
}
