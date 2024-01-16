package com.osayijoy.common_utils_library.registhentication.registration.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osayijoy.common_utils_library.registhentication.registration.enums.Status;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@MappedSuperclass
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public  class UserProfile extends BaseModel{

    @Column(name = "username",unique = true,nullable = false)
    private String username;

    private String password;

    private String pin;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    @Column(name = "is_default_password",nullable = false)
    private boolean isDefaultPassword;
}
