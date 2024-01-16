package com.osayijoy.common_utils_library.soteria.repositories;


import java.util.List;

import com.osayijoy.common_utils_library.soteria.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long> {

    Authority findByPermission(String permission);
    List<Authority> findAllByPermission(String permission);

}
