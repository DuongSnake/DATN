package com.example.bloodbankmanagement.common.untils;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Getter
@Setter
public class EntityCommon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate createAt;
    private LocalDate updateAt;
    private String status;
    private String createUser;
    private String updateUser;

    @PrePersist
    public void prePersist() {
        this.createAt = LocalDate.now();
    }
}
