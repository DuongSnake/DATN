package com.example.bloodbankmanagement.repository.report_month_analist;


import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.StudentMappingDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.AssignmentAnalystDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.StudentAnalystDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StudentAnalystRepository {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(StudentAnalystRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    public PageAmtListResponseDto<StudentMappingDto> findCompleteStudentNotMapping(StudentAnalystDto.MajorAnalystSelectListRequest request) {
        PageAmtListResponseDto<StudentMappingDto> pageAmtObject = new PageAmtListResponseDto<>();
        // Values from request
        Long valueStudentId = null;
        LocalDate valueStartDate = null;
        Long valueInstructorId = null;
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
            if (request.getInstructorId() != null) {
                valueInstructorId = request.getInstructorId();
            }
        }
        StringBuilder baseSql = new StringBuilder("FROM student_map_instructor smi " +
                "FULL OUTER JOIN student_map_critical smc ON smi.student_id = smc.student_id " +
                "LEFT JOIN users u_student    ON u_student.id = COALESCE(smi.student_id, smc.student_id) " +
                "LEFT JOIN users u_critical   ON u_critical.id = smc.critical_teacher_id " +
                "LEFT JOIN users u_instructor ON u_instructor.id = smi.instructor_id " +
                "WHERE  1=1 "
        );
        //Condition map or not map instructor
        if (!StringUtils.isEmpty(request.getStatusMapping()) && CommonUtil.YES_VALUE.equals(request.getStatusMapping())) {
            baseSql.append(" AND smi.instructor_id is not null ");
        }
        if (!StringUtils.isEmpty(request.getStatusMapping()) && !CommonUtil.YES_VALUE.equals(request.getStatusMapping())) {
            baseSql.append(" AND smi.instructor_id is null and  smc.critical_teacher_id is null ");
        }

// Add conditions safely
        if (valueStudentId != null) {
            baseSql.append(" AND u_student.id = :studentId");
        }
        if (valueStartDate != null && valueEndDate != null) {
            baseSql.append(" AND u_student.create_at BETWEEN :startDate AND :endDate");
        }
        if (valueInstructorId != null) {
            baseSql.append(" AND smi.instructor_id = :instructorId");
        }

        String countSql = "SELECT count(smi.student_id)" + baseSql.toString();
        Query countQuery = entityManager.createNativeQuery(countSql);
        if (valueStudentId != null) {
            countQuery.setParameter("studentId", valueStudentId);
        }
        if (valueStartDate != null && valueEndDate != null) {
            countQuery.setParameter("startDate", valueStartDate);
            countQuery.setParameter("endDate", valueEndDate);
        }
        if (valueInstructorId != null) {
            countQuery.setParameter("instructorId", valueInstructorId);
        }
        Number totalRecords = ((Number) countQuery.getSingleResult());
        Number totalPages = 0;
        //Set value total page
        if(totalRecords.intValue() <= valuePageSize){
            totalPages = 1;
        }else{
            totalPages = (totalRecords.intValue()) / valuePageSize;
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
                baseSql.toString()
        );


// Add conditions safely
        if (valueStudentId != null) {
            sql.append(" AND u_student.id = :studentId");
        }
        if (valueStartDate != null && valueEndDate != null) {
            sql.append(" AND u_student.create_at BETWEEN :startDate AND :endDate");
        }
        if (valueInstructorId != null) {
            sql.append(" AND smi.instructor_id = :instructorId");
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
        if (valueInstructorId != null) {
            query.setParameter("instructorId", valueInstructorId);
        }
        query.setParameter("limit", valuePageSize);
        query.setParameter("offset", offset);

// Execute
        List<Object[]> rows = query.getResultList();

        // Map results
        List<StudentMappingDto> listResponse = new ArrayList<>();
        for(Object[] row : rows){
            StudentMappingDto objectResponse = new StudentMappingDto();
            objectResponse.setStudentId(null != row[0] ? ((Number) row[0]).longValue() : null);
            objectResponse.setCriticalId(null != row[1] ? ((Number) row[1]).longValue() : null);
            objectResponse.setInstructorId(null != row[2] ? ((Number) row[2]).longValue() : null);
            objectResponse.setStudentName((String) row[3]);
            objectResponse.setStudentEmail((String) row[4]);
            objectResponse.setCriticalName((String) row[5]);
            objectResponse.setCriticalEmail((String) row[6]);
            objectResponse.setInstructorName((String) row[7]);
            objectResponse.setInstructorEmail((String) row[8]);
            objectResponse.setCreateAt((Date) row[9]);
            listResponse.add(objectResponse);
        }
        logger.info("Total record:"+totalRecords);
        logger.info("Total page:"+totalPages);
        pageAmtObject.setData(listResponse);
        pageAmtObject.setTotalRecord((Integer) totalRecords);
        return pageAmtObject;
    }

}
