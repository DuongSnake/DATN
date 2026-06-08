package com.example.bloodbankmanagement.controller.report_analist;

import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentAnalystDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.AssignmentAnalystDto;
import com.example.bloodbankmanagement.service.report_analyst.AssignmentAnalystServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reportAssignmentByMajor")
@RequiredArgsConstructor
public class ReportAssignmentAnalystController {
    private final AssignmentAnalystServiceImpl assignmentAnalystService;

    @PostMapping("/selectAllTotalRecrod")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentAnalystDto.AssignmentAnalystSelectListResponse>>> selectAllTotalRecrod(@RequestBody AssignmentAnalystDto.AssignmentAnalystSelectListRequest request) {
        return new ResponseEntity<>(
                assignmentAnalystService.selectAllTotalData(request),
                HttpStatus.OK
        );
    }
}
