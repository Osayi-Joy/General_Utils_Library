package com.osayijoy.common_utils_library.registhentication.common.dto.request;


import com.osayijoy.common_utils_library.commonUtils.validator.RequestBodyChecker;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.osayijoy.common_utils_library.registhentication.common.constants.RequestConstants.ROLE_IS_REQUIRED_MESSAGE;

@Setter
@Getter
@ToString
public class ThirdBaseRequestDTO extends SecondBaseRequestDTO {
 @RequestBodyChecker(message = ROLE_IS_REQUIRED_MESSAGE)
 private String assignedRole;
}
