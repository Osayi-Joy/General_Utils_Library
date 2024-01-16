package com.osayijoy.common_utils_library.soteria.repositories;

import java.util.Optional;

import com.osayijoy.common_utils_library.soteria.models.Role;
import com.osayijoy.common_utils_library.soteria.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndRoles(String username, Role role);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndRoles(String username, Role role);

    boolean existsByEmail(String email);

    boolean existsByEmailAndRoles(String email, Role role);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndRoles(String email, Role role);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndRoles(String phoneNumber, Role role);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndRoles(String phoneNumber, Role role);

    @Query("SELECT o.roles from User o where o.username = :username")
    Role getRoleNameForUser(String username);
}
