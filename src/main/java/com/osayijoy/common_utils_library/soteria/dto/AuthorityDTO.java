package com.osayijoy.common_utils_library.soteria.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osayijoy.common_utils_library.soteria.enums.PermissionType;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorityDTO {

    private String permission;
    private String permissionDescription;
    private PermissionType permissionType;
}
