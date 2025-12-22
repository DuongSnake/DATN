package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

public class ScoreAssignmentDto {
    @Data
    public static class ScoreAssignmentSelectListInfo {
        private Long scoreAssignmentId;
        private Long assignmentRegisterId;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String status;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class ScoreAssignmentListInfo {
        private Long scoreAssignmentId;
        private Long assignmentRegisterId;
        private Double scoreAverage;
        private Double scoreInstructor;
        private Double scoreExaminer;
        private Double scoreCritical;
        private String assignmentRegisterName;
        private String status;
        private LocalDate createAt;
    }

    @Data
    public static class ScoreAssignmentSelectInfo {
        private Long scoreAssignmentId;
    }

    @Data
    public static class ScoreAssignmentSelectInfoResponse {
        private Long scoreAssignmentId;
        private Long assignmentRegisterId;
        private Double scoreAverage;
        private Double scoreInstructor;
        private Double scoreExaminer;
        private Double scoreCritical;
        private String assignmentRegisterName;
        private String status;
        private LocalDate createAt;
    }

    @Data
    public static class ScoreAssignmentInsertInfo {
        private Long assignmentRegisterId;
        private Double scoreInstructor;
        private Double scoreExaminer;
        private Double scoreCritical;
    }

    @Data
    public static class ScoreAssignmentUpdateInfo {
        private Long scoreAssignmentId;
        private Long assignmentRegisterId;
        private Double scoreInstructor;
        private Double scoreExaminer;
        private Double scoreCritical;
    }

    @Data
    public static class ScoreAssignmentDeleteInfo {
        private List<Long> listData;
    }
}
