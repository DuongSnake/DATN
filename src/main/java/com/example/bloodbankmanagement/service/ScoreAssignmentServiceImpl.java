package com.example.bloodbankmanagement.service;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentRegisterDTO;
import com.example.bloodbankmanagement.dto.objectRepository.ScoreSelectListAssignmentDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegister;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegisterDto;
import com.example.bloodbankmanagement.entity.*;
import com.example.bloodbankmanagement.repository.AssignmentStudentRegisterRepository;
import com.example.bloodbankmanagement.repository.CalculateAverageScoreRepository;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScoreAssignmentServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final ScoreAssignmentRepository scoreAssignmentRepository;
    private final UserRepository userRepository;
    private final AssignmentStudentRegisterRepository assignmentStudentRegisterRepository;
    private final ResponseCommon responseService;
    private final CalculateAverageScoreRepository calculateAverageScoreRepository;

    @Transactional
    public BasicResponseDto insertScoreAssignment(ScoreAssignmentDto.ScoreAssignmentInsertInfo request, String lang) {
        BasicResponseDto result;
        if(null == request){
            throw new CustomException("the object send request not null ", "en");
        }
        //Check status assignment to set score
        AssignmentStudentRegister checkExist = assignmentStudentRegisterRepository.assignmentHaveTypeFinalApproveOrNot(request.getAssignmentRegisterId(), CommonUtil.STATUS_APPROVE_FINAL);
        if(ObjectUtils.isEmpty(checkExist)){
            throw new CustomException("The assignment status approve not final approve->Please choose another assignment and try again ", lang);
        }
        String userIdCreate = CommonUtil.getUsernameByToken();
        String userIdRegister = checkExistUser(userIdCreate);
        ScoreAssignment objectUpdate = new ScoreAssignment();
        objectUpdate.setScoreInstructor(request.getScoreInstructor());
        objectUpdate.setScoreExaminer(request.getScoreExaminer());
        objectUpdate.setScoreCritical(request.getScoreCritical());
        //Find the Assignment register
        AssignmentStudentRegister assignmentStudentRegisterInfo = assignmentStudentRegisterRepository.findByFileId2(request.getAssignmentRegisterId());
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
        //Check status assignment to set score
        AssignmentStudentRegister checkExist = assignmentStudentRegisterRepository.assignmentHaveTypeFinalApproveOrNot(request.getAssignmentRegisterId(), CommonUtil.STATUS_APPROVE_FINAL);
        if(ObjectUtils.isEmpty(checkExist)){
            throw new CustomException("The assignment status approve not final approve->Please choose another assignment and try again ", lang);
        }
        ScoreAssignment objectUpdate = new ScoreAssignment();
        objectUpdate.setId(request.getScoreAssignmentId());
        objectUpdate.setScoreExaminer(request.getScoreExaminer());
        objectUpdate.setScoreCritical(request.getScoreCritical());
        objectUpdate.setScoreInstructor(request.getScoreInstructor());
        //Find the Assignment register
        AssignmentStudentRegister assignmentStudentRegisterInfo = assignmentStudentRegisterRepository.findByFileId2(request.getAssignmentRegisterId());
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


    public SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>> selectListAssignmentReadyToAddScore(ScoreAssignmentDto.ListAssignmentRegisterIsFinalApproveByPeriodIdInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        //Set default value of attribute approve type status to final approve
        request.setTypeApprove(CommonUtil.STATUS_APPROVE_FINAL);
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        //Select list file upload
        List<AssignmentStudentRegisterDTO> listDataFileMetadata = scoreAssignmentRepository.findListAssignmentRegisterIsFinalApproveByPeriodId(request);
        pageAmtObject = AssignmentRegister.convertListObjectToDto(listDataFileMetadata, Long.valueOf(listDataFileMetadata.size()));
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListInfo>> selectListNewScoreAssignment(ScoreAssignmentDto.ScoreAssignmentNewSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        if(null == request){
            throw new CustomException("the object send request not null ", "en");
        }
        CalculateAverageScore rateCalculateAverage = new CalculateAverageScore();
        rateCalculateAverage.setRateExam(4.0);
        rateCalculateAverage.setRateExam(6.0);
        rateCalculateAverage.setTotalRate(10.0);
        PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListNewInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<ScoreSelectListAssignmentDto> listDataFileMetadata = scoreAssignmentRepository.findListNewScoreManagement(request, pageable);
        if(null != request && null != request.getAdmissionPeriodId()){
            rateCalculateAverage = calculateAverageScoreRepository.findByPeriodAssignmentId(request.getAdmissionPeriodId());
            if(null != rateCalculateAverage && null != rateCalculateAverage.getRateExam() && null != rateCalculateAverage.getRateInstructor() && null != rateCalculateAverage.getTotalRate()){
                rateCalculateAverage.setRateExam(rateCalculateAverage.getRateExam());
                rateCalculateAverage.setRateExam(rateCalculateAverage.getRateInstructor());
                rateCalculateAverage.setTotalRate(rateCalculateAverage.getTotalRate());
            }
        }
        pageAmtObject = ScoreAssignment.convertListObjectNewToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements(), rateCalculateAverage);
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }
}
