package com.osayijoy.common_utils_library.soteria.dto;

import com.osayijoy.common_utils_library.soteria.enums.ChannelMode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class RegisterDeviceDTO {

    private String identifier;
    private String fingerprintCipher;
    private String imei;
    private String model;
    private ChannelMode deviceType;
    private String otp;

    private String role;
}
