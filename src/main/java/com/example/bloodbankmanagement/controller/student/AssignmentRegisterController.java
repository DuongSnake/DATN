package com.example.bloodbankmanagement.controller.student;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.ListResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegisterDto;
import com.example.bloodbankmanagement.service.student.AssignmentRegisterServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assignmentRegister")
@RequiredArgsConstructor
public class AssignmentRegisterController {

    private final AssignmentRegisterServiceImpl assignmentRegisterService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertAssignmentStudentRegister(@ModelAttribute @Valid AssignmentRegisterDto.AssignmentRegisterInsertInfo request, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                assignmentRegisterService.insertAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateAssignmentStudentRegister(@ModelAttribute @Valid AssignmentRegisterDto.AssignmentRegisterUpdateInfo request, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                assignmentRegisterService.updateAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentRegisterDto.AssignmentRegisterListInfo>>> selectListAssignmentStudentRegister(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectListInfo request) {
        return new ResponseEntity<>(
                assignmentRegisterService.selectListAssignmentRegister(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteAssignmentStudentRegister(@RequestBody @Valid AssignmentRegisterDto.AssignmentRegisterDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                assignmentRegisterService.deleteAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/reserveListAssignment")
    public ResponseEntity<BasicResponseDto> reserveAssignmentStudentRegister(@RequestBody @Valid AssignmentRegisterDto.AssignmentRegisterDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                assignmentRegisterService.reserveAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<AssignmentRegisterDto.AssignmentRegisterSelectInfoResponse>> selectAssignmentStudentRegister(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                assignmentRegisterService.selectAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/sendRequestAssignment")
    public ResponseEntity<BasicResponseDto> sendRequestAssignment(@RequestBody @Valid AssignmentRegisterDto.SendRequestAssignmentInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                assignmentRegisterService.sendRequestAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListWaitingSend")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentRegisterDto.AssignmentRegisterListInfo>>> selectListAssignmentStudentWaitingSend(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectListInfo request) {
        return new ResponseEntity<>(
                assignmentRegisterService.selectListAssignmentWaitingSend(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListAssApprove")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentRegisterDto.AssignmentRegisterListInfo>>> selectListAssignmentRegisterApprove(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectListInfo request) {
        return new ResponseEntity<>(
                assignmentRegisterService.findListAssignmentRegisterIsApprove(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListFileAss")
    public ResponseEntity<ListResponseDto<UploadFileDto.UploadFileListInfo>> selectListFileAssignmentRegisterApprove(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectInfo request) {
        return new ResponseEntity<>(
                assignmentRegisterService.findListFileUploadByAssignmentIdApprove(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/insertListFileAssignment")
    public ResponseEntity<BasicResponseDto> insertListFileAssignment(@ModelAttribute @Valid AssignmentRegisterDto.AssignmentInsertListFileUploadInsertInfo request, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                assignmentRegisterService.insertFileAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/updateListFileAssignment")
    public ResponseEntity<BasicResponseDto> updateListFileAssignment(@ModelAttribute @Valid AssignmentRegisterDto.AssignmentFileUploadInfo request, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                assignmentRegisterService.updateFileAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListAllAssApprove")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentRegisterDto.AssignmentRegisterListInfo>>> selectListAssignmentRegisterApprove() {
        return new ResponseEntity<>(
                assignmentRegisterService.findListAllAssignmentRegisterIsApprove(),
                HttpStatus.OK
        );
    }

}

