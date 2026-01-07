package com.example.bloodbankmanagement.controller.student;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.student.StudentDto;
import com.example.bloodbankmanagement.service.student.StudentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl userService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertStudent(@RequestBody @Valid StudentDto.StudentInsertInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                userService.insertListStudent(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateUser(@RequestBody @Valid StudentDto.StudentUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                userService.updateStudent(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteUser(@RequestBody @Valid StudentDto.StudentDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                userService.deleteStudent(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/select")
    public ResponseEntity<SingleResponseDto<StudentDto.StudentSelectInfoResponse>> selectUser(@RequestBody StudentDto.StudentSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                userService.selectStudent(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<StudentDto.StudentSelectListInfo>>> selectListUser(@RequestBody StudentDto.StudentSelectListRequest request) {
        return new ResponseEntity<>(
                userService.selectListStudent(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<BasicResponseDto> uploadFileRegisterListUser(@ModelAttribute @Valid StudentDto.UploadBatchFileRegisterStudentInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                userService.uploadFileRegisterListStudent(request, lang),
                HttpStatus.OK
        );
    }
}
