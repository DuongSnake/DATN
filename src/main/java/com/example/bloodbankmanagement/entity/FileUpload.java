package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.FileMetadataDto;
import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "file_meta_data")
public class FileUpload extends EntityCommon {
    private String fileName;
    private String fileType;
    private long fileSize;
    private String status;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data")
    private Blob data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_assignment_id")
    private PeriodAssignment periodAssignmentInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_register_info_id")
    private AssignmentStudentRegister assignmentRegisterInfo;

    public static UploadFileDto.UploadFileSelectInfoResponse convertObjectToDto(FileUpload request){
        if(null == request){
            return null;
        }
        Long periodAssignmentId = null != request.getPeriodAssignmentInfo() ? request.getPeriodAssignmentInfo().getId(): null;
        LocalDate periodAssignmentExpireTime = null != request.getPeriodAssignmentInfo() ? request.getPeriodAssignmentInfo().getEndPeriod(): null;
        Long assignmentRegisterId = null != request.getAssignmentRegisterInfo() ? request.getAssignmentRegisterInfo().getId(): null;
        String assignmentName = null != request.getAssignmentRegisterInfo() ? request.getAssignmentRegisterInfo().getAssignmentName(): null;
        String studentName = null != request.getAssignmentRegisterInfo() ? request.getAssignmentRegisterInfo().getStudentMapInstructor().getStudentInfo().getFullName(): null;
        String instructorName = null != request.getAssignmentRegisterInfo() ? request.getAssignmentRegisterInfo().getStudentMapInstructor().getInstructorInfo().getFullName(): null;
        UploadFileDto.UploadFileSelectInfoResponse objectData = new UploadFileDto.UploadFileSelectInfoResponse();
        objectData.setFileId(request.getId());
        objectData.setFileName(request.getFileName());
        objectData.setFileSize(request.getFileSize());
        objectData.setFileType(request.getFileType());
        objectData.setCreateAt(request.getCreateAt());
        objectData.setStatus(request.getStatus());
        objectData.setPeriodAssignmentId(periodAssignmentId);
        objectData.setPeriodAssignmentExpireTime(periodAssignmentExpireTime);
        objectData.setAssignmentRegisterId(assignmentRegisterId);
        objectData.setAssignmentName(assignmentName);
        objectData.setStudentName(studentName);
        objectData.setInstructorName(instructorName);
        return objectData;
    }

    public static PageAmtListResponseDto<UploadFileDto.UploadFileListInfo> convertListObjectToDto(Page<FileUpload> listData){
        if(null == listData.getContent() || listData.getContent().size() == 0){
            return null;
        }
        PageAmtListResponseDto<UploadFileDto.UploadFileListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<UploadFileDto.UploadFileListInfo> listObject = new ArrayList<>();
        for (FileUpload object: listData){
            Long periodAssignmentId = null != object.getPeriodAssignmentInfo() ? object.getPeriodAssignmentInfo().getId(): null;
            LocalDate periodAssignmentExpireTime = null != object.getPeriodAssignmentInfo() ? object.getPeriodAssignmentInfo().getEndPeriod(): null;
            Long assignmentRegisterId = null != object.getAssignmentRegisterInfo() ? object.getAssignmentRegisterInfo().getId(): null;
            String assignmentName = null != object.getAssignmentRegisterInfo() ? object.getAssignmentRegisterInfo().getAssignmentName(): null;
            String studentName = null != object.getAssignmentRegisterInfo() ? object.getAssignmentRegisterInfo().getStudentMapInstructor().getStudentInfo().getFullName(): null;
            String instructorName = null != object.getAssignmentRegisterInfo() ? object.getAssignmentRegisterInfo().getStudentMapInstructor().getInstructorInfo().getFullName(): null;
            UploadFileDto.UploadFileListInfo newObject = new UploadFileDto.UploadFileListInfo();
            newObject.setFileId(object.getId());
            newObject.setFileName(object.getFileName());
            newObject.setFileSize(object.getFileSize());
            newObject.setFileType(object.getFileType());
            newObject.setStatus(object.getStatus());
            newObject.setCreateAt(object.getCreateAt());

            newObject.setPeriodAssignmentId(periodAssignmentId);
            newObject.setPeriodAssignmentExpireTime(periodAssignmentExpireTime);
            newObject.setAssignmentRegisterId(assignmentRegisterId);
            newObject.setAssignmentName(assignmentName);
            newObject.setStudentName(studentName);
            newObject.setInstructorName(instructorName);
            listObject.add(newObject);
        }
        objectDtoResponse.setData(listObject);
        objectDtoResponse.setTotalRecord((int) listData.getTotalElements());
        return objectDtoResponse;
    }
}
