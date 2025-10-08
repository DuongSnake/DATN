package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.AbstractEntity;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.FileMetadataDto;
import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Entity
@Table(name = "file_meta_data")
public class FileMetadata  extends AbstractEntity {
    private String fileName;
    private String fileType;
    private long fileSize;
    private String status;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data")
    private Blob data;

    public FileMetadata(Long id, String createAt, String updateAt, String createTm, String updateTm, String createUser, String updateUser, String fileName, String fileType, long fileSize, String status, Blob data) {
        super(id, createAt, updateAt, createTm, updateTm, createUser, updateUser);
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.status = status;
        this.data = data;
    }

    public FileMetadata(String fileName, String fileType, long fileSize, String status, Blob data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static UploadFileDto.UploadFileSelectInfoResponse convertObjectToDto(FileMetadataDto request){
        if(null == request){
            return null;
        }
        UploadFileDto.UploadFileSelectInfoResponse objectData = new UploadFileDto.UploadFileSelectInfoResponse();
        objectData.setFileId(request.getFileId());
        objectData.setFileName(request.getFileName());
        objectData.setFileSize(request.getFileSize());
        objectData.setFileType(request.getFileType());
        objectData.setCreateAt(request.getCreateAt());
        objectData.setStatus(request.getStatus());
        return objectData;
    }

    public static PageAmtListResponseDto<UploadFileDto.UploadFileListInfo> convertListObjectToDto(Page<FileMetadataDto> listData){
        if(null == listData.getContent() || listData.getContent().size() == 0){
            return null;
        }
        PageAmtListResponseDto<UploadFileDto.UploadFileListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<UploadFileDto.UploadFileListInfo> listObject = new ArrayList<>();
        for (FileMetadataDto object: listData){
            UploadFileDto.UploadFileListInfo newObject = new UploadFileDto.UploadFileListInfo();
            newObject.setFileId(object.getFileId());
            newObject.setFileName(object.getFileName());
            newObject.setFileSize(object.getFileSize());
            newObject.setFileType(object.getFileType());
            newObject.setStatus(object.getStatus());
            newObject.setCreateAt(object.getCreateAt());
            listObject.add(newObject);
        }
        objectDtoResponse.setData(listObject);
        objectDtoResponse.setTotalRecord((int) listData.getTotalElements());
        return objectDtoResponse;
    }
}
