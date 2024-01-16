package com.osayijoy.common_utils_library.soteria.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osayijoy.common_utils_library.soteria.enums.RoleScope;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(
        name = "soteria_roles"
)
@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Role extends BaseModel {
    @Column(nullable = false, unique = true)
    @NotEmpty(message = "required")
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private RoleScope roleScope;

    @OneToMany(mappedBy = "roles")
    @ToString.Exclude
    private  Collection<User> users;

    @Column(nullable = false)
    @NotEmpty(message = "required")
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
            name = "roles_authority",
            joinColumns = {@JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "authority_id",
                    referencedColumnName = "id"
            )}
    )
    @ToString.Exclude
    private Collection<Authority> permissions = new HashSet<>();

    @Column(name = "active")
    private boolean active;

    @Column(name = "date_created")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Column(name = "date_modified")
    private LocalDateTime dateModified = LocalDateTime.now();
}
