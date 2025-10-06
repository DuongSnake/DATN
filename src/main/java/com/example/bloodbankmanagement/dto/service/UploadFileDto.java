package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.util.List;

public class UploadFileDto {
    @Data
    public static class UploadFileSelectListInfo {
        private long fileId;
        private String fileName;
        private String fileType;
        private String fromDate;
        private String toDate;
        private String status;
        private String userUpload;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class UploadFileListInfo {
        private long fileId;
        private String fileName;
        private String fileType;
        private long fileSize;
        private String status;
        private String createAt;
    }

    @Data
    public static class UploadFileSelectInfo {
        private Long fileId;
    }

    @Data
    public static class UploadFileSelectInfoResponse {
        private Long fileId;
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String status;
        private String createAt;
    }

    @Data
    public static class UploadFileDeleteInfo {
        private List<Long> listFileId;
    }
}
