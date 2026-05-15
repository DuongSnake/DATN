package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.StudentManagementDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student extends EntityCommon {
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String email;
    private String phone;
    private String fullName;
    private Integer totalLessonDebt;//Tong so tin chi bi no
    private String statusDoneDebt;// Trang thai tra no mon thanh cong

    public static StudentManagementDto.StudentSelectInfoResponse convertToDto(Student request){
        StudentManagementDto.StudentSelectInfoResponse objectDtoResponse = new StudentManagementDto.StudentSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setStudentId(request.getId());
            objectDtoResponse.setEmail(request.getEmail());
            objectDtoResponse.setPhone(request.getPhone());
            objectDtoResponse.setFullName(request.getFullName());
            objectDtoResponse.setTotalLessonDebt(request.getTotalLessonDebt());
            objectDtoResponse.setStatusDoneDebt(request.getStatusDoneDebt());
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
            objectDtoResponse.setUpdateAt(request.getUpdateAt());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<StudentManagementDto.StudentListInfo> convertListObjectToDto(List<Student> listRequestStudent, Long totalRecord){
        PageAmtListResponseDto<StudentManagementDto.StudentListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<StudentManagementDto.StudentListInfo> listStudentDto = new ArrayList<StudentManagementDto.StudentListInfo>();
        if(listRequestStudent.size() > 0){
            for (int i=0; i<listRequestStudent.size(); i++){
                StudentManagementDto.StudentListInfo newObject = new StudentManagementDto.StudentListInfo();
                newObject.setStudentId(listRequestStudent.get(i).getId());
                newObject.setEmail(listRequestStudent.get(i).getEmail());
                newObject.setPhone(listRequestStudent.get(i).getPhone());
                newObject.setFullName(listRequestStudent.get(i).getFullName());
                newObject.setStatus(listRequestStudent.get(i).getStatus());
                newObject.setCreateAt(listRequestStudent.get(i).getCreateAt());
                listStudentDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listStudentDto);
        objectDtoResponse.setTotalRecord(Math.toIntExact(totalRecord));
        return objectDtoResponse;
    }
}