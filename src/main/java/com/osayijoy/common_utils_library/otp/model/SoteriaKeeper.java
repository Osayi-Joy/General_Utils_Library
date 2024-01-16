package com.osayijoy.common_utils_library.otp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osayijoy.common_utils_library.otp.enums.OtpStatus;
import com.osayijoy.common_utils_library.otp.enums.OtpType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "soteria_keeper")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SoteriaKeeper {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String requester;

    private String code;

    @Enumerated(EnumType.STRING)
    private OtpStatus otpStatus;

    @Enumerated(EnumType.STRING)
    private OtpType otpType;

    private long attempt = 0;
    private LocalDateTime createdOn;
}
