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
import com.example.bloodbankmanagement.dto.service.InstructorMapPeriodAssignmentDto;
import com.example.bloodbankmanagement.entity.PeriodAssignment;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.InstructorMapPeriodAssignment;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.InstructorMapPeriodAssignmentRepository;
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
public class InstructorMapPeriodAssignmentServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final InstructorMapPeriodAssignmentRepository studentMapInstructorRepository;
    private final UserRepository userRepository;
    private final PeriodAssignmentRepository periodAssignmentRepository;
    private final ResponseCommon responseService;

    @Transactional
    public BasicResponseDto insertInstructorMapPeriodAssignment(InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentInsertInfo request, String lang) {
        BasicResponseDto result;
        String userIdRegister = getValueUserIdRegister();
        InstructorMapPeriodAssignment objectUpdate = new InstructorMapPeriodAssignment();
        //Find the value information of instructor
        User instructorInfo = getInfoInstructorById(request.getInstructorId());
        //Find the value period assignment
        PeriodAssignment periodAssignment = periodAssignmentRepository.findByFileId(request.getPeriodAssignmentId());
        objectUpdate.setInstructorInfo(instructorInfo);
        objectUpdate.setPeriodAssignmentInfo(periodAssignment);
        objectUpdate.setCreateUser(userIdRegister);
        objectUpdate.setStatus(CommonUtil.STATUS_USE);
        objectUpdate.setCreateAt(LocalDate.now());
        studentMapInstructorRepository.save(objectUpdate);
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentListInfo>> selectListInstructorMapPeriodAssignment(InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<InstructorMapPeriodAssignment> listDataFileMetadata = studentMapInstructorRepository.findListInstructorMapPeriodAssignment(request, pageable);
        pageAmtObject = InstructorMapPeriodAssignment.convertListObjectToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectInfoResponse> selectInstructorMapPeriodAssignment(InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectInfo request, String lang){
        if(null == request || request.getInstructorMapPeriodAssignmentId().equals("") || null == request.getInstructorMapPeriodAssignmentId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectInfoResponse> selectObject = new SingleResponseDto<>();
        InstructorMapPeriodAssignment dataFileMetadata = studentMapInstructorRepository.findByInstructorMapPeriodAssignmentId(request.getInstructorMapPeriodAssignmentId());
        InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectInfoResponse objectResponse= new InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectInfoResponse();
        if(null ==dataFileMetadata){
            objectResponse = null;
        }else{
            objectResponse = InstructorMapPeriodAssignment.convertToDto(dataFileMetadata);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto updateInstructorMapPeriodAssignment(InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentUpdateInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == request){
            throw new CustomException("the object send request not null ", "en");
        }
        //Find the value information of instructor
        User instructorInfo = getInfoInstructorById(request.getInstructorId());
        //Find the value period assignment
        PeriodAssignment periodAssignment = periodAssignmentRepository.findByFileId(request.getPeriodAssignmentId());
        InstructorMapPeriodAssignment objectUpdate = new InstructorMapPeriodAssignment();
        objectUpdate.setId(request.getInstructorMapPeriodAssignmentId());
        objectUpdate.setInstructorInfo(instructorInfo);
        objectUpdate.setPeriodAssignmentInfo(periodAssignment);
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(LocalDate.now());
        studentMapInstructorRepository.updateInstructorMapPeriodAssignment(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deleteInstructorMapPeriodAssignment(InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentDeleteInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        InstructorMapPeriodAssignment objectDelete = new InstructorMapPeriodAssignment();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        studentMapInstructorRepository.deleteInstructorMapPeriodAssignment(objectDelete, listFileId.getListData());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }

    @Transactional
    public BasicResponseDto insertListInstructorToOneInstructor(InstructorMapPeriodAssignmentDto.InsertListPeriodAssignmentWithOneInstructorInfo request){
        BasicResponseDto objectResponse;
        String userIdRegister = getValueUserIdRegister();
        //Find the value period assignment
        PeriodAssignment periodAssignmentInfo = periodAssignmentRepository.findByFileId(request.getPeriodAssignmentId());
        if(null == periodAssignmentInfo){
            logger.info("Not found instructor info with userId: "+request.getPeriodAssignmentId().toString());
            throw new CustomException(CommonUtil.NOT_ACCEPT_EMPTY_VALUE, "en");
        }
        //Check list user have any student not exist in table user
        Integer listInstructorFind = userRepository.countListUerInRangeAndByTypeRole(request.getListInstructorId(), ERole.ROLE_MODERATOR.toString());
        if(request.getListInstructorId().size() != listInstructorFind){
            logger.info("Not found instructor info with userId: "+request.getListInstructorId().toString());
            throw new CustomException(CommonUtil.EXIST_DATA_USER_NOT_FOUND_IN_LIST, "en");
        }
        List<InstructorMapPeriodAssignment> listStudentMapInsert = new ArrayList<>();
        for (Long instructorId: request.getListInstructorId()){
            InstructorMapPeriodAssignment objectInsert = new InstructorMapPeriodAssignment();
            User studentInfo = getInfoInstructorById(instructorId);
            objectInsert.setPeriodAssignmentInfo(periodAssignmentInfo);
            objectInsert.setInstructorInfo(studentInfo);
            objectInsert.setCreateUser(userIdRegister);
            objectInsert.setStatus(CommonUtil.STATUS_USE);
            objectInsert.setCreateAt(LocalDate.now());
            listStudentMapInsert.add(objectInsert);
        }
        studentMapInstructorRepository.saveAll(listStudentMapInsert);
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);

        return objectResponse;
    }

    @Transactional
    public BasicResponseDto updateListInstructorToNewInstructor(InstructorMapPeriodAssignmentDto.UpdateListPeriodAssignmentWithOneInstructorInfo request){
        BasicResponseDto objectResponse;
        String userIdUpdate = getValueUserIdRegister();
        //Find the value period assignment
        PeriodAssignment periodAssignmentInfo = periodAssignmentRepository.findByFileId(request.getPeriodAssignmentId());
        if(null == periodAssignmentInfo){
            logger.info("Not found instructor info with userId: "+request.getPeriodAssignmentId().toString());
            throw new CustomException(CommonUtil.NOT_ACCEPT_EMPTY_VALUE, "en");
        }
        //Check list period assignment
        Integer listInstructorFind = userRepository.countListUerInRangeAndByTypeRole(request.getListInstructorId(), ERole.ROLE_MODERATOR.toString());
        if(request.getListInstructorId().size() != listInstructorFind){
            logger.info("Not found instructor info with userId: "+request.getListInstructorId().toString());
            throw new CustomException(CommonUtil.EXIST_DATA_USER_NOT_FOUND_IN_LIST, "en");
        }
        InstructorMapPeriodAssignment objectUpdate = new InstructorMapPeriodAssignment();
        objectUpdate.setPeriodAssignmentInfo(periodAssignmentInfo);
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setCreateAt(LocalDate.now());
        studentMapInstructorRepository.updateListStudentWithInstructorId(objectUpdate, request.getListInstructorId());
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

    public User getInfoInstructorById(Long userid){
        User userInfo = userRepository.getValueUserByIdAndRole(userid, ERole.ROLE_MODERATOR.toString());
        if(ObjectUtils.isEmpty(userInfo)){
            logger.info("Not found instructor info with userId: "+userid);
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        return userInfo;
    }

    public String getValueUserIdRegister(){
        String userIdCreate = CommonUtil.getUsernameByToken();
        String userIdRegister = checkExistUser(userIdCreate);
        return userIdRegister;
    }
}
