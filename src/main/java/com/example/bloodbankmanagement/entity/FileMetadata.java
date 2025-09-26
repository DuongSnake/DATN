package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "file_meta_data")
@AllArgsConstructor
public class FileMetadata  extends AbstractEntity {
    private String fileName;
    private String downloadUrl;
    private String fileType;
    private long fileSize;

    @Lob
    private byte[] data;

    public FileMetadata(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }
}
