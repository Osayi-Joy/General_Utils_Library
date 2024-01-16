package com.osayijoy.common_utils_library.otp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-10(Fri)-2023
 */
@Configuration
@RequiredArgsConstructor
public class OtpBeanConfig {

    private final SoteriaKeeperConfig soteriaKeeperConfig;

    @Bean
    public Queue myQueue() {
        return new Queue(soteriaKeeperConfig.getQueueName(), false);
    }


}
