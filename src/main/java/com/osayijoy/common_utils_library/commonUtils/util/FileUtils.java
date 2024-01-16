package com.osayijoy.common_utils_library.commonUtils.util;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.UUID;

import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.helper.response.ApiError;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import static com.osayijoy.common_utils_library.commonUtils.constants.ErrorConstants.*;

/**
 * @author Monsuru
 * @since Mar-14(Tue)-2023
 */
@Slf4j
@Getter
public abstract class FileUtils {

    private static final String VIDEO_MP4_VALUE = "video/mp4";

    protected final String fileUploadDirectory;

    protected int minFileUploadSize;

    protected  int maxFileUploadSize;

    public FileUtils(String fileUploadDirectory) {
        this.fileUploadDirectory = fileUploadDirectory;
    }

    public String saveFile(String type, MultipartFile file, String fileName)  {
        return saveFile(type, file, fileName,true);
    }

    public String saveFile(String encodedString, String fileName) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        org.apache.commons.io.FileUtils.writeByteArrayToFile(new File(fileUploadDirectory.concat(fileName)), decodedBytes);
        return fileUploadDirectory.concat(fileName);
    }

    public String saveFile(String type, MultipartFile file, String fileName, boolean fileIsDocument)  {
        try {
            if (fileIsDocument) {
                if (fileTypeForDocumentIsNotValid(file) && fileSizeIsNotValid(file))
                    throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError(FILE_TYPE_REJECTED_MESSAGE,FILE_TYPE_REJECTED_CODE));
            } else
            if (fileTypeForMultiMediaIsNotValid(file) && fileSizeIsNotValid(file))
                throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError(FILE_TYPE_REJECTED_MESSAGE,FILE_TYPE_REJECTED_CODE));
            return getSavedFilePath(type, file, fileName);
        } catch (IOException e) {
            // TODO add custom fileStorageException
            log.error("Could not save the file because : {}",e.getMessage());
            e.printStackTrace();
            throw new ZeusRuntimeException("Could not save the file");
        }
    }

    protected abstract String getSavedFilePath(String type, MultipartFile file, String fileName) throws IOException;


    public String fileName(String uuid, LocalDate date, MultipartFile file)  {
        return fileName(uuid, date, file, true);
    }


    public String fileName(String uuid, LocalDate date, MultipartFile file, boolean fileIsDocument)  {
        if(fileIsDocument) {
            if (fileTypeForDocumentIsNotValid(file))
                throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError(FILE_TYPE_REJECTED_MESSAGE,FILE_TYPE_REJECTED_CODE));
        } else {
            if (fileTypeForMultiMediaIsNotValid(file))
                throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError(FILE_TYPE_REJECTED_MESSAGE,FILE_TYPE_REJECTED_CODE));
        }
        if(fileSizeIsNotValid(file))
            throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError(FILE_SIZE_REJECTED_MESSAGE,FILE_SIZE_REJECTED_CODE));
        String originalFileName = file.getOriginalFilename();
        if(originalFileName == null || file.isEmpty()) throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError(INVALID_FILE_MESSAGE,INVALID_FILE_CODE));
        return String.format("%s%s",
                uuid.concat("_").concat(date.toString()).concat("_").concat(UUID.randomUUID().toString()),
                originalFileName.lastIndexOf(".") == -1 ? ".jpg" : originalFileName.substring(originalFileName.lastIndexOf(".")));
    }

    public void deleteFile(String pathToFile) throws IOException {
        Files.delete(Paths.get(pathToFile));
    }

    private boolean fileTypeForDocumentIsNotValid(MultipartFile file){
        return !MediaType.TEXT_PLAIN_VALUE.equalsIgnoreCase(file.getContentType()) &&
                !MediaType.APPLICATION_PDF_VALUE.equalsIgnoreCase(file.getContentType()) &&
                !MediaType.IMAGE_JPEG_VALUE.equalsIgnoreCase(file.getContentType()) &&
                !MediaType.IMAGE_PNG_VALUE.equalsIgnoreCase(file.getContentType());
    }

    private boolean fileTypeForMultiMediaIsNotValid(MultipartFile file){
        return !VIDEO_MP4_VALUE.equalsIgnoreCase(file.getContentType());
    }

    public static String getMediaType(String fileExtension){
        return switch (fileExtension) {
            case "txt" -> MediaType.TEXT_PLAIN_VALUE;
            case "jpeg" -> MediaType.IMAGE_JPEG_VALUE;
            case "pdf" -> MediaType.APPLICATION_PDF_VALUE;
            default -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
        };
    }

    private boolean fileSizeIsNotValid(MultipartFile file){
        return file.getSize() < minFileUploadSize || file.getSize() > maxFileUploadSize;
    }

    public byte[] getSavedFile(String filePath) throws IOException {
        byte[] fileContent = new byte[0];
        fileContent = org.apache.commons.io.FileUtils.readFileToByteArray(new File(filePath));

        return fileContent;
    }
}
