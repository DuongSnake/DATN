package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import com.example.bloodbankmanagement.entity.FileMetadata;
import com.example.bloodbankmanagement.service.FileMetadataService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileMetadataService fileMetadataService;

    @PostMapping("/upload")
    public ResponseEntity<List<FileMetadata>> uploadFile(@RequestPart("files")MultipartFile files[]) throws Exception {
        return new ResponseEntity<>(
                fileMetadataService.uploadFiles(files),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<List<FileMetadata>> uploadFile() {
        return new ResponseEntity<>(
                fileMetadataService.getAllFile(),
                HttpStatus.OK
        );
    }

    @PostMapping("/download")
    public void downloadFile(@RequestBody UploadFileDto.UploadFileSelectInfo fileId, HttpServletResponse response) throws Exception {
        fileMetadataService.downloadFile(fileId, response);
    }
}
