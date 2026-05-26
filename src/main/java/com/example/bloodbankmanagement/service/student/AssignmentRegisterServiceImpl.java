package com.example.bloodbankmanagement.service.student;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.AuthenticationException;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.common.untils.ERole;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.ListResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentRegisterDTO;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegister;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegisterDto;
import com.example.bloodbankmanagement.entity.*;
import com.example.bloodbankmanagement.repository.FileMetadataRepository;
import com.example.bloodbankmanagement.repository.PeriodAssignmentRepository;
import com.example.bloodbankmanagement.repository.StudentMapInstructorRepository;
import com.example.bloodbankmanagement.repository.UserRepository;
import com.example.bloodbankmanagement.repository.student.AssignmentRegisterRepository;
import com.example.bloodbankmanagement.service.authorization.UserDetailsImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.InputStream;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentRegisterServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final AssignmentRegisterRepository assignmentRegisterRepository;
    private final StudentMapInstructorRepository studentMapInstructorRepository;
    private final PeriodAssignmentRepository periodAssignmentRepository;
    private final UserRepository userRepository;
    private final ResponseCommon responseService;
    private final FileMetadataRepository fileMetadataRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public BasicResponseDto insertAssignmentRegister(AssignmentRegisterDto.AssignmentRegisterInsertInfo request, String lang) throws Exception {
        BasicResponseDto result;
        try{
            LocalDate nowDate = LocalDate.now();
            String userIdCreate = CommonUtil.getUsernameByToken();
            AssignmentStudentRegister objectUpdate = new AssignmentStudentRegister();
            objectUpdate.setAssignmentName(request.getAssignmentStudentRegisterName());
            //Tim thong tin giao vien map sinh vien
            User studentInfo = getInfoStudentById(request.getStudentId());
            if(ObjectUtils.isEmpty(studentInfo)){
                throw new Exception("Not found the student info:");
            }
            if(null != studentInfo){
                objectUpdate.setStudentInfo(studentInfo);
            }
            //Tim thong tin ky han
            PeriodAssignment periodAssignment = periodAssignmentRepository.findByFileId(request.getPeriodAssignmentId());
            if(null == periodAssignment){
                throw new Exception("Not found the period assignment:");
            }
            //Check expire time upload
            LocalDate currentDate = LocalDate.now();
            if(null != periodAssignment.getEndPeriod() && periodAssignment.getEndPeriod().isBefore(currentDate)){
                throw new CustomException("The time upload file is expire ", "en");
            }
            objectUpdate.setPeriodAssignmentInfo(periodAssignment);
            //Xu lý thong tin file upload
            if(null != request.getFileUpload()){
                //Xu ly file upload
                String fileName = StringUtils.cleanPath(request.getFileUpload().getOriginalFilename());
                String tailFile = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                Blob blob = new SerialBlob(request.getFileUpload().getBytes());
                objectUpdate.setFileName(fileName);
                objectUpdate.setFileType(tailFile);
                objectUpdate.setContentAssignment(blob);
            }
            objectUpdate.setStatusAutoMap(request.getStatusAutoMap());
            objectUpdate.setIsApproved(CommonUtil.STATUS_NOT_ACCEPT);//status default not accept
            objectUpdate.setCreateUser(userIdCreate);
            objectUpdate.setStatus(CommonUtil.STATUS_USE);
            objectUpdate.setCreateAt(nowDate);
            objectUpdate.setOldValueId(studentInfo.getId());
            assignmentRegisterRepository.save(objectUpdate);
            //Logic handle case insert student map instructor by value status autoMap
            handleCaseInsertToStudentMapInstructor(studentInfo, request.getStatusAutoMap(), request.getInstructorId(), userIdCreate, nowDate);
            //Handle for case change userId at case update
            result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>> selectListAssignmentRegister(AssignmentRegisterDto.AssignmentRegisterSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        UserDetailsImpl userIdRegister = CommonUtil.getUserInfoByToken();
        //Only find the list student upload in file
        request.setStudentId(userIdRegister.getId());
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<AssignmentStudentRegisterDTO> listDataFileMetadata = assignmentRegisterRepository.findListAssignmentRegister(request, pageable);
        pageAmtObject = AssignmentRegister.convertListObjectToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>> selectListAssignmentWaitingSend(AssignmentRegisterDto.AssignmentRegisterSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        String userIdRegister = CommonUtil.getUsernameByToken();
        //Only find the list student upload in file
        request.setRegUser(userIdRegister);
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<AssignmentStudentRegisterDTO> listDataFileMetadata = assignmentRegisterRepository.findListAssignmentWaitingsend(request, pageable);
        pageAmtObject = AssignmentRegister.convertListObjectToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }


    public SingleResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse> selectAssignmentRegister(AssignmentRegisterDto.AssignmentRegisterSelectInfo request, String lang){
        if(null == request || request.getAssignmentStudentRegisterId().equals("") || null == request.getAssignmentStudentRegisterId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse> selectObject = new SingleResponseDto<>();
        AssignmentStudentRegisterDTO dataFileMetadata = assignmentRegisterRepository.findByFileId(request.getAssignmentStudentRegisterId());
        AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse objectResponse= new AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse();
        if(null ==dataFileMetadata){
            objectResponse = null;
        }else{
            objectResponse = AssignmentRegister.convertToDto(dataFileMetadata);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto updateAssignmentRegister(AssignmentRegisterDto.AssignmentRegisterUpdateInfo request, String lang) throws Exception {
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        Long valueId = getIdByUserName(userIdUpdate);
        try{
            if(null == request){
                throw new CustomException("the object send request not null ", "en");
            }
            if(null == valueId){
                throw new CustomException("Not found user infor ", "en");
            }
            //Tim thong tin giao vien map sinh vien
//            List<StudentMapInstructor> infoInstructor = studentMapInstructorRepository.getStudentMapInstructorIdActiveByStudentId(valueId);
//            if(infoInstructor.size() == 0 || infoInstructor.get(0).getInstructorInfo() == null || infoInstructor.get(0).getInstructorInfo().getId() == null){
//                throw new CustomException("Not found the  instructor before send request,please call to admin assign the instructor", "en");
//            }
            AssignmentStudentRegister objectUpdate = new AssignmentStudentRegister();
            objectUpdate.setId(request.getAssignmentStudentRegisterId());
            objectUpdate.setAssignmentName(request.getAssignmentStudentRegisterName());
            objectUpdate.setUpdateUser(userIdUpdate);
            objectUpdate.setUpdateAt(LocalDate.now());
            //Tim thong tin ky han
            PeriodAssignment periodAssignment = periodAssignmentRepository.findByFileId(request.getPeriodAssignmentId());
            if(null == periodAssignment){
                throw new Exception("Not found the period assignment:");
            }
            //Check expire time upload
            LocalDate currentDate = LocalDate.now();
            if(null != periodAssignment.getEndPeriod() && periodAssignment.getEndPeriod().isBefore(currentDate)){
                throw new CustomException("The time upload file is expire ", "en");
            }
            objectUpdate.setPeriodAssignmentInfo(periodAssignment);
            //Xu lý thong tin file upload
            if(null != request.getFileUpload() && !"".equals(request.getFileUpload().getOriginalFilename()) ){
                //Xu ly file upload
                String fileName = StringUtils.cleanPath(request.getFileUpload().getOriginalFilename());
                String tailFile = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                Blob blob = new SerialBlob(request.getFileUpload().getBytes());
                objectUpdate.setFileName(fileName);
                objectUpdate.setFileType(tailFile);
                objectUpdate.setContentAssignment(blob);
                assignmentRegisterRepository.updateAssignmentRegister(objectUpdate);
            }else{
                assignmentRegisterRepository.updateAssignmentRegisterNoFile(objectUpdate);
            }
            messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deleteAssignmentRegister(AssignmentRegisterDto.AssignmentRegisterDeleteInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        AssignmentStudentRegister objectDelete = new AssignmentStudentRegister();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        assignmentRegisterRepository.deleteAssignmentRegister(objectDelete, listFileId.getListData());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }

    @Transactional
    public BasicResponseDto reserveAssignmentRegister(AssignmentRegisterDto.AssignmentRegisterDeleteInfo listFileId, String lang){
        //De do an o trang thai bao luu
        BasicResponseDto objectResponse;
        AssignmentStudentRegister objectDelete = new AssignmentStudentRegister();
        objectDelete.setIsApproved(CommonUtil.STATUS_RESERVE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        assignmentRegisterRepository.changeStatusAssignmentRegister(objectDelete, listFileId.getListData());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }

    @Transactional
    public BasicResponseDto sendRequestAssignmentRegister(AssignmentRegisterDto.SendRequestAssignmentInfo idSendRequest, String lang){
        BasicResponseDto objectResponse;
        String userIdSendRequest = CommonUtil.getUsernameByToken();
        Long valueId = getIdByUserName(userIdSendRequest);
        //Check instructor id before send request
        List<StudentMapInstructor> infoInstructor  = studentMapInstructorRepository.getStudentMapInstructorIdActiveByStudentId(valueId);
        if(infoInstructor.size() == 0 || infoInstructor.get(0).getInstructorInfo() == null || infoInstructor.get(0).getInstructorInfo().getId() == null){
            throw new CustomException("Not found the  instructor before send request,please call to admin assign the instructor", "en");
        }
        AssignmentStudentRegister objectSendRequest = new AssignmentStudentRegister();
        objectSendRequest.setId(idSendRequest.getRequestId());
        objectSendRequest.setIsApproved(CommonUtil.STATUS_SEND_REQUEST);
        objectSendRequest.setUpdateAt(LocalDate.now());
        objectSendRequest.setUpdateUser(CommonUtil.getUsernameByToken());
        assignmentRegisterRepository.sendRequestAssignmentRegister(objectSendRequest);
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return objectResponse;
    }

    public Long getIdByUserName(String userName){
        Optional<User> userInfo = userRepository.findByUsername(userName);
        if(ObjectUtils.isEmpty(userInfo.get())){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        return userInfo.get().getId();
    }

    public SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>> findListAssignmentRegisterIsApprove(AssignmentRegisterDto.AssignmentRegisterSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        String userIdRegister = CommonUtil.getUsernameByToken();
        //Find the customer by token
        Long valueId = getIdByUserName(userIdRegister);
        //Only find the list student upload in file
        request.setRegUser(userIdRegister);
        request.setStudentId(valueId);
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<AssignmentStudentRegisterDTO> listDataFileMetadata = assignmentRegisterRepository.findListAssignmentRegisterIsApprove(request, pageable);
        pageAmtObject = AssignmentRegister.convertListObjectToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<PageAmtListResponseDto<UploadFileDto.UploadFileListInfo>> findListFileUploadByAssignmentIdApprove(AssignmentRegisterDto.AssignmentRegisterSelectInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        //Select list file upload
        List<FileUpload> listDataFileMetadata = fileMetadataRepository.findListFileUpload(request.getAssignmentStudentRegisterId());
        PageAmtListResponseDto<UploadFileDto.UploadFileListInfo> listDataFileMetadataDto = FileUpload.convertListObjectToDtoUserSite(listDataFileMetadata);
        objectResponse = responseService.getSingleResponse(listDataFileMetadataDto,new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    @Transactional
    public BasicResponseDto insertFileAssignmentRegister(AssignmentRegisterDto.AssignmentInsertListFileUploadInsertInfo request, String lang) throws Exception {
        String userIdRegister = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        List<FileUpload> listFileUpload = new ArrayList<>();
        try{
            if(null == request){
                throw new CustomException("the object send request not null ", "en");
            }
            for (MultipartFile file : request.getListFile()){
                AssignmentStudentRegister assignmentStudentRegister= assignmentRegisterRepository.findByFileIdApproveAss(request.getAssignmentStudentRegisterId());
                if(null == assignmentStudentRegister){
                    throw new AuthenticationException("Not found the assignment register");
                }
                //Check expire time upload
                LocalDate currentDate = LocalDate.now();
                if(null == assignmentStudentRegister.getPeriodAssignmentInfo()){
                    throw new CustomException("Not found the value period assignemnt ", "en");
                }
                if(null != assignmentStudentRegister.getPeriodAssignmentInfo().getEndPeriod() && assignmentStudentRegister.getPeriodAssignmentInfo().getEndPeriod().isBefore(currentDate)){
                    throw new CustomException("The time upload file is expire ", "en");
                }
                FileUpload objectSave = handleObjectBeforeSave(file, userIdRegister, assignmentStudentRegister);
                listFileUpload.add(objectSave);
            }
            fileMetadataRepository.saveAll(listFileUpload);
            messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto updateFileAssignmentRegister(

            Long assignmentStudentRegisterId,

            String deletedFileIdsJson,

            List<MultipartFile> listFile,

            List<Long> fileIds, String lang) throws Exception {
        BasicResponseDto messageResponse;
        List<Long> fileIdsUpdate = new ArrayList<>();
        List<MultipartFile> fileUpdates = new ArrayList<>();
        List<MultipartFile> fileInserts = new ArrayList<>();
        try{
            if(null == assignmentStudentRegisterId){
                throw new CustomException("the assignmentStudentRegisterId send request not null ", "en");
            }
            AssignmentStudentRegister assignmentStudentRegister= assignmentRegisterRepository.findByFileIdApproveAss(assignmentStudentRegisterId);
            if(null == assignmentStudentRegister){
                throw new AuthenticationException("Not found the assignment register");
            }
            //Check expire time upload
            LocalDate currentDate = LocalDate.now();
            if(null == assignmentStudentRegister.getPeriodAssignmentInfo()){
                throw new CustomException("Not found the value period assignemnt ", "en");
            }
            if(null != assignmentStudentRegister.getPeriodAssignmentInfo().getEndPeriod() && assignmentStudentRegister.getPeriodAssignmentInfo().getEndPeriod().isBefore(currentDate)){
                throw new CustomException("The time upload file is expire ", "en");
            }
            //Check list delete list file
            if (deletedFileIdsJson != null && !deletedFileIdsJson.isEmpty()){
                List<Long> deletedFileIds = objectMapper.readValue( deletedFileIdsJson,new TypeReference<List<Long>>() {});
                //Check list delete list file
                deleteListFileUpload(deletedFileIds);
            }
            //Check list insert list file and update file
            if (listFile != null && !listFile.isEmpty()) {
                for (int i = 0; i < listFile.size(); i++) {
                    MultipartFile multipartFile =listFile.get(i);
                    Long fileId =null;
                    // parse nullable long
                    if (fileIds != null && fileIds.size() > i && fileIds.get(i) != null) {
                        fileId = fileIds.get(i);
                    }
                    if (fileId != null) {
                        fileUpdates.add(multipartFile);
                        fileIdsUpdate.add(fileId);
                    }else {
                        fileInserts.add(multipartFile);
                    }
                }
            }
            insertListFileUpload(fileInserts,assignmentStudentRegister);
            //Check list update list file
            updateListFileUpload(fileIdsUpdate ,fileUpdates, assignmentStudentRegister);
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
        //Insert list id
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }


    public SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>> findListAllAssignmentRegisterIsApprove(){
        SingleResponseDto objectResponse = new SingleResponseDto();
        String userIdRegister = CommonUtil.getUsernameByToken();
        //Find the customer by token
        Long valueId = getIdByUserName(userIdRegister);
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        //Select list file upload
        List<AssignmentStudentRegisterDTO> listDataFileMetadata = assignmentRegisterRepository.findListAllAssignmentRegisterIsApprove();
        pageAmtObject = AssignmentRegister.convertListObjectToDto(listDataFileMetadata, Long.valueOf(listDataFileMetadata.size()));
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }


    public void insertListFileUpload(List<MultipartFile> listFileInsert, AssignmentStudentRegister assignmentRegisterInfo) throws Exception {
        String userIdRegister = CommonUtil.getUsernameByToken();
        List<FileUpload> listFileUpload = new ArrayList<>();
        try{
            if(null == listFileInsert || listFileInsert.size() == 0){
                return;
            }
            for (MultipartFile file : listFileInsert){
                FileUpload objectSave = handleObjectBeforeSave(file, userIdRegister, assignmentRegisterInfo);
                listFileUpload.add(objectSave);
            }
            fileMetadataRepository.saveAll(listFileUpload);
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
    }


    public void updateListFileUpload(List<Long> listIdUpdate,List<MultipartFile> listFileUpdate, AssignmentStudentRegister assignmentRegisterInfo) throws Exception {
        String userIdUpdate = CommonUtil.getUsernameByToken();
        List<FileUpload> listFileUpload = new ArrayList<>();
        try{
            if(null == listFileUpdate || listFileUpdate.size() == 0){
                return;
            }
            if(!(Integer.valueOf(listFileUpdate.size()) == listIdUpdate.size())){
                throw new CustomException("total record upload and total record id update not equal ", "en");
            }
            for (int i=0 ;i < listFileUpdate.size();i++){
                if(null == listFileUpdate.get(i)){
                    continue;
                }
                if(null != listFileUpdate.get(i) && StringUtils.isEmpty(listIdUpdate.get(i).toString())){
                    FileUpload objectSave = handleObjectBeforeSave(listFileUpdate.get(i), userIdUpdate, assignmentRegisterInfo);
                    listFileUpload.add(objectSave);
                }else if(null != listFileUpdate.get(i) && !StringUtils.isEmpty(listIdUpdate.get(i).toString())){
                    FileUpload objectUpdate = handleObjectBeforeUpdate(listFileUpdate.get(i), userIdUpdate, assignmentRegisterInfo);
                    objectUpdate.setId(listIdUpdate.get(i));
                    fileMetadataRepository.updateFileUpload(objectUpdate);
                }
            }
            fileMetadataRepository.saveAll(listFileUpload);
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
    }


    public void deleteListFileUpload(List<Long> listIdDelete){
        if(null == listIdDelete || listIdDelete.size() == 0){
            return;
        }
        FileUpload objectDelete = new FileUpload();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        fileMetadataRepository.deleteFileUpload(objectDelete, listIdDelete);
    }

    public FileUpload handleObjectBeforeSave(MultipartFile file, String userIdRegister, AssignmentStudentRegister assignmentStudentRegister) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String tailFile = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        Blob blob = new SerialBlob(file.getBytes());
        FileUpload objectSave = new FileUpload();
        try{
            objectSave.setData(blob);
            objectSave.setFileType(tailFile);
            objectSave.setFileName(fileName);
            objectSave.setFileSize(file.getSize());
            objectSave.setCreateUser(userIdRegister);
            objectSave.setStatus(CommonUtil.STATUS_USE);
            objectSave.setAssignmentRegisterInfo(assignmentStudentRegister);
        }catch (Exception e){
            throw new Exception("Could not save file:" +fileName);
        }
        return objectSave;
    }

    public FileUpload handleObjectBeforeUpdate(MultipartFile file, String userIdUpdate, AssignmentStudentRegister assignmentStudentRegister) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String tailFile = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        Blob blob = new SerialBlob(file.getBytes());
        FileUpload objectUpdate = new FileUpload();
        try{
            objectUpdate.setData(blob);
            objectUpdate.setFileType(tailFile);
            objectUpdate.setFileName(fileName);
            objectUpdate.setFileSize(file.getSize());
            objectUpdate.setAssignmentRegisterInfo(assignmentStudentRegister);
            objectUpdate.setUpdateUser(userIdUpdate);
            objectUpdate.setUpdateAt(LocalDate.now());
        }catch (Exception e){
            throw new Exception("Could not save file:" +fileName);
        }
        return objectUpdate;
    }

    public void handleCaseInsertToStudentMapInstructor(User studentInfo, String statusAutoMap, Long instructorId,String updateUser, LocalDate nowDate){
        //Check if mapping auto -> insert to table student map instructor
        List<StudentMapInstructor> listStudentByStudentId = studentMapInstructorRepository.getStudentMapInstructorIdActiveByStudentId(studentInfo.getId());
        if(!CommonUtil.YES_VALUE.equals(statusAutoMap) && listStudentByStudentId.size() == 0){
            logger.info("Gia tri map la N va khong co trong danh sach insert truocs do:");
            StudentMapInstructor studentMapInstructor = new StudentMapInstructor();
            studentMapInstructor.setStudentInfo(studentInfo);
            studentMapInstructor.setCreateUser(updateUser);
            studentMapInstructor.setCreateAt(nowDate);
            studentMapInstructor.setStatus(CommonUtil.STATUS_USE);
            studentMapInstructorRepository.save(studentMapInstructor);
        }
        //Check if user not mapping auto + value instructorId not null -> update to table student map instructor
        if(CommonUtil.YES_VALUE.equals(statusAutoMap) && !StringUtils.isEmpty(instructorId)){
            //Find the value information of instructor
            User instructorInfo = getInfoInstructorById(instructorId);
            StudentMapInstructor studentMapInstructor = new StudentMapInstructor();
            if(ObjectUtils.isEmpty(instructorInfo)){
                logger.info("Not found instructor info with instructorId: "+instructorId.toString());
                throw new CustomException(CommonUtil.NOT_ACCEPT_EMPTY_VALUE, "en");
            }else{
                studentMapInstructor.setStudentInfo(studentInfo);
                studentMapInstructor.setCreateUser(updateUser);
                studentMapInstructor.setCreateAt(nowDate);
                studentMapInstructor.setInstructorInfo(instructorInfo);
                studentMapInstructor.setStatus(CommonUtil.STATUS_USE);
                //Check not exist data with studentID-> insert studentId and instructorId otherwise update instructorId by studentId
                if(listStudentByStudentId.size() == 0){
                    logger.info("Them moi vi khong tim thay thong tin student");
                    studentMapInstructorRepository.save(studentMapInstructor);
                }else{
                    logger.info("Cap nhat vi tim thay thong tin student");
                    studentMapInstructorRepository.updateStudentMapInstructorByStudentId(studentMapInstructor);
                }
            }
        }else if(CommonUtil.YES_VALUE.equals(statusAutoMap) && StringUtils.isEmpty(instructorId)){
            logger.info("Not found instructor info with instructorId: "+instructorId.toString());
            throw new CustomException(CommonUtil.NOT_ACCEPT_EMPTY_VALUE, "en");
        }
    }

    public User getInfoInstructorById(Long userid){
        User userInfo = userRepository.getValueUserByIdAndRole(userid, ERole.ROLE_INSTRUCTOR.toString());
        if(ObjectUtils.isEmpty(userInfo)){
            logger.info("Not found instructor info with userId: "+userid);
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        return userInfo;
    }

    public User getInfoStudentById(Long userid){
        User userInfo = userRepository.getValueUserByIdAndRole(userid, ERole.ROLE_USER.toString());
        if(ObjectUtils.isEmpty(userInfo)){
            logger.info("Not found student info with userId: "+userid);
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        return userInfo;
    }

    public FileUpload downloadFile(Long fileId) throws Exception {
        FileUpload fileObject =  fileMetadataRepository.findByFileIdToDownload(fileId);
        if(null ==fileObject){
            throw new CustomException("Not found object by fileId", "en");
        }
        InputStream inputStream = fileObject.getData().getBinaryStream();

        return fileObject;
    }
}
