package com.example.bloodbankmanagement.repository.student;

import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentRegisterDTO;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegisterDto;
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
public interface AssignmentRegisterRepository extends JpaRepository<AssignmentStudentRegister, Long> {
    @Query(value = "select  " +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        asr.period_assignment_id as periodAssignmentId," +
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
            " where (:#{#request.assignmentStudentRegisterId} is null or ''  = :#{#request.assignmentStudentRegisterId} or asr.id like :#{#request.assignmentStudentRegisterId})" +
            " and (:#{#request.assignmentStudentRegisterName} is null or ''  = :#{#request.assignmentStudentRegisterName} or asr.assignment_name like '%'+:#{#request.assignmentStudentRegisterName}+'%')" +
            " and (:#{#request.status} is null or ''  = :#{#request.status} or asr.status = :#{#request.status})" +
            " and (:#{#request.periodAssignmentId} is null or ''  = :#{#request.periodAssignmentId} or asr.period_assignment_id = :#{#request.periodAssignmentId})" +
            " and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or asr.create_at >= :#{#request.fromDate}) " +
            " and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or asr.create_at <= :#{#request.toDate}) " +
            "and (:#{#request.studentId} is null or ''  = :#{#request.regUser} or asr.student_id = :#{#request.studentId}) " +
            "and asr.is_approved in (0,1,2,3,4,5,6,7) " +
            " order by asr.create_at DESC, asr.update_at DESC ",
            nativeQuery = true)
    Page<AssignmentStudentRegisterDTO> findListAssignmentRegister(AssignmentRegisterDto.AssignmentRegisterSelectListInfo request, Pageable pageable);

    @Query(value = "select" +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        asr.period_assignment_id as periodAssignmentId," +
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
            " where asr.id = ?1 and asr.is_approved in (0,1,2,3,4,5,6,7) ",nativeQuery = true)
    AssignmentStudentRegisterDTO findByFileId(Long id);


    @Query(value = "select * from assignment_student_register where id = ?1 and is_approved in (2) ",nativeQuery = true)
    AssignmentStudentRegister findByFileIdApproveAss(Long id);
    @Query(value = "select" +
            "        smi.instructor_id as instructorId " +
            " from assignment_student_register asr " +
            " join period_assignment pa on asr.period_assignment_id = pa.id " +
            " join admission_period ap on pa.admission_period_id = ap.id " +
            " join student_map_instructor smi on asr.student_id = smi.student_id " +
            " where asr.id = ?1 and asr.is_approved = ?2 and asr.status <> ?3 ",nativeQuery = true)
    Long findAssignmentByInstructorId(Long id, Integer statusInitial, String statusDelete);
    @Query(value = "select" +
            "        smi.instructor_id as instructorId " +
            " from assignment_student_register asr " +
            " join period_assignment pa on asr.period_assignment_id = pa.id " +
            " join admission_period ap on pa.admission_period_id = ap.id " +
            " join student_map_instructor smi on asr.student_id = smi.student_id " +
            " where asr.id = ?1 and asr.is_approved = ?2 and asr.status <> ?3 ",nativeQuery = true)
    Long findAssignmentApproveTypeById(Long id, Integer statusWaitingApprove, String statusDelete);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set assignment_name =:#{#request.assignmentName},file_name =:#{#request.fileName},file_type =:#{#request.fileType},content_assignment =:#{#request.contentAssignment}" +
            ",period_assignment_id = :#{#request.periodAssignmentInfo.id} ,update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id} and is_approved in (0,1,2,3,4,5,6,7) ",nativeQuery = true)
    void updateAssignmentRegister(@Param("request") AssignmentStudentRegister request);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set assignment_name =:#{#request.assignmentName}" +
            ",period_assignment_id = :#{#request.periodAssignmentInfo.id} ,update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id} and is_approved in (0,1,2,3,4,5,6,7) ",nativeQuery = true)
    void updateAssignmentRegisterNoFile(@Param("request") AssignmentStudentRegister request);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids and is_approved in (0,1,2,3,4,5,6,7) ",nativeQuery = true)
    void deleteAssignmentRegister(@Param("request") AssignmentStudentRegister request, @Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set is_approved =:#{#request.isApproved},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void changeStatusAssignmentRegister(@Param("request") AssignmentStudentRegister request, @Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set is_approved =:#{#request.isApproved},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id =:#{#request.id}",nativeQuery = true)
    void sendRequestAssignmentRegister(@Param("request") AssignmentStudentRegister request);

    @Query(value = "select" +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        asr.period_assignment_id as periodAssignmentId," +
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
            "where (:#{#request.assignmentStudentRegisterId} is null or ''  = :#{#request.assignmentStudentRegisterId} or asr.id like :#{#request.assignmentStudentRegisterId})" +
            "and (:#{#request.assignmentStudentRegisterName} is null or ''  = :#{#request.assignmentStudentRegisterName} or asr.assignment_name like '%'+:#{#request.assignmentStudentRegisterName}+'%')" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or asr.status = :#{#request.status})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or asr.create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.studentId} is null or ''  = :#{#request.studentId} or asr.student_id = :#{#request.studentId}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or asr.create_at <= :#{#request.toDate}) " +
            "and asr.is_approved = 2 " +
            " order by asr.create_at DESC, asr.update_at DESC ",
            nativeQuery = true)
    Page<AssignmentStudentRegisterDTO> findListAssignmentRegisterIsApprove(AssignmentRegisterDto.AssignmentRegisterSelectListInfo request, Pageable pageable);

    @Query(value = "select" +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        asr.period_assignment_id as periodAssignmentId," +
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
            "where is_approved = 2 " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    List<AssignmentStudentRegisterDTO> findListAllAssignmentRegisterIsApprove();

    @Query(value = "select " +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        asr.period_assignment_id as periodAssignmentId," +
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
            "where (:#{#request.assignmentStudentRegisterId} is null or ''  = :#{#request.assignmentStudentRegisterId} or asr.id like :#{#request.assignmentStudentRegisterId})" +
            "and (:#{#request.assignmentStudentRegisterName} is null or ''  = :#{#request.assignmentStudentRegisterName} or asr.assignment_name like '%'+:#{#request.assignmentStudentRegisterName}+'%')" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or asr.status = :#{#request.status})" +
            "and (:#{#request.periodAssignmentId} is null or ''  = :#{#request.periodAssignmentId} or period_assignment_id = :#{#request.periodAssignmentId})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or asr.create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.regUser} is null or ''  = :#{#request.regUser} or asr.create_user = :#{#request.regUser}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or asr.create_at <= :#{#request.toDate}) " +
            "and asr.is_approved in (1) " +
            " order by asr.create_at DESC, asr.update_at DESC ",
            nativeQuery = true)
    Page<AssignmentStudentRegisterDTO> findListAssignmentWaitingsend(AssignmentRegisterDto.AssignmentRegisterSelectListInfo request, Pageable pageable);


    @Query(value = "select  " +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        asr.period_assignment_id as periodAssignmentId," +
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
            " where (:#{#request.assignmentStudentRegisterId} is null or ''  = :#{#request.assignmentStudentRegisterId} or asr.id like :#{#request.assignmentStudentRegisterId})" +
            " and (:#{#request.assignmentStudentRegisterName} is null or ''  = :#{#request.assignmentStudentRegisterName} or asr.assignment_name like '%'+:#{#request.assignmentStudentRegisterName}+'%')" +
            " and (:#{#request.status} is null or ''  = :#{#request.status} or asr.status = :#{#request.status})" +
            " and (:#{#request.periodAssignmentId} is null or ''  = :#{#request.periodAssignmentId} or asr.period_assignment_id = :#{#request.periodAssignmentId})" +
            " and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or asr.create_at >= :#{#request.fromDate}) " +
            " and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or asr.create_at <= :#{#request.toDate}) " +
            "and (:#{#request.intructorId} is null or ''  = :#{#request.intructorId} or smi.instructor_id = :#{#request.intructorId}) " +
            "and asr.is_approved in (0,1,2,3,4,5,6,7) " +
            " order by asr.create_at DESC, asr.update_at DESC ",
            nativeQuery = true)
    Page<AssignmentStudentRegisterDTO> findListAssignmentRegisterByInstructorId(AssignmentRegisterDto.AssignmentRegisterSelectListOfInstructorIdInfo request, Pageable pageable);



    @Query(value = "select" +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        asr.period_assignment_id as periodAssignmentId," +
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
            "where (:#{#request.assignmentStudentRegisterId} is null or ''  = :#{#request.assignmentStudentRegisterId} or asr.id like :#{#request.assignmentStudentRegisterId})" +
            "and (:#{#request.assignmentStudentRegisterName} is null or ''  = :#{#request.assignmentStudentRegisterName} or asr.assignment_name like '%'+:#{#request.assignmentStudentRegisterName}+'%')" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or asr.status = :#{#request.status})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or asr.create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.intructorId} is null or ''  = :#{#request.intructorId} or smi.instructor_id = :#{#request.intructorId}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or asr.create_at <= :#{#request.toDate}) " +
            "and asr.is_approved = 2 " +
            " order by asr.create_at DESC, asr.update_at DESC ",
            nativeQuery = true)
    Page<AssignmentStudentRegisterDTO> findListAssignmentRegisterIsApproveByInstructorId(AssignmentRegisterDto.AssignmentRegisterSelectListOfInstructorIdInfo request, Pageable pageable);



    @Query(value = "select " +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        asr.period_assignment_id as periodAssignmentId," +
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
            "where (:#{#request.assignmentStudentRegisterId} is null or ''  = :#{#request.assignmentStudentRegisterId} or asr.id like :#{#request.assignmentStudentRegisterId})" +
            "and (:#{#request.assignmentStudentRegisterName} is null or ''  = :#{#request.assignmentStudentRegisterName} or asr.assignment_name like '%'+:#{#request.assignmentStudentRegisterName}+'%')" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or asr.status = :#{#request.status})" +
            "and (:#{#request.periodAssignmentId} is null or ''  = :#{#request.periodAssignmentId} or period_assignment_id = :#{#request.periodAssignmentId})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or asr.create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.intructorId} is null or ''  = :#{#request.intructorId} or smi.instructor_id = :#{#request.intructorId}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or asr.create_at <= :#{#request.toDate}) " +
            "and asr.is_approved in (1) " +
            " order by asr.create_at DESC, asr.update_at DESC ",
            nativeQuery = true)
    Page<AssignmentStudentRegisterDTO> findListAssignmentWaitingsendByInstructor(AssignmentRegisterDto.AssignmentRegisterSelectListOfInstructorIdInfo request, Pageable pageable);

    @Query(value = "select * from assignment_student_register where is_approved in (1) and id in :ids ",nativeQuery = true)
    List<AssignmentStudentRegister> findListWaitingApproveAssignment(@Param("ids") List<Long> ids);


    @Query(value = "select * from assignment_student_register where is_approved = 0 and id in :ids ",nativeQuery = true)
    List<AssignmentStudentRegister> findListInitialApproveAssignment(@Param("ids") List<Long> ids);


    @Query(value = "select * from assignment_student_register where is_approved = 2 and id in :ids ",nativeQuery = true)
    List<AssignmentStudentRegister> findListProcessApproveAssignment(@Param("ids") List<Long> ids);


    @Query(value = "select * from assignment_student_register where is_approved in (5) and id in :ids ",nativeQuery = true)
    List<AssignmentStudentRegister> findListWaitingFinalApproveAssignment(@Param("ids") List<Long> ids);


    @Query(value = "select * from assignment_student_register where is_approved in (7) and id in :ids ",nativeQuery = true)
    List<AssignmentStudentRegister> findListFinalApproveAssignment(@Param("ids") List<Long> ids);
}
