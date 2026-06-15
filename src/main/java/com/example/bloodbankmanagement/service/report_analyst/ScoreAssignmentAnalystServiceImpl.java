package com.example.bloodbankmanagement.service.report_analyst;

import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.ScoreAssignmentAnalystDto;
import com.example.bloodbankmanagement.repository.report_month_analist.ScoreAssignmentAnalystRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreAssignmentAnalystServiceImpl {
    private final ResponseCommon responseService;
    private final MessageSource messageSource;
    private final ScoreAssignmentAnalystRepository scoreAssignmentAnalystRepository;

    public SingleResponseDto<PageAmtListResponseDto<ScoreAssignmentAnalystDto.ScoreAssignmentAnalystSelectListResponse>> selectAllTotalData(ScoreAssignmentAnalystDto.ScoreAssignmentAnalystSelectListRequest  request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<ScoreAssignmentAnalystDto.ScoreAssignmentAnalystSelectListResponse > pageAmtObject = new PageAmtListResponseDto<>();
        pageAmtObject = scoreAssignmentAnalystRepository.findAllAssignment(request);
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }
}
