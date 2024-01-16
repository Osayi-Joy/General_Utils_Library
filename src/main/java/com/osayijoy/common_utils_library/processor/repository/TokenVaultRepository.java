package com.osayijoy.common_utils_library.processor.repository;

import java.util.Optional;

import com.osayijoy.common_utils_library.processor.model.TokenVault;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenVaultRepository extends JpaRepository<TokenVault,Long> {
    Optional<TokenVault> findByUserName(String username);
    Optional<TokenVault> findByToken(String token);
}
