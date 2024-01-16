package com.osayijoy.common_utils_library.referral.services;


import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.referral.dto.ReferralDto;

public interface ReferralService {
    default void saveReferral(ReferralDto referralDTO) {
        throw new ZeusRuntimeException("No implementation written to save referral");
    }

    default void updateReferralStatus(String email) {
        throw new ZeusRuntimeException("No implementation written to update referral");
    }
}
