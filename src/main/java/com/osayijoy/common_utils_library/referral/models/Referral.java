package com.osayijoy.common_utils_library.referral.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osayijoy.common_utils_library.referral.enums.ReferralStatus;
import com.osayijoy.common_utils_library.referral.enums.ReferralType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "referral")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String email;

    private String referralName;

    private String referralId;

    private String customerName;

    @Enumerated(EnumType.STRING)
    private ReferralStatus referralStatus;

    @Enumerated(EnumType.STRING)
    private ReferralType referralType;

    private LocalDateTime referralDate;

}
