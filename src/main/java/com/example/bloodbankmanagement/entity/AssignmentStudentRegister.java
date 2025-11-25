package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
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
    private Integer isApproved;//status approve 0: not accept 1:accept 2:bao luu
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "content_assignment")
    private Blob contentAssignment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_map_instructor_id")
    private StudentMapInstructor studentMapInstructor;

    public static AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse convertToDto(AssignmentStudentRegister request){
        AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse objectDtoResponse = new AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse();
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
            objectDtoResponse.setAssignmentStudentRegisterId(request.getId());
            objectDtoResponse.setAssignmentStudentRegisterName(request.getAssignmentName());
            objectDtoResponse.setStudentName(studentName);
            objectDtoResponse.setStudentMapInstructorId(studentMapIntructorId);
            objectDtoResponse.setInstructorName(instructorName);
            objectDtoResponse.setFileName(request.getFileName());
            objectDtoResponse.setFileType(request.getFileType());
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
            objectDtoResponse.setCreateUser(request.getCreateUser());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> convertListObjectToDto(List<AssignmentStudentRegister> listRequestUser){
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> listAssignmentStudentRegisterDto = new ArrayList<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>();
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
                AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo newObject = new AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo();
                newObject.setAssignmentStudentRegisterId(listRequestUser.get(i).getId());
                newObject.setAssignmentStudentRegisterName(listRequestUser.get(i).getAssignmentName());
                newObject.setStudentName(studentName);
                newObject.setInstructorName(instructorName);
                newObject.setStudentMapInstructorId(studentMapIntructorId);
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                newObject.setCreateUser(listRequestUser.get(i).getCreateUser());
                listAssignmentStudentRegisterDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listAssignmentStudentRegisterDto);
        objectDtoResponse.setTotalRecord(listRequestUser.size());
        return objectDtoResponse;
    }

}
