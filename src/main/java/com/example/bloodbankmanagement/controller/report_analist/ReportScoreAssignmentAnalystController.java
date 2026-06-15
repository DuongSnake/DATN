package com.example.bloodbankmanagement.controller.report_analist;

import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.ScoreAssignmentAnalystDto;
import com.example.bloodbankmanagement.service.report_analyst.ScoreAssignmentAnalystServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reportScoreAssignmentByMajor")
@RequiredArgsConstructor
public class ReportScoreAssignmentAnalystController {
    private final ScoreAssignmentAnalystServiceImpl scoreAssignmentAnalystService;

    @PostMapping("/selectAllTotalRecrod")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<ScoreAssignmentAnalystDto.ScoreAssignmentAnalystSelectListResponse>>> selectAllTotalRecrod(@RequestBody ScoreAssignmentAnalystDto.ScoreAssignmentAnalystSelectListRequest request) {
        return new ResponseEntity<>(
                scoreAssignmentAnalystService.selectAllTotalData(request),
                HttpStatus.OK
        );
    }
}
