package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.AbstractEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.sql.Blob;


@NoArgsConstructor
@Entity
@Table(name = "file_meta_data")
public class FileMetadata  extends AbstractEntity {
    private String fileName;
    private String fileType;
    private long fileSize;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data")
    private Blob data;

    public FileMetadata(Long id, String createAt, String updateAt, String createTm, String updateTm, String createUser, String updateUser, String fileName, String fileType, long fileSize, Blob data) {
        super(id, createAt, updateAt, createTm, updateTm, createUser, updateUser);
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.data = data;
    }

    public FileMetadata(String fileName, String fileType, long fileSize, Blob data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.data = data;
    }

    public FileMetadata(String fileName, String fileType, Blob data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Blob getData() {
        return data;
    }

    public void setData(Blob data) {
        this.data = data;
    }
}
