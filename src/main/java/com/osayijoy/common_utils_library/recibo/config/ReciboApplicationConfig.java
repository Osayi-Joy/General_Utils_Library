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
@ConfigurationProperties(prefix = "digicore.recibo.application")
public class ReciboApplicationConfig {

    private String pathToFonts = "";
    private String htmlFileName = "transactionReceipt";
    private String templatesPath = "config/email/";

    private String logoUrl = "https://uat.digicoreltd.com/assets/logo/zest-logo.png";

    private float rectangleWidth = 456.0F;
    private float rectangleHeight = 842.0F;
    private float topIndent = 125.0F;
    private float rightIndent = 95.0F;
    private float bottomIndent = 50.0F;
    private float leftIndent = 95.0F;


}
