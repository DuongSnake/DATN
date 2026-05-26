package com.example.bloodbankmanagement.service.teacher;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.common.untils.ERole;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentRegisterDTO;
import com.example.bloodbankmanagement.dto.objectRepository.UserInfoDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.dto.service.StudentMapCriticalDto;
import com.example.bloodbankmanagement.dto.service.StudentMapCriticalDto;
import com.example.bloodbankmanagement.dto.service.UserDto;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegister;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegisterDto;
import com.example.bloodbankmanagement.entity.AssignmentStudentRegister;
import com.example.bloodbankmanagement.entity.StudentMapCritical;
import com.example.bloodbankmanagement.entity.StudentMapCritical;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.*;
import com.example.bloodbankmanagement.repository.student.AssignmentRegisterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentMapCriticalServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(StudentMapCriticalServiceImpl.class);
    private final StudentMapCriticalRepository studentMapCriticalRepository;
    private final AssignmentRegisterRepository assignmentRegisterRepository;
    private final UserRepository userRepository;
    private final ResponseCommon responseService;


    @Transactional
    public BasicResponseDto insertStudentMapCritical(StudentMapCriticalDto.StudentMapCriticalInsertInfo request, String lang) {
        BasicResponseDto result;
        if(null == request){
            throw new CustomException("the object send request not null ", "en");
        }
        String userIdRegister = getValueUserIdRegister();
        StudentMapCritical objectUpdate = new StudentMapCritical();
        //Find the value information of user
        User studentInfo = getInfoStudentById(request.getStudentId());
        StudentMapCritical checkExistDataBeforeInsert = studentMapCriticalRepository.findByStudentMapCriticalByStudentId(studentInfo.getId(), CommonUtil.STATUS_EXPIRE);
        //Find info user have exist in database with status active record or not
        if(!ObjectUtils.isEmpty(checkExistDataBeforeInsert)){
            throw new CustomException("Exist studentId storage in databse with status active before,please change ", "en");
        }
        //Find the value information of instructor
        User instructorInfo = getInfoCriticalById(request.getCriticalId());
        objectUpdate.setStudentInfo(studentInfo);
        objectUpdate.setCriticalTeacherInfo(instructorInfo);
        objectUpdate.setCreateUser(userIdRegister);
        objectUpdate.setStatus(CommonUtil.STATUS_USE);
        objectUpdate.setCreateAt(LocalDate.now());
        studentMapCriticalRepository.save(objectUpdate);
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }


    @Transactional
    public BasicResponseDto updateStudentMapCritical(StudentMapCriticalDto.StudentMapCriticalUpdateInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == request){
            throw new CustomException("the object send request not null ", "en");
        }
        //Find the value information of user
        User studentInfo = getInfoStudentById(request.getStudentId());
        //Find the value information of instructor
        StudentMapCritical checkExistDataBeforeInsert = studentMapCriticalRepository.findByStudentMapCriticalByStudentId(studentInfo.getId(), CommonUtil.STATUS_EXPIRE);
        //Find info user have exist in database with status active record or not
        if(!ObjectUtils.isEmpty(checkExistDataBeforeInsert)){
            throw new CustomException("Exist studentId storage in databse with status active before,please change ", "en");
        }
        User instructorInfo = getInfoCriticalById(request.getCriticalId());
        StudentMapCritical objectUpdate = new StudentMapCritical();
        objectUpdate.setId(request.getStudentMapCriticalId());
        objectUpdate.setCriticalTeacherInfo(instructorInfo);
        objectUpdate.setStudentInfo(studentInfo);
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(LocalDate.now());
        studentMapCriticalRepository.updateStudentMapCritical(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }
    
    public SingleResponseDto<PageAmtListResponseDto<StudentMapCriticalDto.StudentMapCriticalListInfo>> selectListStudentMapCritical(StudentMapCriticalDto.StudentMapCriticalSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<StudentMapCriticalDto.StudentMapCriticalListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<StudentMapCritical> listDataFileMetadata = studentMapCriticalRepository.findListStudentMapCritical(request, pageable);
        pageAmtObject = StudentMapCritical.convertListObjectToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<StudentMapCriticalDto.StudentMapCriticalSelectInfoResponse> selectStudentMapCritical(StudentMapCriticalDto.StudentMapCriticalSelectInfo request, String lang){
        if(null == request || request.getStudentMapCriticalId().equals("") || null == request.getStudentMapCriticalId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<StudentMapCriticalDto.StudentMapCriticalSelectInfoResponse> selectObject = new SingleResponseDto<>();
        StudentMapCritical dataFileMetadata = studentMapCriticalRepository.findByStudentMapCriticalById(request.getStudentMapCriticalId());
        StudentMapCriticalDto.StudentMapCriticalSelectInfoResponse objectResponse= new StudentMapCriticalDto.StudentMapCriticalSelectInfoResponse();
        if(null ==dataFileMetadata){
            objectResponse = null;
        }else{
            objectResponse = StudentMapCritical.convertToDto(dataFileMetadata);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto deleteStudentMapCritical(StudentMapCriticalDto.StudentMapCriticalDeleteInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        StudentMapCritical objectDelete = new StudentMapCritical();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        studentMapCriticalRepository.deleteStudentMapCritical(objectDelete, listFileId.getListData());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }
    //This API use for case get list critical by student id(for screen student map crititcal)
    public SingleResponseDto<PageAmtListResponseDto<UserDto.AllStudentByInstructorInfo>> selectListCriticalTeacherByStudentId(StudentMapCriticalDto.FindCriticalTeacherByStudentIdInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        //Select list file upload
        PageAmtListResponseDto<UserDto.AllStudentByInstructorInfo> pageAmtObject = new PageAmtListResponseDto<>();
        List<UserInfoDto> listDataFileMetadata = studentMapCriticalRepository.getListCriticalByStudentId(ERole.ROLE_INSTRUCTOR.toString(), request.getStudentId());
        pageAmtObject = User.convertListStudentByInstructor(listDataFileMetadata);
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    //This API use for case get list user to map with critical
    //Get list student with not register in table student map critical
    public SingleResponseDto<PageAmtListResponseDto<UserDto.UserSelectListInfo>> selectListStudentHaveStatusAssignmentIsWaitingFinalApprove() {
        //For case map student with instructor
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<UserDto.AllStudentByInstructorInfo> pageAmtObject = new PageAmtListResponseDto<>();
        List<UserInfoDto> listDataUser = studentMapCriticalRepository.getListUserHaveApproveTypeAssignment(ERole.ROLE_USER.toString(), CommonUtil.STATUS_EXPIRE, CommonUtil.STATUS_WAITING_FINAL);
        pageAmtObject = User.convertListStudentByInstructor(listDataUser);
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }
//Critical


    public SingleResponseDto<List<UserDto.UserSelectListInfo>> selectListStudentMapWithCriticalId(StudentMapCriticalDto.SelectListStudentByCriticalIdInfo request)  throws Exception{
        if(ObjectUtils.isEmpty(request)){
            throw new Exception("Request data not null");
        }
        if(StringUtils.isEmpty(request.getCriticalId())){
            throw new Exception("InstructorId not null");
        }
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<UserDto.AllStudentByInstructorInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        Page<StudentMapCritical> listDataUser = studentMapCriticalRepository.getListStudentByCriticalId(request, pageable);
        pageAmtObject = StudentMapCritical.convertListStudentByInstructor(listDataUser.getContent());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }


    //API select list assignment register from waiting final to final approve
    public SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>> selectListWaitngFinalApprove(AssignmentRegisterDto.AssignmentRegisterSelectListOfInstructorIdInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<AssignmentStudentRegisterDTO> listDataFileMetadata = studentMapCriticalRepository.findListAssignmentWaitingFinalApproveByInstructor(request, pageable);
        pageAmtObject = AssignmentRegister.convertListObjectToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }


    //API approve assignment register from waiting final to final approve
    @Transactional
    public BasicResponseDto approveFinalAssignmentStudentRegister(AssignmentRegisterDto.SendListRequestAssignmentInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        List<AssignmentStudentRegister> listObjectWaitingForApprove = assignmentRegisterRepository.findListWaitingFinalApproveAssignment(listFileId.getListData());
        if(null != listObjectWaitingForApprove && null != listFileId && listObjectWaitingForApprove.size() != listFileId.getListData().size()){
            throw new CustomException("Exist one or more than record not type approve not equal waiting final request");
        }
        AssignmentStudentRegister objectDelete = new AssignmentStudentRegister();
        objectDelete.setIsApproved(CommonUtil.STATUS_APPROVE_FINAL);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        assignmentRegisterRepository.changeStatusAssignmentRegister(objectDelete, listFileId.getListData());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return objectResponse;
    }

    public SingleResponseDto<List<UserDto.UserSelectListInfo>> selectListCriticalByStudentId(StudentMapCriticalDto.SelectListCriticalByStudentIdInfo request)  throws Exception{
        if(ObjectUtils.isEmpty(request)){
            throw new Exception("Request data not null");
        }
        if(StringUtils.isEmpty(request.getStudentId())){
            throw new Exception("InstructorId not null");
        }
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<UserDto.AllStudentByInstructorInfo> pageAmtObject = new PageAmtListResponseDto<>();
        List<StudentMapCritical> listDataUser = studentMapCriticalRepository.getListCriticalByStudentId(request);
        pageAmtObject = StudentMapCritical.convertListStudentByInstructor(listDataUser);
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
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

    public User getInfoStudentById(Long userid){
        User userInfo = userRepository.getValueUserByIdAndRole(userid, ERole.ROLE_USER.toString());
        if(ObjectUtils.isEmpty(userInfo)){
            logger.info("Not found instructor info with userId: "+userid);
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        return userInfo;
    }

    public User getInfoCriticalById(Long userid){
        User userInfo = userRepository.getValueUserByIdAndRole(userid, ERole.ROLE_INSTRUCTOR.toString());
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
