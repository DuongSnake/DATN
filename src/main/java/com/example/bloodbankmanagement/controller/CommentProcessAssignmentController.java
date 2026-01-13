package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.CommentProcessAssignmentDto;
import com.example.bloodbankmanagement.service.CommentProcessAssignmentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/commentProcessAssignment")
@RequiredArgsConstructor
public class CommentProcessAssignmentController {

    private final CommentProcessAssignmentServiceImpl majorService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertCommentProcessAssignment(@RequestBody @Valid CommentProcessAssignmentDto.CommentProcessAssignmentInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                majorService.insertCommentProcessAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateCommentProcessAssignment(@RequestBody @Valid CommentProcessAssignmentDto.CommentProcessAssignmentUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                majorService.updateCommentProcessAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<CommentProcessAssignmentDto.CommentProcessAssignmentListInfo>>> selectListCommentProcessAssignment(@RequestBody CommentProcessAssignmentDto.CommentProcessAssignmentSelectListInfo request) {
        return new ResponseEntity<>(
                majorService.selectListCommentProcessAssignment(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteCommentProcessAssignment(@RequestBody @Valid CommentProcessAssignmentDto.CommentProcessAssignmentDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                majorService.deleteCommentProcessAssignment(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfoResponse>> selectCommentProcessAssignment(@RequestBody CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                majorService.selectCommentProcessAssignment(request, lang),
                HttpStatus.OK
        );
    }

}
