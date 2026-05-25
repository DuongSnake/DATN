package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "calculate_average_score")
public class CalculateAverageScore extends EntityCommon {
    Double rateExam;
    Double rateInstructor;
    Double totalRate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_period_id")
    private AdmissionPeriod admissionPeriodInfo;
}
