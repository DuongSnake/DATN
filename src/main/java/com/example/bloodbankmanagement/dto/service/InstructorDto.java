package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class InstructorDto {
    
    @Data
    public static class InstructorSelectListRequest {
        private Long instructorId;
        private String email;
        private String phone;
        private String fullName;
        private String status;
        private LocalDate fromDate;
        private LocalDate toDate;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class InstructorListInfo {
        private Long instructorId;
        private String email;
        private String phone;
        private String fullName;
        private String status;
        private LocalDate createAt;
    }

    @Data
    public static class InstructorSelectInfo {
        private Long instructorId;
    }

    @Data
    public static class InstructorSelectInfoResponse {
        private Long instructorId;
        private String email;
        private String phone;
        private String fullName;
        private String status;
        private LocalDate createAt;
        private LocalDate updateAt;
    }

    @Data
    public static class InstructorInsertInfo {
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;
        
        @NotBlank(message = "Full name is required")
        private String fullName;
        
        private String phone;
    }

    @Data
    public static class InstructorUpdateInfo {
        private Long instructorId;
        
        @Email(message = "Email should be valid")
        private String email;
        
        private String phone;
        private String fullName;
    }

    @Data
    public static class InstructorDeleteInfo {
        private List<Long> listData;
    }
}
