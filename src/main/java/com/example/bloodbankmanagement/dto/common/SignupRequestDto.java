package com.example.bloodbankmanagement.dto.common;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

public class SignupRequestDto {

    @Data
    public static class SignupInfoRequest{
        private String userId;
        private String lang;
        private SignupDataRequest data;
    }

    @Data
    public static class SignupDataRequest{
        @NotBlank
        @Size(max = 50)
        private String username;
        private Set<String> role;
        @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
        private String email;
        private String phone;
        private String fullName;
    }
}
