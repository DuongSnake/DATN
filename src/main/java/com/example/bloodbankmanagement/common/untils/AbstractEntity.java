package com.example.bloodbankmanagement.common.untils;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Getter
@Setter
public class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String createAt;
    private String updateAt;
    private String createTm;
    private String updateTm;
    private String createUser;
    private String updateUser;

    @PrePersist
    public void prePersist() {
        this.createAt = DateUtil.strNowDate();
        this.createTm = DateUtil.strNowTime();
    }
}
