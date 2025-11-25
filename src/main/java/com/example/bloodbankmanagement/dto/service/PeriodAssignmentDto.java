package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class PeriodAssignmentDto {
    @Data
    public static class PeriodAssignmentSelectListInfo {
        private long periodAssignmentId;
        private Long admissionPeriodId;
        private Long majorId;
        private String status;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String createUser;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class PeriodAssignmentListInfo {
        private long periodAssignmentId;
        private Long admissionPeriodId;
        private String admissionPeriodIdName;
        private Long majorId;
        private String majorName;
        private LocalDate startPeriod;
        private LocalDate endPeriod;
        private String status;
        private LocalDate createAt;
        private String note;
    }

    @Data
    public static class PeriodAssignmentSelectInfo {
        private Long periodAssignmentId;
    }

    @Data
    public static class PeriodAssignmentSelectInfoResponse {
        private Long periodAssignmentId;
        private Long admissionPeriodId;
        private String admissionPeriodIdName;
        private Long majorId;
        private String majorName;
        private LocalDate startPeriod;
        private LocalDate endPeriod;
        private String status;
        private String note;
        private LocalDate createAt;
    }

    @Data
    public static class PeriodAssignmentInsertInfo {
        private LocalDate startPeriod;
        private LocalDate endPeriod;
        private Long admissionPeriodId;
        private Long majorId;
        private String note;
    }

    @Data
    public static class PeriodAssignmentUpdateInfo {
        private long periodAssignmentId;
        private LocalDate startPeriod;
        private LocalDate endPeriod;
        private Long admissionPeriodId;
        private Long majorId;
        private String note;
    }

    @Data
    public static class PeriodAssignmentDeleteInfo {
        private List<Long> listData;
    }

    @Data
    public static class InsertListPeriodAssignmentByMajorInfo {
        private LocalDate startPeriod;
        private LocalDate endPeriod;
        private Long admissionPeriodId;
        private List<Long> listMajorId;
        private String note;
    }
}
