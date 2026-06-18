package com.example.bloodbankmanagement.common.untils;

import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import com.example.bloodbankmanagement.dto.service.UserDto;
import com.example.bloodbankmanagement.dto.service.student.StudentDto;
import com.example.bloodbankmanagement.entity.Student;

import java.util.List;

public interface UserEntityPermissionBulkInsertStrategy {
    void bulkInsert(List<UserDto.UploadFileRegisterUserInfo> permissions);
    void bulkInsertUser(List<Student> permissions, Long roleIdd, String passwordDefaultEncrypt);
    void bulkInsertStudent(List<StudentDto.UploadFileRegisterStudentInfo> permissions);
    void bulkInsertScoreAssignment(List<ScoreAssignmentDto.UploadFileScoreAssignmentInfo> permissions);
}
