package com.example.bloodbankmanagement.controller.teacher;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.StudentMapCriticalDto;
import com.example.bloodbankmanagement.dto.service.UserDto;
import com.example.bloodbankmanagement.service.teacher.StudentMapCriticalServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/studentMapCritical")
@RequiredArgsConstructor
public class StudentMapCriticalController {

    private final StudentMapCriticalServiceImpl studentMapCriticalService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertStudentMapCritical(@RequestBody @Valid StudentMapCriticalDto.StudentMapCriticalInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                studentMapCriticalService.insertStudentMapCritical(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateStudentMapCritical(@RequestBody @Valid StudentMapCriticalDto.StudentMapCriticalUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                studentMapCriticalService.updateStudentMapCritical(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<StudentMapCriticalDto.StudentMapCriticalListInfo>>> selectListStudentMapCritical(@RequestBody StudentMapCriticalDto.StudentMapCriticalSelectListInfo request) {
        return new ResponseEntity<>(
                studentMapCriticalService.selectListStudentMapCritical(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteStudentMapCritical(@RequestBody @Valid StudentMapCriticalDto.StudentMapCriticalDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                studentMapCriticalService.deleteStudentMapCritical(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<StudentMapCriticalDto.StudentMapCriticalSelectInfoResponse>> selectStudentMapCritical(@RequestBody StudentMapCriticalDto.StudentMapCriticalSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                studentMapCriticalService.selectStudentMapCritical(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListCriticalByStudentId")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<UserDto.AllStudentByInstructorInfo>>> selectListCriticalTeacherByStudentId(@RequestBody StudentMapCriticalDto.FindCriticalTeacherByStudentIdInfo request) {
        return new ResponseEntity<>(
                studentMapCriticalService.selectListCriticalTeacherByStudentId(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListUserToMapCritical")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<UserDto.UserSelectListInfo>>> selectListStudentHaveStatusAssignmentIsWaitingFinalApprove() {
        return new ResponseEntity<>(
                studentMapCriticalService.selectListStudentHaveStatusAssignmentIsWaitingFinalApprove(),
                HttpStatus.OK
        );
    }

}
