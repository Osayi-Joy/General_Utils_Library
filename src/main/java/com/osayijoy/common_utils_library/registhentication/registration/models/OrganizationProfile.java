package com.osayijoy.common_utils_library.registhentication.registration.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import lombok.*;

@MappedSuperclass
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public  class OrganizationProfile extends BaseModel implements Serializable {
    @Column(unique = true,nullable = false)
    private String organizationName;
    @Column(unique = true,nullable = false)
    private String organizationEmail;
    @Column(unique = true,nullable = false)
    private String organizationId;
}
