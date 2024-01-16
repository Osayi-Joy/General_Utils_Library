package com.osayijoy.common_utils_library.referral.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

import com.osayijoy.common_utils_library.referral.enums.ReferralStatus;
import com.osayijoy.common_utils_library.referral.enums.ReferralType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferralDto {

    private String email;

    private String referralCode;

    private String customerName;

    private String referralName;

    private ReferralType referralType;

    private ReferralStatus referralStatus;

    private LocalDateTime referralDate;



    private String referralId;
}
