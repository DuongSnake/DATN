package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.service.StudentMapInstructorDto;
import com.example.bloodbankmanagement.entity.StudentMapInstructor;
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
public interface StudentMapInstructorRepository extends JpaRepository<StudentMapInstructor, Long> {
    @Query(value = "select *  from student_map_instructor " +
            "where (:#{#request.studentMapInstructorId} is null or ''  = :#{#request.studentMapInstructorId} or id like :#{#request.studentMapInstructorId})" +
            "and (:#{#request.instructorId} is null or ''  = :#{#request.instructorId} or instructor_id = :#{#request.instructorId})" +
            "and (:#{#request.studentId} is null or ''  = :#{#request.studentId} or student_id = :#{#request.studentId})" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<StudentMapInstructor> findListStudentMapInstructor(StudentMapInstructorDto.StudentMapInstructorSelectListInfo request, Pageable pageable);

    @Query(value = "select * from student_map_instructor where id = ?1",nativeQuery = true)
    StudentMapInstructor findByFileId(Long id);


    @Query("select u from StudentMapInstructor u where u.id = ?1")
    StudentMapInstructor findByStudentMapInstructorId(Long id);

    @Query("select u from StudentMapInstructor u where u.status = '1'")
    List<StudentMapInstructor> getStudentMapInstructorIdActive();

    @Modifying
    @Transactional
    @Query(value = "update student_map_instructor set instructor_id =:#{#request.instructorInfo.id}, student_id =:#{#request.studentInfo.id},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateStudentMapInstructor(@Param("request") StudentMapInstructor request);

    @Modifying
    @Transactional
    @Query(value = "update student_map_instructor set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deleteStudentMapInstructor(@Param("request") StudentMapInstructor request, @Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "update student_map_instructor set instructor_id =:#{#request.instructorInfo.id},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE student_id in :ids",nativeQuery = true)
    void updateListStudentWithInstructorId(@Param("request") StudentMapInstructor request, @Param("ids") List<Long> ids);
}
