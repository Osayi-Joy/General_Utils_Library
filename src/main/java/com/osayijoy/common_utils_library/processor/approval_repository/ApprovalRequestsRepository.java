package com.osayijoy.common_utils_library.processor.approval_repository;

import java.util.List;

import com.osayijoy.common_utils_library.processor.enums.ApprovalRequestStatus;
import com.osayijoy.common_utils_library.processor.model.ApprovalRequests;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ApprovalRequestsRepository extends JpaRepository<ApprovalRequests,Long> {

   // @Query(nativeQuery =true,value = "SELECT * FROM approval_requests  WHERE permission IN (:permissions) and status = 'NOT_TREATED'")
    Page<ApprovalRequests> findByPermissionInAndStatusOrderByCreatedOnDesc(List<String> permissions, ApprovalRequestStatus status, Pageable pageable);

    @Query(nativeQuery =true,value = "SELECT * FROM approval_requests  WHERE permission IN (:permissions) and treated_date is not null")
    Page<ApprovalRequests> findByPermissionAndRequestIsTreated(@Param("permissions") List<String> permissions, Pageable pageable);

}