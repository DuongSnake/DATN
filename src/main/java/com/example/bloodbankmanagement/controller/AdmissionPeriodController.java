package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.AdmissionPeriodDto;
import com.example.bloodbankmanagement.service.AdmissionPeriodServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admissionPeriod")
@RequiredArgsConstructor
public class AdmissionPeriodController {

    private final AdmissionPeriodServiceImpl admissionPeriodService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertAdmissionPeriod(@RequestBody @Valid AdmissionPeriodDto.AdmissionPeriodInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                admissionPeriodService.insertAdmissionPeriod(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateAdmissionPeriod(@RequestBody @Valid AdmissionPeriodDto.AdmissionPeriodUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                admissionPeriodService.updateAdmissionPeriod(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AdmissionPeriodDto.AdmissionPeriodListInfo>>> selectListAdmissionPeriod(@RequestBody AdmissionPeriodDto.AdmissionPeriodSelectListInfo request) {
        return new ResponseEntity<>(
                admissionPeriodService.selectListAdmissionPeriod(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteAdmissionPeriod(@RequestBody @Valid AdmissionPeriodDto.AdmissionPeriodDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                admissionPeriodService.deleteAdmissionPeriod(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<AdmissionPeriodDto.AdmissionPeriodSelectInfoResponse>> selectAdmissionPeriod(@RequestBody AdmissionPeriodDto.AdmissionPeriodSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                admissionPeriodService.selectAdmissionPeriod(request, lang),
                HttpStatus.OK
        );
    }

}
