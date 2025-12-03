package com.example.bloodbankmanagement.dto.service.student;

import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.entity.AssignmentStudentRegister;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AssignmentRegister {

    public static AssignmentRegisterDto.AssignmentRegisterSelectInfoResponse convertToDto(AssignmentStudentRegister request){
        AssignmentRegisterDto.AssignmentRegisterSelectInfoResponse objectDtoResponse = new AssignmentRegisterDto.AssignmentRegisterSelectInfoResponse();
        if(request != null){
            Long studentMapIntructorId = null;
            String studentName = "";
            String instructorName = "";
            if(null != request.getStudentMapInstructor() && null != request.getStudentMapInstructor().getId()){
                studentMapIntructorId = request.getStudentMapInstructor().getId();
            }
            if(null != request.getStudentMapInstructor() && null != request.getStudentMapInstructor().getStudentInfo()){
                studentName = request.getStudentMapInstructor().getStudentInfo().getFullName();
            }
            if(null != request.getStudentMapInstructor() && null != request.getStudentMapInstructor().getInstructorInfo()){
                instructorName = request.getStudentMapInstructor().getInstructorInfo().getFullName();
            }
            objectDtoResponse.setAssignmentRegisterId(request.getId());
            objectDtoResponse.setAssignmentRegisterName(request.getAssignmentName());
            objectDtoResponse.setStudentName(studentName);
            objectDtoResponse.setStudentMapInstructorId(studentMapIntructorId);
            objectDtoResponse.setInstructorName(instructorName);
            objectDtoResponse.setFileName(request.getFileName());
            objectDtoResponse.setFileType(request.getFileType());
            objectDtoResponse.setIsApproved(request.getIsApproved());
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
            objectDtoResponse.setCreateUser(request.getCreateUser());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<AssignmentRegisterDto.AssignmentRegisterListInfo> convertListObjectToDto(List<AssignmentStudentRegister> listRequestUser, Long totalRecord){
        PageAmtListResponseDto<AssignmentRegisterDto.AssignmentRegisterListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<AssignmentRegisterDto.AssignmentRegisterListInfo> listAssignmentRegisterDto = new ArrayList<AssignmentRegisterDto.AssignmentRegisterListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                Long studentMapIntructorId = null;
                String studentName = "";
                String instructorName = "";
                if(null != listRequestUser.get(i).getStudentMapInstructor() && null != listRequestUser.get(i).getStudentMapInstructor().getId()){
                    studentMapIntructorId = listRequestUser.get(i).getStudentMapInstructor().getId();
                }
                if(null != listRequestUser.get(i).getStudentMapInstructor() && null != listRequestUser.get(i).getStudentMapInstructor().getStudentInfo()){
                    studentName = listRequestUser.get(i).getStudentMapInstructor().getStudentInfo().getFullName();
                }
                if(null != listRequestUser.get(i).getStudentMapInstructor() && null != listRequestUser.get(i).getStudentMapInstructor().getInstructorInfo()){
                    instructorName = listRequestUser.get(i).getStudentMapInstructor().getInstructorInfo().getFullName();
                }
                AssignmentRegisterDto.AssignmentRegisterListInfo newObject = new AssignmentRegisterDto.AssignmentRegisterListInfo();
                newObject.setAssignmentRegisterId(listRequestUser.get(i).getId());
                newObject.setAssignmentRegisterName(listRequestUser.get(i).getAssignmentName());
                newObject.setStudentName(studentName);
                newObject.setInstructorName(instructorName);
                newObject.setStudentMapInstructorId(studentMapIntructorId);
                newObject.setFileName(listRequestUser.get(i).getFileName());
                newObject.setFileType(listRequestUser.get(i).getFileType());
                newObject.setIsApproved(listRequestUser.get(i).getIsApproved());
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                newObject.setCreateUser(listRequestUser.get(i).getCreateUser());
                listAssignmentRegisterDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listAssignmentRegisterDto);
        objectDtoResponse.setTotalRecord(Math.toIntExact(totalRecord));
        return objectDtoResponse;
    }

}
