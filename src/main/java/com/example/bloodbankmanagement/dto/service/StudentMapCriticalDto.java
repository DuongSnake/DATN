package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class StudentMapCriticalDto {
    @Data
    public static class StudentMapCriticalSelectListInfo {
        private Long studentMapCriticalId;
        private Long criticalId;
        private Long studentId;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String status;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class StudentMapCriticalListInfo {
        private Long studentMapCriticalId;
        private Long criticalId;
        private Long studentId;
        private String instructorName;
        private String studentName;
        private String criticalName;
        private String status;
        private LocalDate createAt;
        private String createUser;
    }

    @Data
    public static class StudentMapCriticalSelectInfo {
        private Long studentMapCriticalId;
    }

    @Data
    public static class StudentMapCriticalSelectInfoResponse {
        private Long studentMapCriticalId;
        private Long criticalId;
        private Long studentId;
        private String instructorName;
        private String studentName;
        private String criticalName;
        private String status;
        private LocalDate createAt;
        private String createUser;
    }

    @Data
    public static class StudentMapCriticalInsertInfo {
        private Long criticalId;
        private Long studentId;
    }

    @Data
    public static class StudentMapCriticalUpdateInfo {
        private Long studentMapCriticalId;
        private Long criticalId;
        private Long studentId;
    }

    @Data
    public static class FindCriticalTeacherByStudentIdInfo {
        private Long studentId;
    }

    @Data
    public static class StudentMapCriticalTeacherInfo {
        private Long studentMapCriticalId;
        private Long criticalId;
        private Long studentId;
    }

    @Data
    public static class StudentMapCriticalDeleteInfo {
        private List<Long> listData;
    }

    @Data
    public static class InsertListStudentWithOneCriticalInfo {
        private Long criticalId;
        private List<Long> listStudentId;
    }

    @Data
    public static class UpdateListStudentWithOneCriticalInfo {
        private Long criticalId;
        private List<Long> listStudentId;
    }
}
