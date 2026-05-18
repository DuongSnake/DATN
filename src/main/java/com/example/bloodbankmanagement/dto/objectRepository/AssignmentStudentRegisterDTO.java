package com.example.bloodbankmanagement.dto.objectRepository;

import java.time.LocalDate;

public interface AssignmentStudentRegisterDTO {
    long getAssignmentStudentRegisterId();
    String getAssignmentStudentRegisterName();
    Long getPeriodAssignmentId();
    String getPeriodAssignmentName();
    LocalDate getExpirePeriodDate();
    Long getStudentId();
    String getStudentName();
    String getInstructorName();
    String getFileName();
    String getFileType();
    Integer getIsApproved();
    String getStatusAutoMap();
    String getStatus();
    String getCreateUser();
    LocalDate getCreateAt();
    LocalDate getOldValueId();
}
