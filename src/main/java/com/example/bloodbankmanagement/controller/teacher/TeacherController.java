package com.example.bloodbankmanagement.controller.teacher;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.student.StudentDto;
import com.example.bloodbankmanagement.service.teacher.TeacherServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherServiceImpl userService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertTeacher(@RequestBody @Valid StudentDto.StudentInsertInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                userService.insertListTeacher(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateUser(@RequestBody @Valid StudentDto.StudentUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                userService.updateTeacher(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteUser(@RequestBody @Valid StudentDto.StudentDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                userService.deleteTeacher(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/select")
    public ResponseEntity<SingleResponseDto<StudentDto.StudentSelectInfoResponse>> selectUser(@RequestBody StudentDto.StudentSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                userService.selectTeacher(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<StudentDto.StudentSelectListInfo>>> selectListUser(@RequestBody StudentDto.StudentSelectListRequest request) {
        return new ResponseEntity<>(
                userService.selectListTeacher(request),
                HttpStatus.OK
        );
    }
}
