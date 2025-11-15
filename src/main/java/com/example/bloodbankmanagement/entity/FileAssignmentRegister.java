package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Data
@NoArgsConstructor
@Entity
@Table(name = "file_assignment_register")//thong tin file upload de giao vien duyet de tai
public class FileAssignmentRegister extends EntityCommon {
    private String fileName;
    private String fileType;
    private long fileSize;
    private String status;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data")
    private Blob data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_map_instructor_id")
    private StudentMapInstructor studentMapInstructor;
}
