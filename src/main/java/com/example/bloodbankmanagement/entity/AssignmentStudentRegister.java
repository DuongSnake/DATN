package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.CommonUtil;
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
    private String commentReasonResever;//Comment reason why student want "bảo lưu"
    private String commentRejectApproveRegister;//Comment reason why not approve for register assignment
    private String commentRejectApproveFinal;//Comment reason why not approve for student "bảo vệ"

    public static AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse convertToDto(AssignmentStudentRegisterDTO request){
        AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse objectDtoResponse = new AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setAssignmentStudentRegisterId(request.getAssignmentStudentRegisterId());
            objectDtoResponse.setAssignmentStudentRegisterName(request.getAssignmentStudentRegisterName());
            objectDtoResponse.setStudentId(request.getStudentId());
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
            objectDtoResponse.setOldValueId(request.getOldValueId());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> convertListObjectToDto(List<AssignmentStudentRegisterDTO> listRequestUser, Long totalRecord){
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> listAssignmentStudentRegisterDto = new ArrayList<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>();
        if(listRequestUser.size() >0 ){
            for (AssignmentStudentRegisterDTO objectInfo : listRequestUser){
                String isApprovedDisplayName = getDisplayNameStatusAutoMap(objectInfo.getStatusAutoMap());
                String statusAutoMapDisplayName = getDisplayNameStatusIsApprove(objectInfo.getIsApproved());
                objectInfo.getAssignmentStudentRegisterId();
                AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo newObject = new AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo();
                newObject.setAssignmentStudentRegisterId(objectInfo.getAssignmentStudentRegisterId());
                newObject.setAssignmentStudentRegisterName(objectInfo.getAssignmentStudentRegisterName());
                newObject.setStudentId(objectInfo.getStudentId());
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
                newObject.setOldValueId(objectInfo.getOldValueId());
                newObject.setIsApprovedDisplayName(isApprovedDisplayName);
                newObject.setStatusAutoMapDisplayName(statusAutoMapDisplayName);
                listAssignmentStudentRegisterDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listAssignmentStudentRegisterDto);
        objectDtoResponse.setTotalRecord(Math.toIntExact(totalRecord));
        return objectDtoResponse;
    }

    public static String getDisplayNameStatusAutoMap(String statusAutoMap){
        if(CommonUtil.YES_VALUE.equals(statusAutoMap)){
            return CommonUtil.AUTO_MAP_INSTRUCTOR_DISPLAY_TEXT;
        }else{
            return CommonUtil.MANUAL_MAP_INSTRUCTOR_DISPLAY_TEXT;
        }
    }

    public static String getDisplayNameStatusIsApprove(Integer statusAutoMap){
        switch (statusAutoMap){
            case 0:
                return CommonUtil.STATUS_NOT_ACCEPT_DISPLAY_TEXT;
            case 1:
                return CommonUtil.STATUS_SEND_REQUEST_DISPLAY_TEXT;
            case 3:
                return CommonUtil.STATUS_RESERVE_DISPLAY_TEXT;
            case 4:
                return CommonUtil.STATUS_REJECT_SEND_REGISTER_ASSIGNMENT_DISPLAY_TEXT;
            case 5:
                return CommonUtil.STATUS_WAITING_FINAL_DISPLAY_TEXT;
            case 6:
                return CommonUtil.STATUS_REJECT_APPROVE_FINAL_ASSIGNMENT_DISPLAY_TEXT;
            case 7:
                return CommonUtil.STATUS_APPROVE_FINAL_DISPLAY_TEXT;
            default:
                return CommonUtil.STATUS_NOT_ACCEPT_DISPLAY_TEXT;
        }
    }
    public static AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse convertToDtoCheckLog(AssignmentStudentRegisterDTO request){
        AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse objectDtoResponse = new AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setAssignmentStudentRegisterId(request.getAssignmentStudentRegisterId());
            objectDtoResponse.setAssignmentStudentRegisterName(request.getAssignmentStudentRegisterName());
            objectDtoResponse.setStudentId(request.getStudentId());
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
            objectDtoResponse.setOldValueId(request.getOldValueId());
        }
        return objectDtoResponse;
    }

}
