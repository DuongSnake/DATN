package com.example.bloodbankmanagement.controller.student;

import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import com.example.bloodbankmanagement.service.student.ScoreAssignmentStudentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scoreStudent")
@RequiredArgsConstructor
public class ScoreAssignmentStudentController {

    private final ScoreAssignmentStudentServiceImpl scoreAssignmentStudentService;


    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListInfo>>> selectListNewScoreAssignment(@RequestBody ScoreAssignmentDto.ScoreAssignmentStudentSelectListInfo request) {
        return new ResponseEntity<>(
                scoreAssignmentStudentService.selectListScoreAssignment(request),
                HttpStatus.OK
        );
    }
}
