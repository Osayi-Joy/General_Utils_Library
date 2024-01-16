package com.osayijoy.common_utils_library.soteria.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;



@Getter
@Setter
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "digicore.soteria.application.message")
public class SoteriaApplicationSuccessConfig {

    private String successCodePrefix = "SUC_AUT_";

    private String loginSuccessCode = successCodePrefix.concat("220");
    private String loginSuccessMessage = "Login Successful";



}
