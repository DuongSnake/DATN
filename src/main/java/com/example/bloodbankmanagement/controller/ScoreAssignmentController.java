package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import com.example.bloodbankmanagement.dto.service.StudentManagementDto;
import com.example.bloodbankmanagement.service.ScoreAssignmentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/scoreAssignment")
@RequiredArgsConstructor
public class ScoreAssignmentController {

    private final ScoreAssignmentServiceImpl scoreAssignmentService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertScoreAssignment(@RequestBody @Valid ScoreAssignmentDto.ScoreAssignmentInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                scoreAssignmentService.insertScoreAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateScoreAssignment(@RequestBody @Valid ScoreAssignmentDto.ScoreAssignmentUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                scoreAssignmentService.updateScoreAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListInfo>>> selectListScoreAssignment(@RequestBody ScoreAssignmentDto.ScoreAssignmentSelectListInfo request) {
        return new ResponseEntity<>(
                scoreAssignmentService.selectListScoreAssignment(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteScoreAssignment(@RequestBody @Valid ScoreAssignmentDto.ScoreAssignmentDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                scoreAssignmentService.deleteScoreAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<ScoreAssignmentDto.ScoreAssignmentSelectInfoResponse>> selectScoreAssignment(@RequestBody ScoreAssignmentDto.ScoreAssignmentSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                scoreAssignmentService.selectScoreAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListAssignmentReadyToAddScore")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>>> selectListAssignmentReadyToAddScore(@RequestBody ScoreAssignmentDto.ListAssignmentRegisterIsFinalApproveByPeriodIdInfo request) {
        return new ResponseEntity<>(
                scoreAssignmentService.selectListAssignmentReadyToAddScore(request),
                HttpStatus.OK
        );
    }


    @PostMapping("/selectListNewScoreAssignment")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListInfo>>> selectListNewScoreAssignment(@RequestBody ScoreAssignmentDto.ScoreAssignmentNewSelectListInfo request) {
        return new ResponseEntity<>(
                scoreAssignmentService.selectListNewScoreAssignment(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/insertListScoreAssignment")
    public ResponseEntity<BasicResponseDto> insertListStudent(@ModelAttribute @Valid ScoreAssignmentDto.UploadBatchFileRegisterScoreAssignmentInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                scoreAssignmentService.uploadFileRegisterListStudent(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/downloadTemplate")
    public ResponseEntity<Resource> downloadTemplate() throws IOException {
        Resource resource = new ClassPathResource("static/example_insert_list_score_assignment.xlsx");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=example_insert_list_student.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
