package com.osayijoy.common_utils_library.registhentication.common.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

import com.osayijoy.common_utils_library.registhentication.common.dto.request.ThirdBaseRequestDTO;
import com.osayijoy.common_utils_library.registhentication.registration.enums.Status;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ProfileDTO extends ThirdBaseRequestDTO {

    private Status status;

    private boolean isDeleted;

    private LocalDateTime lastLoginDate;

    private String pin;


    private boolean isDefaultPassword;

    private String profileId;

    private String organizationId;

}
