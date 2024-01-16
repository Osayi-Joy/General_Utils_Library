package com.osayijoy.common_utils_library.soteria.api_models.request;

import com.osayijoy.common_utils_library.soteria.enums.SystemConfigurationGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AddSystemConfigurationRequest {
    private String value;

    private String description;

    private SystemConfigurationGroup group;
}
