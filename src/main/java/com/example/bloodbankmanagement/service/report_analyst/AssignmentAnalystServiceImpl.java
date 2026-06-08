package com.example.bloodbankmanagement.service.report_analyst;

import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentAnalystDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.AssignmentAnalystDto;
import com.example.bloodbankmanagement.repository.report_month_analist.AssignmentAnalystRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentAnalystServiceImpl {
    private final ResponseCommon responseService;
    private final MessageSource messageSource;
    private final AssignmentAnalystRepository assignmentAnalystRepository;

    public SingleResponseDto<PageAmtListResponseDto<AssignmentAnalystDto.AssignmentAnalystSelectListResponse>> selectAllTotalData(AssignmentAnalystDto.AssignmentAnalystSelectListRequest request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<AssignmentAnalystDto.AssignmentAnalystSelectListResponse> pageAmtObject = new PageAmtListResponseDto<>();
        List<AssignmentAnalystDto.AssignmentAnalystSelectListResponse> listDataFileMetadata = new ArrayList<>();
        //Select list file upload
        if(!ObjectUtils.isEmpty(request) && "not_map".equals(request.getStatusAssignment())){
            listDataFileMetadata = assignmentAnalystRepository.findAllAssignment(request);
        }else if(!ObjectUtils.isEmpty(request) && "map".equals(request.getStatusAssignment())){
            listDataFileMetadata = assignmentAnalystRepository.findAllAssignment(request);
        }else{
            listDataFileMetadata = assignmentAnalystRepository.findAllAssignment(request);
        }
        pageAmtObject.setData(listDataFileMetadata);
        pageAmtObject.setTotalRecord(listDataFileMetadata.size());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }
}
