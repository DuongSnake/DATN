package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.UserInfoDto;
import com.example.bloodbankmanagement.dto.service.StudentMapCriticalDto;
import com.example.bloodbankmanagement.dto.service.UserDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "student_map_critical")//thong tin sinh vien duoc map vơi giao vien nao
public class StudentMapCritical extends EntityCommon {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User studentInfo;//student choose to map with teacher

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "critical_teacher_id")
    private User criticalTeacherInfo;//giao vien phan bien
    private String note;

    public static StudentMapCriticalDto.StudentMapCriticalSelectInfoResponse convertToDto(StudentMapCritical request){
        StudentMapCriticalDto.StudentMapCriticalSelectInfoResponse objectDtoResponse = new StudentMapCriticalDto.StudentMapCriticalSelectInfoResponse();
        if(request != null){
            Long instructorId = null != request.getCriticalTeacherInfo() ? request.getCriticalTeacherInfo().getId() : null;
            Long studentId = null != request.getStudentInfo() ? request.getStudentInfo().getId() : null;
            String instructorName = null != request.getCriticalTeacherInfo() ? request.getCriticalTeacherInfo().getFullName() : null;
            String studentName = null != request.getStudentInfo() ? request.getStudentInfo().getFullName() : null;
            String criticalName = null != request.getCriticalTeacherInfo() ? request.getCriticalTeacherInfo().getFullName() : null;
            Long criticalId = null != request.getCriticalTeacherInfo() ? request.getCriticalTeacherInfo().getId() : null;
            objectDtoResponse.setStudentMapCriticalId(request.getId());
            objectDtoResponse.setCriticalId(instructorId);
            objectDtoResponse.setCriticalName(instructorName);
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

    public static PageAmtListResponseDto<StudentMapCriticalDto.StudentMapCriticalListInfo> convertListObjectToDto(List<StudentMapCritical> listRequestUser, Long totalRecord){
        PageAmtListResponseDto<StudentMapCriticalDto.StudentMapCriticalListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<StudentMapCriticalDto.StudentMapCriticalListInfo> listStudentMapCriticalDto = new ArrayList<StudentMapCriticalDto.StudentMapCriticalListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                Long instructorId = null != listRequestUser.get(i).getCriticalTeacherInfo() ? listRequestUser.get(i).getCriticalTeacherInfo().getId() : null;
                Long studentId = null != listRequestUser.get(i).getStudentInfo() ? listRequestUser.get(i).getStudentInfo().getId() : null;
                String instructorName = null != listRequestUser.get(i).getCriticalTeacherInfo() ? listRequestUser.get(i).getCriticalTeacherInfo().getFullName() : null;
                String studentName = null != listRequestUser.get(i).getStudentInfo() ? listRequestUser.get(i).getStudentInfo().getFullName() : null;
                String criticalName = null != listRequestUser.get(i).getCriticalTeacherInfo() ? listRequestUser.get(i).getCriticalTeacherInfo().getFullName() : null;
                Long criticalId = null != listRequestUser.get(i).getCriticalTeacherInfo() ? listRequestUser.get(i).getCriticalTeacherInfo().getId() : null;
                StudentMapCriticalDto.StudentMapCriticalListInfo newObject = new StudentMapCriticalDto.StudentMapCriticalListInfo();
                newObject.setStudentMapCriticalId(listRequestUser.get(i).getId());
                newObject.setCriticalId(instructorId);
                newObject.setCriticalName(instructorName);
                newObject.setStudentId(studentId);
                newObject.setStudentName(studentName);
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                newObject.setCreateUser(listRequestUser.get(i).getCreateUser());
                newObject.setCriticalId(criticalId);
                newObject.setCriticalName(criticalName);
                listStudentMapCriticalDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listStudentMapCriticalDto);
        objectDtoResponse.setTotalRecord(Math.toIntExact(totalRecord));
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<UserDto.AllStudentByInstructorInfo> convertListStudentByInstructor(List<StudentMapCritical> request){
        PageAmtListResponseDto<UserDto.AllStudentByInstructorInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<UserDto.AllStudentByInstructorInfo> listUserDto = new ArrayList<UserDto.AllStudentByInstructorInfo>();
        //Get information
        if(request.size() >0 ){
            for (int i=0;i<request.size();i++){
                User studentInfo = request.get(i).getStudentInfo();
                if(!ObjectUtils.isEmpty(studentInfo)){
                    UserDto.AllStudentByInstructorInfo newObject = new UserDto.AllStudentByInstructorInfo();
                    newObject.setId(studentInfo.getId());
                    newObject.setFullName(studentInfo.getFullName());
                    listUserDto.add(newObject);
                }
            }
        }
        objectDtoResponse.setData(listUserDto);
        objectDtoResponse.setTotalRecord(request.size());
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<UserDto.AllStudentByInstructorInfo> convertListInstructorByStudent(List<StudentMapCritical> request){
        PageAmtListResponseDto<UserDto.AllStudentByInstructorInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<UserDto.AllStudentByInstructorInfo> listUserDto = new ArrayList<UserDto.AllStudentByInstructorInfo>();
        //Get information
        if(request.size() >0 ){
            for (int i=0;i<request.size();i++){
                User studentInfo = request.get(i).getCriticalTeacherInfo();
                if(!ObjectUtils.isEmpty(studentInfo)){
                    UserDto.AllStudentByInstructorInfo newObject = new UserDto.AllStudentByInstructorInfo();
                    newObject.setId(studentInfo.getId());
                    newObject.setFullName(studentInfo.getFullName());
                    listUserDto.add(newObject);
                }
            }
        }
        objectDtoResponse.setData(listUserDto);
        objectDtoResponse.setTotalRecord(request.size());
        return objectDtoResponse;
    }
}
