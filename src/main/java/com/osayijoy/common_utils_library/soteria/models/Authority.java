package com.osayijoy.common_utils_library.soteria.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osayijoy.common_utils_library.soteria.enums.PermissionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(
        name = "soteria_authorities"
)
@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Getter
@Setter
@NoArgsConstructor
public class Authority extends BaseModel {
    @NotNull
    @NotEmpty
    private String permission;
    private String description;

    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;

    @ManyToMany(mappedBy = "permissions")
    private Collection<Role> roles;

    @Column(name = "date_created")
    private LocalDateTime dateCreated = LocalDateTime.now();;

    @Column(name = "date_modified")
    private LocalDateTime dateModified = LocalDateTime.now();;

    public Authority(String permission,String description) {
        this.permission = permission;
        this.description = description;

    }
}
