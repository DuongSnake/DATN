package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.entity.CalculateAverageScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculateAverageScoreRepository extends JpaRepository<CalculateAverageScore, Long> {
    @Query(value = "select * from calculate_average_score where id = ?1",nativeQuery = true)
    CalculateAverageScore findByFileId(Long id);

    @Query(value = "select * from calculate_average_score where admission_period_id = ?1",nativeQuery = true)
    CalculateAverageScore findByPeriodAssignmentId(Long id);
}
