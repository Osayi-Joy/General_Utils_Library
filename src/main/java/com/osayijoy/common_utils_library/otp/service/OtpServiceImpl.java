package com.osayijoy.common_utils_library.otp.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.otp.config.SoteriaKeeperConfig;
import com.osayijoy.common_utils_library.otp.enums.OtpStatus;
import com.osayijoy.common_utils_library.otp.enums.OtpType;
import com.osayijoy.common_utils_library.otp.model.SoteriaKeeper;
import com.osayijoy.common_utils_library.otp.repository.SoteriaKeeperRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService{

    private final SoteriaKeeperRepository otpRepository;
    private final SoteriaKeeperConfig propertyConfig;

    private final NotificationDispatcher notificationDispatcher;





    public void send(NotificationServiceRequest notificationServiceRequest, OtpType otpType){

        closeExistingCode(notificationServiceRequest.getRecipients().get(0), otpType);

        SoteriaKeeper otp = new SoteriaKeeper();
        String code;
        if (OtpType.CUSTOMER_REGISTRATION.equals(otpType)){
            code = generateCode();
            notificationServiceRequest.setRegistrationCode(code);
            otp.setCreatedOn(LocalDateTime.now().plusMonths(1L));//to be refactored later
        }else {
            code = generateOtpCode();
            notificationServiceRequest.setUserCode(code);
            otp.setCreatedOn(LocalDateTime.now());
        }
        otp.setCode(code);
        otp.setOtpStatus(OtpStatus.OPEN);
        otp.setRequester(notificationServiceRequest.getRecipients().get(0));
        otp.setOtpType(otpType);
        otpRepository.save(otp);

            notificationDispatcher.dispatchEmail(notificationServiceRequest);
    }

    private void closeExistingCode(String identifier, OtpType otpType) {
        Optional<SoteriaKeeper> optionalOtp = otpRepository.findByRequesterAndOtpStatusAndOtpType(identifier, OtpStatus.OPEN, otpType);

        if(optionalOtp.isPresent()){
            SoteriaKeeper previousOtp = optionalOtp.get();
            previousOtp.setOtpStatus(OtpStatus.CLOSED);
            otpRepository.save(previousOtp);
        }
    }


    public String store(String identifier,OtpType otpType){
        closeExistingCode(identifier,otpType);
        SoteriaKeeper otp = new SoteriaKeeper();
        String code = generateCode();
        otp.setCode(code);
        otp.setOtpStatus(OtpStatus.OPEN);
        otp.setRequester(identifier);
        otp.setOtpType(otpType);
        otp.setCreatedOn(LocalDateTime.now().plusMonths(1L));//to be refactored later
        otpRepository.save(otp);
        return code;

    }


    public void effect(String identifier, OtpType otpType, String code){

        Optional<SoteriaKeeper> optionalOtp = otpRepository.findByRequesterAndOtpStatusAndOtpType(identifier, OtpStatus.OPEN, otpType);

        if(optionalOtp.isEmpty()){
            throw new ZeusRuntimeException("Code verification failed", HttpStatus.BAD_REQUEST, List.of(new ApiError("Code verification failed","09")));
        }

        SoteriaKeeper otp = optionalOtp.get();
        otp.setAttempt(otp.getAttempt() + 1);
        otpRepository.save(otp);

        long maxAttempt = propertyConfig.getIncorrectLimit();
        doChecks(otp);
        if(!otp.getCode().equals(code)){
            if(otp.getAttempt() == maxAttempt){
                otp.setOtpStatus(OtpStatus.CLOSED);
                otpRepository.save(otp);
                throw new ZeusRuntimeException("Code is no longer valid", HttpStatus.BAD_REQUEST, List.of(new ApiError("Code is no longer valid","09")));

            }
            throw new ZeusRuntimeException(String.format("Invalid Code, You have %d more incorrect attempts", maxAttempt - otp.getAttempt()), HttpStatus.BAD_REQUEST, List.of(new ApiError(String.format("Invalid Code, You have %d more incorrect attempts", maxAttempt - otp.getAttempt()),"09")));

        }
        otp.setOtpStatus(OtpStatus.COMPLETE);
        otpRepository.save(otp);
    }

    public void effectWithOutUpdate(String identifier, OtpType otpType, String code){

        Optional<SoteriaKeeper> optionalOtp = otpRepository.findByRequesterAndOtpStatusAndOtpType(identifier, OtpStatus.OPEN, otpType);

        if(optionalOtp.isEmpty()){
            throw new ZeusRuntimeException("Code verification failed", HttpStatus.BAD_REQUEST, List.of(new ApiError("Code verification failed","09")));
        }

        SoteriaKeeper otp = optionalOtp.get();

        long maxAttempt = propertyConfig.getIncorrectLimit();
        doChecks(otp);
        if(!otp.getCode().equals(code)){
            if(otp.getAttempt() == maxAttempt){
                otp.setOtpStatus(OtpStatus.CLOSED);
                otpRepository.save(otp);
                throw new ZeusRuntimeException("Code is no longer valid", HttpStatus.BAD_REQUEST, List.of(new ApiError("Code is no longer valid","09")));
            }

            otp.setAttempt(otp.getAttempt() + 1);
            otpRepository.save(otp);
            throw new ZeusRuntimeException(String.format("Invalid Code, You have %d more incorrect attempts", maxAttempt - otp.getAttempt()), HttpStatus.BAD_REQUEST, List.of(new ApiError(String.format("Invalid Code, You have %d more incorrect attempts", maxAttempt - otp.getAttempt()),"09")));
        }
    }


    private String generateOtpCode() {
        return RandomStringUtils.randomNumeric(propertyConfig.getOtpLength());
    }

    private String generateCode() {
        return RandomStringUtils.randomAlphanumeric(18);
    }

    private void doChecks(SoteriaKeeper otp){
        if(OtpStatus.EXPIRED.equals(otp.getOtpStatus())){
            throw new ZeusRuntimeException("Code has expired", HttpStatus.BAD_REQUEST, List.of(new ApiError("Code has expired","09")));
        }

        if(OtpStatus.CLOSED.equals(otp.getOtpStatus()) || OtpStatus.COMPLETE.equals(otp.getOtpStatus())){

            throw new ZeusRuntimeException("Code is no longer valid", HttpStatus.BAD_REQUEST, List.of(new ApiError("Code is no longer valid","09")));
        }

        if(otp.getCreatedOn().plusMinutes(propertyConfig.getExpiryTimeInMinutes()).isBefore(LocalDateTime.now())){
            otp.setOtpStatus(OtpStatus.EXPIRED);
            otpRepository.save(otp);
            throw new ZeusRuntimeException("Code has expired", HttpStatus.BAD_REQUEST, List.of(new ApiError("Code has expired","09")));
        }
    }
}
