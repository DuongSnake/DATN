package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

public class StudentMapInstructorDto {
    @Data
    public static class StudentMapInstructorSelectListInfo {
        private Long studentMapInstructorId;
        private Long instructorId;
        private Long studentId;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String status;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class StudentMapInstructorListInfo {
        private Long studentMapInstructorId;
        private Long instructorId;
        private Long studentId;
        private Long criticalId;
        private String instructorName;
        private String studentName;
        private String criticalName;
        private String status;
        private LocalDate createAt;
        private String createUser;
    }

    @Data
    public static class StudentMapInstructorSelectInfo {
        private Long studentMapInstructorId;
    }

    @Data
    public static class StudentMapInstructorSelectInfoResponse {
        private Long studentMapInstructorId;
        private Long instructorId;
        private Long studentId;
        private Long criticalId;
        private String instructorName;
        private String studentName;
        private String criticalName;
        private String status;
        private LocalDate createAt;
        private String createUser;
    }

    @Data
    public static class StudentMapInstructorInsertInfo {
        private Long instructorId;
        private Long studentId;
    }

    @Data
    public static class StudentMapInstructorUpdateInfo {
        private Long studentMapInstructorId;
        private Long instructorId;
        private Long studentId;
    }

    @Data
    public static class StudentMapInstructorDeleteInfo {
        private List<Long> listData;
    }

    @Data
    public static class InsertListStudentWithOneInstructorInfo {
        private Long instructorId;
        private List<Long> listStudentId;
    }

    @Data
    public static class UpdateListStudentWithOneInstructorInfo {
        private Long instructorId;
        private List<Long> listStudentId;
    }
}
