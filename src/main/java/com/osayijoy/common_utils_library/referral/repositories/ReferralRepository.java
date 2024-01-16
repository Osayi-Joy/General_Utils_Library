package com.osayijoy.common_utils_library.referral.repositories;

import java.util.Optional;

import com.osayijoy.common_utils_library.referral.enums.ReferralStatus;
import com.osayijoy.common_utils_library.referral.enums.ReferralType;
import com.osayijoy.common_utils_library.referral.models.Referral;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {

    Page<Referral> findAllByReferralId(String referralId,Pageable pageable);

    long countByReferralIdAndReferralStatus(String referralId, ReferralStatus referralStatus);
    Optional<Referral> findByEmail(String email);
    Page<Referral> findAllByReferralType(ReferralType referralType, Pageable pageable);

}
