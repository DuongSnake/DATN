package com.example.bloodbankmanagement.dto.objectRepository;

import java.time.LocalDate;

public interface ScoreSelectListAssignmentDto {
    Long getScoreAssignmentId();
    Double getScoreInstructor();
    Double getScoreExaminer();
    Double getScoreCritical();
    Long getAssignmentRegisterId();
    String getAssignmentRegisterName();
    Long getAdmissionPeriodId();
    String getAdmissionPeriodName();
    String getStudentName();
    String getStatus();
    LocalDate getCreateAt();
}
