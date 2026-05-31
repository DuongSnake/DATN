package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class UserDto {

    @Data
    public static class UserSelectListRequest {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String identityCard;
        private String fullName;
        private String status;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class UserSelectListInfo {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String fullName;
        private String status;
        private Long majorId;
        private String majorName;
        private LocalDate createAt;
        private LocalDate updateAt;
        private Role roles;
    }

    @Data
    public static class UserSelectInfoResponse {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String fullName;
        private LocalDate createAt;
        private LocalDate updateAt;
        private Role roles;
    }

    @Data
    public static class UserUpdateInfo {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String fullName;
        public static User convertToEntity(UserUpdateInfo userUpdateInfo, String userId){
            User objectEntity = new User();
            objectEntity.setId(userUpdateInfo.getId());
            objectEntity.setUsername(userUpdateInfo.getUsername());
            objectEntity.setEmail(userUpdateInfo.getEmail());
            objectEntity.setPhone(userUpdateInfo.getPhone());
            objectEntity.setFullName(userUpdateInfo.getFullName());
            objectEntity.setUpdateUser(userId);
            return objectEntity;
        }
    }

    @Data
    public static class UserInsertInfo {
        private String username;
        private String email;
        private String phone;
        private String fullName;
        private String roles;

        public static User convertToEntity(UserInsertInfo userInsertInfo) {
            User objectEntity = new User();
            objectEntity.setUsername(userInsertInfo.getUsername());
            objectEntity.setEmail(userInsertInfo.getEmail());
            objectEntity.setPhone(userInsertInfo.getPhone());
            objectEntity.setFullName(userInsertInfo.getFullName());
            return objectEntity;
        }
    }

    @Data
    public static class UserSelectInfo {
        private long id;
    }

    @Data
    public static class ChangePasswordInfo {
        private long id;
        private String currentPassword;
        private String newPassword;
    }

    @Data
    public static class ChangePasswordNoAuthInfo {
        private String userName;
        private String currentPassword;
        private String newPassword;
    }

    @Data
    public static class MailForgotPasswordInfo {
        private String email;
    }

    @Data
    public static class ForgotPasswordInfo {
        private long id;
        private String newPassword;
    }

    @Data
    public static class StudentInsertInfo {
        private String username;
        private Long periodId;
        private String email;
        private String fullName;
        private String identityCard;
        private String note;
        private Long majorId;
        private Set<String> roles;

        public static User convertToEntity(StudentInsertInfo userInsertInfo) {
            User objectEntity = new User();
            objectEntity.setUsername(userInsertInfo.getUsername());
            objectEntity.setEmail(userInsertInfo.getEmail());
            objectEntity.setFullName(userInsertInfo.getFullName());
            return objectEntity;
        }
    }

    @Data
    public static class AllRoleInfo {
        private long id;
        private String name;
    }


    @Data
    public static class AllStudentByInstructorInfo {
        private long id;
        private String fullName;
    }


    @Data
    public static class UserDeleteInfo {
        private List<Long> listData;
    }

    @Data
    public static class UploadBatchFileRegisterUserInfo {
        private MultipartFile fileUploadContent;
    }

    @Data
    public static class UploadFileRegisterUserInfo {
        private String numberIndex;
        private String email;
        private String phone;
        private String fullName;
        private Long admissionPeriodId;
        private Long roleId;
        private List<String> errors;
    }
}
