package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.service.ClassRoomDto;
import com.example.bloodbankmanagement.entity.ClassRoom;
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
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    @Query(value = "select *  from class_room " +
            "where (:#{#request.classId} is null or ''  = :#{#request.majorId} or id like :#{#request.majorId})" +
            "and (:#{#request.className} is null or ''  = :#{#request.className} or class_name like '%'+:#{#request.className}+'%')" +
            "and (:#{#request.majorId} is null or ''  = :#{#request.majorId} or major_id = :#{#request.majorId})" +
            "and (:#{#request.createUser} is null or ''  = :#{#request.createUser} or create_user like '%'+:#{#request.createUser}+'%')" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<ClassRoom> findListClassRoom(ClassRoomDto.ClassRoomSelectListInfo request, Pageable pageable);

    @Query(value = "select * from class_room where id = ?1",nativeQuery = true)
    ClassRoom findByFileId(Long id);


    @Query("select u from ClassRoom u where u.id = ?1")
    ClassRoom findByFileIdToDownload(Long id);

    @Modifying
    @Transactional
    @Query(value = "update class_room set class_name =:#{#request.className},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateClassRoom(@Param("request") ClassRoom request);

    @Modifying
    @Transactional
    @Query(value = "update class_room set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deleteClassRoom(@Param("request") ClassRoom request, @Param("ids") List<Long> ids);

}
