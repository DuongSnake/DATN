package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
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
    private Integer isApproved;//status approve 0: not accept 1:pending 2:accept 3:bao luu
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "content_assignment")
    private Blob contentAssignment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_map_instructor_id")
    private StudentMapInstructor studentMapInstructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_assignment_id")
    private PeriodAssignment periodAssignmentInfo;

    public static AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse convertToDto(AssignmentStudentRegister request){
        AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse objectDtoResponse = new AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse();
        if(request != null){
            Long studentMapIntructorId = null;
            Long periodAssignmentId = null;
            String periodName = "";
            LocalDate expireDate = null;
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
            if(null != request.getPeriodAssignmentInfo() && null != request.getPeriodAssignmentInfo().getId()){
                periodAssignmentId = request.getPeriodAssignmentInfo().getId();
            }
            if(null != request.getPeriodAssignmentInfo() && null != request.getPeriodAssignmentInfo().getAdmissionPeriodInfo().getAdmissionPeriodName()){
                periodName = request.getPeriodAssignmentInfo().getAdmissionPeriodInfo().getAdmissionPeriodName();
            }
            if(null != request.getPeriodAssignmentInfo() && null != request.getPeriodAssignmentInfo().getAdmissionPeriodInfo().getEndPeriod()){
                expireDate = request.getPeriodAssignmentInfo().getAdmissionPeriodInfo().getEndPeriod();
            }
            objectDtoResponse.setAssignmentStudentRegisterId(request.getId());
            objectDtoResponse.setAssignmentStudentRegisterName(request.getAssignmentName());
            objectDtoResponse.setStudentName(studentName);
            objectDtoResponse.setStudentMapInstructorId(studentMapIntructorId);
            objectDtoResponse.setInstructorName(instructorName);
            objectDtoResponse.setFileName(request.getFileName());
            objectDtoResponse.setFileType(request.getFileType());
            objectDtoResponse.setIsApproved(request.getIsApproved());
            objectDtoResponse.setPeriodAssignmentId(periodAssignmentId);
            objectDtoResponse.setPeriodAssignmentName(periodName);
            objectDtoResponse.setExpirePeriodDate(expireDate);
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
            objectDtoResponse.setCreateUser(request.getCreateUser());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> convertListObjectToDto(List<AssignmentStudentRegister> listRequestUser, Long totalRecord){
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> listAssignmentStudentRegisterDto = new ArrayList<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                Long studentMapIntructorId = null;
                String studentName = "";
                String instructorName = "";
                Long periodAssignmentId = null;
                String periodName = "";
                LocalDate expireDate = null;
                if(null != listRequestUser.get(i).getStudentMapInstructor() && null != listRequestUser.get(i).getStudentMapInstructor().getId()){
                    studentMapIntructorId = listRequestUser.get(i).getStudentMapInstructor().getId();
                }
                if(null != listRequestUser.get(i).getStudentMapInstructor() && null != listRequestUser.get(i).getStudentMapInstructor().getStudentInfo()){
                    studentName = listRequestUser.get(i).getStudentMapInstructor().getStudentInfo().getFullName();
                }
                if(null != listRequestUser.get(i).getStudentMapInstructor() && null != listRequestUser.get(i).getStudentMapInstructor().getInstructorInfo()){
                    instructorName = listRequestUser.get(i).getStudentMapInstructor().getInstructorInfo().getFullName();
                }
                if(null != listRequestUser.get(i).getPeriodAssignmentInfo() && null != listRequestUser.get(i).getPeriodAssignmentInfo().getId()){
                    periodAssignmentId = listRequestUser.get(i).getPeriodAssignmentInfo().getId();
                }
                if(null != listRequestUser.get(i).getPeriodAssignmentInfo() && null != listRequestUser.get(i).getPeriodAssignmentInfo().getAdmissionPeriodInfo().getAdmissionPeriodName()){
                    periodName = listRequestUser.get(i).getPeriodAssignmentInfo().getAdmissionPeriodInfo().getAdmissionPeriodName();
                }
                if(null != listRequestUser.get(i).getPeriodAssignmentInfo() && null != listRequestUser.get(i).getPeriodAssignmentInfo().getAdmissionPeriodInfo().getEndPeriod()){
                    expireDate = listRequestUser.get(i).getPeriodAssignmentInfo().getAdmissionPeriodInfo().getEndPeriod();
                }
                AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo newObject = new AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo();
                newObject.setAssignmentStudentRegisterId(listRequestUser.get(i).getId());
                newObject.setAssignmentStudentRegisterName(listRequestUser.get(i).getAssignmentName());
                newObject.setStudentName(studentName);
                newObject.setInstructorName(instructorName);
                newObject.setStudentMapInstructorId(studentMapIntructorId);
                newObject.setFileName(listRequestUser.get(i).getFileName());
                newObject.setFileType(listRequestUser.get(i).getFileType());
                newObject.setIsApproved(listRequestUser.get(i).getIsApproved());
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                newObject.setPeriodAssignmentId(periodAssignmentId);
                newObject.setPeriodAssignmentName(periodName);
                newObject.setExpirePeriodDate(expireDate);
                newObject.setCreateUser(listRequestUser.get(i).getCreateUser());
                listAssignmentStudentRegisterDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listAssignmentStudentRegisterDto);
        objectDtoResponse.setTotalRecord(Math.toIntExact(totalRecord));
        return objectDtoResponse;
    }

}
