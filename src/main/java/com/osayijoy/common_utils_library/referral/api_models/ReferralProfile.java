package com.osayijoy.common_utils_library.referral.api_models;

import java.math.BigDecimal;
import lombok.*;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-25(Sat)-2023
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferralProfile {

    private String referralCode;
    private long   totalReferredCustomers;

    private BigDecimal totalEarnings;
}
