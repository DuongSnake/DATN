package com.example.bloodbankmanagement.service.student;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegister;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegisterDto;
import com.example.bloodbankmanagement.entity.AssignmentStudentRegister;
import com.example.bloodbankmanagement.entity.FileUpload;
import com.example.bloodbankmanagement.entity.StudentMapInstructor;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.FileMetadataRepository;
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

    public SingleResponseDto<PageAmtListResponseDto<AssignmentRegisterDto.AssignmentRegisterListInfo>> saveAllFileUpload(AssignmentRegisterDto.AssignmentRegisterSelectListInfo request){
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
    @Transactional
    public BasicResponseDto updateAssignmentRegister(AssignmentRegisterDto.AssignmentInsertListFileUploadInsertInfo request, String lang) throws Exception {
        String userIdRegister = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        List<FileUpload> listFileUpload = new ArrayList<>();
        try{
            if(null == request){
                throw new CustomException("the object send request not null ", "en");
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
//                    objectSave.setAssignmentRegisterInfo(assignmentStudentRegister);
                    listFileUpload.add(objectSave);
                }catch (Exception e){
                    throw new Exception("Could not save file:" +fileName);
                }
            }
            fileMetadataRepository.saveAll(listFileUpload);
            messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
        return messageResponse;
    }



}
