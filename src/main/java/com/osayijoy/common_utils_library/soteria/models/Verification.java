package com.osayijoy.common_utils_library.soteria.models;//package com.digicore.soteria.models;
//
//import com.digicore.otp.enums.VerificationStatus;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "verification")
//@JsonIgnoreProperties(
//        ignoreUnknown = true
//)
//@Getter
//@Setter
//public class Verification extends BaseUserModel {
//
//    @Column(name = "email_verification_status", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private VerificationStatus emailVerificationStatus;
//
//    @Column(name = "phone_number_verification_status", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private VerificationStatus phoneNumberVerificationStatus;
//
//    @Column(name = "bvn_verification_status", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private VerificationStatus bvnVerificationStatus;
//
//}
