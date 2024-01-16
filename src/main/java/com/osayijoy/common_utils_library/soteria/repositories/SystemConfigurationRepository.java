package com.osayijoy.common_utils_library.soteria.repositories;


import java.util.List;
import java.util.Optional;

import com.osayijoy.common_utils_library.soteria.enums.SystemConfigurationGroup;
import com.osayijoy.common_utils_library.soteria.models.SystemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, Long>, JpaSpecificationExecutor<SystemConfiguration> {

    Optional<SystemConfiguration> findByKeyAndGroup(String key, SystemConfigurationGroup group);

    List<SystemConfiguration> findByGroup(SystemConfigurationGroup group);

    boolean existsByKeyAndGroup(String key, SystemConfigurationGroup group);
}
