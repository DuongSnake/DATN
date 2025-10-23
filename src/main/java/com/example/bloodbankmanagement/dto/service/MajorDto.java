package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class MajorDto {
    @Data
    public static class MajorSelectListInfo {
        private long majorId;
        private String majorName;
        private Long departmentId;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String status;
        private String createUser;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class MajorListInfo {
        private long majorId;
        private String majorName;
        private Long departmentId;
        private String status;
        private LocalDate createAt;
    }

    @Data
    public static class MajorSelectInfo {
        private Long majorId;
    }

    @Data
    public static class MajorSelectInfoResponse {
        private Long majorId;
        private String majorName;
        private Long departmentId;
        private String status;
        private LocalDate createAt;
    }

    @Data
    public static class MajorInsertInfo {
        private String majorName;
        private Long departmentId;
    }

    @Data
    public static class MajorUpdateInfo {
        private long majorId;
        private String majorName;
    }

    @Data
    public static class MajorDeleteInfo {
        private List<Long> listMajorId;
    }
}
