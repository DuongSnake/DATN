package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public class UploadFileDto {
    @Data
    public static class InsertUploadFileInfo {
        private MultipartFile fileUploadContent;
        private Long periodAssignmentId;
        private Long assignmentRegisterId;
    }
    @Data
    public static class UpdateUploadFileInfo {
        private Long fileId;
        private MultipartFile fileUploadContent;
        private Long periodAssignmentId;
        private Long assignmentRegisterId;
    }
    @Data
    public static class UploadFileSelectListInfo {
        private long fileId;
        private String fileName;
        private String fileType;
        private String fromDate;
        private String toDate;
        private Long periodAssignmentId;
        private Long assignmentRegisterId;
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
        private Long periodAssignmentId;
        private LocalDate periodAssignmentExpireTime;
        private Long assignmentRegisterId;
        private String assignmentName;
        private String studentName;
        private String instructorName;
        private String status;
        private LocalDate createAt;
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
        private Long periodAssignmentId;
        private LocalDate periodAssignmentExpireTime;
        private Long assignmentRegisterId;
        private String assignmentName;
        private String studentName;
        private String instructorName;
        private String status;
        private LocalDate createAt;
    }

    @Data
    public static class UploadFileDeleteInfo {
        private List<Long> listData;
    }
    @Data
    public static class InserListFiletUploadFileInfo {
        private MultipartFile listFile[];
        private Long periodAssignmentId;
        private Long assignmentRegisterId;
    }
}
