package com.example.bloodbankmanagement.dto.service.report_month_analist;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.time.LocalDate;

public class MajorAnalystDto {

    @Data
    public static class MajorAnalystSelectListRequest {
        private Long studentId;
        private Long admissionPeriod;
        private String statusMapping;
        private LocalDate fromDate;
        private LocalDate toDate;
        private PageRequestDto pageRequestDto;
    }
}
