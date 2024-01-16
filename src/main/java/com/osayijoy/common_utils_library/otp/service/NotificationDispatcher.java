package com.osayijoy.common_utils_library.otp.service;


import com.osayijoy.common_utils_library.commonUtils.util.ClientUtil;
import com.osayijoy.common_utils_library.otp.config.SoteriaKeeperConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationDispatcher {

   private final RabbitTemplate rabbitTemplate;

   private final SoteriaKeeperConfig soteriaKeeperConfig;

    //todo complete notification dispatcher method

    @Async
    public void dispatchEmail(NotificationServiceRequest notificationServiceRequest){
        notificationServiceRequest.setChannel(notificationServiceRequest.getChannel() == null || notificationServiceRequest.getChannel().isEmpty()?"EMAIL":notificationServiceRequest.getChannel());
            String json = ClientUtil.getGsonMapper().toJson(notificationServiceRequest);
            log.info("sending mail notification to rabbitmq :: <{}>", json);
            rabbitTemplate.convertAndSend(soteriaKeeperConfig.getQueueName(), json);

    }

    @Async
    public void dispatchSMS(NotificationServiceRequest notificationServiceRequest){

    }

}
