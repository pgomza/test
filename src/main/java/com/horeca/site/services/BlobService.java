package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.HashSet;
import java.util.Set;

@Service
public class BlobService {

    private CloudBlobClient blobClient;

    @Value("${storage.connectionString}")
    private String storageConnectionString;

    @PostConstruct
    void initStorageContainer() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
        blobClient = account.createCloudBlobClient();
    }

    public void registerContainer(String containerName, BlobContainerPermissions permissions) throws URISyntaxException,
            StorageException {
        CloudBlobContainer container = blobClient.getContainerReference(containerName);
        container.createIfNotExists();
        container.uploadPermissions(permissions);
    }

    public boolean doesExist(String containerName, String filename) {
        if (!doesContainerExist(containerName)) {
            throw new BusinessRuleViolationException("No such container exist");
        }
        try {
            CloudBlobContainer container = blobClient.getContainerReference(containerName);
            Set<ListBlobItem> blobs = new HashSet<>();
            container.listBlobs(containerName).forEach(blobs::add);
            return blobs.stream().anyMatch(item -> {
                String itemUrl = item.getUri().toString();
                int containerIndex = itemUrl.indexOf(containerName + "/");
                if (containerIndex != -1) {
                    String itemFilename = itemUrl.substring(containerIndex + (containerName + "/").length());
                    return itemFilename.equals(filename);
                }
                else {
                    return false;
                }
            });
        } catch (URISyntaxException | StorageException e) {
            throw new RuntimeException("There was an problem while trying to list all blobs in the container " +
                    containerName, e);
        }
    }

    public String upload(String containerName, String filename, InputStream inputStream) {
        if (!doesContainerExist(containerName)) {
            throw new BusinessRuleViolationException("No such container exist");
        }
        try {
            CloudBlobContainer container = blobClient.getContainerReference(containerName);
            CloudBlockBlob blob = container.getBlockBlobReference(filename);
            blob.upload(inputStream, -1);
            CloudBlob uploadedBlob = container.getBlobReferenceFromServer(filename);
            return uploadedBlob.getUri().toString();
        } catch (URISyntaxException | StorageException | IOException e) {
            throw new RuntimeException("There was an error while trying to upload file " + filename +
                    " to Azure Storage", e);
        }
    }

    public void delete(String containerName, String filename) {
        if (!doesContainerExist(containerName)) {
            throw new BusinessRuleViolationException("No such container exist");
        }
        try {
            CloudBlobContainer container = blobClient.getContainerReference(containerName);
            CloudBlob blob = container.getBlobReferenceFromServer(filename);
            blob.delete();
        } catch (URISyntaxException | StorageException e) {
            throw new RuntimeException("There was an error while trying to delete file " + filename +
                    " from Azure Storage", e);
        }
    }

    private boolean doesContainerExist(String containerName) {
        Set<CloudBlobContainer> containers = new HashSet<>();
        blobClient.listContainers(containerName).forEach(containers::add);
        return containers.stream().anyMatch(c -> c.getName().equals(containerName));
    }
}
