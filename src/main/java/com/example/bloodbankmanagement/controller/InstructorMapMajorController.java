package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.InstructorMapMajorDto;
import com.example.bloodbankmanagement.service.InstructorMapMajorServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/instructorMapMajor")
@RequiredArgsConstructor
public class InstructorMapMajorController {

    private final InstructorMapMajorServiceImpl instructorMapMajorService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertInstructorMapMajor(@RequestBody @Valid InstructorMapMajorDto.InstructorMapMajorInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                instructorMapMajorService.insertInstructorMapMajor(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateInstructorMapMajor(@RequestBody @Valid InstructorMapMajorDto.InstructorMapMajorUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                instructorMapMajorService.updateInstructorMapMajor(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<InstructorMapMajorDto.InstructorMapMajorListInfo>>> selectListInstructorMapMajor(@RequestBody InstructorMapMajorDto.InstructorMapMajorSelectListInfo request) {
        return new ResponseEntity<>(
                instructorMapMajorService.selectListInstructorMapMajor(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteInstructorMapMajor(@RequestBody @Valid InstructorMapMajorDto.InstructorMapMajorDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                instructorMapMajorService.deleteInstructorMapMajor(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<InstructorMapMajorDto.InstructorMapMajorSelectInfoResponse>> selectInstructorMapMajor(@RequestBody InstructorMapMajorDto.InstructorMapMajorSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                instructorMapMajorService.selectInstructorMapMajor(request, lang),
                HttpStatus.OK
        );
    }
}
