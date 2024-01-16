package com.osayijoy.common_utils_library.soteria.models;
import com.osayijoy.common_utils_library.soteria.enums.SystemConfigurationGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "soteria_system_configurations")
public class SystemConfiguration extends BaseModel {

    @Column(name = "config_key")
    private String key;

    @Column(name = "config_group")
    @Enumerated(EnumType.STRING)
    private SystemConfigurationGroup group;

    @Column(name = "config_value")
    private String value;

    private String description;
}
