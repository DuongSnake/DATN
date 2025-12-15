package com.example.bloodbankmanagement.repository.student;

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
    @Query(value = "select *  from assignment_student_register " +
            "where (:#{#request.assignmentRegisterId} is null or ''  = :#{#request.assignmentRegisterId} or id like :#{#request.assignmentRegisterId})" +
            "and (:#{#request.assignmentRegisterName} is null or ''  = :#{#request.assignmentRegisterName} or assignment_name like '%'+:#{#request.assignmentRegisterName}+'%')" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status})" +
            "and (:#{#request.periodAssignmentId} is null or ''  = :#{#request.periodAssignmentId} or period_assignment_id = :#{#request.periodAssignmentId})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.regUser} is null or ''  = :#{#request.regUser} or create_user = :#{#request.regUser}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            "and is_approved in (0,1,3) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<AssignmentStudentRegister> findListAssignmentRegister(AssignmentRegisterDto.AssignmentRegisterSelectListInfo request, Pageable pageable);

    @Query(value = "select * from assignment_student_register where id = ?1 and is_approved in (0,1,3) ",nativeQuery = true)
    AssignmentStudentRegister findByFileId(Long id);


    @Query(value = "select * from assignment_student_register where id = ?1 and is_approved in (2) ",nativeQuery = true)
    AssignmentStudentRegister findByFileIdApproveAss(Long id);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set assignment_name =:#{#request.assignmentName},file_name =:#{#request.fileName},file_type =:#{#request.fileType},content_assignment =:#{#request.contentAssignment}" +
            " ,period_assignment_id = :#{#request.periodAssignmentInfo.id} ,update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id} and is_approved in (0,1,3) ",nativeQuery = true)
    void updateAssignmentRegister(@Param("request") AssignmentStudentRegister request);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set assignment_name =:#{#request.assignmentName}" +
            ",period_assignment_id = :#{#request.periodAssignmentInfo.id} ,update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id} and is_approved in (0,1,3) ",nativeQuery = true)
    void updateAssignmentRegisterNoFile(@Param("request") AssignmentStudentRegister request);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids and is_approved in (0,1,3) ",nativeQuery = true)
    void deleteAssignmentRegister(@Param("request") AssignmentStudentRegister request, @Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set is_approved =:#{#request.isApproved},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void changeStatusAssignmentRegister(@Param("request") AssignmentStudentRegister request, @Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set is_approved =:#{#request.isApproved},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id =:#{#request.id}",nativeQuery = true)
    void sendRequestAssignmentRegister(@Param("request") AssignmentStudentRegister request);

    @Query(value = "select *  from assignment_student_register " +
            "where (:#{#request.assignmentRegisterId} is null or ''  = :#{#request.assignmentRegisterId} or id like :#{#request.assignmentRegisterId})" +
            "and (:#{#request.assignmentRegisterName} is null or ''  = :#{#request.assignmentRegisterName} or assignment_name like '%'+:#{#request.assignmentRegisterName}+'%')" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.regUser} is null or ''  = :#{#request.regUser} or create_user = :#{#request.regUser}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            "and is_approved = 2 " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<AssignmentStudentRegister> findListAssignmentRegisterIsApprove(AssignmentRegisterDto.AssignmentRegisterSelectListInfo request, Pageable pageable);
}
