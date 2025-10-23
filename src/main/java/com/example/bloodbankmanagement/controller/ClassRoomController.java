package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.ClassRoomDto;
import com.example.bloodbankmanagement.service.ClassRoomServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/classRoom")
@RequiredArgsConstructor
public class ClassRoomController {

    private final ClassRoomServiceImpl classRoomService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertClassRoom(@RequestBody @Valid ClassRoomDto.ClassRoomInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                classRoomService.insertClassRoom(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateClassRoom(@RequestBody @Valid ClassRoomDto.ClassRoomUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                classRoomService.updateClassRoom(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<ClassRoomDto.ClassRoomListInfo>>> selectListClassRoom(@RequestBody ClassRoomDto.ClassRoomSelectListInfo request) {
        return new ResponseEntity<>(
                classRoomService.selectListClassRoom(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteClassRoom(@RequestBody @Valid ClassRoomDto.ClassRoomDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                classRoomService.deleteClassRoom(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<ClassRoomDto.ClassRoomSelectInfoResponse>> selectClassRoom(@RequestBody ClassRoomDto.ClassRoomSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                classRoomService.selectClassRoom(request, lang),
                HttpStatus.OK
        );
    }

}
