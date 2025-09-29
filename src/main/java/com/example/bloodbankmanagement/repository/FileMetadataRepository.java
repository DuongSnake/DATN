package com.example.bloodbankmanagement.repository;


import com.example.bloodbankmanagement.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    @Query("SELECT f FROM FileMetadata f WHERE f.id = :id")
    Optional<FileMetadata> timTheoId(Long id);
}
