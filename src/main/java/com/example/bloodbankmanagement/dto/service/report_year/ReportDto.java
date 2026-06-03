package com.example.bloodbankmanagement.dto.service.report_year;

import lombok.Data;

public class ReportDto {

    @Data
    public static class InstructorHaveMaxStudentAssignByYearRequest {
        private String yearQuery;
    }
}
