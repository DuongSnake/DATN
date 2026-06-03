package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.StudentManagementDto;
import com.example.bloodbankmanagement.service.StudentManagementServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @PostMapping("/insertListStudent")
    public ResponseEntity<BasicResponseDto> insertListStudent(@ModelAttribute @Valid StudentManagementDto.UploadBatchFileRegisterUserInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                studentManagementService.uploadFileRegisterListStudent(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/downloadTemplate")
    public ResponseEntity<Resource> downloadTemplate() throws IOException {
        Resource resource = new ClassPathResource("static/example_insert_list_student.xlsx");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=example_insert_list_student.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
