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
import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import com.example.bloodbankmanagement.entity.AssignmentStudentRegister;
import com.example.bloodbankmanagement.entity.ScoreAssignment;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.AssignmentStudentRegisterRepository;
import com.example.bloodbankmanagement.repository.ScoreAssignmentRepository;
import com.example.bloodbankmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScoreAssignmentServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final ScoreAssignmentRepository scoreAssignmentRepository;
    private final UserRepository userRepository;
    private final AssignmentStudentRegisterRepository assignmentStudentRegisterRepository;
    private final ResponseCommon responseService;

    @Transactional
    public BasicResponseDto insertScoreAssignment(ScoreAssignmentDto.ScoreAssignmentInsertInfo request, String lang) {
        BasicResponseDto result;
        String userIdCreate = CommonUtil.getUsernameByToken();
        String userIdRegister = checkExistUser(userIdCreate);
        ScoreAssignment objectUpdate = new ScoreAssignment();
        objectUpdate.setScoreInstructor(request.getScoreInstructor());
        objectUpdate.setScoreExaminer(request.getScoreExaminer());
        objectUpdate.setScoreCritical(request.getScoreCritical());
        //Find the Assignment register
        AssignmentStudentRegister assignmentStudentRegisterInfo = assignmentStudentRegisterRepository.findByFileId(request.getAssignmentRegisterId());
        if(null == assignmentStudentRegisterInfo){
            throw new CustomException("Not found value request param assignmentRegisterId ", lang);
        }
        objectUpdate.setAssignmentRegisterInfo(assignmentStudentRegisterInfo);
        objectUpdate.setCreateUser(userIdRegister);
        objectUpdate.setStatus(CommonUtil.STATUS_USE);
        objectUpdate.setCreateAt(LocalDate.now());
        scoreAssignmentRepository.save(objectUpdate);
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListInfo>> selectListScoreAssignment(ScoreAssignmentDto.ScoreAssignmentSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<ScoreAssignment> listDataFileMetadata = scoreAssignmentRepository.findListScoreAssignment(request, pageable);
        pageAmtObject = ScoreAssignment.convertListObjectToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<ScoreAssignmentDto.ScoreAssignmentSelectInfoResponse> selectScoreAssignment(ScoreAssignmentDto.ScoreAssignmentSelectInfo request, String lang){
        if(null == request || request.getScoreAssignmentId().equals("") || null == request.getScoreAssignmentId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<ScoreAssignmentDto.ScoreAssignmentSelectInfoResponse> selectObject = new SingleResponseDto<>();
        ScoreAssignment dataFileMetadata = scoreAssignmentRepository.findByFileId(request.getScoreAssignmentId());
        ScoreAssignmentDto.ScoreAssignmentSelectInfoResponse objectResponse= new ScoreAssignmentDto.ScoreAssignmentSelectInfoResponse();
        if(null ==dataFileMetadata){
            objectResponse = null;
        }else{
            objectResponse = ScoreAssignment.convertToDto(dataFileMetadata);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto updateScoreAssignment(ScoreAssignmentDto.ScoreAssignmentUpdateInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == request){
            throw new CustomException("the object send request not null ", "en");
        }
        ScoreAssignment objectUpdate = new ScoreAssignment();
        objectUpdate.setId(request.getScoreAssignmentId());
        objectUpdate.setScoreExaminer(request.getScoreExaminer());
        objectUpdate.setScoreCritical(request.getScoreCritical());
        objectUpdate.setScoreInstructor(request.getScoreInstructor());
        //Find the Assignment register
        AssignmentStudentRegister assignmentStudentRegisterInfo = assignmentStudentRegisterRepository.findByFileId(request.getAssignmentRegisterId());
        if(null == assignmentStudentRegisterInfo){
            throw new CustomException("Not found value request param assignmentRegisterId ", lang);
        }
        objectUpdate.setAssignmentRegisterInfo(assignmentStudentRegisterInfo);
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(LocalDate.now());
        scoreAssignmentRepository.updateScoreAssignment(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deleteScoreAssignment(ScoreAssignmentDto.ScoreAssignmentDeleteInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        ScoreAssignment objectDelete = new ScoreAssignment();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        scoreAssignmentRepository.deleteScoreAssignment(objectDelete, listFileId.getListData());
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
