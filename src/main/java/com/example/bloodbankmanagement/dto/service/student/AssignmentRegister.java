package com.example.bloodbankmanagement.dto.service.student;

import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentRegisterDTO;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.entity.AssignmentStudentRegister;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AssignmentRegister {

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
            case 2:
                return CommonUtil.STATUS_PROCESS_DISPLAY_TEXT;
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

}
