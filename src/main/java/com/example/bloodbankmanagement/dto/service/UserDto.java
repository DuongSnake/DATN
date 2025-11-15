package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import lombok.*;

import java.time.LocalDate;
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
        private String identityCard;
        private String address;
        private String status;
        private String note;
        private LocalDate createAt;
        private LocalDate updateAt;
        private Set<Role> roles;
    }

    @Data
    public static class UserSelectInfoResponse {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String fullName;
        private String identityCard;
        private String address;
        private String status;
        private String note;
        private LocalDate createAt;
        private LocalDate updateAt;
        private Set<Role> roles;
    }

    @Data
    public static class UserUpdateInfo {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String fullName;
        private String identityCard;
        private String address;
        private String status;
        private String note;
        public static User convertToEntity(UserUpdateInfo userUpdateInfo, String userId){
            User objectEntity = new User();
            objectEntity.setId(userUpdateInfo.getId());
            objectEntity.setUsername(userUpdateInfo.getUsername());
            objectEntity.setEmail(userUpdateInfo.getEmail());
            objectEntity.setPhone(userUpdateInfo.getPhone());
            objectEntity.setFullName(userUpdateInfo.getFullName());
            objectEntity.setIdentityCard(userUpdateInfo.getIdentityCard());
            objectEntity.setAddress(userUpdateInfo.getAddress());
            objectEntity.setStatus(userUpdateInfo.getStatus());
            objectEntity.setNote(userUpdateInfo.getNote());
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
        private String identityCard;
        private String address;
        private String note;
        private Set<String> roles;

        public static User convertToEntity(UserInsertInfo userInsertInfo) {
            User objectEntity = new User();
            objectEntity.setUsername(userInsertInfo.getUsername());
            objectEntity.setEmail(userInsertInfo.getEmail());
            objectEntity.setPhone(userInsertInfo.getPhone());
            objectEntity.setFullName(userInsertInfo.getFullName());
            objectEntity.setIdentityCard(userInsertInfo.getIdentityCard());
            objectEntity.setAddress(userInsertInfo.getAddress());
            objectEntity.setNote(userInsertInfo.getNote());
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
        private Set<String> roles;

        public static User convertToEntity(StudentInsertInfo userInsertInfo) {
            User objectEntity = new User();
            objectEntity.setUsername(userInsertInfo.getUsername());
            objectEntity.setEmail(userInsertInfo.getEmail());
            objectEntity.setFullName(userInsertInfo.getFullName());
            objectEntity.setIdentityCard(userInsertInfo.getIdentityCard());
            objectEntity.setNote(userInsertInfo.getNote());
            return objectEntity;
        }
    }
}
