package com.example.bloodbankmanagement.service;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.common.untils.ERole;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.PeriodAssignmentDto;
import com.example.bloodbankmanagement.entity.*;
import com.example.bloodbankmanagement.repository.AdmissionPeriodRepository;
import com.example.bloodbankmanagement.repository.MajorRepository;
import com.example.bloodbankmanagement.repository.PeriodAssignmentRepository;
import com.example.bloodbankmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PeriodAssignmentServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final PeriodAssignmentRepository periodAssignmentRepository;
    private final AdmissionPeriodRepository admissionPeriodRepository;
    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    private final ResponseCommon responseService;

    @Transactional
    public BasicResponseDto insertPeriodAssignment(PeriodAssignmentDto.PeriodAssignmentInsertInfo request, String lang) {
        BasicResponseDto result;
        String userIdCreate = CommonUtil.getUsernameByToken();
        String userIdRegister = checkExistUser(userIdCreate);
        PeriodAssignment objectUpdate = new PeriodAssignment();
        //Find the major register period
        Major objectMajor = majorRepository.findByFileId(request.getMajorId());
        if(null == objectMajor){
            throw new CustomException("Not found value request param ", lang);
        }
        objectUpdate.setMajorInfo(objectMajor);
        //Find the admission period
        AdmissionPeriod objectAdmissionPeriod = admissionPeriodRepository.findByFileId(request.getAdmissionPeriodId());
        if(null == objectAdmissionPeriod){
            throw new CustomException("Not found value request param ", lang);
        }
        objectUpdate.setAdmissionPeriodInfo(objectAdmissionPeriod);
        objectUpdate.setTotalFileRequest(3);//Default 3 file docs,source code,powerpoint
        objectUpdate.setNote(request.getNote());
        objectUpdate.setStartPeriod(request.getStartPeriod());
        objectUpdate.setEndPeriod(request.getEndPeriod());
        objectUpdate.setCreateUser(userIdRegister);
        objectUpdate.setStatus(CommonUtil.STATUS_USE);
        objectUpdate.setCreateAt(LocalDate.now());
        periodAssignmentRepository.save(objectUpdate);
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<PeriodAssignmentDto.PeriodAssignmentListInfo>> selectListPeriodAssignment(PeriodAssignmentDto.PeriodAssignmentSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        //Set value userId
        String userId = isUserHaveRoleAdmin(request.getCreateUser());
        request.setCreateUser(userId);
        PageAmtListResponseDto<PeriodAssignmentDto.PeriodAssignmentListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<PeriodAssignment> listDataFileMetadata = periodAssignmentRepository.findListPeriodAssignment(request, pageable);
        pageAmtObject = PeriodAssignment.convertListObjectToDto(listDataFileMetadata.getContent());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<PeriodAssignmentDto.PeriodAssignmentSelectInfoResponse> selectPeriodAssignment(PeriodAssignmentDto.PeriodAssignmentSelectInfo request, String lang){
        if(null == request || request.getPeriodAssignmentId().equals("") || null == request.getPeriodAssignmentId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<PeriodAssignmentDto.PeriodAssignmentSelectInfoResponse> selectObject = new SingleResponseDto<>();
        PeriodAssignment dataFileMetadata = periodAssignmentRepository.findByFileId(request.getPeriodAssignmentId());
        PeriodAssignmentDto.PeriodAssignmentSelectInfoResponse objectResponse= new PeriodAssignmentDto.PeriodAssignmentSelectInfoResponse();
        if(null ==dataFileMetadata){
            objectResponse = null;
        }else{
            objectResponse = PeriodAssignment.convertToDto(dataFileMetadata);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto updatePeriodAssignment(PeriodAssignmentDto.PeriodAssignmentUpdateInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == request){
            throw new CustomException("the object send request not null ", "en");
        }
        PeriodAssignment objectUpdate = new PeriodAssignment();
        //Find the major register period
        Major objectMajor = majorRepository.findByFileId(request.getMajorId());
        if(null == objectMajor){
            throw new CustomException("Not found value request param ", lang);
        }
        objectUpdate.setMajorInfo(objectMajor);
        //Find the admission period
        AdmissionPeriod objectAdmissionPeriod = admissionPeriodRepository.findByFileId(request.getAdmissionPeriodId());
        if(null == objectAdmissionPeriod){
            throw new CustomException("Not found value request param ", lang);
        }
        objectUpdate.setAdmissionPeriodInfo(objectAdmissionPeriod);
        objectUpdate.setId(request.getPeriodAssignmentId());
        objectUpdate.setNote(request.getNote());
        objectUpdate.setStartPeriod(request.getStartPeriod());
        objectUpdate.setEndPeriod(request.getEndPeriod());
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(LocalDate.now());
        periodAssignmentRepository.updatePeriodAssignment(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deletePeriodAssignment(PeriodAssignmentDto.PeriodAssignmentDeleteInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        PeriodAssignment objectDelete = new PeriodAssignment();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        periodAssignmentRepository.deletePeriodAssignment(objectDelete, listFileId.getListPeriodAssignmentId());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }

    @Transactional
    public BasicResponseDto insertListPeriodAssignmentByMajorId(PeriodAssignmentDto.InsertListPeriodAssignmentByMajorInfo request, String lang){
        BasicResponseDto objectResponse;
        List<PeriodAssignment> listPeriodAssignment = new ArrayList<>();
        //Find the admission period
        AdmissionPeriod objectAdmissionPeriod = admissionPeriodRepository.findByFileId(request.getAdmissionPeriodId());
        if(null == objectAdmissionPeriod){
            throw new CustomException("Not found value request param ", lang);
        }
        List<Major> listMajor = majorRepository.findMajorInListId(request.getListMajorId());
        if(request.getListMajorId().size() != listMajor.size()){
            throw new CustomException("In list major Id have value not exist in database,please check attribute listMajorId ", lang);
        }
        for (Major majorInfo: listMajor){
            PeriodAssignment objectInsert = new PeriodAssignment();
            objectInsert.setTotalFileRequest(3);//Default 3 file docs,source code,powerpoint
            objectInsert.setStartPeriod(request.getStartPeriod());
            objectInsert.setEndPeriod(request.getEndPeriod());
            objectInsert.setStatus(CommonUtil.STATUS_USE);
            objectInsert.setUpdateAt(LocalDate.now());
            objectInsert.setMajorInfo(majorInfo);
            objectInsert.setAdmissionPeriodInfo(objectAdmissionPeriod);
            objectInsert.setUpdateUser(CommonUtil.getUsernameByToken());
            objectInsert.setNote(request.getNote());
            listPeriodAssignment.add(objectInsert);
        }
        periodAssignmentRepository.saveAll(listPeriodAssignment);
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
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
