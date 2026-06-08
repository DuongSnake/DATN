package com.example.bloodbankmanagement.dto.objectRepository;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@NoArgsConstructor
public class AssignmentStudentAnalystDto {
    private Long studentId;
    private Long criticalId;
    private Long instructorId;
    private Long assignmentId;
    private Long admissionPeriodId;

    // Student Info
    private String studentName;
    private String studentEmail;

    // Critical Teacher Info
    private String criticalName;
    private String criticalEmail;

    // Instructor Info
    private String instructorName;
    private String instructorEmail;

    // Assignment Info
    private String assignmentName;
    private Date createAt;

    // Admission Period Info
    private String admissionPeriodName;

    // Admission Period Info
    private Integer statusAssignment;

    public AssignmentStudentAnalystDto(Long studentId, Long criticalId, Long instructorId, Long assignmentId, Long admissionPeriodId, String studentName, String studentEmail, String criticalName, String criticalEmail, String instructorName, String instructorEmail, String assignmentName, Date createAt, String admissionPeriodName, Integer statusAssignment) {
        this.studentId = studentId;
        this.criticalId = criticalId;
        this.instructorId = instructorId;
        this.assignmentId = assignmentId;
        this.admissionPeriodId = admissionPeriodId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.criticalName = criticalName;
        this.criticalEmail = criticalEmail;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.assignmentName = assignmentName;
        this.createAt = createAt;
        this.admissionPeriodName = admissionPeriodName;
        this.statusAssignment = statusAssignment;
    }
}
