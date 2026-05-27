package com.example.bloodbankmanagement.dto.objectRepository;

import java.time.LocalDate;

public interface SelectListFileUploadDto {
    Long getFileId();
    String getFileName();
    String getFileType();
    Long getAssignmentRegisterId();
    String getAssignmentName();
    String getStudentName();
    Long getAdmissionPeriodId();
    String getAdmissionPeriodName();
    String getStatus();
    LocalDate getCreateAt();
}
