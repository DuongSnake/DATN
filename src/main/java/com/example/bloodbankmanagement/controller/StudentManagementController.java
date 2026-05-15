package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.StudentManagementDto;
import com.example.bloodbankmanagement.service.StudentManagementServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentManagementController {

    private final StudentManagementServiceImpl studentManagementService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertStudent(@RequestBody @Valid StudentManagementDto.StudentInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                studentManagementService.insertStudent(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateStudent(@RequestBody @Valid StudentManagementDto.StudentUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                studentManagementService.updateStudent(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<StudentManagementDto.StudentListInfo>>> selectListStudent(@RequestBody StudentManagementDto.StudentSelectListRequest request) {
        return new ResponseEntity<>(
                studentManagementService.selectListStudent(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteStudent(@RequestBody @Valid StudentManagementDto.StudentDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                studentManagementService.deleteStudent(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<StudentManagementDto.StudentSelectInfoResponse>> selectStudent(@RequestBody StudentManagementDto.StudentSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                studentManagementService.selectStudent(request, lang),
                HttpStatus.OK
        );
    }
}
