package com.osayijoy.common_utils_library.soteria.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString
public class RecoveryPasswordDTO {

    private String otp;
    private String recoveryKey;

    private String oldPassword;

    private String newPassword;

    private String username;
}
