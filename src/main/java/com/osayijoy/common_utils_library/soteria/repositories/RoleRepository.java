package com.osayijoy.common_utils_library.soteria.repositories;


import java.util.List;

import com.osayijoy.common_utils_library.soteria.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String role);

    @Query(nativeQuery =true,value = "SELECT * FROM soteria_roles  WHERE active = :active and role_scope IN (:roleScopes)")
    List<Role> findAllByRoleScopeAndIsActive(@Param("roleScopes") List<String> roleScopes, @Param("active") Boolean active);


    @Query(nativeQuery =true,value = "SELECT * FROM soteria_roles  WHERE name IN (:roles)")
    List<Role> findAllByRoleName(@Param("roles") List<String> roles);

    @Query(value = "SELECT * FROM soteria_roles WHERE active = :active", nativeQuery = true)
    List<Role> findAllRoles(Boolean active);

}
