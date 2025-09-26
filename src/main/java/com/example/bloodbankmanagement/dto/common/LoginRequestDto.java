package com.example.bloodbankmanagement.dto.common;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @Data
    public static class LoginInfoRequest{
        private String lang;
        private LoginDataRequest data;
    }

    @Data
    public static class LoginDataRequest{
        @NotBlank
        private String userName;
        @NotBlank
        private String password;
    }
}
