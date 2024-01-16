package com.osayijoy.common_utils_library.soteria.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "soteria_user_login_attempts")
public class UserLoginAttempt extends BaseModel {

    private String username;

    private int failedAttempts;

    private boolean locked;

}
