package com.osayijoy.common_utils_library.otp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class OtpVerificationRequest {

    @NotNull(message = "Email is Required")
    @NotEmpty(message = "Email is Required")
    @NotBlank(message = "Email is Required")
    private String email;

    @NotNull(message = "FirstName is Required")
    @NotEmpty(message = "FirstName is Required")
    @NotBlank(message = "FirstName is Required")
    private String firstName;

    @NotNull(message = "OTP is Required")
    @NotEmpty(message = "OTP is Required")
    @NotBlank(message = "OTP is Required")
    private String otp;


    private String phoneNumber;

}
