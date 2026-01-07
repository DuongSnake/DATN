package com.example.bloodbankmanagement.common.untils;

import com.example.bloodbankmanagement.dto.service.UserDto;
import com.example.bloodbankmanagement.dto.service.student.StudentDto;

import java.util.List;

public interface UserEntityPermissionBulkInsertStrategy {
    void bulkInsert(List<UserDto.UploadFileRegisterUserInfo> permissions);
    void bulkInsertStudent(List<StudentDto.UploadFileRegisterStudentInfo> permissions);
}
