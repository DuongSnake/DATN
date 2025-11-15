package com.example.bloodbankmanagement.service;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.entity.*;
import com.example.bloodbankmanagement.repository.AssignmentStudentRegisterRepository;
import com.example.bloodbankmanagement.repository.StudentMapInstructorRepository;
import com.example.bloodbankmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentStudentRegisterServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final AssignmentStudentRegisterRepository assignmentStudentRegisterRepository;
    private final StudentMapInstructorRepository studentMapInstructorRepository;
    private final UserRepository userRepository;
    private final ResponseCommon responseService;

    @Transactional
    public BasicResponseDto insertAssignmentStudentRegister(AssignmentStudentRegisterDto.AssignmentStudentRegisterInsertInfo request, String lang) throws Exception {
        BasicResponseDto result;
        try{

        String userIdCreate = CommonUtil.getUsernameByToken();
        String userIdRegister = checkExistUser(userIdCreate);
        AssignmentStudentRegister objectUpdate = new AssignmentStudentRegister();
        objectUpdate.setAssignmentName(request.getAssignmentStudentRegisterName());
        StudentMapInstructor objectStudentMapInstructor = studentMapInstructorRepository.findByStudentMapInstructorId(request.getStudentMapInstructorId());
        if(null != objectStudentMapInstructor){
            objectUpdate.setStudentMapInstructor(objectStudentMapInstructor);
        }
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
        assignmentStudentRegisterRepository.save(objectUpdate);
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>> selectListAssignmentStudentRegister(AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        //Set value userId
        String userId = isUserHaveRoleAdmin(request.getCreateUser());
        request.setCreateUser(userId);
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<AssignmentStudentRegister> listDataFileMetadata = assignmentStudentRegisterRepository.findListAssignmentStudentRegister(request, pageable);
        pageAmtObject = AssignmentStudentRegister.convertListObjectToDto(listDataFileMetadata.getContent());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse> selectAssignmentStudentRegister(AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfo request, String lang){
        if(null == request || request.getAssignmentStudentRegisterId().equals("") || null == request.getAssignmentStudentRegisterId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse> selectObject = new SingleResponseDto<>();
        AssignmentStudentRegister dataFileMetadata = assignmentStudentRegisterRepository.findByFileId(request.getAssignmentStudentRegisterId());
        AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse objectResponse= new AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse();
        if(null ==dataFileMetadata){
            objectResponse = null;
        }else{
            objectResponse = AssignmentStudentRegister.convertToDto(dataFileMetadata);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto updateAssignmentStudentRegister(AssignmentStudentRegisterDto.AssignmentStudentRegisterUpdateInfo request, String lang) throws Exception {
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        try{
        if(null == request){
            throw new CustomException("the object send request not null ", "en");
        }

        AssignmentStudentRegister objectUpdate = new AssignmentStudentRegister();
        objectUpdate.setId(request.getAssignmentStudentRegisterId());
        objectUpdate.setAssignmentName(request.getAssignmentStudentRegisterName());
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(LocalDate.now());
        //Find info
        StudentMapInstructor objectStudentMapInstructor = studentMapInstructorRepository.findByStudentMapInstructorId(request.getStudentMapInstructorId());
        if(null != objectStudentMapInstructor){
            objectUpdate.setStudentMapInstructor(objectStudentMapInstructor);
        }
        //Xu lý thong tin file upload
        if(null != request.getFileUpload() && !"".equals(request.getFileUpload().getOriginalFilename()) ){
            //Xu ly file upload
            String fileName = StringUtils.cleanPath(request.getFileUpload().getOriginalFilename());
            String tailFile = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            Blob blob = new SerialBlob(request.getFileUpload().getBytes());
            objectUpdate.setFileName(fileName);
            objectUpdate.setFileType(tailFile);
            objectUpdate.setContentAssignment(blob);
            assignmentStudentRegisterRepository.updateAssignmentStudentRegister(objectUpdate);
        }else{
            assignmentStudentRegisterRepository.updateAssignmentStudentRegisterNoFile(objectUpdate);
        }
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deleteAssignmentStudentRegister(AssignmentStudentRegisterDto.AssignmentStudentRegisterDeleteInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        AssignmentStudentRegister objectDelete = new AssignmentStudentRegister();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        assignmentStudentRegisterRepository.deleteAssignmentStudentRegister(objectDelete, listFileId.getListAssignmentStudentRegisterId());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }

    @Transactional
    public BasicResponseDto reserveAssignmentStudentRegister(AssignmentStudentRegisterDto.AssignmentStudentRegisterDeleteInfo listFileId, String lang){
        //De do an o trang thai bao luu
        BasicResponseDto objectResponse;
        AssignmentStudentRegister objectDelete = new AssignmentStudentRegister();
        objectDelete.setIsApproved(CommonUtil.STATUS_RESERVE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        assignmentStudentRegisterRepository.reserveAssignmentStudentRegister(objectDelete, listFileId.getListAssignmentStudentRegisterId());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
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

}
