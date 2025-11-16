package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.PeriodAssignmentDto;
import com.example.bloodbankmanagement.service.PeriodAssignmentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/periodAssignment")
@RequiredArgsConstructor
public class PeriodAssignmentController {

    private final PeriodAssignmentServiceImpl periodAssignmentService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertPeriodAssignment(@RequestBody @Valid PeriodAssignmentDto.PeriodAssignmentInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                periodAssignmentService.insertPeriodAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updatePeriodAssignment(@RequestBody @Valid PeriodAssignmentDto.PeriodAssignmentUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                periodAssignmentService.updatePeriodAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<PeriodAssignmentDto.PeriodAssignmentListInfo>>> selectListPeriodAssignment(@RequestBody PeriodAssignmentDto.PeriodAssignmentSelectListInfo request) {
        return new ResponseEntity<>(
                periodAssignmentService.selectListPeriodAssignment(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deletePeriodAssignment(@RequestBody @Valid PeriodAssignmentDto.PeriodAssignmentDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                periodAssignmentService.deletePeriodAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<PeriodAssignmentDto.PeriodAssignmentSelectInfoResponse>> selectPeriodAssignment(@RequestBody PeriodAssignmentDto.PeriodAssignmentSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                periodAssignmentService.selectPeriodAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/insertByMajorId")
    public ResponseEntity<BasicResponseDto> deletePeriodAssignment(@RequestBody @Valid PeriodAssignmentDto.InsertListPeriodAssignmentByMajorInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                periodAssignmentService.insertListPeriodAssignmentByMajorId(request, lang),
                HttpStatus.OK
        );
    }

}
