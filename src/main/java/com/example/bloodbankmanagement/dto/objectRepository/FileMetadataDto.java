package com.example.bloodbankmanagement.dto.objectRepository;

public interface FileMetadataDto {
    long getFileId();
    String getFileName();
    String getFileType();
    long getFileSize();
    String getStatus();
    String getCreateAt();
}
