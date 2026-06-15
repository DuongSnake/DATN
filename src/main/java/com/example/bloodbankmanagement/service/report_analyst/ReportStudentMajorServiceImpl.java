package com.example.bloodbankmanagement.service.report_analyst;

import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.StudentMappingDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.StudentAnalystDto;
import com.example.bloodbankmanagement.repository.report_month_analist.StudentAnalystRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportStudentMajorServiceImpl {
    private final ResponseCommon responseService;
    private final MessageSource messageSource;
    private final StudentAnalystRepository studentAnalystRepository;

    public SingleResponseDto<PageAmtListResponseDto<StudentMappingDto>> selectAllTotalData(StudentAnalystDto.MajorAnalystSelectListRequest request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<StudentMappingDto> pageAmtObject = new PageAmtListResponseDto<>();
        //Select list file upload
        pageAmtObject = studentAnalystRepository.findCompleteStudentNotMapping(request);
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }
}
