package com.osayijoy.common_utils_library.processor.approval_repository;

import com.osayijoy.common_utils_library.processor.model.PendingFileRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PendingFileRequestRepository extends JpaRepository<PendingFileRequest, Long> {
}
