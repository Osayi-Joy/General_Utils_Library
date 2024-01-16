package com.osayijoy.common_utils_library.soteria.api_models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.HashMap;
import java.util.Map;

import com.osayijoy.common_utils_library.soteria.enums.DeviceStatus;
import lombok.*;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginApiModel {

    private String token;
    private DeviceStatus deviceStatus;
    private Map<String, Object> additionalInformation = new HashMap<>();

}
