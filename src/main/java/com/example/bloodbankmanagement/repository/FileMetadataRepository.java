package com.example.bloodbankmanagement.repository;


import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentRegisterDTO;
import com.example.bloodbankmanagement.dto.objectRepository.SelectListFileUploadDto;
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
            "and (:#{#request.fileName} is null or ''  = :#{#request.fileName} or file_name like '%'+:#{#request.fileName}+'%')" +
            "and (:#{#request.fileType} is null or ''  = :#{#request.fileType} or file_type like '%'+:#{#request.fileType}+'%')" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status})" +
            "and (:#{#request.userUpload} is null or ''  = :#{#request.userUpload} or create_user like '%'+:#{#request.userUpload}+'%')" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<FileUpload> findListFileUpload(UploadFileDto.UploadFileSelectListInfo request, Pageable pageable);

    @Query(value = "select " +
            " fmd.id as fileId,fmd.file_name as fileName,fmd.file_type as fileType,us.full_name as studentName " +
            " ,fmd.assignment_register_info_id as assignmentRegisterId,asr.assignment_name as assignmentName " +
            " ,ap.id as admissionPeriodId, ap.admission_period_name as admissionPeriodName,fmd.status " +
            " from file_meta_data fmd " +
            " join assignment_student_register asr on fmd.assignment_register_info_id = asr.id" +
            " join period_assignment pa on asr.period_assignment_id = pa.id " +
            " join admission_period ap on pa.admission_period_id = ap.id " +
            " join users us on asr.student_id = us.id " +
            " where (:#{#request.fileId} is null or ''  = :#{#request.fileId} or fmd.id like :#{#request.fileId}) " +
            " and (:#{#request.fileName} is null or ''  = :#{#request.fileName} or fmd.file_name like '%'+:#{#request.fileName}+'%') " +
            " and (:#{#request.fileType} is null or ''  = :#{#request.fileType} or fmd.file_type like '%'+:#{#request.fileType}+'%') " +
            " and (:#{#request.admissionPeriodId} is null or ''  = :#{#request.admissionPeriodId} or ap.id like '%'+:#{#request.admissionPeriodId}+'%') " +
            " and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or fmd.create_at >= :#{#request.fromDate}) " +
            " and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or fmd.create_at <= :#{#request.toDate}) " +
            " and asr.is_approved not in (0,1) " +
            " order by fmd.create_at DESC, fmd.update_at DESC ",
            nativeQuery = true)
    Page<SelectListFileUploadDto> findListFileUploadResponseObjectInterfaceObject(UploadFileDto.UploadFileSelectListNewInfo request, Pageable pageable);

    @Query(value = "select * from file_meta_data where id = ?1",nativeQuery = true)
    FileUpload findByFileId(Long id);


    @Query("select u from FileUpload u where u.id = ?1")
    FileUpload findByFileIdToDownload(Long id);


    @Query("select u from FileUpload u where u.assignmentRegisterInfo.id = ?1 and u.status = '1' ")
    List<FileUpload> findListFileUpload(Long id);

    @Modifying
    @Transactional
    @Query(value = "update file_meta_data set file_name =:#{#request.fileName},file_type =:#{#request.fileType},file_size =:#{#request.fileSize},assignment_register_info_id =:#{#request.assignmentRegisterInfo.id}" +
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

    @Query(value = "select  " +
            "        asr.id as assignmentStudentRegisterId," +
            "        asr.assignment_name as assignmentStudentRegisterName," +
            "        ap.id as periodAssignmentId," +
            "        ap.admission_period_name as periodAssignmentName," +
            "        pa.end_period as expirePeriodDate," +
            "        asr.student_id as studentId," +
            "        (select full_name from users where id = asr.student_id and status = '1') as studentName," +
            "         case " +
            "          when smi.instructor_id is null then '' " +
            "          when smi.instructor_id is not null then(select full_name from users where id = smi.instructor_id and status = '1') " +
            "          else '' end as instructorName,"+
            "        asr.file_name as fileName," +
            "        asr.file_type as fileType," +
            "        asr.is_approved as isApproved," +
            "        asr.status_auto_map as statusAutoMap," +
            "        asr.status as status," +
            "        asr.create_user as createUser," +
            "        asr.create_at as createAt," +
            "        asr.old_value_id as oldValueId " +
            " from assignment_student_register asr " +
            " join period_assignment pa on asr.period_assignment_id = pa.id " +
            " join admission_period ap on pa.admission_period_id = ap.id " +
            " join student_map_instructor smi on asr.student_id = smi.student_id " +
            " and (:#{#request.admissionPeriodId} is null or ''  = :#{#request.admissionPeriodId} or ap.id = :#{#request.admissionPeriodId}) " +
            " and asr.is_approved  not in (0,1) " +
            " order by asr.create_at DESC, asr.update_at DESC ",
            nativeQuery = true)
    List<AssignmentStudentRegisterDTO> findListAssignmentRegisterIsFinalApproveByPeriodId(UploadFileDto.ListAssignmentRegisterByAdmissionPeriodInfo request);
}
