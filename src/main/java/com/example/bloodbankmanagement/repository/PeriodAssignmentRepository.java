package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.service.PeriodAssignmentDto;
import com.example.bloodbankmanagement.entity.PeriodAssignment;
import com.example.bloodbankmanagement.entity.PeriodAssignment;
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
public interface PeriodAssignmentRepository extends JpaRepository<PeriodAssignment, Long> {
    @Query(value = "select *  from period_assignment " +
            "where (:#{#request.periodAssignmentId} is null or ''  = :#{#request.periodAssignmentId} or id like :#{#request.periodAssignmentId})" +
            "and (:#{#request.admissionPeriodId} is null or ''  = :#{#request.admissionPeriodId} or admission_period_id like :#{#request.admissionPeriodId})" +
            "and (:#{#request.majorId} is null or ''  = :#{#request.majorId} or major_id like :#{#request.majorId})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<PeriodAssignment> findListPeriodAssignment(PeriodAssignmentDto.PeriodAssignmentSelectListInfo request, Pageable pageable);

    @Query(value = "select * from period_assignment where id = ?1",nativeQuery = true)
    PeriodAssignment findByFileId(Long id);


    @Query("select u from PeriodAssignment u where u.status = '1' ")
    List<PeriodAssignment> getListPeriodAssignmentAllActive();

    @Modifying
    @Transactional
    @Query(value = "update period_assignment set note =:#{#request.note}, start_period =:#{#request.startPeriod}, end_period =:#{#request.endPeriod}" +
    ",admission_period_id =:#{#request.admissionPeriodInfo.id},major_id =:#{#request.majorInfo.id},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updatePeriodAssignment(@Param("request") PeriodAssignment request);

    @Modifying
    @Transactional
    @Query(value = "update period_assignment set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deletePeriodAssignment(@Param("request") PeriodAssignment request, @Param("ids") List<Long> ids);
}
