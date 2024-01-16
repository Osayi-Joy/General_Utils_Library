package com.osayijoy.common_utils_library.recibo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;



@Getter
@Setter
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "digicore.recibo.application.failure")
public class ReciboApplicationErrorConfig {

    private String errorContextCodePrefix = "ERR_CTX_";
    private String errorGenCodePrefix = "ERR_GEN_";
    private String adminHelpSuggestionMessage = "Please contact administrator for help";
    private String contextBuildingFailedErrorCode = errorContextCodePrefix.concat("700");
    private String contextBuildingFailedErrorMessage = "Oops, Apologies we ran into a technical error and the Admin has been notified";
}
