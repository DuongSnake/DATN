package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.objectRepository.UserInfoDto;
import com.example.bloodbankmanagement.dto.service.StudentMapCriticalDto;
import com.example.bloodbankmanagement.entity.StudentMapCritical;
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
public interface StudentMapCriticalRepository  extends JpaRepository<StudentMapCritical, Long> {
    @Query(value = "select *  from student_map_critical " +
            "where (:#{#request.studentMapCriticalId} is null or ''  = :#{#request.studentMapCriticalId} or id like :#{#request.studentMapCriticalId})" +
            "and (:#{#request.criticalId} is null or ''  = :#{#request.criticalId} or critical_teacher_id = :#{#request.criticalId})" +
            "and (:#{#request.studentId} is null or ''  = :#{#request.studentId} or student_id = :#{#request.studentId})" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<StudentMapCritical> findListStudentMapCritical(StudentMapCriticalDto.StudentMapCriticalSelectListInfo request, Pageable pageable);

    @Query("select u from StudentMapCritical u where u.studentInfo.id = ?1 and u.status <> '99' ")
    StudentMapCritical findByStudentMapCriticalByStudentId(Long studentId, String statusDelete);

    @Modifying
    @Transactional
    @Query(value = "update student_map_critical set critical_teacher_id =:#{#request.criticalTeacherInfo.id},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id} and student_id =:#{#request.studentInfo.id}",nativeQuery = true)
    void updateStudentMapCritical(@Param("request") StudentMapCritical request);

    @Query("select u from StudentMapCritical u where u.id = ?1")
    StudentMapCritical findByStudentMapCriticalById(Long studentId);

    @Modifying
    @Transactional
    @Query(value = "update student_map_critical set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deleteStudentMapCritical(@Param("request") StudentMapCritical request, @Param("ids") List<Long> ids);

    @Query(value = "with StudentCTE AS(" +
            "select u.id, u.full_name  from users u " +
            "join roles r on r.id = u.role_id " +
            "where r.name = ?1 and u.status ='1') "+
            "select asr.student_id as id,scte.full_name as fullName " +
            " from assignment_student_register asr " +
            " join StudentCTE scte on asr.student_id = scte.id " +
            " where asr.status <> ?2 and asr.is_approved = ?3 " +
            " and scte.id not in (select student_id from student_map_critical where (critical_teacher_id <> 0 and status <> ?2) )",nativeQuery = true)
    List<UserInfoDto> getListUserHaveApproveTypeAssignment(String valueRole, String statusDelete, Integer statusWaitingFinalApprove);


    @Query(value = "select u.id as id, u.full_name as fullName from users u " +
            "join roles r on r.id = u.role_id " +
            "where r.name = ?1 and u.status ='1' " +
            "and u.id not in (select critical_teacher_id from student_map_critical where student_id = ?2)", nativeQuery = true)
    List<UserInfoDto> getListCriticalByStudentId(String valueRole, Long studentId);
}
