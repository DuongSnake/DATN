package com.example.bloodbankmanagement.repository.report_month_analist;

import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentAnalystDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.AssignmentAnalystDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AssignmentAnalystRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<AssignmentAnalystDto.AssignmentAnalystSelectListResponse> findAllAssignment(AssignmentAnalystDto.AssignmentAnalystSelectListRequest request) {
        // Values from request
        Long valueStudentId = null;
        LocalDate valueStartDate = null;
        LocalDate valueEndDate = null;
        Long valueAssignmentId = null;
        Long valueAdmissionPeriodId = null;
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
            if (request.getAssignmentId() != null) {
                valueAssignmentId = request.getAssignmentId();
            }
            if (request.getAdmissionPeriodId() != null) {
                valueAdmissionPeriodId = request.getAdmissionPeriodId();
            }
        }

// Base SQL
        StringBuilder sql = new StringBuilder(
                "SELECT " +
                        "  COALESCE(smi.student_id, smc.student_id) AS studentId, " +
                        "  smc.critical_teacher_id AS criticalId, " +
                        "  smi.instructor_id AS instructorId, " +
                        "  asr.id as assignmentId, " +
                        "  ap.id as admissionPeriodId, " +
                        "  u_student.full_name AS studentName," +
                        "  u_student.email AS studentEmail, " +
                        "  u_critical.full_name AS criticalName, u_critical.email AS criticalEmail, " +
                        "  u_instructor.full_name AS instructorName, u_instructor.email AS instructorEmail, " +
                        "  asr.assignment_name as assignmentName, "  +
                        "  asr.create_at as createAt," +
                        "  ap.admission_period_name as admissionPeriodName, " +
                        "  asr.is_approved as statusAssignment " +
                        "FROM student_map_instructor smi " +
                        "FULL OUTER JOIN student_map_critical smc ON smi.student_id = smc.student_id " +
                        "LEFT JOIN users u_student    ON u_student.id = COALESCE(smi.student_id, smc.student_id) " +
                        "LEFT JOIN users u_critical   ON u_critical.id = smc.critical_teacher_id " +
                        "LEFT JOIN users u_instructor ON u_instructor.id = smi.instructor_id " +
                        "JOIN assignment_student_register asr ON u_student.id = asr.student_id " +
                        "JOIN period_assignment pa ON  asr.period_assignment_id = pa.id " +
                        "JOIN admission_period ap on pa.admission_period_id = ap.id " +
                        "WHERE 1=1 "
        );

// Add conditions safely
        if (valueStudentId != null) {
            sql.append(" AND u_student.id = :studentId");
        }
        if (valueAssignmentId != null) {
            sql.append(" AND asr.id = :assignmentId");
        }
        if (valueAdmissionPeriodId != null) {
            sql.append(" AND ap.id = :admissionPeriodId");
        }
        if (valueStartDate != null && valueEndDate != null) {
            sql.append(" AND asr.create_at BETWEEN :startDate AND :endDate");
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
        if (valueAssignmentId != null) {
            query.setParameter("assignmentId", valueAssignmentId);
        }
        if (valueAdmissionPeriodId != null) {
            query.setParameter("admissionPeriodId", valueAdmissionPeriodId);
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
        List<AssignmentAnalystDto.AssignmentAnalystSelectListResponse> listResponse = new ArrayList<>();
        for(Object[] row : rows){
            AssignmentAnalystDto.AssignmentAnalystSelectListResponse objectResponse = new AssignmentAnalystDto.AssignmentAnalystSelectListResponse();
            objectResponse.setStudentId(null != row[0] ? ((Number) row[0]).longValue() : null);
            objectResponse.setCriticalId(null != row[1] ? ((Number) row[1]).longValue() : null);
            objectResponse.setInstructorId(null != row[2] ? ((Number) row[2]).longValue() : null);
            objectResponse.setAssignmentId(null != row[3] ? ((Number) row[3]).longValue() : null);
            objectResponse.setAdmissionPeriodId(null != row[4] ? ((Number) row[4]).longValue() : null);
            objectResponse.setStudentName((String) row[5]);
            objectResponse.setStudentEmail((String) row[6]);
            objectResponse.setCriticalName((String) row[7]);
            objectResponse.setCriticalEmail((String) row[8]);
            objectResponse.setInstructorName((String) row[9]);
            objectResponse.setInstructorEmail((String) row[10]);
            objectResponse.setAssignmentName((String) row[11]);
            objectResponse.setCreateAt((Date) row[12]);
            objectResponse.setAdmissionPeriodName((String) row[13]);
            objectResponse.setStatusAssignment((Integer) row[14]);
            objectResponse.setValueStatusAssignmentDisplayName(CommonUtil.getDisplayNameStatusIsApprove(objectResponse.getStatusAssignment()));
            listResponse.add(objectResponse);
        }
        return listResponse;

    }
}
