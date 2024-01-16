package com.osayijoy.common_utils_library.soteria.api_models.response;

import java.util.List;
import lombok.*;

/**
 * Created by Oluwatobi ogunwuyi on 24/07/2022
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleApiModel {
    private String roleName;
    private String roleDescription;
    private List<String> permissions;
}
