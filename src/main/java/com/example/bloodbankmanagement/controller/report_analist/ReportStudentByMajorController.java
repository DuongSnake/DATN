package com.example.bloodbankmanagement.controller.report_analist;

import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.CountTotalRecordDto;
import com.example.bloodbankmanagement.dto.objectRepository.StudentMappingDto;
import com.example.bloodbankmanagement.service.report_student_major.ReportStudentMajorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reportStudentByMajor")
@RequiredArgsConstructor
public class ReportStudentByMajorController {

    private final ReportStudentMajorServiceImpl reportStudentByMajorService;

    @PostMapping("/selectAllTotalRecrod")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<StudentMappingDto>>> selectAllTotalRecrod() {
        return new ResponseEntity<>(
                reportStudentByMajorService.selectAllTotalData(),
                HttpStatus.OK
        );
    }
}
