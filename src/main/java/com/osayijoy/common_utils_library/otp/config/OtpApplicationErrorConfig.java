package com.osayijoy.common_utils_library.otp.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class OtpApplicationErrorConfig {

    private String errorGenCodePrefix = "ERR_GEN_";

    private String inviteAlreadySentErrorCode = errorGenCodePrefix.concat("429");

    private String inviteAlreadySentErrorMessage = "Invite already sent";
}
