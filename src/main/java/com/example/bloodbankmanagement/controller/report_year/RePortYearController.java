package com.example.bloodbankmanagement.controller.report_year;

import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.AdmissionPeriodDto;
import com.example.bloodbankmanagement.service.report_year.ReportYearServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reportYear")
@RequiredArgsConstructor
public class RePortYearController {

    private final ReportYearServiceImpl reportYearService;
    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AdmissionPeriodDto.AdmissionPeriodListInfo>>> selectListAdmissionPeriod(@RequestBody AdmissionPeriodDto.AdmissionPeriodSelectListInfo request) {
        return new ResponseEntity<>(
                reportYearService.selectListAdmissionPeriod(request),
                HttpStatus.OK
        );
    }
}
