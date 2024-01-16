package com.osayijoy.common_utils_library.soteria.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osayijoy.common_utils_library.soteria.enums.ChannelMode;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "soteria_devices")
public class Device extends BaseModel {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "soteria_user_id")
    private User user;

    private String identifier;

    private String fingerprintCipher;
    private String imei;

    private String model;

    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    private ChannelMode deviceType;

}
