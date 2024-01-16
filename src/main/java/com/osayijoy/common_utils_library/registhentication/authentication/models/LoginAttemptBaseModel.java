package com.osayijoy.common_utils_library.registhentication.authentication.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osayijoy.common_utils_library.registhentication.registration.models.Auditable;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginAttemptBaseModel extends Auditable<String> implements Serializable {

  private String username;
  private int failedAttemptCount;
  private boolean loginAccessDenied;
  private LocalDateTime automatedUnlockTime;
}
