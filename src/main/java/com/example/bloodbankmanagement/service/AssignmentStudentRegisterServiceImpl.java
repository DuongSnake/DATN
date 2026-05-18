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
import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentRegisterDTO;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.entity.*;
import com.example.bloodbankmanagement.repository.*;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentStudentRegisterServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final AssignmentStudentRegisterRepository assignmentStudentRegisterRepository;
    private final StudentMapInstructorRepository studentMapInstructorRepository;
    private final PeriodAssignmentRepository periodAssignmentRepository;
    private final UserRepository userRepository;
    private final ResponseCommon responseService;

    @Transactional
    public BasicResponseDto insertAssignmentStudentRegister(AssignmentStudentRegisterDto.AssignmentStudentRegisterInsertInfo request, String lang) throws Exception {
        BasicResponseDto result;
        try{

        LocalDate nowDate = LocalDate.now();
        String userIdCreate = CommonUtil.getUsernameByToken();
        String userIdRegister = checkExistUser(userIdCreate);
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
        //Logic handle case insert student map instructor by value status autoMap
        handleCaseInsertToStudentMapInstructor(studentInfo, request.getStatusAutoMap(), request.getInstructorId(), userIdRegister, nowDate);
        objectUpdate.setStatusAutoMap(request.getStatusAutoMap());
        objectUpdate.setIsApproved(CommonUtil.STATUS_NOT_ACCEPT);//status default not accept
        objectUpdate.setCreateUser(userIdRegister);
        objectUpdate.setStatus(CommonUtil.STATUS_USE);
        objectUpdate.setCreateAt(nowDate);
        objectUpdate.setOldValueId(studentInfo.getId());
        assignmentStudentRegisterRepository.save(objectUpdate);
        //Handle for case change userId at case update
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>> selectListAssignmentStudentRegister(AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<AssignmentStudentRegisterDTO> listDataFileMetadata = assignmentStudentRegisterRepository.findListAssignmentStudentRegister(request, pageable);
        pageAmtObject = AssignmentStudentRegister.convertListObjectToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse> selectAssignmentStudentRegister(AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfo request, String lang){
        if(null == request || request.getAssignmentStudentRegisterId().equals("") || null == request.getAssignmentStudentRegisterId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterSelectInfoResponse> selectObject = new SingleResponseDto<>();
        AssignmentStudentRegisterDTO dataFileMetadata = assignmentStudentRegisterRepository.findByFileId(request.getAssignmentStudentRegisterId());
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
        LocalDate nowDate = LocalDate.now();
        BasicResponseDto messageResponse;
        try{
        if(null == request){
            throw new CustomException("the object send request not null ", "en");
        }
        AssignmentStudentRegister objectAssignStudentRegister =  assignmentStudentRegisterRepository.findByFileId2(request.getAssignmentStudentRegisterId());
        if(ObjectUtils.isEmpty(objectAssignStudentRegister)){
            logger.info("Not found assignment student register info with id: "+request.getAssignmentStudentRegisterId());
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }

        //Find user student have exist in database
        User studentInfo = getInfoStudentById(request.getStudentId());
        if(ObjectUtils.isEmpty(studentInfo)){
             logger.info("Not found assignment student register info with studentId: "+request.getStudentId());
             throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        AssignmentStudentRegister objectUpdate = new AssignmentStudentRegister();
        objectUpdate.setId(request.getAssignmentStudentRegisterId());
        objectUpdate.setStudentInfo(studentInfo);
        objectUpdate.setAssignmentName(request.getAssignmentStudentRegisterName());
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(nowDate);
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
            assignmentStudentRegisterRepository.updateAssignmentStudentRegister(objectUpdate);
        }else{
            assignmentStudentRegisterRepository.updateAssignmentStudentRegisterNoFile(objectUpdate);
        }
        //Handle case when value student Id will be change
        handleCaseUpdateToStudentMapInstructor(studentInfo, request.getStatusAutoMap(), request.getInstructorId(), request.getOldValueId(), userIdUpdate, nowDate);
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
        assignmentStudentRegisterRepository.deleteAssignmentStudentRegister(objectDelete, listFileId.getListData());
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
        assignmentStudentRegisterRepository.reserveAssignmentStudentRegister(objectDelete, listFileId.getListData());
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
        if(CommonUtil.ROLE_ADMIN.equals(userInfo.get().getRoleInfo().getName().toString())){
            isTypeAdmin = true;
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

    public void handleCaseInsertToStudentMapInstructor(User studentInfo, String statusAutoMap, Long instructorId,String updateUser, LocalDate nowDate){
        //Check if mapping auto -> insert to table student map instructor
        List<StudentMapInstructor> listStudentByStudentId = studentMapInstructorRepository.getStudentMapInstructorIdActiveByStudentId(studentInfo.getId());
        if(!CommonUtil.YES_VALUE.equals(statusAutoMap) && listStudentByStudentId.size() == 0){
            StudentMapInstructor studentMapInstructor = new StudentMapInstructor();
            studentMapInstructor.setStudentInfo(studentInfo);
            studentMapInstructor.setCreateUser(updateUser);
            studentMapInstructor.setCreateAt(nowDate);
            studentMapInstructorRepository.save(studentMapInstructor);
        }
        //Check if user not mapping auto + value instructorId not null -> update to table student map instructor
        if(CommonUtil.YES_VALUE.equals(statusAutoMap) && StringUtils.isEmpty(instructorId)){
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
                //Check not exist data with studentID-> insert studentId and instructorId otherwise update instructorId by studentId
                if(listStudentByStudentId.size() == 0){
                    studentMapInstructorRepository.save(studentMapInstructor);
                }else{
                    studentMapInstructorRepository.updateStudentMapInstructorByStudentId(studentMapInstructor);
                }
            }
        }
    }

    public void handleCaseUpdateToStudentMapInstructor(User studentInfo, String statusAutoMap, Long instructorId, Long valueOldId,String updateUser, LocalDate nowDate){
        if(studentInfo.getId().equals(valueOldId)){
            return;
        }
        //Check studentId already insert in table student map instructor
        StudentMapInstructor existInDbBefore = studentMapInstructorRepository.findByStudentMapStudentId(studentInfo.getId());
        //Check if mapping auto -> insert to table student map instructor(update new value)
        if(!CommonUtil.YES_VALUE.equals(statusAutoMap)){
            StudentMapInstructor studentMapInstructor = new StudentMapInstructor();
            studentMapInstructor.setStudentInfo(studentInfo);
            studentMapInstructor.setUpdateUser(updateUser);
            studentMapInstructor.setUpdateAt(nowDate);
            studentMapInstructorRepository.save(studentMapInstructor);
        }
        //Check if user not mapping auto + value instructorId not null -> update to table student map instructor
        if(CommonUtil.YES_VALUE.equals(statusAutoMap) && StringUtils.isEmpty(instructorId)){
            //Find the value information of instructor
            User instructorInfo = getInfoInstructorById(instructorId);
            StudentMapInstructor studentMapInstructor = new StudentMapInstructor();
            //When status is not auto map -> the value instructorId not be null
            if(ObjectUtils.isEmpty(instructorInfo)){
                logger.info("Not found instructor info with instructorId: "+instructorId.toString());
                throw new CustomException(CommonUtil.NOT_ACCEPT_EMPTY_VALUE, "en");
            }else{
                studentMapInstructor.setInstructorInfo(instructorInfo);
                studentMapInstructor.setStudentInfo(studentInfo);
                studentMapInstructor.setUpdateUser(updateUser);
                studentMapInstructor.setUpdateAt(nowDate);
                //Update information by studentId
                studentMapInstructorRepository.updateStudentMapInstructorByStudentId(studentMapInstructor);
            }
        }
        //Will update old value -> remove value instructorId
        String messageNote = "change value instructor by have request change from studentId:"+valueOldId +" to new value studentId:"+studentInfo.getId();
        StudentMapInstructor removeValueInstructorIdForOldValue = new StudentMapInstructor();
        removeValueInstructorIdForOldValue.setId(valueOldId);
        removeValueInstructorIdForOldValue.setNote(messageNote);
        removeValueInstructorIdForOldValue.setUpdateAt(nowDate);
        removeValueInstructorIdForOldValue.setUpdateUser(updateUser);
        studentMapInstructorRepository.removeOldMapInstructorByOldStudentId(removeValueInstructorIdForOldValue);

    }

}
