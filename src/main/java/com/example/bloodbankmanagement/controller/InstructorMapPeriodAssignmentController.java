package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.InstructorMapPeriodAssignmentDto;
import com.example.bloodbankmanagement.service.InstructorMapPeriodAssignmentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/InstructorMapPeriodAssignment")
@RequiredArgsConstructor
public class InstructorMapPeriodAssignmentController {

    private final InstructorMapPeriodAssignmentServiceImpl studentMapInstructorService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertInstructorMapPeriodAssignment(@RequestBody @Valid InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                studentMapInstructorService.insertInstructorMapPeriodAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateInstructorMapPeriodAssignment(@RequestBody @Valid InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                studentMapInstructorService.updateInstructorMapPeriodAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentListInfo>>> selectListInstructorMapPeriodAssignment(@RequestBody InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectListInfo request) {
        return new ResponseEntity<>(
                studentMapInstructorService.selectListInstructorMapPeriodAssignment(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteInstructorMapPeriodAssignment(@RequestBody @Valid InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                studentMapInstructorService.deleteInstructorMapPeriodAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectInfoResponse>> selectInstructorMapPeriodAssignment(@RequestBody InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                studentMapInstructorService.selectInstructorMapPeriodAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/insertListInstructorMapPeriodAssignment")
    public ResponseEntity<BasicResponseDto> insertListInstructorToOneInstructor(@RequestBody @Valid InstructorMapPeriodAssignmentDto.InsertListPeriodAssignmentWithOneInstructorInfo request) {
        return new ResponseEntity<>(
                studentMapInstructorService.insertListInstructorToOneInstructor(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/updateListInstructorMapNewInstructor")
    public ResponseEntity<BasicResponseDto> updateListInstructorToNewInstructor(@RequestBody @Valid InstructorMapPeriodAssignmentDto.UpdateListPeriodAssignmentWithOneInstructorInfo request) {
        return new ResponseEntity<>(
                studentMapInstructorService.updateListInstructorToNewInstructor(request),
                HttpStatus.OK
        );
    }
}
