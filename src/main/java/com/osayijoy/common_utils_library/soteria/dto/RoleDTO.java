package com.osayijoy.common_utils_library.soteria.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDTO {

    private String roleName;
    private List<AuthorityDTO> permissions;
    private String description;

}
