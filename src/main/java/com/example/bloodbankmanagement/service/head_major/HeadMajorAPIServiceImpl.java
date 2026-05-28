package com.example.bloodbankmanagement.service.head_major;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.ScoreSelectListAssignmentDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import com.example.bloodbankmanagement.entity.CalculateAverageScore;
import com.example.bloodbankmanagement.entity.ScoreAssignment;
import com.example.bloodbankmanagement.repository.ScoreAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeadMajorAPIServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(HeadMajorAPIServiceImpl.class);
    private final ScoreAssignmentRepository scoreAssignmentRepository;
    private final ResponseCommon responseService;

    public SingleResponseDto<PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListNewInfo>> selectListScoreAssignmentHeadRoomSite(ScoreAssignmentDto.ScoreAssignmentMajorSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        CalculateAverageScore rateCalculateAverage = new CalculateAverageScore();
        rateCalculateAverage.setRateExam(4.0);
        rateCalculateAverage.setRateInstructor(6.0);
        rateCalculateAverage.setTotalRate(10.0);
        PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListNewInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<ScoreSelectListAssignmentDto> listDataFileMetadata = scoreAssignmentRepository.findListScoreManagementHeadMajorSite(request, pageable);
        pageAmtObject = ScoreAssignment.convertListObjectNewToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements(), rateCalculateAverage);
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }
}
