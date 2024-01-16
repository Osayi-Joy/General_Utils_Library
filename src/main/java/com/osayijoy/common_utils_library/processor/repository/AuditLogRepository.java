package com.osayijoy.common_utils_library.processor.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.osayijoy.common_utils_library.processor.enums.LogActivityType;
import com.osayijoy.common_utils_library.processor.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>,
    JpaSpecificationExecutor<AuditLog> {

    Page<AuditLog> findAllByEmailAndLogStartDateBetween(String email, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<AuditLog> findAllByLogStartDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<AuditLog> findAllByActivity(String activity, Pageable pageable);
    Page<AuditLog> findAllByEmailAndActivity(String email, String activity, Pageable pageable);
    Page<AuditLog> findByNameContainsIgnoreCase(String name, Pageable pageable);

    Page<AuditLog> findAllByEmail(String email, Pageable pageable);
    Page<AuditLog> findAllByAuditType(String auditType, Pageable pageable);

    Page<AuditLog> findAllByEmailAndActivityAndLogStartDateBetween(String email, String activity, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<AuditLog> findAllByActivityAndLogStartDateBetween(LogActivityType activity, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<AuditLog> findAllByActivityOrLogStartDateBetween(String activity, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<AuditLog> findAllByActivityAndLogStartDateBetween(String activity, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    @Query("SELECT DISTINCT a.activity FROM AuditLog a")
    List<String> findDistinctActivityTypes();
}
