package com.example.bloodbankmanagement.service.report_student_major;

import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.CountTotalRecordDto;
import com.example.bloodbankmanagement.dto.objectRepository.StudentMappingDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.MajorAnalystDto;
import com.example.bloodbankmanagement.repository.report_month_analist.MajorAnalystRepository;
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
    private final MajorAnalystRepository majorAnalystRepository;

    public SingleResponseDto<PageAmtListResponseDto<StudentMappingDto>> selectAllTotalData(MajorAnalystDto.MajorAnalystSelectListRequest request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<StudentMappingDto> pageAmtObject = new PageAmtListResponseDto<>();
        List<StudentMappingDto> listDataFileMetadata = new ArrayList<>();
        //Select list file upload
        if(!ObjectUtils.isEmpty(request) && "not_map".equals(request.getStatusMapping())){
            listDataFileMetadata = majorAnalystRepository.findCompleteStudentNotMapping(request);
        }else if(!ObjectUtils.isEmpty(request) && "map".equals(request.getStatusMapping())){
            listDataFileMetadata = majorAnalystRepository.findCompleteStudentMapping(request);
        }else{
            listDataFileMetadata = majorAnalystRepository.findCompleteStudentAll(request);
        }
        pageAmtObject.setData(listDataFileMetadata);
        pageAmtObject.setTotalRecord(listDataFileMetadata.size());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }
}
