package com.osayijoy.common_utils_library.commonUtils.util;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import java.io.IOException;

import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Monsuru <br/>
 * @since Sep-09(Fri)-2022
 */
@Component("libFileUtil")
@Slf4j
@Getter
@Profile("zest-storage")
public class ZestFileUtils extends FileUtils{

    @Value("${zest.storage.account-name}")
    private String storageAccountName;

    @Value("${zest.storage.account-key}")
    private String storageAccountKey;

    @Value("${zest.storage.container-name}")
    private String storageContainerName;

    public ZestFileUtils(@Value("${digicore.file.upload.directory:/zest}") String fileUploadDirectory,
                         @Value("${digicore.file.upload.min-size:1000}") int minFileUploadSize,
                         @Value("${digicore.file.upload.max-size:5000000}") int maxFileUploadSize) {
        super(fileUploadDirectory);
        this.minFileUploadSize = minFileUploadSize;
        this.maxFileUploadSize = maxFileUploadSize;
    }

    protected String getSavedFilePath(String type, MultipartFile file, String fileName) throws IOException {
        try {
            String connectionString = String.format("DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s", storageAccountName, storageAccountKey);

            log.trace("::::::::::::::: connecting to blob service client :::::::::::::::");
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            log.trace("::::::::::::::: connected to blob service client :::::::::::::::");

            log.trace("::::::::::::::: connecting to blob container client :::::::::::::::");
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(storageContainerName);
            log.trace("::::::::::::::: connected to blob container client :::::::::::::::");

            log.trace("::::::::::::::: connecting to blob client :::::::::::::::");
            BlobClient blobClient = containerClient.getBlobClient(type + "/" + fileName);
            log.trace("::::::::::::::: connected to blob client :::::::::::::::");

            log.trace("::::::::::::::: uploading file <{}> type <{}> to blob storage :::::::::::::::", fileName, type);
            blobClient.upload(file.getInputStream(), file.getSize(), true);
            log.trace("::::::::::::::: uploaded successfully file <{}> type <{}> to blob storage :::::::::::::::", fileName, type);

            return blobClient.getBlobUrl();
        }catch (Exception e){
            log.error("::::::::::::::: Error uploading file <{}> type <{}> to zest blob storage:::::::::::::::", fileName, type, e);
            e.printStackTrace();
            throw new ZeusRuntimeException(String.format("Error uploading file <%s> type <%s> to the zest blob storage", fileName, type));
        }
    }
}