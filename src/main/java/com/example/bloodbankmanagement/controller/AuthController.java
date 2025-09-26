package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.JwtResponseDto;
import com.example.bloodbankmanagement.dto.common.LoginRequestDto;
import com.example.bloodbankmanagement.dto.common.SignupRequestDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.service.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthServiceImpl authService;

    @PostMapping("/signin")
    public ResponseEntity<SingleResponseDto<JwtResponseDto>> authenticationUser(@Valid @RequestBody LoginRequestDto.LoginDataRequest loginInfoRequest){
        return new ResponseEntity<>(
                authService.authenticationUser(loginInfoRequest),
                HttpStatus.OK
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<SingleResponseDto<SignupRequestDto.SignupDataRequest>> registerUser(@Valid @RequestBody SignupRequestDto.SignupDataRequest signupRequestDto, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                authService.registerUser(signupRequestDto, lang),
                HttpStatus.OK
        );
    }
}
