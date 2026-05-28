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
        private Long majorId;
        private String majorName;
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
        private Long majorId;
        private String majorName;
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

    @Data
    public static class ScoreAssignmentNewSelectListInfo {
        private Long scoreAssignmentId;
        private Long admissionPeriodId;
        private String status;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class ListAssignmentRegisterIsFinalApproveByPeriodIdInfo {
        private Long admissionPeriodId;
        private Integer typeApprove;
    }

    @Data
    public static class ListAssignmentRegisterByPeriodIdInstructorSiteInfo {
        private Long admissionPeriodId;
        private Long instructorId;
        private Integer typeApprove;
    }

    @Data
    public static class ScoreAssignmentListNewInfo {
        private Long scoreAssignmentId;
        private Long assignmentRegisterId;
        private String assignmentRegisterName;
        private Long admissionPeriodId;
        private String admissionPeriodName;
        private Long majorId;
        private String majorName;
        private String studentName;
        private Double scoreAverage;
        private Double scoreInstructor;
        private Double scoreExaminer;
        private Double scoreCritical;
        private String status;
        private LocalDate createAt;
    }

    @Data
    public static class ScoreAssignmentMajorSelectListInfo {
        private Long scoreAssignmentId;
        private Long majorId;
        private String status;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class ScoreAssignmentStudentSelectListInfo {
        private Long scoreAssignmentId;
        private Long studentId;
        private String status;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class ScoreAssignmentInstructorSelectListInfo {
        private Long scoreAssignmentId;
        private Long instructorId;
        private Long admissionPeriodId;
        private String status;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class AssignmentInstructorSelectListNoPaginationInfo {
        private Long instructorId;
        private Long admissionPeriodId;
        private Integer typeApprove;
    }

    @Data
    public static class ListAssignmentRegisterHeadRoomSiteInfo {
        private Long admissionPeriodId;
        private Long majorId;
        private Long instructorId;
        private Integer typeApprove;
    }
}
