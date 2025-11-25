package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.service.AdmissionPeriodDto;
import com.example.bloodbankmanagement.entity.AdmissionPeriod;
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
public interface AdmissionPeriodRepository extends JpaRepository<AdmissionPeriod, Long> {
    @Query(value = "select *  from admission_period " +
            "where (:#{#request.admissionPeriodId} is null or ''  = :#{#request.admissionPeriodId} or id like :#{#request.admissionPeriodId})" +
            "and (:#{#request.admissionPeriodName} is null or ''  = :#{#request.admissionPeriodName} or admission_period_name like '%'+:#{#request.admissionPeriodName}+'%')" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<AdmissionPeriod> findListAdmissionPeriod(AdmissionPeriodDto.AdmissionPeriodSelectListInfo request, Pageable pageable);

    @Query(value = "select * from admission_period where id = ?1",nativeQuery = true)
    AdmissionPeriod findByFileId(Long id);


    @Query("select u from AdmissionPeriod u where u.status = '1'")
    List<AdmissionPeriod> selectListAdmissionPeriodActiveInNowYear();//Query tat ca cac ky han trong nam

    @Modifying
    @Transactional
    @Query(value = "update admission_period set admission_period_name =:#{#request.admissionPeriodName},start_period =:#{#request.startPeriod},end_period =:#{#request.endPeriod}," +
    "update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateAdmissionPeriod(@Param("request") AdmissionPeriod request);

    @Modifying
    @Transactional
    @Query(value = "update admission_period set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deleteAdmissionPeriod(@Param("request") AdmissionPeriod request, @Param("ids") List<Long> ids);
}
