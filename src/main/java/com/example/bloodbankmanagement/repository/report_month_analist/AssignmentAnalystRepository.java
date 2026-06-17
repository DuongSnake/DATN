package com.example.bloodbankmanagement.repository.report_month_analist;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.excelObject.AssignmentListExcel;
import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentAnalystDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.AssignmentAnalystDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AssignmentAnalystRepository {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AssignmentAnalystRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    public PageAmtListResponseDto<AssignmentAnalystDto.AssignmentAnalystSelectListResponse> findAllAssignment(AssignmentAnalystDto.AssignmentAnalystSelectListRequest request) {
        PageAmtListResponseDto<AssignmentAnalystDto.AssignmentAnalystSelectListResponse> pageAmtObject = new PageAmtListResponseDto<>();
        // Values from request
        Long valueStudentId = null;
        Long valueInstructorId = null;
        LocalDate valueStartDate = null;
        LocalDate valueEndDate = null;
        Long valueAssignmentId = null;
        Long valueAdmissionPeriodId = null;
        Long valueMajorId = null;
        Integer valueStatusAssignment = null;
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
            if (request.getMajorId() != null) {
                valueMajorId = request.getMajorId();
            }
            if (request.getStatusAssignment() != null) {
                valueStatusAssignment = request.getStatusAssignment();
            }
            if (request.getInstructorId() != null) {
                valueInstructorId = request.getInstructorId();
            }
        }
        StringBuilder baseSql = new StringBuilder(" FROM student_map_instructor smi " +
                "FULL OUTER JOIN student_map_critical smc ON smi.student_id = smc.student_id " +
                "LEFT JOIN users u_student    ON u_student.id = COALESCE(smi.student_id, smc.student_id) " +
                "LEFT JOIN users u_critical   ON u_critical.id = smc.critical_teacher_id " +
                "LEFT JOIN users u_instructor ON u_instructor.id = smi.instructor_id " +
                "JOIN assignment_student_register asr ON u_student.id = asr.student_id " +
                "JOIN period_assignment pa ON  asr.period_assignment_id = pa.id " +
                "JOIN admission_period ap on pa.admission_period_id = ap.id " +
                "JOIN major majors on pa.major_id = majors.id " +
                " WHERE 1=1 ");
        // Add conditions safely
        if (valueStudentId != null) {
            baseSql.append(" AND u_student.id = :studentId");
        }
        if (valueAssignmentId != null) {
            baseSql.append(" AND asr.id = :assignmentId");
        }
        if (valueAdmissionPeriodId != null) {
            baseSql.append(" AND ap.id = :admissionPeriodId");
        }
        if (valueMajorId != null) {
            baseSql.append(" AND pa.major_id = :majorId");
        }
        if (valueStatusAssignment != null) {
            baseSql.append(" AND asr.is_approved = :statusAssignment");
        }
        if (valueInstructorId != null) {
            baseSql.append(" AND smi.instructor_id = :instructorId");
        }
        if (valueStartDate != null && valueEndDate != null) {
            baseSql.append(" AND asr.create_at BETWEEN :startDate AND :endDate");
        }
        String countSql = "SELECT count(smi.student_id)" + baseSql.toString();
        logger.info("Sql count query:"+countSql);
        Query countQuery = entityManager.createNativeQuery(countSql);

        // Bind parameters
        if (valueStudentId != null) {
            countQuery.setParameter("studentId", valueStudentId);
        }
        if (valueAssignmentId != null) {
            countQuery.setParameter("assignmentId", valueAssignmentId);
        }
        if (valueAdmissionPeriodId != null) {
            countQuery.setParameter("admissionPeriodId", valueAdmissionPeriodId);
        }
        if (valueMajorId != null) {
            countQuery.setParameter("majorId", valueMajorId);
        }
        if (valueStatusAssignment != null) {
            countQuery.setParameter("statusAssignment", valueStatusAssignment);
        }
        if (valueInstructorId != null) {
            countQuery.setParameter("instructorId", valueInstructorId);
        }
        if (valueStartDate != null && valueEndDate != null) {
            countQuery.setParameter("startDate", valueStartDate);
            countQuery.setParameter("endDate", valueEndDate);
        }
        Number totalRecords = ((Number) countQuery.getSingleResult());
        logger.info("Sql count query  have full condition:"+countQuery.toString());
        Number totalPages = 0;
        //Set value total page
        if(totalRecords.intValue() <= valuePageSize){
            totalPages = 1;
        }else{
            totalPages = (totalRecords.intValue()) / valuePageSize;
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
                        "  asr.is_approved as statusAssignment, " +
                        "  pa.major_id as majorId, " +
                        "  majors.major_name as majorName " +
                        baseSql.toString()
        );
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
        logger.info("Sql query with pagination:"+sql.toString());
// Create query
        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("limit", valuePageSize);
        query.setParameter("offset", offset);

// Execute
        List<Object[]> rows = query.getResultList();
        logger.info("Sql query with pagination have full condition:"+countQuery.toString());

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
            objectResponse.setMajorId(null != row[15] ? ((Number) row[15]).longValue() : null);
            objectResponse.setMajorName((String) row[16]);
            listResponse.add(objectResponse);
        }
        logger.info("Total record:"+totalRecords);
        logger.info("Total page:"+totalPages);
        pageAmtObject.setData(listResponse);
        pageAmtObject.setTotalRecord((Integer) totalRecords);
        return pageAmtObject;

    }

    public List<AssignmentListExcel> findAllAssignmentExportExcel(AssignmentAnalystDto.AssignmentAnalystExportExcelRequest request) {
        // Values from request
        Long valueStudentId = null;
        Long valueInstructorId = null;
        LocalDate valueStartDate = null;
        LocalDate valueEndDate = null;
        Long valueAssignmentId = null;
        Long valueAdmissionPeriodId = null;
        Long valueMajorId = null;
        Integer valueStatusAssignment = null;
        int valuePageNum = 0;
        int valuePageSize = 10;

        if (request != null) {
            if (request.getStudentId() != null) {
                valueStudentId = request.getStudentId();
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
            if (request.getMajorId() != null) {
                valueMajorId = request.getMajorId();
            }
            if (request.getStatusAssignment() != null) {
                valueStatusAssignment = request.getStatusAssignment();
            }
            if (request.getInstructorId() != null) {
                valueInstructorId = request.getInstructorId();
            }
        }
        StringBuilder baseSql = new StringBuilder(" FROM student_map_instructor smi " +
                "FULL OUTER JOIN student_map_critical smc ON smi.student_id = smc.student_id " +
                "LEFT JOIN users u_student    ON u_student.id = COALESCE(smi.student_id, smc.student_id) " +
                "LEFT JOIN users u_critical   ON u_critical.id = smc.critical_teacher_id " +
                "LEFT JOIN users u_instructor ON u_instructor.id = smi.instructor_id " +
                "JOIN assignment_student_register asr ON u_student.id = asr.student_id " +
                "JOIN period_assignment pa ON  asr.period_assignment_id = pa.id " +
                "JOIN admission_period ap on pa.admission_period_id = ap.id " +
                "JOIN major majors on pa.major_id = majors.id " +
                " WHERE 1=1 ");
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
                        "  asr.is_approved as statusAssignment, " +
                        "  pa.major_id as majorId, " +
                        "  majors.major_name as majorName " +
                        baseSql.toString()
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
        if (valueMajorId != null) {
            sql.append(" AND pa.major_id = :majorId");
        }
        if (valueStatusAssignment != null) {
            sql.append(" AND asr.is_approved = :statusAssignment");
        }
        if (valueInstructorId != null) {
            sql.append(" AND smi.instructor_id = :instructorId");
        }
        if (valueStartDate != null && valueEndDate != null) {
            sql.append(" AND asr.create_at BETWEEN :startDate AND :endDate");
        }
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
        if (valueMajorId != null) {
            query.setParameter("majorId", valueMajorId);
        }
        if (valueStatusAssignment != null) {
            query.setParameter("statusAssignment", valueStatusAssignment);
        }
        if (valueInstructorId != null) {
            query.setParameter("instructorId", valueInstructorId);
        }
        if (valueStartDate != null && valueEndDate != null) {
            query.setParameter("startDate", valueStartDate);
            query.setParameter("endDate", valueEndDate);
        }

// Execute
        List<Object[]> rows = query.getResultList();

// Map results
        List<AssignmentListExcel> listResponse = new ArrayList<>();
        for(Object[] row : rows){
            AssignmentListExcel objectResponse = new AssignmentListExcel();
            objectResponse.setStudentId(null != row[0] ? ((Number) row[0]).longValue() : null);
            Long criticalId = (null != row[1]) ? ((Number) row[1]).longValue() : null;
            Long instructorId = (null != row[2]) ? ((Number) row[2]).longValue() : null;
            Long assignmentId = (null != row[3]) ? ((Number) row[3]).longValue() : null;
            Long admissionPeriodId = (null != row[4]) ? ((Number) row[4]).longValue() : null;
            objectResponse.setStudentName((String) row[5]);
            objectResponse.setAssignmentId(assignmentId);
            objectResponse.setStudentEmail((String) row[6]);
            if(null != criticalId){
                objectResponse.setCriticalName((String) row[7]);
            }else{
                objectResponse.setCriticalName(CommonUtil.NOT_ASSIGN);
            }
            if(null != instructorId){
                objectResponse.setInstructorName((String) row[9]);
            }else{
                objectResponse.setInstructorName(CommonUtil.NOT_ASSIGN);
            }
            if(null != assignmentId){
                objectResponse.setAssignmentName((String) row[11]);
            }else{
                objectResponse.setInstructorName(CommonUtil.NOT_REGISTER);
            }
            if(null != admissionPeriodId){
                objectResponse.setAdmissionPeriodName((String) row[13]);
            }
            objectResponse.setCreateAt((Date) row[12]);
            Integer statusAssignment = (null != row[14]) ? ((Integer) row[14]) : null;
            if(null != statusAssignment){
                objectResponse.setValueStatusAssignmentDisplayName(CommonUtil.getDisplayNameStatusIsApprove(statusAssignment));
            }
            objectResponse.setMajorName((String) row[16]);
            listResponse.add(objectResponse);
        }
        return listResponse;

    }
}
