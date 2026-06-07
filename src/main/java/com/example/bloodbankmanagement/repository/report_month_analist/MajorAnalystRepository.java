package com.example.bloodbankmanagement.repository.report_month_analist;


import com.example.bloodbankmanagement.dto.objectRepository.StudentMappingDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.MajorAnalystDto;
import com.example.bloodbankmanagement.entity.AdmissionPeriod;
import com.example.bloodbankmanagement.repository.AdmissionPeriodRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MajorAnalystRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<StudentMappingDto> findCompleteStudentAll(MajorAnalystDto.MajorAnalystSelectListRequest request) {
        // Values from request
        Long valueStudentId = null;
        LocalDate valueStartDate = null;
        LocalDate valueEndDate = null;
        int valuePageNum = 0;
        int valuePageSize = 10;

        if (request != null) {
            if (request.getStudentId() != null) {
                valueStudentId = request.getStudentId();
            }
            if (request.getPageRequestDto() != null) {
                valuePageNum = request.getPageRequestDto().getPageNum();
                valuePageSize = request.getPageRequestDto().getPageSize();
            }
            if(request.getToDate() != null && request.getFromDate() != null){
                valueStartDate = request.getFromDate();
                valueEndDate = request.getToDate();
            }
        }

// Base SQL
        StringBuilder sql = new StringBuilder(
                "SELECT " +
                        "  COALESCE(smi.student_id, smc.student_id) AS studentId, " +
                        "  smc.critical_teacher_id AS criticalId, " +
                        "  smi.instructor_id AS instructorId, " +
                        "  u_student.full_name AS studentName, u_student.email AS studentEmail, " +
                        "  u_critical.full_name AS criticalName, u_critical.email AS criticalEmail, " +
                        "  u_instructor.full_name AS instructorName, u_instructor.email AS instructorEmail, "  +
                        "  u_student.create_at as createAt " +
                        "FROM student_map_instructor smi " +
                        "FULL OUTER JOIN student_map_critical smc ON smi.student_id = smc.student_id " +
                        "LEFT JOIN users u_student    ON u_student.id = COALESCE(smi.student_id, smc.student_id) " +
                        "LEFT JOIN users u_critical   ON u_critical.id = smc.critical_teacher_id " +
                        "LEFT JOIN users u_instructor ON u_instructor.id = smi.instructor_id " +
                        "WHERE 1=1 "
        );

// Add conditions safely
        if (valueStudentId != null) {
            sql.append(" AND u_student.id = :studentId");
        }
        if (valueStartDate != null && valueEndDate != null) {
            sql.append(" AND u_student.create_at BETWEEN :startDate AND :endDate");
        }

// Pagination
        Integer valueRealPageNum = 0;
        if(valuePageNum == 0){
            valueRealPageNum = 0;
        }else{
            //set value reduce if the value pagenum > 1
            valueRealPageNum = --valuePageNum;
        }
        int offset = valueRealPageNum * valuePageSize;
        sql.append(" ORDER BY u_student.id"); // always order for pagination
        sql.append(" OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY");

// Create query
        Query query = entityManager.createNativeQuery(sql.toString());

// Bind parameters
        if (valueStudentId != null) {
            query.setParameter("studentId", valueStudentId);
        }
        if (valueStartDate != null && valueEndDate != null) {
            query.setParameter("startDate", valueStartDate);
            query.setParameter("endDate", valueEndDate);
        }
        query.setParameter("limit", valuePageSize);
        query.setParameter("offset", offset);

// Execute
        List<Object[]> rows = query.getResultList();

// Map results
        return rows.stream().map(row -> new StudentMappingDto(
                row[0] != null ? ((Number) row[0]).longValue() : null,
                row[1] != null ? ((Number) row[1]).longValue() : null,
                row[2] != null ? ((Number) row[2]).longValue() : null,
                (String) row[3],
                (String) row[4],
                (String) row[5],
                (String) row[6],
                (String) row[7],
                (String) row[8],
                (Date) row[9]
        )).collect(Collectors.toList());

    }

    public List<StudentMappingDto> findCompleteStudentNotMapping(MajorAnalystDto.MajorAnalystSelectListRequest request) {
        // Values from request
        Long valueStudentId = null;
        LocalDate valueStartDate = null;
        LocalDate valueEndDate = null;
        int valuePageNum = 0;
        int valuePageSize = 10;

        if (request != null) {
            if (request.getStudentId() != null) {
                valueStudentId = request.getStudentId();
            }
            if (request.getPageRequestDto() != null) {
                valuePageNum = request.getPageRequestDto().getPageNum();
                valuePageSize = request.getPageRequestDto().getPageSize();
            }
            if(request.getToDate() != null && request.getFromDate() != null){
                valueStartDate = request.getFromDate();
                valueEndDate = request.getToDate();
            }
        }
// Base SQL
        StringBuilder sql = new StringBuilder("SELECT " +
                "  COALESCE(smi.student_id, smc.student_id) AS studentId, " +
                "  smc.critical_teacher_id AS criticalId, " +
                "  smi.instructor_id AS instructorId, " +
                "  u_student.full_name AS studentName, u_student.email AS studentEmail, " +
                "  u_critical.full_name AS criticalName, u_critical.email AS criticalEmail, " +
                "  u_instructor.full_name AS instructorName, u_instructor.email AS instructorEmail," +
                "  u_student.create_at as createAt " +
                "FROM student_map_instructor smi " +
                "FULL OUTER JOIN student_map_critical smc ON smi.student_id = smc.student_id " +
                "LEFT JOIN users u_student    ON u_student.id = COALESCE(smi.student_id, smc.student_id) " +
                "LEFT JOIN users u_critical   ON u_critical.id = smc.critical_teacher_id " +
                "LEFT JOIN users u_instructor ON u_instructor.id = smi.instructor_id " +
                "WHERE smi.instructor_id is null and  smc.critical_teacher_id is null "
        );


// Add conditions safely
        if (valueStudentId != null) {
            sql.append(" AND u_student.id = :studentId");
        }
        if (valueStartDate != null && valueEndDate != null) {
            sql.append(" AND u_student.create_at BETWEEN :startDate AND :endDate");
        }

// Pagination
        Integer valueRealPageNum = 0;
        if(valuePageNum == 0){
            valueRealPageNum = 0;
        }else{
            //set value reduce if the value pagenum > 1
            valueRealPageNum = --valuePageNum;
        }
        int offset = valueRealPageNum * valuePageSize;
        sql.append(" ORDER BY u_student.id"); // always order for pagination
        sql.append(" OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY");

// Create query
        Query query = entityManager.createNativeQuery(sql.toString());

// Bind parameters
        if (valueStudentId != null) {
            query.setParameter("studentId", valueStudentId);
        }
        if (valueStartDate != null && valueEndDate != null) {
            query.setParameter("startDate", valueStartDate);
            query.setParameter("endDate", valueEndDate);
        }
        query.setParameter("limit", valuePageSize);
        query.setParameter("offset", offset);

// Execute
        List<Object[]> rows = query.getResultList();
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
                (String) row[8],
                (Date) row[9]
        )).collect(Collectors.toList());
    }

    public List<StudentMappingDto> findCompleteStudentMapping(MajorAnalystDto.MajorAnalystSelectListRequest request) {
        // Values from request
        Long valueStudentId = null;
        LocalDate valueStartDate = null;
        LocalDate valueEndDate = null;
        int valuePageNum = 0;
        int valuePageSize = 10;

        if (request != null) {
            if (request.getStudentId() != null) {
                valueStudentId = request.getStudentId();
            }
            if (request.getPageRequestDto() != null) {
                valuePageNum = request.getPageRequestDto().getPageNum();
                valuePageSize = request.getPageRequestDto().getPageSize();
            }
            if(request.getToDate() != null && request.getFromDate() != null){
                valueStartDate = request.getFromDate();
                valueEndDate = request.getToDate();
            }
        }
// Base SQL
        StringBuilder sql = new StringBuilder("SELECT " +
                "  COALESCE(smi.student_id, smc.student_id) AS studentId, " +
                "  smc.critical_teacher_id AS criticalId, " +
                "  smi.instructor_id AS instructorId, " +
                "  u_student.full_name AS studentName, u_student.email AS studentEmail, " +
                "  u_critical.full_name AS criticalName, u_critical.email AS criticalEmail, " +
                "  u_instructor.full_name AS instructorName, u_instructor.email AS instructorEmail, " +
                "  u_student.create_at as createAt " +
                "FROM student_map_instructor smi " +
                "FULL OUTER JOIN student_map_critical smc ON smi.student_id = smc.student_id " +
                "LEFT JOIN users u_student    ON u_student.id = COALESCE(smi.student_id, smc.student_id) " +
                "LEFT JOIN users u_critical   ON u_critical.id = smc.critical_teacher_id " +
                "LEFT JOIN users u_instructor ON u_instructor.id = smi.instructor_id " +
                "WHERE smi.instructor_id is not null "
        );


// Add conditions safely
        if (valueStudentId != null) {
            sql.append(" AND u_student.id = :studentId");
        }
        if (valueStartDate != null && valueEndDate != null) {
            sql.append(" AND u_student.create_at BETWEEN :startDate AND :endDate");
        }

// Pagination
        Integer valueRealPageNum = 0;
        if(valuePageNum == 0){
            valueRealPageNum = 0;
        }else{
            //set value reduce if the value pagenum > 1
            valueRealPageNum = --valuePageNum;
        }
        int offset = valueRealPageNum * valuePageSize;
        sql.append(" ORDER BY u_student.id"); // always order for pagination
        sql.append(" OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY");

// Create query
        Query query = entityManager.createNativeQuery(sql.toString());

// Bind parameters
        if (valueStudentId != null) {
            query.setParameter("studentId", valueStudentId);
        }
        if (valueStartDate != null && valueEndDate != null) {
            query.setParameter("startDate", valueStartDate);
            query.setParameter("endDate", valueEndDate);
        }
        query.setParameter("limit", valuePageSize);
        query.setParameter("offset", offset);

// Execute
        List<Object[]> rows = query.getResultList();
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
                (String) row[8],
                (Date) row[9]
        )).collect(Collectors.toList());
    }
}
