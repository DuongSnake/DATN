package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.service.InstructorMapMajorDto;
import com.example.bloodbankmanagement.entity.InstructorMapMajor;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstructorMapMajorRepository  extends JpaRepository<InstructorMapMajor, Long> {
    @Query(value = "select *  from instructor_map_major " +
            "where (:#{#request.instructorMapMajorId} is null or ''  = :#{#request.instructorMapMajorId} or id like :#{#request.instructorMapMajorId})" +
            "and (:#{#request.instructorId} is null or ''  = :#{#request.instructorId} or instructor_id = :#{#request.instructorId})" +
            "and (:#{#request.majorId} is null or ''  = :#{#request.majorId} or major_id = :#{#request.majorId})" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<InstructorMapMajor> findListInstructorMapMajor(InstructorMapMajorDto.InstructorMapMajorSelectListInfo request, Pageable pageable);

    @Query(value = "select * from instructor_map_major where id = ?1",nativeQuery = true)
    InstructorMapMajor findByFileId(Long id);


    @Query("select u from InstructorMapMajor u where u.id = ?1")
    InstructorMapMajor findByInstructorMapMajorId(Long id);

    @Modifying
    @Transactional
    @Query(value = "update instructor_map_major set instructor_id =:#{#request.instructorInfo.id}, major_id =:#{#request.majorInfo.id},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateInstructorMapMajor(@Param("request") InstructorMapMajor request);

    @Modifying
    @Transactional
    @Query(value = "update instructor_map_major set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deleteInstructorMapMajor(@Param("request") InstructorMapMajor request, @Param("ids") List<Long> ids);
}
