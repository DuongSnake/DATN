package com.example.bloodbankmanagement.controller.student;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.ListResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegisterDto;
import com.example.bloodbankmanagement.service.student.AssignmentRegisterServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>>> selectListAssignmentStudentRegister(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectListInfo request) {
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
    public ResponseEntity<SingleResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse>> selectAssignmentStudentRegister(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectInfo request, @RequestHeader("lang") String lang){
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
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>>> selectListAssignmentStudentWaitingSend(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectListInfo request) {
        return new ResponseEntity<>(
                assignmentRegisterService.selectListAssignmentWaitingSend(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListAssApprove")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>>> selectListAssignmentRegisterApprove(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectListInfo request) {
        return new ResponseEntity<>(
                assignmentRegisterService.findListAssignmentRegisterIsApprove(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListFileAss")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<UploadFileDto.UploadFileListInfo>>> selectListFileAssignmentRegisterApprove(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectInfo request) {
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

    @PostMapping(value = "/updateListFileAssignment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BasicResponseDto> updateListFileAssignment(            @RequestParam("assignmentStudentRegisterId")
                                                                                             Long assignmentStudentRegisterId,

                                                                                 @RequestParam(value = "deletedFileIds", required = false)
                                                                                             String deletedFileIdsJson,

                                                                                 @RequestParam(value = "listFile", required = false)
                                                                                             List<MultipartFile> listFile,

                                                                                 @RequestParam(value = "fileIds", required = false)
                                                                                             List<Long> fileIds, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                assignmentRegisterService.updateFileAssignmentRegister(assignmentStudentRegisterId, deletedFileIdsJson, listFile, fileIds, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListAllAssApprove")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>>> selectListAssignmentRegisterApprove() {
        return new ResponseEntity<>(
                assignmentRegisterService.findListAllAssignmentRegisterIsApprove(),
                HttpStatus.OK
        );
    }

}

