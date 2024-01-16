package com.osayijoy.common_utils_library.otp.repository;

import java.util.Optional;

import com.osayijoy.common_utils_library.otp.enums.OtpStatus;
import com.osayijoy.common_utils_library.otp.enums.OtpType;
import com.osayijoy.common_utils_library.otp.model.SoteriaKeeper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoteriaKeeperRepository extends JpaRepository<SoteriaKeeper, Long> {

    Optional<SoteriaKeeper> findByRequesterAndOtpStatusAndOtpType(String requester, OtpStatus otpStatus, OtpType otpType);

    Optional<SoteriaKeeper> findByRequesterAndOtpStatusAndOtpTypeAndCode(String user, OtpStatus otpStatus, OtpType otpType, String code);

}
