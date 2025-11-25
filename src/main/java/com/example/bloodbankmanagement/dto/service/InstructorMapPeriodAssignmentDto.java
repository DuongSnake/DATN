package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class InstructorMapPeriodAssignmentDto {
    @Data
    public static class InstructorMapPeriodAssignmentSelectListInfo {
        private Long instructorMapPeriodAssignmentId;
        private Long instructorId;
        private Long periodAssignmentId;
        private String status;
        private LocalDate fromDate;
        private LocalDate toDate;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class InstructorMapPeriodAssignmentListInfo {
        private Long instructorMapPeriodAssignmentId;
        private Long instructorId;
        private Long periodAssignmentId;
        private String majorName;
        private String instructorName;
        private String status;
        private LocalDate createAt;
        private String createUser;
    }

    @Data
    public static class InstructorMapPeriodAssignmentSelectInfo {
        private Long instructorMapPeriodAssignmentId;
    }

    @Data
    public static class InstructorMapPeriodAssignmentSelectInfoResponse {
        private Long instructorMapPeriodAssignmentId;
        private Long instructorId;
        private Long periodAssignmentId;
        private String majorName;
        private String instructorName;
        private String status;
        private LocalDate createAt;
        private String createUser;
    }

    @Data
    public static class InstructorMapPeriodAssignmentInsertInfo {
        private Long instructorId;
        private Long periodAssignmentId;
    }

    @Data
    public static class InstructorMapPeriodAssignmentUpdateInfo {
        private Long instructorMapPeriodAssignmentId;
        private Long instructorId;
        private Long periodAssignmentId;
    }

    @Data
    public static class InstructorMapPeriodAssignmentDeleteInfo {
        private List<Long> listData;
    }

    @Data
    public static class InsertListPeriodAssignmentWithOneInstructorInfo {
        private Long periodAssignmentId;
        private List<Long> listInstructorId;
    }

    @Data
    public static class UpdateListPeriodAssignmentWithOneInstructorInfo {
        private Long periodAssignmentId;
        private List<Long> listInstructorId;
    }
}
