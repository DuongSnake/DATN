package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.MajorDto;
import com.example.bloodbankmanagement.service.MajorServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/major")
@RequiredArgsConstructor
public class MajorController {

    private final MajorServiceImpl majorService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertMajor(@RequestBody @Valid MajorDto.MajorInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                majorService.insertMajor(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateMajor(@RequestBody @Valid MajorDto.MajorUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                majorService.updateMajor(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<MajorDto.MajorListInfo>>> selectListMajor(@RequestBody MajorDto.MajorSelectListInfo request) {
        return new ResponseEntity<>(
                majorService.selectListMajor(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteMajor(@RequestBody @Valid MajorDto.MajorDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                majorService.deleteMajor(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<MajorDto.MajorSelectInfoResponse>> selectMajor(@RequestBody MajorDto.MajorSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                majorService.selectMajor(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListAllActive")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<MajorDto.MajorListInfo>>> selectListMajorAllActive() {
        return new ResponseEntity<>(
                majorService.selectListMajorAllActive(),
                HttpStatus.OK
        );
    }

}
