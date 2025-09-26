package com.example.bloodbankmanagement.service;

import com.example.bloodbankmanagement.entity.FileMetadata;
import com.example.bloodbankmanagement.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileMetadataService {
    private final FileMetadataRepository fileMetadataRepository;

    public List<FileMetadata> uploadFiles(MultipartFile files[]) throws Exception {
        List<FileMetadata> savedFiles = new ArrayList<>();
        Path uploadPath = Paths.get("upload").toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        for (MultipartFile file : files){
//            String fileName = UUID.randomUUID()+"."+ StringUtils.getFilenameExtension(file.getOriginalFilename());
//            Path targetLocation = uploadPath.resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            FileMetadata objectSave = new FileMetadata();
//            objectSave.setFileLocation(targetLocation.toString());
//            objectSave.setName(file.getOriginalFilename());
//            objectSave.setType(file.getContentType());
//            objectSave.setUserId("1");
//            objectSave.setIsPublic(false);
//            objectSave.setContent(file.getBytes());

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try{
                FileMetadata objectSave = new FileMetadata();
                objectSave.setData(file.getBytes());
                objectSave.setFileType(file.getContentType());
                objectSave.setFileName(fileName);
                String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(objectSave.getId().toString())
                        .toUriString();
                objectSave.setDownloadUrl(downloadUrl);
                savedFiles.add(fileMetadataRepository.save(objectSave));
            }catch (Exception e){
                throw new Exception("Could not save file:" +fileName);
            }
        }
        return savedFiles;
    }

    public List<FileMetadata> getAllFile(){
        List<FileMetadata> listFileUpload = new ArrayList<>();
        return listFileUpload;
    }
}
