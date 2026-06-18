package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentRegisterDTO;
import com.example.bloodbankmanagement.dto.objectRepository.ScoreSelectListAssignmentDto;
import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import com.example.bloodbankmanagement.entity.ScoreAssignment;
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
public interface ScoreAssignmentRepository extends JpaRepository<ScoreAssignment, Long> {
    @Query(value = "select *  from score_assignment " +
            "where (:#{#request.scoreAssignmentId} is null or ''  = :#{#request.scoreAssignmentId} or id like :#{#request.scoreAssignmentId})" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<ScoreAssignment> findListScoreAssignment(ScoreAssignmentDto.ScoreAssignmentSelectListInfo request, Pageable pageable);

    @Query(value = "select * from score_assignment where id = ?1",nativeQuery = true)
    ScoreAssignment findByFileId(Long id);

    @Query(value = "select * from score_assignment where assignment_register_info_id = ?1 and status  = ?2 ",nativeQuery = true)
    ScoreAssignment findInfoScoreActive(Long id, String activeStatus);

    @Modifying
    @Transactional
    @Query(value = "update score_assignment set " +
            "score_instructor =:#{#request.scoreInstructor} " +
            ",score_examiner =:#{#request.scoreExaminer} " +
            ",score_critical =:#{#request.scoreCritical} " +
            ",assignment_register_info_id =:#{#request.assignmentRegisterInfo.id},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateScoreAssignment(@Param("request") ScoreAssignment request);

    @Modifying
    @Transactional
    @Query(value = "update score_assignment set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deleteScoreAssignment(@Param("request") ScoreAssignment request, @Param("ids") List<Long> ids);

    @Query(value = "select  " +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        ap.id as periodAssignmentId," +
            "        ap.admission_period_name as periodAssignmentName," +
            "        pa.end_period as expirePeriodDate," +
            "        asr.student_id as studentId," +
            "        (select full_name from users where id = asr.student_id and status = '1') as studentName," +
            "         case " +
            "          when smi.instructor_id is null then '' " +
            "          when smi.instructor_id is not null then(select full_name from users where id = smi.instructor_id and status = '1') " +
            "          else '' end as instructorName,"+
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
            " where (:#{#request.admissionPeriodId} is null or ''  = :#{#request.admissionPeriodId} or ap.id = :#{#request.admissionPeriodId}) " +
            "  and (:#{#request.typeApprove} is null or ''  = :#{#request.typeApprove} or asr.is_approved = :#{#request.typeApprove}) " +
            " order by asr.create_at DESC, asr.update_at DESC ",
            nativeQuery = true)
    List<AssignmentStudentRegisterDTO> findListAssignmentRegisterIsFinalApproveByPeriodId(ScoreAssignmentDto.ListAssignmentRegisterIsFinalApproveByPeriodIdInfo request);

    @Query(value = "select  " +
            "         sa.id as scoreAssignmentId, asr.id as assignmentRegisterId,asr.assignment_name as assignmentRegisterName, " +
            "        us.full_name as studentName,ap.id as admissionPeriodId, " +
            "        sa.score_critical as scoreCritical, sa.score_examiner as scoreExaminer, sa.score_instructor as scoreInstructor, " +
            "        ap.admission_period_name as admissionPeriodName, sa.status as status, sa.create_at as createAt, " +
            "        maj.id as majorId,maj.major_name as majorName " +
            " from score_assignment sa " +
            " join assignment_student_register asr on sa.assignment_register_info_id = asr.id " +
            " join period_assignment pa on pa.id = asr.period_assignment_id " +
            " join admission_period ap on ap.id = pa.admission_period_id " +
            " join major maj on pa.major_id = maj.id " +
            " join users us on us.id = asr.student_id " +
            " where (:#{#request.scoreAssignmentId} is null or ''  = :#{#request.scoreAssignmentId} or sa.id like :#{#request.scoreAssignmentId})" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or sa.status = :#{#request.status})" +
            "and (:#{#request.admissionPeriodId} is null or ''  = :#{#request.admissionPeriodId} or ap.id = :#{#request.admissionPeriodId})" +
            " order by sa.create_at DESC, sa.update_at DESC ",
            nativeQuery = true)
    Page<ScoreSelectListAssignmentDto> findListNewScoreManagement(ScoreAssignmentDto.ScoreAssignmentNewSelectListInfo request, Pageable pageable);

    @Query(value = "select  " +
            "         sa.id as scoreAssignmentId, asr.id as assignmentRegisterId,asr.assignment_name as assignmentRegisterName, " +
            "        us.full_name as studentName,ap.id as admissionPeriodId, " +
            "        sa.score_critical as scoreCritical, sa.score_examiner as scoreExaminer, sa.score_instructor as scoreInstructor, " +
            "        ap.admission_period_name as admissionPeriodName, sa.status as status, sa.create_at as createAt, " +
            "        maj.id as majorId,maj.major_name as majorName " +
            " from score_assignment sa " +
            " join assignment_student_register asr on sa.assignment_register_info_id = asr.id " +
            " join period_assignment pa on pa.id = asr.period_assignment_id " +
            " join admission_period ap on ap.id = pa.admission_period_id " +
            " join major maj on pa.major_id = maj.id " +
            " join users us on us.id = asr.student_id " +
            " where (:#{#request.scoreAssignmentId} is null or ''  = :#{#request.scoreAssignmentId} or sa.id like :#{#request.scoreAssignmentId})" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or sa.status = :#{#request.status})" +
            "and (:#{#request.majorId} is null or ''  = :#{#request.majorId} or maj.id = :#{#request.majorId})" +
            " order by sa.create_at DESC, sa.update_at DESC ",
            nativeQuery = true)
    Page<ScoreSelectListAssignmentDto> findListScoreManagementHeadMajorSite(ScoreAssignmentDto.ScoreAssignmentMajorSelectListInfo request, Pageable pageable);

    @Query(value = "select  " +
            "         sa.id as scoreAssignmentId, asr.id as assignmentRegisterId,asr.assignment_name as assignmentRegisterName, " +
            "        us.full_name as studentName,ap.id as admissionPeriodId, " +
            "        sa.score_critical as scoreCritical, sa.score_examiner as scoreExaminer, sa.score_instructor as scoreInstructor, " +
            "        ap.admission_period_name as admissionPeriodName, sa.status as status, sa.create_at as createAt, " +
            "        maj.id as majorId,maj.major_name as majorName " +
            " from score_assignment sa " +
            " join assignment_student_register asr on sa.assignment_register_info_id = asr.id " +
            " join period_assignment pa on pa.id = asr.period_assignment_id " +
            " join admission_period ap on ap.id = pa.admission_period_id " +
            " join major maj on pa.major_id = maj.id " +
            " join users us on us.id = asr.student_id " +
            " where (:#{#request.scoreAssignmentId} is null or ''  = :#{#request.scoreAssignmentId} or sa.id like :#{#request.scoreAssignmentId})" +
            "and sa.status = '1' " +
            "and (:#{#request.studentId} is null or ''  = :#{#request.studentId} or asr.student_id = :#{#request.studentId})" +
            " order by sa.create_at DESC, sa.update_at DESC ",
            nativeQuery = true)
    Page<ScoreSelectListAssignmentDto> findListScoreManagementStudentSite(ScoreAssignmentDto.ScoreAssignmentStudentSelectListInfo request, Pageable pageable);

    @Query(value = "select  " +
            "         sa.id as scoreAssignmentId, asr.id as assignmentRegisterId,asr.assignment_name as assignmentRegisterName, " +
            "        us.full_name as studentName,ap.id as admissionPeriodId, " +
            "        sa.score_critical as scoreCritical, sa.score_examiner as scoreExaminer, sa.score_instructor as scoreInstructor, " +
            "        ap.admission_period_name as admissionPeriodName, sa.status as status, sa.create_at as createAt, " +
            "        maj.id as majorId,maj.major_name as majorName " +
            " from score_assignment sa " +
            " join assignment_student_register asr on sa.assignment_register_info_id = asr.id " +
            " join student_map_instructor smi on asr.student_id = smi.student_id and smi.instructor_id is not null " +
            " join period_assignment pa on pa.id = asr.period_assignment_id " +
            " join admission_period ap on ap.id = pa.admission_period_id " +
            " join major maj on pa.major_id = maj.id " +
            " join users us on us.id = asr.student_id " +
            " where (:#{#request.scoreAssignmentId} is null or ''  = :#{#request.scoreAssignmentId} or sa.id like :#{#request.scoreAssignmentId})" +
            " and (:#{#request.admissionPeriodId} is null or ''  = :#{#request.admissionPeriodId} or ap.id = :#{#request.admissionPeriodId}) "+
            "and (:#{#request.status} is null or ''  = :#{#request.status} or sa.status = :#{#request.status})" +
            "and smi.instructor_id = :#{#request.instructorId}" +
            " order by sa.create_at DESC, sa.update_at DESC ",
            nativeQuery = true)
    Page<ScoreSelectListAssignmentDto> findListScoreManagementInstructorSite(ScoreAssignmentDto.ScoreAssignmentInstructorSelectListInfo request, Pageable pageable);


    @Query(value = "select  " +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        ap.id as periodAssignmentId," +
            "        ap.admission_period_name as periodAssignmentName," +
            "        pa.end_period as expirePeriodDate," +
            "        asr.student_id as studentId," +
            "        (select full_name from users where id = asr.student_id and status = '1') as studentName," +
            "         case " +
            "          when smi.instructor_id is null then '' " +
            "          when smi.instructor_id is not null then(select full_name from users where id = smi.instructor_id and status = '1') " +
            "          else '' end as instructorName,"+
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
            " where (:#{#request.admissionPeriodId} is null or ''  = :#{#request.admissionPeriodId} or ap.id = :#{#request.admissionPeriodId}) "+
            "  and (:#{#request.typeApprove} is null or ''  = :#{#request.typeApprove} or asr.is_approved = :#{#request.typeApprove}) " +
            "and smi.instructor_id = :#{#request.instructorId}" +
            " order by asr.create_at DESC, asr.update_at DESC ",
            nativeQuery = true)
    List<AssignmentStudentRegisterDTO> findListScoreManagementNoPaginationInstructorSite(ScoreAssignmentDto.AssignmentInstructorSelectListNoPaginationInfo request);


    @Query(value = "select  " +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        ap.id as periodAssignmentId," +
            "        ap.admission_period_name as periodAssignmentName," +
            "        pa.end_period as expirePeriodDate," +
            "        asr.student_id as studentId," +
            "        (select full_name from users where id = asr.student_id and status = '1') as studentName," +
            "         case " +
            "          when smi.instructor_id is null then '' " +
            "          when smi.instructor_id is not null then(select full_name from users where id = smi.instructor_id and status = '1') " +
            "          else '' end as instructorName,"+
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
            " join major maj on pa.major_id = maj.id " +
            " join student_map_instructor smi on asr.student_id = smi.student_id and smi.instructor_id is not null " +
            " and (:#{#request.admissionPeriodId} is null or ''  = :#{#request.admissionPeriodId} or ap.id = :#{#request.admissionPeriodId}) "+
            " and (:#{#request.majorId} is null or ''  = :#{#request.majorId} or maj.id = :#{#request.majorId}) " +
            " and (:#{#request.typeApprove} is null or ''  = :#{#request.typeApprove} or asr.is_approved = :#{#request.typeApprove}) " +
            " order by asr.create_at DESC, asr.update_at DESC ",
            nativeQuery = true)
    List<AssignmentStudentRegisterDTO> findListAssignmentByPeriodIdHeadRoomSite(ScoreAssignmentDto.ListAssignmentRegisterHeadRoomSiteInfo request);

    @Query(value = "select * from score_assignment WHERE status = '1' and assignment_register_info_id in :ids",nativeQuery = true)
    List<ScoreAssignment> findByListScoreActiveByAssignmentId(@Param("ids") List<Long> ids);


}
