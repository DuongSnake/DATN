package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.service.InstructorMapPeriodAssignmentDto;
import com.example.bloodbankmanagement.entity.InstructorMapPeriodAssignment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstructorMapPeriodAssignmentRepository  extends JpaRepository<InstructorMapPeriodAssignment, Long> {
    @Query(value = "select *  from instructor_map_instructor " +
            "where (:#{#request.instructorMapPeriodAssignmentId} is null or ''  = :#{#request.instructorMapPeriodAssignmentId} or id like :#{#request.instructorMapPeriodAssignmentId})" +
            "and (:#{#request.instructorId} is null or ''  = :#{#request.instructorId} or instructor_id = :#{#request.instructorId})" +
            "and (:#{#request.periodAssignmentId} is null or ''  = :#{#request.periodAssignmentId} or period_assignment_id = :#{#request.periodAssignmentId})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<InstructorMapPeriodAssignment> findListInstructorMapPeriodAssignment(InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectListInfo request, Pageable pageable);

    @Query(value = "select * from instructor_map_instructor where id = ?1",nativeQuery = true)
    InstructorMapPeriodAssignment findByFileId(Long id);


    @Query("select u from InstructorMapPeriodAssignment u where u.id = ?1")
    InstructorMapPeriodAssignment findByInstructorMapPeriodAssignmentId(Long id);

    @Modifying
    @Transactional
    @Query(value = "update instructor_map_instructor set instructor_id =:#{#request.instructorInfo.id}, period_assignment_id =:#{#request.periodAssignmentInfo.id},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateInstructorMapPeriodAssignment(@Param("request") InstructorMapPeriodAssignment request);

    @Modifying
    @Transactional
    @Query(value = "update instructor_map_instructor set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deleteInstructorMapPeriodAssignment(@Param("request") InstructorMapPeriodAssignment request, @Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "update instructor_map_instructor set instructor_id =:#{#request.periodAssignmentInfo.id},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE period_assignment_id in :ids",nativeQuery = true)
    void updateListStudentWithInstructorId(@Param("request") InstructorMapPeriodAssignment request, @Param("ids") List<Long> ids);
}
