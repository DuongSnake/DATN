package com.example.bloodbankmanagement.service.report_student_major;

import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.CountTotalRecordDto;
import com.example.bloodbankmanagement.dto.objectRepository.StudentMappingDto;
import com.example.bloodbankmanagement.repository.report_month_analist.MajorAnalystRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportStudentMajorServiceImpl {
    private final ResponseCommon responseService;
    private final MessageSource messageSource;
    private final MajorAnalystRepository majorAnalystRepository;

    public SingleResponseDto<PageAmtListResponseDto<StudentMappingDto>> selectAllTotalData(){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<StudentMappingDto> pageAmtObject = new PageAmtListResponseDto<>();
        //Select list file upload
        List<StudentMappingDto> listDataFileMetadata = majorAnalystRepository.findCompleteStudentMapping();
        pageAmtObject.setData(listDataFileMetadata);
        pageAmtObject.setTotalRecord(listDataFileMetadata.size());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }
}
