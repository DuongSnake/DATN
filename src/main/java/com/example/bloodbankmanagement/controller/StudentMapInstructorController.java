package com.example.bloodbankmanagement.controller;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.StudentMapInstructorDto;
import com.example.bloodbankmanagement.service.StudentMapInstructorServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student_map_instructor")
@RequiredArgsConstructor
public class StudentMapInstructorController {

    private final StudentMapInstructorServiceImpl studentMapInstructorService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertStudentMapInstructor(@RequestBody @Valid StudentMapInstructorDto.StudentMapInstructorInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                studentMapInstructorService.insertStudentMapInstructor(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateStudentMapInstructor(@RequestBody @Valid StudentMapInstructorDto.StudentMapInstructorUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                studentMapInstructorService.updateStudentMapInstructor(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<StudentMapInstructorDto.StudentMapInstructorListInfo>>> selectListStudentMapInstructor(@RequestBody StudentMapInstructorDto.StudentMapInstructorSelectListInfo request) {
        return new ResponseEntity<>(
                studentMapInstructorService.selectListStudentMapInstructor(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteStudentMapInstructor(@RequestBody @Valid StudentMapInstructorDto.StudentMapInstructorDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                studentMapInstructorService.deleteStudentMapInstructor(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<StudentMapInstructorDto.StudentMapInstructorSelectInfoResponse>> selectStudentMapInstructor(@RequestBody StudentMapInstructorDto.StudentMapInstructorSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                studentMapInstructorService.selectStudentMapInstructor(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/insertListStudentMapInstructor")
    public ResponseEntity<BasicResponseDto> insertListStudentToOneInstructor(@RequestBody @Valid StudentMapInstructorDto.InsertListStudentWithOneInstructorInfo request) {
        return new ResponseEntity<>(
                studentMapInstructorService.insertListStudentToOneInstructor(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/updateListStudentMapNewInstructor")
    public ResponseEntity<BasicResponseDto> updateListStudentToNewInstructor(@RequestBody @Valid StudentMapInstructorDto.UpdateListStudentWithOneInstructorInfo request) {
        return new ResponseEntity<>(
                studentMapInstructorService.updateListStudentToNewInstructor(request),
                HttpStatus.OK
        );
    }

}
