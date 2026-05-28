package com.example.bloodbankmanagement.service.teacher;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentRegisterDTO;
import com.example.bloodbankmanagement.dto.objectRepository.ScoreSelectListAssignmentDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.AssignmentStudentRegisterDto;
import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import com.example.bloodbankmanagement.dto.service.student.AssignmentRegister;
import com.example.bloodbankmanagement.entity.CalculateAverageScore;
import com.example.bloodbankmanagement.entity.ScoreAssignment;
import com.example.bloodbankmanagement.repository.CalculateAverageScoreRepository;
import com.example.bloodbankmanagement.repository.ScoreAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreAssignmentInstructorServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ScoreAssignmentInstructorServiceImpl.class);
    private final ScoreAssignmentRepository scoreAssignmentRepository;
    private final ResponseCommon responseService;

    public SingleResponseDto<PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListInfo>> selectListScoreAssignment(ScoreAssignmentDto.ScoreAssignmentInstructorSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        CalculateAverageScore rateCalculateAverage = new CalculateAverageScore();
        rateCalculateAverage.setRateExam(4.0);
        rateCalculateAverage.setRateInstructor(6.0);
        rateCalculateAverage.setTotalRate(10.0);
        PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListNewInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<ScoreSelectListAssignmentDto> listDataFileMetadata = scoreAssignmentRepository.findListScoreManagementInstructorSite(request, pageable);

        pageAmtObject = ScoreAssignment.convertListObjectNewToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements(), rateCalculateAverage);
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }



    public SingleResponseDto<PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo>> selectListAssignmentReadyToAddScore(ScoreAssignmentDto.AssignmentInstructorSelectListNoPaginationInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        //Set default value of attribute approve type status to final approve
        request.setTypeApprove(CommonUtil.STATUS_APPROVE_FINAL);
        PageAmtListResponseDto<AssignmentStudentRegisterDto.AssignmentStudentRegisterListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        //Select list file upload
        List<AssignmentStudentRegisterDTO> listDataFileMetadata = scoreAssignmentRepository.findListScoreManagementNoPaginationInstructorSite(request);
        pageAmtObject = AssignmentRegister.convertListObjectToDto(listDataFileMetadata, Long.valueOf(listDataFileMetadata.size()));
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

}
