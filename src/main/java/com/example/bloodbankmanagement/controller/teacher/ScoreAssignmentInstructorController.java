package com.example.bloodbankmanagement.controller.teacher;

import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import com.example.bloodbankmanagement.service.teacher.ScoreAssignmentInstructorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scoreInstructor")
@RequiredArgsConstructor
public class ScoreAssignmentInstructorController {
    private final ScoreAssignmentInstructorServiceImpl scoreAssignmentInstructorService;

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListInfo>>> selectListNewScoreAssignment(@RequestBody ScoreAssignmentDto.ScoreAssignmentInstructorSelectListInfo request) {
        return new ResponseEntity<>(
                scoreAssignmentInstructorService.selectListScoreAssignment(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListAssignmentByAdmissionPeriod")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>>> selectListAssignmentByAdmissionPeriod(@RequestBody ScoreAssignmentDto.AssignmentInstructorSelectListNoPaginationInfo request) {
        return new ResponseEntity<>(
                scoreAssignmentInstructorService.selectListAssignmentReadyToAddScore(request),
                HttpStatus.OK
        );
    }

}
