package com.example.bloodbankmanagement.dto.objectRepository;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentMappingDto {
    private Long studentId;
    private Long criticalId;
    private Long instructorId;

    // Student Info
    private String studentName;
    private String studentEmail;

    // Critical Teacher Info
    private String criticalName;
    private String criticalEmail;

    // Instructor Info
    private String instructorName;
    private String instructorEmail;

    // Create a constructor matching the exact selection order in your SQL query
    public StudentMappingDto(Long studentId, Long criticalId, Long instructorId,
                             String studentName, String studentEmail,
                             String criticalName, String criticalEmail,
                             String instructorName, String instructorEmail) {
        this.studentId = studentId;
        this.criticalId = criticalId;
        this.instructorId = instructorId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.criticalName = criticalName;
        this.criticalEmail = criticalEmail;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
    }
}
