package com.example.bloodbankmanagement.repository;


import com.example.bloodbankmanagement.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
}
