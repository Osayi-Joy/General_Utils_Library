package com.osayijoy.common_utils_library.soteria.repositories;

import java.util.Optional;

import com.osayijoy.common_utils_library.soteria.models.UserLoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginAttemptRepository extends JpaRepository<UserLoginAttempt, Long>, JpaSpecificationExecutor<UserLoginAttempt> {


    Optional<UserLoginAttempt> findByUsername(String username);
}
