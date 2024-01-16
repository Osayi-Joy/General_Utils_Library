package com.osayijoy.common_utils_library.otp.service;


import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.otp.enums.OtpType;

public interface OtpService {

    default void send(NotificationServiceRequest notificationServiceRequest, OtpType otpType){
        throw new ZeusRuntimeException("No Implementation Found");
    }

    default String store(String identifier, OtpType otpType){
        throw new ZeusRuntimeException("No Implementation Found");
    }

    default void effect(String identifier, OtpType otpType, String code){
        throw new ZeusRuntimeException("No Implementation Found");
    }

    default void effectWithOutUpdate(String identifier, OtpType otpType, String code){
        throw new ZeusRuntimeException("No Implementation Found");
    }

    default void sendOTPToMobile(String phoneNumber,String firstName, OtpType otpType){
        throw new ZeusRuntimeException("No Implementation Found");
    }
}
