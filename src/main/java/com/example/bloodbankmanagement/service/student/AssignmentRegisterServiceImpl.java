package com.example.bloodbankmanagement.service.student;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.AuthenticationException;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.ListResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.UploadFileDto;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegister;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegisterDto;
import com.example.bloodbankmanagement.entity.*;
import com.example.bloodbankmanagement.repository.FileMetadataRepository;
import com.example.bloodbankmanagement.repository.PeriodAssignmentRepository;
import com.example.bloodbankmanagement.repository.StudentMapInstructorRepository;
import com.example.bloodbankmanagement.repository.UserRepository;
import com.example.bloodbankmanagement.repository.student.AssignmentRegisterRepository;
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

    @Transactional
    public BasicResponseDto insertAssignmentRegister(AssignmentRegisterDto.AssignmentRegisterInsertInfo request, String lang) throws Exception {
        BasicResponseDto result;
        try{
            String userIdRegister = CommonUtil.getUsernameByToken();
            AssignmentStudentRegister objectUpdate = new AssignmentStudentRegister();
            objectUpdate.setAssignmentName(request.getAssignmentRegisterName());
            //Tim thong tin giao vien map sinh vien

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
            objectUpdate.setIsApproved(CommonUtil.STATUS_NOT_ACCEPT);//status default not accept
            objectUpdate.setCreateUser(userIdRegister);
            objectUpdate.setStatus(CommonUtil.STATUS_USE);
            objectUpdate.setCreateAt(LocalDate.now());
            assignmentRegisterRepository.save(objectUpdate);
            result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<AssignmentRegisterDto.AssignmentRegisterListInfo>> selectListAssignmentRegister(AssignmentRegisterDto.AssignmentRegisterSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        String userIdRegister = CommonUtil.getUsernameByToken();
        //Only find the list student upload in file
        request.setRegUser(userIdRegister);
        PageAmtListResponseDto<AssignmentRegisterDto.AssignmentRegisterListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<AssignmentStudentRegister> listDataFileMetadata = assignmentRegisterRepository.findListAssignmentRegister(request, pageable);
        pageAmtObject = AssignmentRegister.convertListObjectToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<AssignmentRegisterDto.AssignmentRegisterSelectInfoResponse> selectAssignmentRegister(AssignmentRegisterDto.AssignmentRegisterSelectInfo request, String lang){
        if(null == request || request.getAssignmentRegisterId().equals("") || null == request.getAssignmentRegisterId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<AssignmentRegisterDto.AssignmentRegisterSelectInfoResponse> selectObject = new SingleResponseDto<>();
        AssignmentStudentRegister dataFileMetadata = assignmentRegisterRepository.findByFileId(request.getAssignmentRegisterId());
        AssignmentRegisterDto.AssignmentRegisterSelectInfoResponse objectResponse= new AssignmentRegisterDto.AssignmentRegisterSelectInfoResponse();
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
        try{
            if(null == request){
                throw new CustomException("the object send request not null ", "en");
            }
            AssignmentStudentRegister objectUpdate = new AssignmentStudentRegister();
            objectUpdate.setId(request.getAssignmentRegisterId());
            objectUpdate.setAssignmentName(request.getAssignmentRegisterName());
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

    public SingleResponseDto<PageAmtListResponseDto<AssignmentRegisterDto.AssignmentRegisterListInfo>> findListAssignmentRegisterIsApprove(AssignmentRegisterDto.AssignmentRegisterSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        String userIdRegister = CommonUtil.getUsernameByToken();
        //Find the customer by token
        Long valueId = getIdByUserName(userIdRegister);
        //Only find the list student upload in file
        request.setRegUser(userIdRegister);
        PageAmtListResponseDto<AssignmentRegisterDto.AssignmentRegisterListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<AssignmentStudentRegister> listDataFileMetadata = assignmentRegisterRepository.findListAssignmentRegisterIsApprove(request, pageable);
        pageAmtObject = AssignmentRegister.convertListObjectToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public ListResponseDto<UploadFileDto.UploadFileListInfo> findListFileUploadByAssignmentIdApprove(AssignmentRegisterDto.AssignmentRegisterSelectInfo request){
        ListResponseDto<UploadFileDto.UploadFileListInfo> objectResponse = new ListResponseDto<>();
        //Select list file upload
        List<FileUpload> listDataFileMetadata = fileMetadataRepository.findListFileUpload(request.getAssignmentRegisterId());
        List<UploadFileDto.UploadFileListInfo> listDataFileMetadataDto = FileUpload.convertListObjectToDtoUserSite(listDataFileMetadata);
        objectResponse = responseService.getListResponseMessage(listDataFileMetadataDto,CommonUtil.successValue, CommonUtil.querySuccess);
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
                AssignmentStudentRegister assignmentStudentRegister= assignmentRegisterRepository.findByFileId(request.getAssignmentRegisterId());
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
    public BasicResponseDto updateFileAssignmentRegister(AssignmentRegisterDto.AssignmentFileUploadInfo request, String lang) throws Exception {
        BasicResponseDto messageResponse;
        try{
            if(null == request){
                throw new CustomException("the object send request not null ", "en");
            }
            AssignmentStudentRegister assignmentStudentRegister= assignmentRegisterRepository.findByFileId(request.getAssignmentRegisterId());
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
            //Check list insert list file
            insertListFileUpload(request.getListFileInsert(),assignmentStudentRegister);
            //Check list update list file
            updateListFileUpload(request.getListIdUpdate(), request.getListFileUpdate() ,assignmentStudentRegister);
            //Check list delete list file
            deleteListFileUpload(request.getListIdUpdate());
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
        //Insert list id
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }


    public void insertListFileUpload(MultipartFile listFileInsert[], AssignmentStudentRegister assignmentRegisterInfo) throws Exception {
        String userIdRegister = CommonUtil.getUsernameByToken();
        List<FileUpload> listFileUpload = new ArrayList<>();
        try{
            if(null == listFileInsert){
                throw new CustomException("the object send request not null ", "en");
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


    public void updateListFileUpload(List<Long> listIdUpdate,MultipartFile listFileUpdate[], AssignmentStudentRegister assignmentRegisterInfo) throws Exception {
        String userIdUpdate = CommonUtil.getUsernameByToken();
        try{
            if(null == listFileUpdate || null == listIdUpdate){
                throw new CustomException("the object send request not null ", "en");
            }
            if(listFileUpdate.length != listIdUpdate.size()){
                throw new CustomException("total record upload and total record id update not equal ", "en");
            }
            for (int i=0 ;i < listFileUpdate.length;i++){
                FileUpload objectUpdate = handleObjectBeforeUpdate(listFileUpdate[i], userIdUpdate, assignmentRegisterInfo);
                objectUpdate.setId(listIdUpdate.get(i));
                fileMetadataRepository.updateFileUpload(objectUpdate);
            }
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
    }


    public void deleteListFileUpload(List<Long> listIdDelete){
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
}
