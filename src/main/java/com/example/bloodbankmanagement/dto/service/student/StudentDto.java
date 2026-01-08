package com.example.bloodbankmanagement.dto.service.student;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class StudentDto {

    @Data
    public static class StudentSelectListRequest {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String identityCard;
        private String fullName;
        private Long majorId;
        private Long admissionPeriodId;
        private String status;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class StudentSelectListInfo {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String fullName;
        private String identityCard;
        private String address;
        private String status;
        private String note;
        private Long majorId;
        private String majorName;
        private Long admissionPeriodId;
        private String admissionPeriodName;
        private LocalDate createAt;
        private LocalDate updateAt;
        private Role roles;
    }

    @Data
    public static class StudentSelectInfoResponse {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String fullName;
        private String identityCard;
        private String address;
        private String status;
        private String note;
        private Long majorId;
        private String majorName;
        private Long admissionPeriodId;
        private String admissionPeriodName;
        private LocalDate createAt;
        private LocalDate updateAt;
        private Role roles;
    }

    @Data
    public static class StudentUpdateInfo {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String fullName;
        private String identityCard;
        private String address;
        private Long majorId;
        private Long admissionPeriodId;
        private String note;
        public static User convertToEntity(StudentDto.StudentUpdateInfo userUpdateInfo, String userId){
            User objectEntity = new User();
            objectEntity.setId(userUpdateInfo.getId());
            objectEntity.setUsername(userUpdateInfo.getUsername());
            objectEntity.setEmail(userUpdateInfo.getEmail());
            objectEntity.setPhone(userUpdateInfo.getPhone());
            objectEntity.setFullName(userUpdateInfo.getFullName());
            objectEntity.setIdentityCard(userUpdateInfo.getIdentityCard());
            objectEntity.setAddress(userUpdateInfo.getAddress());
            objectEntity.setNote(userUpdateInfo.getNote());
            objectEntity.setUpdateUser(userId);
            return objectEntity;
        }
    }

    @Data
    public static class StudentInsertInfo {
        private String username;
        private String email;
        private String phone;
        private String fullName;
        private String identityCard;
        private String address;
        private String note;
        private Long majorId;
        private Long admissionPeriodId;

        public static User convertToEntity(StudentDto.StudentInsertInfo userInsertInfo) {
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
    public static class StudentSelectInfo {
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
    public static class StudentDeleteInfo {
        private List<Long> listData;
    }

    @Data
    public static class UploadBatchFileRegisterStudentInfo {
        private MultipartFile fileUploadContent;
    }

    @Data
    public static class UploadFileRegisterStudentInfo {
        private String numberIndex;
        private String email;
        private String phone;
        private String fullName;
        private String identityCard;
        private String address;
        private String note;
        private Long majorId;
        private Long admissionPeriodId;
        private List<String> errors;
    }

    @Data
    @NoArgsConstructor
    public static class StudentForExportExcelResponse {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String fullName;
        private String identityCard;
        private String address;
        private String status;
        private String note;
        private Long majorId;
        private String majorName;
        private Long admissionPeriodId;
        private String admissionPeriodName;
        private LocalDate createAt;
        private LocalDate updateAt;
        private Set<Role> roles;
        ///Add for line code convert to entity to object export excel at line List<StudentExcel> dtoObject = studentExportMapper.toEntity(listAllUser);
        @Builder
        public StudentForExportExcelResponse(String username, String email, String phone, String fullName, String identityCard, String address, String note, String status, String majorName, String admissionPeriodName) {
            this.username = username;
            this.email = email;
            this.phone = phone;
            this.fullName = fullName;
            this.identityCard = identityCard;
            this.address = address;
            this.note = note;
            this.status = status;
            this.majorName = majorName;
            this.admissionPeriodName = admissionPeriodName;
        }
    }
}
