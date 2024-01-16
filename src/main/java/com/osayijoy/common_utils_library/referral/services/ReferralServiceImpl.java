package com.osayijoy.common_utils_library.referral.services;
import java.time.LocalDateTime;
import java.util.Optional;

import com.osayijoy.common_utils_library.referral.dto.ReferralDto;
import com.osayijoy.common_utils_library.referral.enums.ReferralStatus;
import com.osayijoy.common_utils_library.referral.models.Referral;
import com.osayijoy.common_utils_library.referral.repositories.ReferralRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ReferralServiceImpl implements ReferralService{

    private final ReferralRepository referralRepository;

    @Override
    public void saveReferral(ReferralDto referralDTO){
        Optional<Referral> optionalReferral = referralRepository.findByEmail(referralDTO.getEmail());
        if (optionalReferral.isPresent()){
            return;
        }
        Referral referral = new Referral();
        referral.setReferralDate(LocalDateTime.now());
        referral.setReferralStatus(ReferralStatus.PENDING);
        referral.setReferralId(referralDTO.getReferralId());
        referral.setReferralName(referralDTO.getReferralName());
        referral.setReferralType(referralDTO.getReferralType());
        referral.setEmail(referralDTO.getEmail());
        referral.setCustomerName(referralDTO.getCustomerName());

        referralRepository.save(referral);

    }


    @Override
    public void updateReferralStatus(String email){
        Optional<Referral> referral =  referralRepository.findByEmail(email);
        if (referral.isPresent()) {
            referral.get().setReferralDate(LocalDateTime.now());
            referral.get().setReferralStatus(ReferralStatus.ACTIVE);
            referralRepository.save(referral.get());
        }

    }




}
