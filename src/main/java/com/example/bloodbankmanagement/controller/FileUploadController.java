package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import com.example.bloodbankmanagement.service.FileMetadataServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileMetadataServiceImpl fileMetadataService;

    @PostMapping("/insertListFile")
    public ResponseEntity<BasicResponseDto> uploadFile(@ModelAttribute @Valid UploadFileDto.InserListFiletUploadFileInfo request, @RequestHeader("lang") String lang) throws Exception {
        return new ResponseEntity<>(
                fileMetadataService.insertListFileToFolderUpload(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<UploadFileDto.UploadFileListInfo>>> selectListFileUpload(@RequestBody UploadFileDto.UploadFileSelectListInfo request) {
        return new ResponseEntity<>(
                fileMetadataService.selectListFileUpload(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/select")
    public ResponseEntity<SingleResponseDto<UploadFileDto.UploadFileSelectInfoResponse>> uploadFile(@RequestBody UploadFileDto.UploadFileSelectInfo request) {
        return new ResponseEntity<>(
                fileMetadataService.selectFileUpload(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/download")
    public void downloadFile(@RequestBody UploadFileDto.UploadFileSelectInfo request, HttpServletResponse response) throws Exception {
        fileMetadataService.downloadFile(request, response);
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateFileUpload(@ModelAttribute @Valid UploadFileDto.UpdateUploadFileInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                fileMetadataService.updateFileUpload(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertFileUpload(@ModelAttribute @Valid UploadFileDto.InsertUploadFileInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                fileMetadataService.insertFileUpload(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteFileUpload(@RequestBody UploadFileDto.UploadFileDeleteInfo listFileId){
        return new ResponseEntity<>(
                fileMetadataService.deleteFileUpload(listFileId),
                HttpStatus.OK
        );
    }

    @GetMapping("/download2")
    public void downloadFile2(@RequestParam("id") Long fileId, HttpServletResponse response) throws Exception {
        fileMetadataService.downloadFile2(fileId, response);
    }
}
