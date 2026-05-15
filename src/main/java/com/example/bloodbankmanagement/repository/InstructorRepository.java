package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.service.InstructorDto;
import com.example.bloodbankmanagement.entity.Instructor;
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
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    
    @Query(value = "select * from instructor " +
            "where (:#{#request.instructorId} is null or ''  = :#{#request.instructorId} or id like :#{#request.instructorId}) " +
            "and (:#{#request.email} is null or ''  = :#{#request.email} or email like '%'+:#{#request.email}+'%') " +
            "and (:#{#request.phone} is null or ''  = :#{#request.phone} or phone like '%'+:#{#request.phone}+'%') " +
            "and (:#{#request.fullName} is null or ''  = :#{#request.fullName} or full_name like '%'+:#{#request.fullName}+'%') " +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status}) " +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            "order by create_at DESC, update_at DESC ",
            nativeQuery = true)
    Page<Instructor> findListInstructor(@Param("request") InstructorDto.InstructorSelectListRequest request, Pageable pageable);

    @Query(value = "select * from instructor where id = ?1", nativeQuery = true)
    Instructor findInstructorById(Long id);

    @Query(value = "select * from instructor where status = '1'", nativeQuery = true)
    List<Instructor> getAllInstructorActive();

    @Query(value = "select * from instructor where id in :ids", nativeQuery = true)
    List<Instructor> findInstructorInListId(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "insert into instructor (email, phone, full_name, status, create_user, create_at, update_user, update_at) " +
            "values (:#{#request.email}, :#{#request.phone}, :#{#request.fullName}, :#{#request.status}, :#{#request.createUser}, :#{#request.createAt}, :#{#request.updateUser}, :#{#request.updateAt})",
            nativeQuery = true)
    void insertInstructor(@Param("request") Instructor request);

    @Modifying
    @Transactional
    @Query(value = "update instructor set email =:#{#request.email}, phone =:#{#request.phone}, full_name =:#{#request.fullName}, " +
            "update_user =:#{#request.updateUser}, update_at =:#{#request.updateAt} where id =:#{#request.id}",
            nativeQuery = true)
    void updateInstructor(@Param("request") Instructor request);

    @Modifying
    @Transactional
    @Query(value = "update instructor set status =:#{#request.status}, update_user =:#{#request.updateUser}, update_at =:#{#request.updateAt} where id in :ids",
            nativeQuery = true)
    void deleteInstructor(@Param("request") Instructor request, @Param("ids") List<Long> ids);
}
