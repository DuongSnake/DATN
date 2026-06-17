package com.example.bloodbankmanagement.dto.service.report_month_analist;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.time.LocalDate;

public class StudentAnalystDto {

    @Data
    public static class StudentAnalystSelectListRequest {
        private Long studentId;
        private Long instructorId;
        private Long admissionPeriod;
        private String statusMapping;
        private LocalDate fromDate;
        private LocalDate toDate;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class StudentAnalystExportExcelRequest {
        private Long studentId;
        private Long instructorId;
        private Long admissionPeriod;
        private String statusMapping;
        private LocalDate fromDate;
        private LocalDate toDate;
    }
}
