package com.osayijoy.common_utils_library.soteria.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VerifyUserDTO {

    private String email;

    private String otp;

    private String phoneNumber;
}
