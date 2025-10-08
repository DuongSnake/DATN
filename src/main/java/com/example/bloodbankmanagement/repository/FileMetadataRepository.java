package com.example.bloodbankmanagement.repository;


import com.example.bloodbankmanagement.dto.objectRepository.FileMetadataDto;
import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import com.example.bloodbankmanagement.entity.FileMetadata;
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
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    @Query(value = "select id as fileId,file_name as fileName,file_type as fileType" +
            ",file_size as fileSize,create_at as createAt  from file_meta_data " +
            "where (:#{#request.fileId} is null or ''  = :#{#request.fileId} or id like :#{#request.fileId})" +
            "and (:#{#request.fileName} is null or ''  = :#{#request.fileName} or file_name like '%'+:#{#request.fileName}+'%')" +
            "and (:#{#request.fileType} is null or ''  = :#{#request.fileType} or file_type like '%'+:#{#request.fileType}+'%')" +
            "and (:#{#request.userUpload} is null or ''  = :#{#request.userUpload} or create_user like '%'+:#{#request.userUpload}+'%')" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,create_tm DESC,update_at DESC,update_tm DESC ",
            nativeQuery = true)
    Page<FileMetadataDto> findListFileUpload(UploadFileDto.UploadFileSelectListInfo request, Pageable pageable);

    @Query(value = "select id as fileId,file_name as fileName,file_type as fileType from file_meta_data where id = ?1",nativeQuery = true)
    FileMetadataDto findByFileId(Long id);


    @Query("select u from FileMetadata u where u.id = ?1")
    FileMetadata findByFileIdToDownload(Long id);

    @Modifying
    @Transactional
    @Query(value = "update file_meta_data set file_name =:#{#request.fileName},file_type =:#{#request.fileType},file_size =:#{#request.fileSize}" +
    ",update_user =:#{#request.updateUser},update_at =:#{#request.updateAt},update_tm =:#{#request.updateTm} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateFileUpload(@Param("request") FileMetadata request);

    @Modifying
    @Transactional
    @Query(value = "update file_meta_data set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt}, update_tm =:#{#request.updateTm}" +
    " WHERE id in :ids",nativeQuery = true)
    void deleteFileUpload(@Param("request") FileMetadata request, @Param("ids") List<Long> ids);
}
