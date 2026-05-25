package com.example.bloodbankmanagement.controller.teacher;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import com.example.bloodbankmanagement.dto.service.UserDto;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegisterDto;
import com.example.bloodbankmanagement.entity.FileUpload;
import com.example.bloodbankmanagement.service.teacher.AssignmentRegisterByInstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1/assignmentRegisterByInstructor")
@RequiredArgsConstructor
public class AssignmentRegisterByInstructorController {

    private final AssignmentRegisterByInstructorService assignmentRegisterByInstructorService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertAssignmentStudentRegister(@ModelAttribute @Valid AssignmentRegisterDto.AssignmentRegisterInsertInfo request, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.insertAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateAssignmentStudentRegister(@ModelAttribute @Valid AssignmentRegisterDto.AssignmentRegisterUpdateInfo request, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.updateAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>>> selectListAssignmentStudentRegister(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectListOfInstructorIdInfo request) {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.selectListAssignmentRegister(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteAssignmentStudentRegister(@RequestBody @Valid AssignmentRegisterDto.AssignmentRegisterDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.deleteAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/approveAssignment")
    public ResponseEntity<BasicResponseDto> approveAssignmentStudentRegister(@RequestBody @Valid AssignmentRegisterDto.AssignmentRegisterDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.approveAssignmentStudentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/reserveListAssignment")
    public ResponseEntity<BasicResponseDto> reserveAssignmentStudentRegister(@RequestBody @Valid AssignmentRegisterDto.AssignmentRegisterDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.reserveAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse>> selectAssignmentStudentRegister(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.selectAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/sendRequestAssignment")
    public ResponseEntity<BasicResponseDto> sendRequestAssignment(@RequestBody @Valid AssignmentRegisterDto.SendRequestAssignmentInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.sendRequestAssignmentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListWaitingSend")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>>> selectListAssignmentStudentWaitingSend(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectListOfInstructorIdInfo request) {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.selectListAssignmentWaitingSend(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListAssApprove")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>>> selectListAssignmentRegisterApprove(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectListOfInstructorIdInfo request) {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.findListAssignmentRegisterIsApprove(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListFileAss")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<UploadFileDto.UploadFileListInfo>>> selectListFileAssignmentRegisterApprove(@RequestBody AssignmentRegisterDto.AssignmentRegisterSelectInfo request) {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.findListFileUploadByAssignmentIdApprove(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/insertListFileAssignment")
    public ResponseEntity<BasicResponseDto> insertListFileAssignment(@ModelAttribute @Valid AssignmentRegisterDto.AssignmentInsertListFileUploadInsertInfo request, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.insertFileAssignmentRegister(request, lang),
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
                assignmentRegisterByInstructorService.updateFileAssignmentRegister(assignmentStudentRegisterId, deletedFileIdsJson, listFile, fileIds, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListAllAssApprove")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>>> selectListAssignmentRegisterApprove() {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.findListAllAssignmentRegisterIsApprove(),
                HttpStatus.OK
        );
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile2(@RequestParam Long fileId) throws Exception {

        FileUpload file = assignmentRegisterByInstructorService.downloadFile(fileId);
        InputStream inputStream = file.getData().getBinaryStream();
        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFileName() + "\"")
                .contentLength(file.getData().length())
                .body(resource);
    }

    @PostMapping("/selectListStudentMapWithInstructorId")
    public ResponseEntity<SingleResponseDto<List<UserDto.UserSelectListInfo>>> selectListStudentMapWithInstructorId(@RequestBody UserDto.UserSelectInfo request, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.selectListStudentMapWithInstructor(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectListStudentNotRegisterAssignment")
    public ResponseEntity<SingleResponseDto<List<UserDto.UserSelectListInfo>>> listStudentAlreadyMapInstructorNotHaveAssignemt(@RequestBody UserDto.UserSelectInfo request, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.listStudentAlreadyMapInstructorNotHaveAssignemt(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/rejectAssignmentStudentRegister")
    public ResponseEntity<BasicResponseDto> rejectAssignmentStudentRegister(@RequestBody @Valid AssignmentRegisterDto.AssignmentRegisterDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.rejectAssignmentStudentRegister(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/rejectApproveFinalAssignmentStudentRegister")
    public ResponseEntity<BasicResponseDto> rejectApproveFinalAssignmentStudentRegister(@RequestBody @Valid AssignmentRegisterDto.AssignmentRegisterDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                assignmentRegisterByInstructorService.rejectApproveFinalAssignmentStudentRegister(request, lang),
                HttpStatus.OK
        );
    }
}
