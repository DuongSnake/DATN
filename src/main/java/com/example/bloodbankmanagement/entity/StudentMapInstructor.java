package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.StudentMapInstructorDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "student_map_instructor")//thong tin sinh vien duoc map vơi giao vien nao
public class StudentMapInstructor extends EntityCommon {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructorInfo;//teacher coaching for student

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User studentInfo;//student choose to map with teacher

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "critical_teacher_id")
    private User criticalTeacherInfo;//giao vien phan bien

    public static StudentMapInstructorDto.StudentMapInstructorSelectInfoResponse convertToDto(StudentMapInstructor request){
        StudentMapInstructorDto.StudentMapInstructorSelectInfoResponse objectDtoResponse = new StudentMapInstructorDto.StudentMapInstructorSelectInfoResponse();
        if(request != null){
            Long instructorId = null != request.getInstructorInfo() ? request.getInstructorInfo().getId() : null;
            Long studentId = null != request.getStudentInfo() ? request.getStudentInfo().getId() : null;
            String instructorName = null != request.getInstructorInfo() ? request.getInstructorInfo().getFullName() : null;
            String studentName = null != request.getStudentInfo() ? request.getStudentInfo().getFullName() : null;
            String criticalName = null != request.getCriticalTeacherInfo() ? request.getCriticalTeacherInfo().getFullName() : null;
            Long criticalId = null != request.getCriticalTeacherInfo() ? request.getCriticalTeacherInfo().getId() : null;
            objectDtoResponse.setStudentMapInstructorId(request.getId());
            objectDtoResponse.setInstructorId(instructorId);
            objectDtoResponse.setInstructorName(instructorName);
            objectDtoResponse.setStudentId(studentId);
            objectDtoResponse.setStudentName(studentName);
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
            objectDtoResponse.setCriticalId(criticalId);
            objectDtoResponse.setCriticalName(criticalName);
            objectDtoResponse.setCreateUser(request.getCreateUser());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<StudentMapInstructorDto.StudentMapInstructorListInfo> convertListObjectToDto(List<StudentMapInstructor> listRequestUser){
        PageAmtListResponseDto<StudentMapInstructorDto.StudentMapInstructorListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<StudentMapInstructorDto.StudentMapInstructorListInfo> listStudentMapInstructorDto = new ArrayList<StudentMapInstructorDto.StudentMapInstructorListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                Long instructorId = null != listRequestUser.get(i).getInstructorInfo() ? listRequestUser.get(i).getInstructorInfo().getId() : null;
                Long studentId = null != listRequestUser.get(i).getStudentInfo() ? listRequestUser.get(i).getStudentInfo().getId() : null;
                String instructorName = null != listRequestUser.get(i).getInstructorInfo() ? listRequestUser.get(i).getInstructorInfo().getFullName() : null;
                String studentName = null != listRequestUser.get(i).getStudentInfo() ? listRequestUser.get(i).getStudentInfo().getFullName() : null;
                String criticalName = null != listRequestUser.get(i).getCriticalTeacherInfo() ? listRequestUser.get(i).getCriticalTeacherInfo().getFullName() : null;
                Long criticalId = null != listRequestUser.get(i).getCriticalTeacherInfo() ? listRequestUser.get(i).getCriticalTeacherInfo().getId() : null;
                StudentMapInstructorDto.StudentMapInstructorListInfo newObject = new StudentMapInstructorDto.StudentMapInstructorListInfo();
                newObject.setStudentMapInstructorId(listRequestUser.get(i).getId());
                newObject.setInstructorId(instructorId);
                newObject.setInstructorName(instructorName);
                newObject.setStudentId(studentId);
                newObject.setStudentName(studentName);
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                newObject.setCreateUser(listRequestUser.get(i).getCreateUser());
                newObject.setCriticalId(criticalId);
                newObject.setCriticalName(criticalName);
                listStudentMapInstructorDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listStudentMapInstructorDto);
        objectDtoResponse.setTotalRecord(listRequestUser.size());
        return objectDtoResponse;
    }
}
