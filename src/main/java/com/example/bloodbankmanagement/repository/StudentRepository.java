package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.service.StudentManagementDto;
import com.example.bloodbankmanagement.entity.Student;
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
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    @Query(value = "select * from student " +
            "where (:#{#request.studentId} is null or ''  = :#{#request.studentId} or id like :#{#request.studentId}) " +
            "and (:#{#request.email} is null or ''  = :#{#request.email} or email like '%'+:#{#request.email}+'%') " +
            "and (:#{#request.phone} is null or ''  = :#{#request.phone} or phone like '%'+:#{#request.phone}+'%') " +
            "and (:#{#request.fullName} is null or ''  = :#{#request.fullName} or full_name like '%'+:#{#request.fullName}+'%') " +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status}) " +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            "order by create_at DESC, update_at DESC ",
            nativeQuery = true)
    Page<Student> findListStudent(@Param("request") StudentManagementDto.StudentSelectListRequest request, Pageable pageable);

    @Query(value = "select * from student where id = ?1", nativeQuery = true)
    Student findStudentById(Long id);

    @Query(value = "select * from student where status = '1'", nativeQuery = true)
    List<Student> getAllStudentActive();

    @Query(value = "select * from student where id in :ids", nativeQuery = true)
    List<Student> findStudentInListId(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "insert into student (email, phone, full_name, total_lesson_debt, status_done_debt, status, create_user, create_at, update_user, update_at) " +
            "values (:#{#request.email}, :#{#request.phone}, :#{#request.fullName}, :#{#request.totalLessonDebt}, :#{#request.statusDoneDebt}, :#{#request.status}, :#{#request.createUser}, :#{#request.createAt}, :#{#request.updateUser}, :#{#request.updateAt})",
            nativeQuery = true)
    void insertStudent(@Param("request") Student request);

    @Modifying
    @Transactional
    @Query(value = "update student set email =:#{#request.email}, phone =:#{#request.phone}, full_name =:#{#request.fullName}, " +
            "update_user =:#{#request.updateUser}, update_at =:#{#request.updateAt} where id =:#{#request.id}",
            nativeQuery = true)
    void updateStudent(@Param("request") Student request);

    @Modifying
    @Transactional
    @Query(value = "update student set status =:#{#request.status}, update_user =:#{#request.updateUser}, update_at =:#{#request.updateAt} where id in :ids",
            nativeQuery = true)
    void deleteStudent(@Param("request") Student request, @Param("ids") List<Long> ids);
}
