package com.osayijoy.common_utils_library.registhentication.authentication.dtos.request;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osayijoy.common_utils_library.registhentication.authentication.enums.AuthenticationType;
import com.osayijoy.common_utils_library.registhentication.common.dto.request.FirstBaseRequestDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class LoginRequestDTO extends FirstBaseRequestDTO {
  private String password;
  private String fingerPrintHash;
  private String otp;
  private String token;
  private String pin;
  private AuthenticationType authenticationType;
}
