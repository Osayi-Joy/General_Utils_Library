package com.osayijoy.common_utils_library.soteria.api_models.request;

import com.osayijoy.common_utils_library.soteria.enums.AuthenticationType;
import com.osayijoy.common_utils_library.soteria.enums.ChannelMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class UsernameAndPasswordAuthenticationRequest {

    private String username;

    private String password;

    private String pin;

    private String fingerPrintCipher;

    private String imei;

    private String deviceIdentifier;

    private String otp;

    private ChannelMode channelMode = ChannelMode.WEB;

    private AuthenticationType authenticationType = AuthenticationType.PASSWORD;

    private String notificationToken;

    private String role;

}