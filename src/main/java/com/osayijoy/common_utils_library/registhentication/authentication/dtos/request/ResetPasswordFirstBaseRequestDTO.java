package com.osayijoy.common_utils_library.registhentication.authentication.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osayijoy.common_utils_library.registhentication.common.dto.request.SixthBaseRequestDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ResetPasswordFirstBaseRequestDTO extends SixthBaseRequestDTO {
    private String otp;

}
