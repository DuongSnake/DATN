package com.example.bloodbankmanagement.repository;

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
    @Query(value = "select *  from assignment_student_register " +
            "where (:#{#request.assignmentStudentRegisterId} is null or ''  = :#{#request.assignmentStudentRegisterId} or id like :#{#request.assignmentStudentRegisterId})" +
            "and (:#{#request.assignmentStudentRegisterName} is null or ''  = :#{#request.assignmentStudentRegisterName} or assignment_name like '%'+:#{#request.assignmentStudentRegisterName}+'%')" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<AssignmentStudentRegister> findListAssignmentStudentRegister(AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectListInfo request, Pageable pageable);

    @Query(value = "select * from assignment_student_register where id = ?1",nativeQuery = true)
    AssignmentStudentRegister findByFileId(Long id);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set assignment_name =:#{#request.assignmentName},file_name =:#{#request.fileName},file_type =:#{#request.fileType},content_assignment =:#{#request.contentAssignment}" +
            ",student_map_instructor_id =:#{#request.studentMapInstructor.id} ,update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateAssignmentStudentRegister(@Param("request") AssignmentStudentRegister request);

    @Modifying
    @Transactional
    @Query(value = "update assignment_student_register set assignment_name =:#{#request.assignmentName}" +
            ",student_map_instructor_id =:#{#request.studentMapInstructor.id},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
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
