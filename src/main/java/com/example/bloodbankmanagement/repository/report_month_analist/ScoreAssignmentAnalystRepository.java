package com.example.bloodbankmanagement.repository.report_month_analist;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.excelObject.ScoreAssignmentListExcel;
import com.example.bloodbankmanagement.dto.service.report_month_analist.ScoreAssignmentAnalystDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ScoreAssignmentAnalystRepository {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ScoreAssignmentAnalystRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    public PageAmtListResponseDto<ScoreAssignmentAnalystDto.ScoreAssignmentAnalystSelectListResponse> findAllAssignment(ScoreAssignmentAnalystDto.ScoreAssignmentAnalystSelectListRequest request) {
        PageAmtListResponseDto<ScoreAssignmentAnalystDto.ScoreAssignmentAnalystSelectListResponse> pageAmtObject = new PageAmtListResponseDto<>();
        Double defaultRateExam = 6.0;
        Double defaultRateInstructor = 4.0;
        Double defaultTotalRate = 10.0;
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
                "JOIN score_assignment score_ass on asr.id = score_ass.assignment_register_info_id and score_ass.status = '1' " +
                "LEFT JOIN calculate_average_score calculate_avg on ap.id = calculate_avg.admission_period_id " +
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
            baseSql.append(" AND score_ass.create_at BETWEEN :startDate AND :endDate");
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
                        "  score_ass.id as scoreAssignmentId, " +
                        "  u_student.full_name AS studentName," +
                        "  u_student.email AS studentEmail, " +
                        "  u_critical.full_name AS criticalName, " +
                        "  u_instructor.full_name AS instructorName, " +
                        "  asr.assignment_name as assignmentName, "  +
                        "  asr.create_at as createAt," +
                        "  ap.admission_period_name as admissionPeriodName, " +
                        "  asr.is_approved as statusAssignment, " +
                        "  pa.major_id as majorId, " +
                        "  majors.major_name as majorName, " +
                        "  score_ass.score_instructor as scoreInstructor, " +
                        "  score_ass.score_examiner as scoreExaminer, " +
                        "  score_ass.score_critical as scoreCritical, " +
                        "  calculate_avg.rate_exam as rateExam, " +
                        "  calculate_avg.rate_instructor as rateInstructor, " +
                        "  calculate_avg.total_rate as totalRate " +
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

// Bind parameters
        query.setParameter("limit", valuePageSize);
        query.setParameter("offset", offset);

// Execute
        List<Object[]> rows = query.getResultList();
        logger.info("Sql query with pagination have full condition:"+countQuery.toString());

// Map results
        List<ScoreAssignmentAnalystDto.ScoreAssignmentAnalystSelectListResponse> listResponse = new ArrayList<>();
        for(Object[] row : rows){
            ScoreAssignmentAnalystDto.ScoreAssignmentAnalystSelectListResponse objectResponse = new ScoreAssignmentAnalystDto.ScoreAssignmentAnalystSelectListResponse();
            objectResponse.setStudentId(null != row[0] ? ((Number) row[0]).longValue() : null);
            objectResponse.setCriticalId(null != row[1] ? ((Number) row[1]).longValue() : null);
            objectResponse.setInstructorId(null != row[2] ? ((Number) row[2]).longValue() : null);
            objectResponse.setAssignmentId(null != row[3] ? ((Number) row[3]).longValue() : null);
            objectResponse.setAdmissionPeriodId(null != row[4] ? ((Number) row[4]).longValue() : null);
            objectResponse.setScoreAssignmentId(null != row[5] ? ((Number) row[5]).longValue() : null);
            objectResponse.setStudentName((String) row[6]);
            objectResponse.setStudentEmail((String) row[7]);
            objectResponse.setCriticalName((String) row[8]);
            objectResponse.setInstructorName((String) row[9]);
            objectResponse.setAssignmentName((String) row[10]);
            objectResponse.setCreateAt((Date) row[11]);
            objectResponse.setAdmissionPeriodName((String) row[12]);
            objectResponse.setStatusAssignment((Integer) row[13]);
            objectResponse.setValueStatusAssignmentDisplayName(CommonUtil.getDisplayNameStatusIsApprove(objectResponse.getStatusAssignment()));
            objectResponse.setMajorId(null != row[14] ? ((Number) row[14]).longValue() : null);
            objectResponse.setMajorName((String) row[15]);

            objectResponse.setScoreInstructor((Double) row[16]);
            objectResponse.setScoreExaminer((Double) row[17]);
            objectResponse.setScoreCritical((Double) row[18]);
            //Count average score assignment
            //Please update get value from column in table calculate_average_score instead of set value default rate to 40-60
            Double valueRateExam = (null != row[19] ? ((Double) row[19]) : defaultRateExam);
            Double valueRateInstructor = (null != row[20] ? ((Double) row[20]) : defaultRateInstructor);
            Double valueTotalRate = (null != row[21] ? ((Double) row[21]) : defaultTotalRate);
            if(valueRateExam != 0.0 && valueRateInstructor != 0.0 && valueTotalRate != 0.0 ){
                Double valueAverage = 0.0;
                Double valueScoreExam = valueRateExam * objectResponse.getScoreExaminer();
                Double valueScoreInstructor = valueRateInstructor * objectResponse.getScoreInstructor();
                valueAverage = (valueScoreExam +valueScoreInstructor)/ valueTotalRate;
                objectResponse.setScoreAverage(valueAverage);
            }else{
                objectResponse.setScoreAverage(null);
            }
            listResponse.add(objectResponse);
        }
        logger.info("Total record:"+totalRecords);
        logger.info("Total page:"+totalPages);
        pageAmtObject.setData(listResponse);
        pageAmtObject.setTotalRecord((Integer) totalRecords);
        return pageAmtObject;

    }

    public List<ScoreAssignmentListExcel> findAllAssignmentExportExcel(ScoreAssignmentAnalystDto.ScoreAssignmentAnalystExportExcelRequest request) {
        Double defaultRateExam = 6.0;
        Double defaultRateInstructor = 4.0;
        Double defaultTotalRate = 10.0;
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
                "JOIN score_assignment score_ass on asr.id = score_ass.assignment_register_info_id and score_ass.status = '1' " +
                "LEFT JOIN calculate_average_score calculate_avg on ap.id = calculate_avg.admission_period_id " +
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
            baseSql.append(" AND score_ass.create_at BETWEEN :startDate AND :endDate");
        }

        // Base SQL
        StringBuilder sql = new StringBuilder(
                "SELECT " +
                        "  COALESCE(smi.student_id, smc.student_id) AS studentId, " +
                        "  smc.critical_teacher_id AS criticalId, " +
                        "  smi.instructor_id AS instructorId, " +
                        "  asr.id as assignmentId, " +
                        "  ap.id as admissionPeriodId, " +
                        "  score_ass.id as scoreAssignmentId, " +
                        "  u_student.full_name AS studentName," +
                        "  u_student.email AS studentEmail, " +
                        "  u_critical.full_name AS criticalName, " +
                        "  u_instructor.full_name AS instructorName, " +
                        "  asr.assignment_name as assignmentName, "  +
                        "  asr.create_at as createAt," +
                        "  ap.admission_period_name as admissionPeriodName, " +
                        "  asr.is_approved as statusAssignment, " +
                        "  pa.major_id as majorId, " +
                        "  majors.major_name as majorName, " +
                        "  score_ass.score_instructor as scoreInstructor, " +
                        "  score_ass.score_examiner as scoreExaminer, " +
                        "  score_ass.score_critical as scoreCritical, " +
                        "  calculate_avg.rate_exam as rateExam, " +
                        "  calculate_avg.rate_instructor as rateInstructor, " +
                        "  calculate_avg.total_rate as totalRate " +
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

// Bind parameters
        query.setParameter("limit", valuePageSize);
        query.setParameter("offset", offset);

// Execute
        List<Object[]> rows = query.getResultList();

// Map results
        List<ScoreAssignmentListExcel> listResponse = new ArrayList<>();
        for(Object[] row : rows){
            ScoreAssignmentListExcel objectResponse = new ScoreAssignmentListExcel();
//            Long studentId = (null != row[0]) ? ((Number) row[0]).longValue() : null;
//            Long criticalId = (null != row[1]) ? ((Number) row[1]).longValue() : null;
//            Long instructorId = (null != row[2]) ? ((Number) row[2]).longValue() : null;
//            Long admissionPeriodId = (null != row[4]) ? ((Number) row[4]).longValue() : null;
//            Long scoreAssignmentId = (null != row[5]) ? ((Number) row[5]).longValue() : null;
//            Double scoreCritical = ((Double) row[18]);
//            Integer statusAssignment = ((Integer) row[13]);// get status display name (CommonUtil.getDisplayNameStatusIsApprove(objectResponse.getStatusAssignment()))
//            String studentEmail = ((String) row[7]);
//            Long majorId = (null != row[14]) ? ((Number) row[14]).longValue() : null;
            objectResponse.setAssignmentId(null != row[3] ? ((Number) row[3]).longValue() : null);
            objectResponse.setStudentName((String) row[6]);
            objectResponse.setCriticalName((String) row[8]);
            objectResponse.setInstructorName((String) row[9]);
            objectResponse.setAssignmentName((String) row[10]);
            objectResponse.setCreateAt((Date) row[11]);
            objectResponse.setAdmissionPeriodName((String) row[12]);
            objectResponse.setMajorName((String) row[15]);
            objectResponse.setScoreInstructor((Double) row[16]);
            objectResponse.setScoreExaminer((Double) row[17]);
            //Count average score assignment
            //Please update get value from column in table calculate_average_score instead of set value default rate to 40-60
            Double valueRateExam = (null != row[19] ? ((Double) row[19]) : defaultRateExam);
            Double valueRateInstructor = (null != row[20] ? ((Double) row[20]) : defaultRateInstructor);
            Double valueTotalRate = (null != row[21] ? ((Double) row[21]) : defaultTotalRate);
            if(valueRateExam != 0.0 && valueRateInstructor != 0.0 && valueTotalRate != 0.0 ){
                Double valueAverage = 0.0;
                Double valueScoreExam = valueRateExam * objectResponse.getScoreExaminer();
                Double valueScoreInstructor = valueRateInstructor * objectResponse.getScoreInstructor();
                valueAverage = (valueScoreExam +valueScoreInstructor)/ valueTotalRate;
                objectResponse.setScoreAverage(valueAverage);
            }else{
                objectResponse.setScoreAverage(null);
            }
            listResponse.add(objectResponse);
        }
        return listResponse;

    }
}
