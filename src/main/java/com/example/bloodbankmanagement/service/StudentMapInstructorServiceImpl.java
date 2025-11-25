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
import com.example.bloodbankmanagement.dto.service.StudentMapInstructorDto;
import com.example.bloodbankmanagement.entity.StudentMapInstructor;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.StudentMapInstructorRepository;
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
public class StudentMapInstructorServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final StudentMapInstructorRepository studentMapInstructorRepository;
    private final UserRepository userRepository;
    private final ResponseCommon responseService;

    @Transactional
    public BasicResponseDto insertStudentMapInstructor(StudentMapInstructorDto.StudentMapInstructorInsertInfo request, String lang) {
        BasicResponseDto result;
        String userIdRegister = getValueUserIdRegister();
        StudentMapInstructor objectUpdate = new StudentMapInstructor();
        //Find the value information of user
        User studentInfo = getInfoStudentById(request.getStudentId());
        //Find the value information of instructor
        User instructorInfo = getInfoInstructorById(request.getInstructorId());
        objectUpdate.setStudentInfo(studentInfo);
        objectUpdate.setInstructorInfo(instructorInfo);
        objectUpdate.setCreateUser(userIdRegister);
        objectUpdate.setStatus(CommonUtil.STATUS_USE);
        objectUpdate.setCreateAt(LocalDate.now());
        studentMapInstructorRepository.save(objectUpdate);
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<StudentMapInstructorDto.StudentMapInstructorListInfo>> selectListStudentMapInstructor(StudentMapInstructorDto.StudentMapInstructorSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        String userId = isUserHaveRoleAdmin(request.getCreateUser());
        request.setCreateUser(userId);
        PageAmtListResponseDto<StudentMapInstructorDto.StudentMapInstructorListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<StudentMapInstructor> listDataFileMetadata = studentMapInstructorRepository.findListStudentMapInstructor(request, pageable);
        pageAmtObject = StudentMapInstructor.convertListObjectToDto(listDataFileMetadata.getContent());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<StudentMapInstructorDto.StudentMapInstructorSelectInfoResponse> selectStudentMapInstructor(StudentMapInstructorDto.StudentMapInstructorSelectInfo request, String lang){
        if(null == request || request.getStudentMapInstructorId().equals("") || null == request.getStudentMapInstructorId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<StudentMapInstructorDto.StudentMapInstructorSelectInfoResponse> selectObject = new SingleResponseDto<>();
        StudentMapInstructor dataFileMetadata = studentMapInstructorRepository.findByStudentMapInstructorId(request.getStudentMapInstructorId());
        StudentMapInstructorDto.StudentMapInstructorSelectInfoResponse objectResponse= new StudentMapInstructorDto.StudentMapInstructorSelectInfoResponse();
        if(null ==dataFileMetadata){
            objectResponse = null;
        }else{
            objectResponse = StudentMapInstructor.convertToDto(dataFileMetadata);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto updateStudentMapInstructor(StudentMapInstructorDto.StudentMapInstructorUpdateInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == request){
            throw new CustomException("the object send request not null ", "en");
        }
        //Find the value information of user
        User studentInfo = getInfoStudentById(request.getStudentId());
        //Find the value information of instructor
        User instructorInfo = getInfoInstructorById(request.getInstructorId());
        StudentMapInstructor objectUpdate = new StudentMapInstructor();
        objectUpdate.setId(request.getStudentMapInstructorId());
        objectUpdate.setInstructorInfo(instructorInfo);
        objectUpdate.setStudentInfo(studentInfo);
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(LocalDate.now());
        studentMapInstructorRepository.updateStudentMapInstructor(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deleteStudentMapInstructor(StudentMapInstructorDto.StudentMapInstructorDeleteInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        StudentMapInstructor objectDelete = new StudentMapInstructor();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        studentMapInstructorRepository.deleteStudentMapInstructor(objectDelete, listFileId.getListData());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }

    @Transactional
    public BasicResponseDto insertListStudentToOneInstructor(StudentMapInstructorDto.InsertListStudentWithOneInstructorInfo request){
        BasicResponseDto objectResponse;
        String userIdRegister = getValueUserIdRegister();
        //Find the value information of instructor
        User instructorInfo = getInfoInstructorById(request.getInstructorId());
        if(request.getListStudentId().size() <= 0){
            logger.info("Not found instructor info with userId: "+request.getListStudentId().toString());
            throw new CustomException(CommonUtil.NOT_ACCEPT_EMPTY_VALUE, "en");
        }
        //Check list user have any student not exist in table user
        Integer listUserFind = userRepository.countListUerInRangeAndByTypeRole(request.getListStudentId(), ERole.ROLE_USER.toString());
        if(request.getListStudentId().size() != listUserFind){
            logger.info("Not found instructor info with userId: "+request.getListStudentId().toString());
            throw new CustomException(CommonUtil.EXIST_DATA_USER_NOT_FOUND_IN_LIST, "en");
        }
        List<StudentMapInstructor> listStudentMapInsert = new ArrayList<>();
        for (Long studentMapInfo: request.getListStudentId()){
            StudentMapInstructor objectInsert = new StudentMapInstructor();
            User studentInfo = getInfoStudentById(studentMapInfo);
            objectInsert.setStudentInfo(studentInfo);
            objectInsert.setInstructorInfo(instructorInfo);
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
    public BasicResponseDto updateListStudentToNewInstructor(StudentMapInstructorDto.UpdateListStudentWithOneInstructorInfo request){
        BasicResponseDto objectResponse;
        String userIdUpdate = getValueUserIdRegister();
        //Find the value information of instructor
        User instructorInfo = getInfoInstructorById(request.getInstructorId());
        if(request.getListStudentId().size() <= 0){
            logger.info("Not found instructor info with userId: "+request.getListStudentId().toString());
            throw new CustomException(CommonUtil.NOT_ACCEPT_EMPTY_VALUE, "en");
        }
        //Check list user have any student not exist in table user
        Integer listUserFind = userRepository.countListUerInRangeAndByTypeRole(request.getListStudentId(), ERole.ROLE_USER.toString());
        if(request.getListStudentId().size() != listUserFind){
            logger.info("Not found instructor info with userId: "+request.getListStudentId().toString());
            throw new CustomException(CommonUtil.EXIST_DATA_USER_NOT_FOUND_IN_LIST, "en");
        }
        StudentMapInstructor objectUpdate = new StudentMapInstructor();
        objectUpdate.setInstructorInfo(instructorInfo);
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setCreateAt(LocalDate.now());
        studentMapInstructorRepository.updateListStudentWithInstructorId(objectUpdate, request.getListStudentId());
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

    public User getInfoStudentById(Long userid){
        User userInfo = userRepository.getValueUserByIdAndRole(userid, ERole.ROLE_USER.toString());
        if(ObjectUtils.isEmpty(userInfo)){
            logger.info("Not found instructor info with userId: "+userid);
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        return userInfo;
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
