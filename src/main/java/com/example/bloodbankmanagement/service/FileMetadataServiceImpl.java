package com.example.bloodbankmanagement.service;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.FileMetadataDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import com.example.bloodbankmanagement.entity.*;
import com.example.bloodbankmanagement.repository.AssignmentStudentRegisterRepository;
import com.example.bloodbankmanagement.repository.FileMetadataRepository;
import com.example.bloodbankmanagement.repository.PeriodAssignmentRepository;
import com.example.bloodbankmanagement.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileMetadataServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final FileMetadataRepository fileMetadataRepository;
    private final PeriodAssignmentRepository periodAssignmentRepository;
    private final AssignmentStudentRegisterRepository assignmentStudentRegisterRepository;
    private final UserRepository userRepository;
    private final ResponseCommon responseService;

    @Transactional
    public BasicResponseDto uploadToFolderUpload(MultipartFile files[]) throws Exception {
        BasicResponseDto result;
        String userIdUpload = CommonUtil.getUsernameByToken();
        //Storage in folder upload in project
//        Path uploadPath = Paths.get("upload").toAbsolutePath().normalize();
//        Files.createDirectories(uploadPath);
        String userIdRegister = checkExistUser(userIdUpload);
        for (MultipartFile file : files){
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String tailFile = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            Blob blob = new SerialBlob(file.getBytes());
            try{
                FileUpload objectSave = new FileUpload();
                objectSave.setData(blob);
                objectSave.setFileType(tailFile);
                objectSave.setFileName(fileName);
                objectSave.setFileSize(file.getSize());
                objectSave.setCreateUser(userIdRegister);
                objectSave.setStatus(CommonUtil.STATUS_USE);
//                fileMetadataRepository.save(objectSave);
            }catch (Exception e){
                throw new Exception("Could not save file:" +fileName);
            }
        }
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<UploadFileDto.UploadFileListInfo>> selectListFileUpload(UploadFileDto.UploadFileSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        //Set value userId
        String userId = isUserHaveRoleAdmin(request.getUserUpload());
        request.setUserUpload(userId);
        PageAmtListResponseDto<UploadFileDto.UploadFileListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<FileUpload> listDataFileMetadata = fileMetadataRepository.findListFileUpload(request, pageable);
        pageAmtObject = FileUpload.convertListObjectToDto(listDataFileMetadata);
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public void downloadFile(UploadFileDto.UploadFileSelectInfo request, HttpServletResponse response) {
        FileUpload fileObject =  fileMetadataRepository.findByFileIdToDownload(request.getFileId());
        if(null ==fileObject){
            throw new CustomException("Not found object by fileId", "en");
        }
        // Prepare the Headers.
        String contentType = URLConnection.guessContentTypeFromName(fileObject.getFileName());
        logger.info("Type file upload: "+contentType);
        response.setContentType(contentType != null ? contentType : "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileObject.getFileName());

        try (InputStream in = fileObject.getData().getBinaryStream();
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

    public SingleResponseDto<UploadFileDto.UploadFileSelectInfoResponse> selectFileUpload(UploadFileDto.UploadFileSelectInfo request){
        if(null == request || request.getFileId().equals("") || null == request.getFileId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<UploadFileDto.UploadFileSelectInfoResponse> selectObject = new SingleResponseDto<>();
        FileUpload dataFileMetadata = fileMetadataRepository.findByFileId(request.getFileId());
        UploadFileDto.UploadFileSelectInfoResponse objectResponse= new UploadFileDto.UploadFileSelectInfoResponse();
        if(null ==dataFileMetadata){
            objectResponse = null;
        }else{
            objectResponse = FileUpload.convertObjectToDto(dataFileMetadata);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto insertFileUpload(UploadFileDto.InsertUploadFileInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == request.getFileUploadContent().getOriginalFilename() || "".equals(request.getFileUploadContent().getOriginalFilename())){
            throw new CustomException("Not found file name upload ", lang);
        }
        //Check
        String fileName = StringUtils.cleanPath(request.getFileUploadContent().getOriginalFilename());
        String tailFile = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        try {
            Blob blob = new SerialBlob(request.getFileUploadContent().getBytes());
            FileUpload objectUpdate = new FileUpload();
            objectUpdate.setData(blob);
            objectUpdate.setFileType(tailFile);
            objectUpdate.setFileName(fileName);
            objectUpdate.setFileSize(request.getFileUploadContent().getSize());
            objectUpdate.setUpdateUser(userIdUpdate);
            objectUpdate.setUpdateAt(LocalDate.now());
            objectUpdate.setStatus(CommonUtil.STATUS_USE);
            //Find the period assignment
            PeriodAssignment objectPeriod = periodAssignmentRepository.findByFileId(request.getPeriodAssignmentId());
            if(null == objectPeriod){
                throw new CustomException("Value periodAssignmentId not exist in database ", "en");
            }
            //Check expire time upload
            LocalDate currentDate = LocalDate.now();
            if(null != objectPeriod.getEndPeriod() && objectPeriod.getEndPeriod().isBefore(currentDate)){
                throw new CustomException("The time upload file is expire ", "en");
            }
            objectUpdate.setPeriodAssignmentInfo(objectPeriod);
            //Check assignment register
            AssignmentStudentRegister assignmentStudentRegister = assignmentStudentRegisterRepository.findByFileId(request.getAssignmentRegisterId());
            if(null == assignmentStudentRegister){
                throw new CustomException("Value assignmentRegisterId not exist in database ", "en");
            }
            objectUpdate.setAssignmentRegisterInfo(assignmentStudentRegister);
            fileMetadataRepository.save(objectUpdate);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto updateFileUpload(UploadFileDto.UpdateUploadFileInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        String userIdRegister = checkExistUser(userIdUpdate);
        BasicResponseDto messageResponse;
        //Check
        String fileName = StringUtils.cleanPath(request.getFileUploadContent().getOriginalFilename());
        String tailFile = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        try {
            Blob blob = new SerialBlob(request.getFileUploadContent().getBytes());
            FileUpload objectUpdate = new FileUpload();
            objectUpdate.setId(request.getFileId());
            objectUpdate.setData(blob);
            objectUpdate.setFileType(tailFile);
            objectUpdate.setFileName(fileName);
            objectUpdate.setFileSize(request.getFileUploadContent().getSize());
            //Find the period assignment
            PeriodAssignment objectPeriod = periodAssignmentRepository.findByFileId(request.getPeriodAssignmentId());
            if(null == objectPeriod){
                throw new CustomException("Value periodAssignmentId not exist in database ", lang);
            }
            //Check expire time upload
            LocalDate currentDate = LocalDate.now();
            if(null != objectPeriod.getEndPeriod() && objectPeriod.getEndPeriod().isBefore(currentDate)){
                throw new CustomException("The time upload file is expire ", lang);
            }
            objectUpdate.setPeriodAssignmentInfo(objectPeriod);
            //Check assignment register
            AssignmentStudentRegister assignmentStudentRegister = assignmentStudentRegisterRepository.findByFileId(request.getAssignmentRegisterId());
            if(null == assignmentStudentRegister){
                throw new CustomException("Value assignmentRegisterId not exist in database ", "en");
            }
            objectUpdate.setAssignmentRegisterInfo(assignmentStudentRegister);
            objectUpdate.setUpdateUser(userIdUpdate);
            objectUpdate.setUpdateAt(LocalDate.now());
            fileMetadataRepository.updateFileUpload(objectUpdate);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto updateFileUploadSimple(String fileId, MultipartFile file){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == fileId){
            throw new CustomException("fileId must not be blank or null ", "en");
        }
        //Check
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String tailFile = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        try {
            Blob blob = new SerialBlob(file.getBytes());
            FileUpload objectUpdate = new FileUpload();
            objectUpdate.setId(Long.valueOf(fileId));
            objectUpdate.setData(blob);
            objectUpdate.setFileType(tailFile);
            objectUpdate.setFileName(fileName);
            objectUpdate.setFileSize(file.getSize());
            objectUpdate.setUpdateUser(userIdUpdate);
            objectUpdate.setUpdateAt(LocalDate.now());
            fileMetadataRepository.updateFileUpload(objectUpdate);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deleteFileUpload(UploadFileDto.UploadFileDeleteInfo listFileId){
        BasicResponseDto objectResponse;
        FileUpload objectDelete = new FileUpload();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        fileMetadataRepository.deleteFileUpload(objectDelete, listFileId.getListData());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }

    @Transactional
    public BasicResponseDto insertListFileToFolderUpload(UploadFileDto.InserListFiletUploadFileInfo request, String lang) throws Exception {
        BasicResponseDto result;
        String userIdCreate = CommonUtil.getUsernameByToken();
        String userIdRegister = checkExistUser(userIdCreate);
        List<FileUpload> listFileUpload = new ArrayList<>();
        //Find the period assignment
        PeriodAssignment objectPeriod = periodAssignmentRepository.findByFileId(request.getPeriodAssignmentId());
        if(null == objectPeriod){
            throw new CustomException("Value periodAssignmentId not exist in database ", lang);
        }
        //Check expire time upload
        LocalDate currentDate = LocalDate.now();
        if(null != objectPeriod.getEndPeriod() && objectPeriod.getEndPeriod().isBefore(currentDate)){
            throw new CustomException("The time upload file is expire ", lang);
        }
        //Check assignment register
        AssignmentStudentRegister assignmentStudentRegister = assignmentStudentRegisterRepository.findByFileId(request.getAssignmentRegisterId());
        if(null == assignmentStudentRegister){
            throw new CustomException("Value assignmentRegisterId not exist in database ", lang);
        }
        //Check list file upload
        if(null != request.getListFile() && request.getListFile().length == 0){
            throw new CustomException("Not found list file upload ", lang);
        }
        for (MultipartFile file : request.getListFile()){
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String tailFile = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            Blob blob = new SerialBlob(file.getBytes());
            try{
                FileUpload objectSave = new FileUpload();
                objectSave.setData(blob);
                objectSave.setFileType(tailFile);
                objectSave.setFileName(fileName);
                objectSave.setFileSize(file.getSize());
                objectSave.setCreateUser(userIdRegister);
                objectSave.setStatus(CommonUtil.STATUS_USE);
                objectSave.setAssignmentRegisterInfo(assignmentStudentRegister);
                objectSave.setPeriodAssignmentInfo(objectPeriod);
                listFileUpload.add(objectSave);
            }catch (Exception e){
                throw new Exception("Could not save file:" +fileName);
            }
        }
        fileMetadataRepository.saveAll(listFileUpload);
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    public String isUserHaveRoleAdmin(String userName){
        boolean isTypeAdmin = false;
        if(ObjectUtils.isEmpty(userName)){
            return "";
        }
        Optional<User> userInfo = userRepository.findByUsername(userName);
        if(ObjectUtils.isEmpty(userInfo.get())){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        for(Role objectRole: userInfo.get().getRoles()){
            logger.info("Value role name: "+objectRole.getName() +" of userId: "+ userInfo.get().getUsername());
            if(CommonUtil.ROLE_ADMIN.equals(objectRole.getName().toString())){
                isTypeAdmin = true;
            }
        }
        if(isTypeAdmin){
            return "";
        }
        return userInfo.get().getUsername();
    }

    public String checkExistUser(String userName){
        Optional<User> userInfo = userRepository.findByUsername(userName);
        if(ObjectUtils.isEmpty(userInfo.get())){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        return userInfo.get().getUsername();
    }

    public void downloadFile2(Long fileId, HttpServletResponse response) {
        FileUpload fileObject =  fileMetadataRepository.findByFileIdToDownload(fileId);
        if(null ==fileObject){
            throw new CustomException("Not found object by fileId", "en");
        }
        // Prepare the Headers.
        String contentType = URLConnection.guessContentTypeFromName(fileObject.getFileName());
        logger.info("Type file upload: "+contentType);
        response.setContentType(contentType != null ? contentType : "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileObject.getFileName());

        try (InputStream in = fileObject.getData().getBinaryStream();
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

}
