package com.osayijoy.common_utils_library.registhentication.registration.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public  class BaseProfileModel extends BaseUserModel implements Serializable {
    @Column(unique = true)
    private String referralCode;

    @Column(unique = true)
    private String profileId;
}
