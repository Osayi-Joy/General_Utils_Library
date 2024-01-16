package com.osayijoy.common_utils_library.processor.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Nov-03(Thu)-2022
 */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingFileRequestDTO {
    private String fileName;
    private String documentType;
    private MultipartFile file;
    private String pathToFile;
}
