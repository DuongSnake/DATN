package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.FileMetadataDto;
import com.example.bloodbankmanagement.dto.objectRepository.SelectListFileUploadDto;
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
    @JoinColumn(name = "assignment_register_info_id")
    private AssignmentStudentRegister assignmentRegisterInfo;

    public static UploadFileDto.UploadFileSelectInfoResponse convertObjectToDto(FileUpload request){
        if(null == request){
            return null;
        }
        Long assignmentRegisterId = null != request.getAssignmentRegisterInfo() ? request.getAssignmentRegisterInfo().getId(): null;
        String assignmentName = null != request.getAssignmentRegisterInfo() ? request.getAssignmentRegisterInfo().getAssignmentName(): null;
        String studentName = null;
        String instructorName =  null;
        UploadFileDto.UploadFileSelectInfoResponse objectData = new UploadFileDto.UploadFileSelectInfoResponse();
        objectData.setFileId(request.getId());
        objectData.setFileName(request.getFileName());
        objectData.setFileSize(request.getFileSize());
        objectData.setFileType(request.getFileType());
        objectData.setCreateAt(request.getCreateAt());
        objectData.setStatus(request.getStatus());
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
            Long assignmentRegisterId = null;
            String assignmentName = null;
            String studentName = null;
            String instructorName = null;
            UploadFileDto.UploadFileListInfo newObject = new UploadFileDto.UploadFileListInfo();
            newObject.setFileId(object.getId());
            newObject.setFileName(object.getFileName());
            newObject.setFileSize(object.getFileSize());
            newObject.setFileType(object.getFileType());
            newObject.setStatus(object.getStatus());
            newObject.setCreateAt(object.getCreateAt());
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

    public static PageAmtListResponseDto<UploadFileDto.UploadFileListInfo>  convertListObjectToDtoUserSite(List<FileUpload> listData){
        PageAmtListResponseDto<UploadFileDto.UploadFileListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        if(null == listData || listData.size() == 0){
            return null;
        }
        List<UploadFileDto.UploadFileListInfo> listObject = new ArrayList<>();
        for (FileUpload object: listData){
            Long assignmentRegisterId = null != object.getAssignmentRegisterInfo() ? object.getAssignmentRegisterInfo().getId(): null;
            String assignmentName = null != object.getAssignmentRegisterInfo() ? object.getAssignmentRegisterInfo().getAssignmentName(): null;
            String studentName = null;
            String instructorName = null;
            UploadFileDto.UploadFileListInfo newObject = new UploadFileDto.UploadFileListInfo();
            newObject.setFileId(object.getId());
            newObject.setFileName(object.getFileName());
            newObject.setFileSize(object.getFileSize());
            newObject.setFileType(object.getFileType());
            newObject.setStatus(object.getStatus());
            newObject.setCreateAt(object.getCreateAt());
            newObject.setAssignmentRegisterId(assignmentRegisterId);
            newObject.setAssignmentName(assignmentName);
            newObject.setStudentName(studentName);
            newObject.setInstructorName(instructorName);
            listObject.add(newObject);
        }
        objectDtoResponse.setData(listObject);
        objectDtoResponse.setTotalRecord((int) listData.size());
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<UploadFileDto.UploadFileNewListInfo> convertListNewObjectToDto(List<SelectListFileUploadDto> listData){
        if(null == listData || listData.size() == 0){
            return null;
        }
        PageAmtListResponseDto<UploadFileDto.UploadFileNewListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<UploadFileDto.UploadFileNewListInfo> listObject = new ArrayList<>();
        for (int i=0;i< listData.size();i++){
            UploadFileDto.UploadFileNewListInfo newObject = new UploadFileDto.UploadFileNewListInfo();
            newObject.setFileId(listData.get(i).getFileId());
            newObject.setFileName(listData.get(i).getFileName());
            newObject.setFileType(listData.get(i).getFileType());
            newObject.setStatus(listData.get(i).getStatus());
            newObject.setAssignmentRegisterId(listData.get(i).getAssignmentRegisterId());
            newObject.setAssignmentName(listData.get(i).getAssignmentName());
            newObject.setStudentName(listData.get(i).getStudentName());
            newObject.setAdmissionPeriodId(listData.get(i).getAdmissionPeriodId());
            newObject.setAdmissionPeriodName(listData.get(i).getAdmissionPeriodName());
            listObject.add(newObject);
        }
        objectDtoResponse.setData(listObject);
        objectDtoResponse.setTotalRecord(listData.size());
        return objectDtoResponse;
    }

}
