package com.osayijoy.common_utils_library.processor.dto;

import java.time.LocalDateTime;

import com.osayijoy.common_utils_library.processor.enums.ApprovalRequestStatus;
import lombok.*;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Oct-30(Sun)-2022
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRequestsDTO {

    private Long id;
    private String requesterName;
    private String description;
    private String dataToUpdate;
    private String requesterUsername;
    private boolean approved;
    private ApprovalRequestStatus status;
    private String approvalRequestType;
    private LocalDateTime createdOn;
    private LocalDateTime treatDate;
    private String errorTrace;
    private Object[] documentsUploaded;

}
