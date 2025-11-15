package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.service.AssignmentStudentRegisterServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assignmentStudentRegister")
@RequiredArgsConstructor
public class AssignmentStudentRegisterController {

    private final AssignmentStudentRegisterServiceImpl majorService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertAssignmentStudentRegister(@ModelAttribute @Valid AssignmentStudentRegisterDto.AssignmentStudentRegisterInsertInfo request, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                majorService.insertAssignmentStudentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateAssignmentStudentRegister(@ModelAttribute @Valid AssignmentStudentRegisterDto.AssignmentStudentRegisterUpdateInfo request, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                majorService.updateAssignmentStudentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>>> selectListAssignmentStudentRegister(@RequestBody AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectListInfo request) {
        return new ResponseEntity<>(
                majorService.selectListAssignmentStudentRegister(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteAssignmentStudentRegister(@RequestBody @Valid AssignmentStudentRegisterDto.AssignmentStudentRegisterDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                majorService.deleteAssignmentStudentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/reserveListAssignment")
    public ResponseEntity<BasicResponseDto> reserveAssignmentStudentRegister(@RequestBody @Valid AssignmentStudentRegisterDto.AssignmentStudentRegisterDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                majorService.reserveAssignmentStudentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse>> selectAssignmentStudentRegister(@RequestBody AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                majorService.selectAssignmentStudentRegister(request, lang),
                HttpStatus.OK
        );
    }

}
