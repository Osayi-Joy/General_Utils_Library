package com.osayijoy.common_utils_library.processor.model;

import com.osayijoy.common_utils_library.processor.enums.ApprovalRequestStatus;
import lombok.*;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Oct-31(Mon)-2022
 */

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalResponse {

    private String description;
    private ApprovalRequestStatus requestStatus;
}
