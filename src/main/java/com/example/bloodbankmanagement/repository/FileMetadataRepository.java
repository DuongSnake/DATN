package com.example.bloodbankmanagement.repository;


import com.example.bloodbankmanagement.dto.objectRepository.FileMetadataDto;
import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import com.example.bloodbankmanagement.entity.FileUpload;
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
public interface FileMetadataRepository extends JpaRepository<FileUpload, Long> {
    @Query(value = "select *  from file_meta_data " +
            "where (:#{#request.fileId} is null or ''  = :#{#request.fileId} or id like :#{#request.fileId})" +
            "and (:#{#request.periodAssignmentId} is null or ''  = :#{#request.periodAssignmentId} or period_assignment_id like :#{#request.periodAssignmentId})" +
            "and (:#{#request.assignmentRegisterId} is null or ''  = :#{#request.assignmentRegisterId} or assignment_register_info_id like :#{#request.assignmentRegisterId})" +
            "and (:#{#request.fileName} is null or ''  = :#{#request.fileName} or file_name like '%'+:#{#request.fileName}+'%')" +
            "and (:#{#request.fileType} is null or ''  = :#{#request.fileType} or file_type like '%'+:#{#request.fileType}+'%')" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status})" +
            "and (:#{#request.userUpload} is null or ''  = :#{#request.userUpload} or create_user like '%'+:#{#request.userUpload}+'%')" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<FileUpload> findListFileUpload(UploadFileDto.UploadFileSelectListInfo request, Pageable pageable);

    @Query(value = "select id as fileId,file_name as fileName,file_type as fileType" +
            ",file_size as fileSize,create_at as createAt  from file_meta_data " +
            "where (:#{#request.fileId} is null or ''  = :#{#request.fileId} or id like :#{#request.fileId})" +
            "and (:#{#request.fileName} is null or ''  = :#{#request.fileName} or file_name like '%'+:#{#request.fileName}+'%')" +
            "and (:#{#request.fileType} is null or ''  = :#{#request.fileType} or file_type like '%'+:#{#request.fileType}+'%')" +
            "and (:#{#request.userUpload} is null or ''  = :#{#request.userUpload} or create_user like '%'+:#{#request.userUpload}+'%')" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<FileMetadataDto> findListFileUploadResponseObjectInterfaceObject(UploadFileDto.UploadFileSelectListInfo request, Pageable pageable);

    @Query(value = "select * from file_meta_data where id = ?1",nativeQuery = true)
    FileUpload findByFileId(Long id);


    @Query("select u from FileUpload u where u.id = ?1")
    FileUpload findByFileIdToDownload(Long id);


    @Query("select u from FileUpload u where u.assignmentRegisterInfo.id = ?1")
    FileUpload findListFileUpload(Long id);

    @Modifying
    @Transactional
    @Query(value = "update file_meta_data set file_name =:#{#request.fileName},file_type =:#{#request.fileType},file_size =:#{#request.fileSize},period_assignment_id =:#{#request.periodAssignmentInfo.id},assignment_register_info_id =:#{#request.assignmentRegisterInfo.id}" +
            ",update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateFileUpload(@Param("request") FileUpload request);

    @Modifying
    @Transactional
    @Query(value = "update file_meta_data set file_name =:#{#request.fileName},file_type =:#{#request.fileType},file_size =:#{#request.fileSize}" +
    ",update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateFileUploadOld(@Param("request") FileUpload request);

    @Modifying
    @Transactional
    @Query(value = "update file_meta_data set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt}" +
    " WHERE id in :ids",nativeQuery = true)
    void deleteFileUpload(@Param("request") FileUpload request, @Param("ids") List<Long> ids);
}
