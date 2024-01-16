package com.osayijoy.common_utils_library.referral.api_models;

import java.time.LocalDateTime;

import com.osayijoy.common_utils_library.referral.enums.ReferralStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReferralApiModel {
    private String fullName;
    private String email;
    private String referralName;
    private ReferralStatus referralStatus;
    private String referralCode;
    private LocalDateTime referralDate;
}
