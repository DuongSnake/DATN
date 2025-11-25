package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class AdmissionPeriodDto {
    @Data
    public static class AdmissionPeriodSelectListInfo {
        private long admissionPeriodId;
        private String admissionPeriodName;
        private Date fromDate;
        private Date toDate;
        private String status;
        private String createUser;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class AdmissionPeriodListInfo {
        private long admissionPeriodId;
        private String admissionPeriodName;
        private LocalDate startPeriod;
        private LocalDate endPeriod;
        private String status;
        private String createUser;
        private LocalDate createAt;
    }

    @Data
    public static class AdmissionPeriodSelectInfo {
        private Long admissionPeriodId;
    }

    @Data
    public static class AdmissionPeriodSelectInfoResponse {
        private Long admissionPeriodId;
        private String admissionPeriodName;
        private LocalDate startPeriod;
        private LocalDate endPeriod;
        private String status;
        private String createUser;
        private LocalDate createAt;
    }

    @Data
    public static class AdmissionPeriodInsertInfo {
        private String admissionPeriodName;
        private LocalDate startPeriod;
        private LocalDate endPeriod;
    }

    @Data
    public static class AdmissionPeriodUpdateInfo {
        private long admissionPeriodId;
        private LocalDate startPeriod;
        private LocalDate endPeriod;
        private String admissionPeriodName;
    }

    @Data
    public static class AdmissionPeriodDeleteInfo {
        private List<Long> listData;
    }
}
