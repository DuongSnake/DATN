package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class StudentManagementDto {
    
    @Data
    public static class StudentSelectListRequest {
        private Long studentId;
        private String email;
        private String phone;
        private String fullName;
        private String status;
        private LocalDate fromDate;
        private LocalDate toDate;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class StudentListInfo {
        private Long studentId;
        private String email;
        private String phone;
        private String fullName;
        private String status;
        private LocalDate createAt;
    }

    @Data
    public static class StudentSelectInfo {
        private Long studentId;
    }

    @Data
    public static class StudentSelectInfoResponse {
        private Long studentId;
        private String email;
        private String phone;
        private String fullName;
        private Integer totalLessonDebt;
        private String statusDoneDebt;
        private String status;
        private LocalDate createAt;
        private LocalDate updateAt;
    }

    @Data
    public static class StudentInsertInfo {
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;
        
        @NotBlank(message = "Full name is required")
        private String fullName;
        
        private String phone;
    }

    @Data
    public static class StudentUpdateInfo {
        private Long studentId;
        
        @Email(message = "Email should be valid")
        private String email;
        
        private String phone;
        private String fullName;
    }

    @Data
    public static class StudentDeleteInfo {
        private List<Long> listData;
    }
}
