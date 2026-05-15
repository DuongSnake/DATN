package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.InstructorDto;
import com.example.bloodbankmanagement.service.InstructorServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/instructor")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorServiceImpl instructorService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertInstructor(@RequestBody @Valid InstructorDto.InstructorInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                instructorService.insertInstructor(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateInstructor(@RequestBody @Valid InstructorDto.InstructorUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                instructorService.updateInstructor(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<InstructorDto.InstructorListInfo>>> selectListInstructor(@RequestBody InstructorDto.InstructorSelectListRequest request) {
        return new ResponseEntity<>(
                instructorService.selectListInstructor(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteInstructor(@RequestBody @Valid InstructorDto.InstructorDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                instructorService.deleteInstructor(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<InstructorDto.InstructorSelectInfoResponse>> selectInstructor(@RequestBody InstructorDto.InstructorSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                instructorService.selectInstructor(request, lang),
                HttpStatus.OK
        );
    }
}
