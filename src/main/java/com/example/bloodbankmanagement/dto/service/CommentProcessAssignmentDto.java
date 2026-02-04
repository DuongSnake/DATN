package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class CommentProcessAssignmentDto {
    @Data
    public static class CommentProcessAssignmentSelectListInfo {
        private long commentProcessAssignmentId;
        private String note;
        private Long fileUploadId;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String status;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class CommentProcessAssignmentListInfo {
        private Long commentProcessAssignmentId;
        private String note;
        private String status;
        private String statusDone;
        private Long fileUploadId;
        private LocalDate examDueDate;
        private LocalDate confirmDateDone;
        private String fileUploadName;
        private String studentName;
        private String instructorName;
        private String assignmentName;
        private LocalDate createAt;
    }

    @Data
    public static class CommentProcessAssignmentSelectInfo {
        private Long commentProcessAssignmentId;
    }

    @Data
    public static class CommentProcessAssignmentSelectInfoResponse {
        private Long commentProcessAssignmentId;
        private String note;
        private String status;
        private String statusDone;
        private Long fileUploadId;
        private LocalDate examDueDate;
        private LocalDate confirmDateDone;
        private String fileUploadName;
        private String studentName;
        private String instructorName;
        private String assignmentName;
        private LocalDate createAt;
    }

    @Data
    public static class CommentProcessAssignmentInsertInfo {
        private String note;
        private Long fileUploadId;
        private LocalDate examDueDate;
    }

    @Data
    public static class CommentProcessAssignmentUpdateInfo {
        private long commentProcessAssignmentId;
        private Long fileUploadId;
        private LocalDate examDueDate;
        private String note;
    }

    @Data
    public static class CommentProcessAssignmentDeleteInfo {
        private List<Long> listData;
    }

    @Data
    public static class ConfirmListCommentProcessAssignmentDone {
        private List<Long> listData;
        private LocalDate examDueDate;
        private String note;
    }

    @Data
    public static class CommentProcessAssignmentAssignmentDone {
        private Long commentProcessAssignmentId;
        private LocalDate examDueDate;
        private String note;
    }
}
