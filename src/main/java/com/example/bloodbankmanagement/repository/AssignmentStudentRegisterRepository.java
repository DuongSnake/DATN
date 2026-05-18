package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentRegisterDTO;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.entity.AssignmentStudentRegister;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentStudentRegisterRepository extends JpaRepository<AssignmentStudentRegister, Long> {
    @Query(value = "select" +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        asr.period_assignment_id as periodAssignmentId," +
            "        ap.admission_period_name as periodAssignmentName," +
            "        pa.endPeriod as expirePeriodDate," +
            "        asr.student_id as studentId," +
            "        (select username from users where id = asr.student_id and status = '1') as studentName," +
            "        us.username as instructorName," +
            "        asr.file_name as fileName," +
            "        asr.file_type as fileType," +
            "        asr.is_approved as isApproved," +
            "        asr.status_auto_map as statusAutoMap," +
            "        asr.status as status," +
            "        asr.create_user as createUser," +
            "        asr.create_at as createAt," +
            "        asr.old_value_id as oldValueId " +
            " from assignment_student_register asr " +
            " join period_assignment pa on asr.period_assignment_id = pa.id " +
            " join admission_period ap on pa.admission_period_id = ap.id " +
            " join student_map_instructor smi on asr.student_id = smi.student_id " +
            " join users us on smi.instructor_id = us.id " +
            " where (:#{#request.assignmentStudentRegisterId} is null or ''  = :#{#request.assignmentStudentRegisterId} or asr.id like :#{#request.assignmentStudentRegisterId})" +
            " and (:#{#request.assignmentStudentRegisterName} is null or ''  = :#{#request.assignmentStudentRegisterName} or asr.assignment_name like '%'+:#{#request.assignmentStudentRegisterName}+'%')" +
            " and (:#{#request.status} is null or ''  = :#{#request.status} or asr.status = :#{#request.status})" +
            " and (:#{#request.periodAssignmentId} is null or ''  = :#{#request.periodAssignmentId} or asr.period_assignment_id = :#{#request.periodAssignmentId})" +
            " and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or asr.create_at >= :#{#request.fromDate}) " +
            " and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or asr.create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<AssignmentStudentRegisterDTO> findListAssignmentStudentRegister(AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectListInfo request, Pageable pageable);

    @Query(value = "select" +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        asr.period_assignment_id as periodAssignmentId," +
            "        ap.admission_period_name as periodAssignmentName," +
            "        pa.endPeriod as expirePeriodDate," +
            "        asr.student_id as studentId," +
            "        (select username from users where id = asr.student_id and status = '1') as studentName," +
            "        us.username as instructorName," +
            "        asr.file_name as fileName," +
            "        asr.file_type as fileType," +
            "        asr.is_approved as isApproved," +
            "        asr.status_auto_map as statusAutoMap," +
            "        asr.status as status," +
            "        asr.create_user as createUser," +
            "        asr.create_at as createAt," +
            "        asr.old_value_id as oldValueId " +
            " from assignment_student_register asr " +
            " join period_assignment pa on asr.period_assignment_id = pa.id " +
            " join admission_period ap on pa.admission_period_id = ap.id " +
            " join student_map_instructor smi on asr.student_id = smi.student_id " +
            " join users us on smi.instructor_id = us.id " +
            " where id = ?1",nativeQuery = true)
    AssignmentStudentRegisterDTO findByFileId(Long id);

    @Query(value = "select * from assignment_student_register where id = ?1",nativeQuery = true)
    AssignmentStudentRegister findByFileId2(Long id);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set old_value_id =:#{#request.oldValueId} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateOldValueIdWhenInsertNewRecord(@Param("request") AssignmentStudentRegister request);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set assignment_name =:#{#request.assignmentName},file_name =:#{#request.fileName},file_type =:#{#request.fileType},content_assignment =:#{#request.contentAssignment}" +
            ",period_assignment_id = :#{#request.periodAssignmentInfo.id} ,student_id = :#{#request.studentInfo.id} ,update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateAssignmentStudentRegister(@Param("request") AssignmentStudentRegister request);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set assignment_name =:#{#request.assignmentName} ,student_id = :#{#request.studentInfo.id} " +
            ",period_assignment_id = :#{#request.periodAssignmentInfo.id} ,update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateAssignmentStudentRegisterNoFile(@Param("request") AssignmentStudentRegister request);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deleteAssignmentStudentRegister(@Param("request") AssignmentStudentRegister request, @Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set is_approved =:#{#request.isApproved},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void reserveAssignmentStudentRegister(@Param("request") AssignmentStudentRegister request, @Param("ids") List<Long> ids);
}
