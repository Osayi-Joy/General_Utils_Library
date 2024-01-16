package com.osayijoy.common_utils_library.soteria.api_models.response;


import com.osayijoy.common_utils_library.soteria.enums.PermissionType;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityApiModel {
    private Long id;
    private String permission;
    private String permissionDescription;
    private PermissionType permissionType;
}