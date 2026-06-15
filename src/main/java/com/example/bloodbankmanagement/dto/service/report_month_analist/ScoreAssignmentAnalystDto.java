package com.example.bloodbankmanagement.dto.service.report_month_analist;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

public class ScoreAssignmentAnalystDto {

    @Data
    public static class ScoreAssignmentAnalystSelectListRequest {
        private Long assignmentId;
        private Long studentId;
        private Long instructorId;
        private Long admissionPeriodId;
        private Integer statusAssignment;
        private Long majorId;
        private LocalDate fromDate;
        private LocalDate toDate;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class ScoreAssignmentAnalystSelectListResponse {
        private Long studentId;
        private Long criticalId;
        private Long instructorId;
        private Long assignmentId;
        private Long majorId;
        private Long admissionPeriodId;
        private Long scoreAssignmentId;
        private String studentName;
        private String studentEmail;
        private String criticalName;
        private String instructorName;
        private String assignmentName;
        private String majorName;
        private Date createAt;
        private String admissionPeriodName;
        private Integer statusAssignment;
        private String valueStatusAssignmentDisplayName;
        private Double scoreAverage;
        private Double scoreInstructor;
        private Double scoreExaminer;
        private Double scoreCritical;
    }
}
