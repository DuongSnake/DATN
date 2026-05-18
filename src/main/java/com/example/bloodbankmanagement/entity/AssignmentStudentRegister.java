package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentRegisterDTO;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "assignment_student_register")
public class AssignmentStudentRegister extends EntityCommon {
    private String assignmentName;
    private String fileName;
    private String fileType;
    private String statusAutoMap;
    private Integer isApproved;//status approve 0: not accept 1:pending 2:accept 3:bao luu
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "content_assignment")
    private Blob contentAssignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_assignment_id")
    private PeriodAssignment periodAssignmentInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User studentInfo;//student choose to map with teacher
    private Long oldValueId;

    public static AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse convertToDto(AssignmentStudentRegisterDTO request){
        AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse objectDtoResponse = new AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setAssignmentStudentRegisterId(request.getAssignmentStudentRegisterId());
            objectDtoResponse.setAssignmentStudentRegisterName(request.getAssignmentStudentRegisterName());
            objectDtoResponse.setStudentName(request.getStudentName());
            objectDtoResponse.setInstructorName(request.getInstructorName());
            objectDtoResponse.setFileName(request.getFileName());
            objectDtoResponse.setFileType(request.getFileType());
            objectDtoResponse.setIsApproved(request.getIsApproved());
            objectDtoResponse.setPeriodAssignmentId(request.getPeriodAssignmentId());
            objectDtoResponse.setPeriodAssignmentName(request.getPeriodAssignmentName());
            objectDtoResponse.setExpirePeriodDate(request.getExpirePeriodDate());
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
            objectDtoResponse.setStatusAutoMap(request.getStatusAutoMap());
            objectDtoResponse.setCreateUser(request.getCreateUser());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> convertListObjectToDto(List<AssignmentStudentRegisterDTO> listRequestUser, Long totalRecord){
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> listAssignmentStudentRegisterDto = new ArrayList<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>();
        if(listRequestUser.size() >0 ){
            for (AssignmentStudentRegisterDTO objectInfo : listRequestUser){
                objectInfo.getAssignmentStudentRegisterId();
                AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo newObject = new AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo();
                newObject.setAssignmentStudentRegisterId(objectInfo.getAssignmentStudentRegisterId());
                newObject.setAssignmentStudentRegisterName(objectInfo.getAssignmentStudentRegisterName());
                newObject.setStudentName(objectInfo.getStudentName());
                newObject.setInstructorName(objectInfo.getInstructorName());
                newObject.setFileName(objectInfo.getFileName());
                newObject.setFileType(objectInfo.getFileType());
                newObject.setIsApproved(objectInfo.getIsApproved());
                newObject.setStatus(objectInfo.getStatus());
                newObject.setCreateAt(objectInfo.getCreateAt());
                newObject.setPeriodAssignmentId(objectInfo.getPeriodAssignmentId());
                newObject.setPeriodAssignmentName(objectInfo.getPeriodAssignmentName());
                newObject.setExpirePeriodDate(objectInfo.getExpirePeriodDate());
                newObject.setStatusAutoMap(objectInfo.getStatusAutoMap());
                newObject.setCreateUser(objectInfo.getCreateUser());
                listAssignmentStudentRegisterDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listAssignmentStudentRegisterDto);
        objectDtoResponse.setTotalRecord(Math.toIntExact(totalRecord));
        return objectDtoResponse;
    }

}
