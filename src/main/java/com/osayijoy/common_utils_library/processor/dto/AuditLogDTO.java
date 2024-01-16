package com.osayijoy.common_utils_library.processor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.*;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Nov-15(Tue)-2022
 */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AuditLogDTO {
    private String name;

    private String email;

    private String role;

    private String activity;

    private String activityDescription;

    private boolean activitySuccessfullyDone;

    private LocalDateTime logStartDate;

    private LocalDateTime logEndDate;

    private String ipAddress;

    private String auditType;
}
