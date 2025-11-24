package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.UserDto;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertUser(@RequestBody @Valid UserDto.UserInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                userService.insertUser(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateUser(@RequestBody @Valid UserDto.UserUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                userService.updateUser(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<UserDto.UserSelectListInfo>>> selectListUser(@RequestBody UserDto.UserSelectListRequest request) {
        return new ResponseEntity<>(
                userService.selectListUser(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteUser(@RequestBody @Valid UserDto.UserDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                userService.deleteUser(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/select")
    public ResponseEntity<SingleResponseDto<UserDto.UserSelectInfoResponse>> selectUser(@RequestBody UserDto.UserSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                userService.selectUser(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/changePass")
    public ResponseEntity<SingleResponseDto<UserDto.ChangePasswordInfo>> changePasswordUser(@RequestBody UserDto.ChangePasswordInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                userService.changePasswordUser(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/sendMailReset")
    public ResponseEntity<SingleResponseDto<UserDto.MailForgotPasswordInfo>> sendMailResetPassword(@RequestBody UserDto.MailForgotPasswordInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                userService.sendMailResetPassword(request,lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/resetPass")
    public ResponseEntity<SingleResponseDto<UserDto.ForgotPasswordInfo>> resetPasswordUser(@RequestBody UserDto.ForgotPasswordInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                userService.resetPasswordUser(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectAllStudent")
    public ResponseEntity<SingleResponseDto<List<UserDto.UserSelectListInfo>>> selectListStudent() {
        return new ResponseEntity<>(
                userService.selectListStudent(),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectAllInstructor")
    public ResponseEntity<SingleResponseDto<List<UserDto.UserSelectListInfo>>> selectListInstructor() {
        return new ResponseEntity<>(
                userService.selectListInstructor(),
                HttpStatus.OK
        );
    }

    @PostMapping("/insertStudent")
    public ResponseEntity<BasicResponseDto> insertStudent(@RequestBody @Valid UserDto.StudentInsertInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                userService.insertListStudent(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectAllRole")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<Role>>> selectAllRole() {
        return new ResponseEntity<>(
                userService.selectListAllRole(),
                HttpStatus.OK
        );
    }
}
