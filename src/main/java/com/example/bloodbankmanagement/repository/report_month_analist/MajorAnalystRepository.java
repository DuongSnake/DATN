package com.example.bloodbankmanagement.repository.report_month_analist;


import com.example.bloodbankmanagement.dto.objectRepository.StudentMappingDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MajorAnalystRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<StudentMappingDto> findCompleteStudentMapping() {
        // SQL query joining users table 3 distinct times
        String sql = "SELECT " +
                "  COALESCE(smi.student_id, smc.student_id) AS studentId, " +
                "  smc.critical_teacher_id AS criticalId, " +
                "  smi.instructor_id AS instructorId, " +
                "  u_student.full_name AS studentName, u_student.email AS studentEmail, " +
                "  u_critical.full_name AS criticalName, u_critical.email AS criticalEmail, " +
                "  u_instructor.full_name AS instructorName, u_instructor.email AS instructorEmail " +
                "FROM student_map_instructor smi " +
                "FULL OUTER JOIN student_map_critical smc ON smi.student_id = smc.student_id " +
                "LEFT JOIN users u_student    ON u_student.id = COALESCE(smi.student_id, smc.student_id) " +
                "LEFT JOIN users u_critical   ON u_critical.id = smc.critical_teacher_id " +
                "LEFT JOIN users u_instructor ON u_instructor.id = smi.instructor_id";

        // Execute as a native query returning raw Object arrays
        List<Object[]> rows = entityManager.createNativeQuery(sql).getResultList();

        // Map the database rows manually into your DTO structure
        return rows.stream().map(row -> new StudentMappingDto(
                row[0] != null ? ((Number) row[0]).longValue() : null,
                row[1] != null ? ((Number) row[1]).longValue() : null,
                row[2] != null ? ((Number) row[2]).longValue() : null,
                (String) row[3],
                (String) row[4],
                (String) row[5],
                (String) row[6],
                (String) row[7],
                (String) row[8]
        )).collect(Collectors.toList());
    }
}
