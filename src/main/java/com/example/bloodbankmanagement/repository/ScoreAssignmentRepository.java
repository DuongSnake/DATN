package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import com.example.bloodbankmanagement.entity.ScoreAssignment;
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
public interface ScoreAssignmentRepository extends JpaRepository<ScoreAssignment, Long> {
    @Query(value = "select *  from score_assignment " +
            "where (:#{#request.scoreAssignmentId} is null or ''  = :#{#request.scoreAssignmentId} or id like :#{#request.scoreAssignmentId})" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<ScoreAssignment> findListScoreAssignment(ScoreAssignmentDto.ScoreAssignmentSelectListInfo request, Pageable pageable);

    @Query(value = "select * from score_assignment where id = ?1",nativeQuery = true)
    ScoreAssignment findByFileId(Long id);

    @Modifying
    @Transactional
    @Query(value = "update score_assignment set " +
            "score_instructor =:#{#request.scoreInstructor} " +
            ",score_examiner =:#{#request.scoreExaminer} " +
            ",score_critical =:#{#request.scoreCritical} " +
            ",assignment_register_info_id =:#{#request.assignmentRegisterInfo.id},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateScoreAssignment(@Param("request") ScoreAssignment request);

    @Modifying
    @Transactional
    @Query(value = "update score_assignment set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deleteScoreAssignment(@Param("request") ScoreAssignment request, @Param("ids") List<Long> ids);
}
