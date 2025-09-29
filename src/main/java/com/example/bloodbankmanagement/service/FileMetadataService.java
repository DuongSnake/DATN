package com.example.bloodbankmanagement.service;

import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import com.example.bloodbankmanagement.entity.FileMetadata;
import com.example.bloodbankmanagement.repository.FileMetadataRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.rowset.serial.SerialBlob;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileMetadataService {
    private final FileMetadataRepository fileMetadataRepository;

    public List<FileMetadata> uploadFiles(MultipartFile files[]) throws Exception {
        List<FileMetadata> savedFiles = new ArrayList<>();
        Path uploadPath = Paths.get("upload").toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        for (MultipartFile file : files){
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String tailFile = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            Blob blob = new SerialBlob(file.getBytes());
            try{
                FileMetadata objectSave = new FileMetadata();
                objectSave.setData(blob);
                objectSave.setFileType(tailFile);
                objectSave.setFileName(fileName);
                savedFiles.add(fileMetadataRepository.save(objectSave));
            }catch (Exception e){
                throw new Exception("Could not save file:" +fileName);
            }
        }
        Blob blobEmpty = null;
        savedFiles.forEach(e -> e.setData(blobEmpty));
        return savedFiles;
    }

    public List<FileMetadata> getAllFile(){
        List<FileMetadata> listFileUpload = new ArrayList<>();
        return listFileUpload;
    }
    public void downloadFile(UploadFileDto.UploadFileSelectInfo fileId, HttpServletResponse response) throws Exception {
        Optional<FileMetadata> fileObject =  fileMetadataRepository.timTheoId(fileId.getFileId());
        if(fileObject.isEmpty()){
            throw new Exception("Not found object 123312");
        }
        // Prepare the Headers.
        response.setContentType(determineContentType(fileObject.get().getFileType()));
        response.setHeader("Content-Disposition", "attachment; filename=" + fileObject.get().getFileName());

        try (InputStream in = fileObject.get().getData().getBinaryStream();
             OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
        }
        catch (SQLException e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        throw new RuntimeException("Error accessing file stream", e);
        } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        throw new RuntimeException("File not found or download failed", e);
        }
    }
    private String determineContentType(String fileName) {
        String extension = FilenameUtils.getExtension(fileName).toLowerCase();
        return switch (extension) {
            case "text" -> "text/html";
            case "doc" -> "application/msword";
            case "pdf"  -> "application/pdf";
            case "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            case "jpeg"  -> "image/jpeg";
            case "zip" -> "application/zip";
            case "rar" -> "application/x-rar-compressed";
            case "7z"  -> "application/x-7z-compressed";
            case "tar" -> "application/x-tar";
            case "gz"  -> "application/gzip";
            default    -> "application/octet-stream";
        };
    }

}
