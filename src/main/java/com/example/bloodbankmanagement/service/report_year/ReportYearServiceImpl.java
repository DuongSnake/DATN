package com.example.bloodbankmanagement.service.report_year;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.AdmissionPeriodDto;
import com.example.bloodbankmanagement.entity.AssignmentStudentRegister;
import com.example.bloodbankmanagement.repository.student.AssignmentRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportYearServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final AssignmentRegisterRepository assignmentRegisterRepository;
    private final ResponseCommon responseService;

    public SingleResponseDto<PageAmtListResponseDto<AdmissionPeriodDto.AdmissionPeriodListInfo>> selectListAdmissionPeriod(AdmissionPeriodDto.AdmissionPeriodSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
//        PageAmtListResponseDto<AdmissionPeriodDto.AdmissionPeriodListInfo> pageAmtObject = new PageAmtListResponseDto<>();
//        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
//        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
//        //Select list file upload
//        List<AssignmentStudentRegister> listDataFileMetadata = assignmentRegisterRepository.findAll();
//        pageAmtObject = AssignmentStudentRegister.convertListObjectToDto(listDataFileMetadata, Long.valueOf("1000"));
//        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }
}
