package com.osayijoy.common_utils_library.soteria.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "soteria_users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseUserModel {

    @NotNull
    @NotEmpty
    @Column(name = "username")
    private String username;


    @NotNull
    @NotEmpty
    private String password;

    private String pin;

    private int wrongPasswordTries;

    private int wrongPinTries;

    @NotNull
    @Column(name = "is_default_password")
    private boolean isDefaultPassword;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "soteria_users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    @ToString.Exclude
    private  Role roles;
}
