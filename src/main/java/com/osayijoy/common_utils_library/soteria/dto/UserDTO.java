package com.osayijoy.common_utils_library.soteria.dto;

import java.util.Set;

import com.osayijoy.common_utils_library.registhentication.registration.enums.Status;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements UserDetails {

    private String username;

    private String password;

    private String pin;

    private String name;

    private String email;

    private boolean isDefaultPassword;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    private int wrongPasswordTries;

    private int wrongPinTries;

    private RoleDTO roleDTO;

    private Status status;

    private Set<SimpleGrantedAuthority> authorities;

    private String notificationToken;

    private String deviceName;

}
