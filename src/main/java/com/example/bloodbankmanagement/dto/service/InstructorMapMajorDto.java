package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class InstructorMapMajorDto {
    @Data
    public static class InstructorMapMajorSelectListInfo {
        private Long instructorMapMajorId;
        private Long instructorId;
        private Long majorId;
        private String status;
        private LocalDate fromDate;
        private LocalDate toDate;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class InstructorMapMajorListInfo {
        private Long instructorMapMajorId;
        private Long instructorId;
        private Long majorId;
        private String majorName;
        private String instructorName;
        private String status;
        private LocalDate createAt;
        private String createUser;
    }

    @Data
    public static class InstructorMapMajorSelectInfo {
        private Long instructorMapMajorId;
    }

    @Data
    public static class InstructorMapMajorSelectInfoResponse {
        private Long instructorMapMajorId;
        private Long instructorId;
        private Long majorId;
        private String majorName;
        private String instructorName;
        private String status;
        private LocalDate createAt;
        private String createUser;
    }

    @Data
    public static class InstructorMapMajorInsertInfo {
        private Long instructorId;
        private Long majorId;
    }

    @Data
    public static class InstructorMapMajorUpdateInfo {
        private Long instructorMapMajorId;
        private Long instructorId;
        private Long majorId;
    }

    @Data
    public static class InstructorMapMajorDeleteInfo {
        private List<Long> listData;
    }
}
