package com.osayijoy.common_utils_library.otp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-3(Fri)-2023
 */
@Getter
@Setter
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "digicore.soteria.keeper")
public class SoteriaKeeperConfig {

    private Long expiryTimeInMinutes = 5L;
    private Long incorrectLimit = 3L;

    private String altSMSUrl = "https://sms.hubtel.com/v1/messages/send?clientsecret=easczzxi&clientid=qymmctkx&from=ENTLIFE&to={phoneNumber}&content={code}";

    private String queueName = "irisNotification";
    private int otpLength = 6;


}
