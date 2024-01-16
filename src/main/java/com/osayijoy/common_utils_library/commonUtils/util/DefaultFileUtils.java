package com.osayijoy.common_utils_library.commonUtils.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Monsuru
 * @since Mar-14(Tue)-2023
 */
@Component("libFileUtil")
@Slf4j
@Getter
@Profile("!zest-storage")
public class DefaultFileUtils extends FileUtils{

    public DefaultFileUtils(@Value("${digicore.file.upload.directory:/digicore}") String fileUploadDirectory,
                            @Value("${digicore.file.upload.min-size:1000}") int minFileUploadSize,
                            @Value("${digicore.file.upload.max-size:5000000}") int maxFileUploadSize) {
        super(fileUploadDirectory);
        this.minFileUploadSize = minFileUploadSize;
        this.maxFileUploadSize = maxFileUploadSize;
    }

    protected String getSavedFilePath(String type, MultipartFile file, String fileName) throws IOException {
       // String homeDir = System.getProperty("user.home");
        // create folder type if it doesn't exist
        Path uploadPath = Paths.get(fileUploadDirectory, "/omni", type).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        // copy file to target location
        Path targetLocation = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return targetLocation.toString();
    }

}
