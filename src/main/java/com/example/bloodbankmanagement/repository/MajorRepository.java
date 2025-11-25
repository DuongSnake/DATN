package com.example.bloodbankmanagement.repository;


import com.example.bloodbankmanagement.dto.service.MajorDto;
import com.example.bloodbankmanagement.entity.Major;
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
public interface MajorRepository extends JpaRepository<Major, Long> {
    @Query(value = "select *  from major " +
            "where (:#{#request.majorId} is null or ''  = :#{#request.majorId} or id like :#{#request.majorId})" +
            "and (:#{#request.majorName} is null or ''  = :#{#request.majorName} or major_name like '%'+:#{#request.majorName}+'%')" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<Major> findListMajor(MajorDto.MajorSelectListInfo request, Pageable pageable);

    @Query(value = "select * from major where id = ?1",nativeQuery = true)
    Major findByFileId(Long id);



    @Query(value = "select * from major where status = '1'",nativeQuery = true)
    List<Major> getAllMajorActive();


    @Query(value = "select * from major where id in :ids",nativeQuery = true)
    List<Major> findMajorInListId( @Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "update major set major_name =:#{#request.majorName},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateMajor(@Param("request") Major request);

    @Modifying
    @Transactional
    @Query(value = "update major set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deleteMajor(@Param("request") Major request, @Param("ids") List<Long> ids);
}
