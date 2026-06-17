package com.example.bloodbankmanagement.service.report_analyst;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.excel.execute.ExcelFile;
import com.example.bloodbankmanagement.common.excel.execute.onesheet.OneSheetExcelFile;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.common.untils.DateUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.excelObject.AssignmentListExcel;
import com.example.bloodbankmanagement.dto.excelObject.StudentListExcel;
import com.example.bloodbankmanagement.dto.objectRepository.AssignmentStudentAnalystDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.AssignmentAnalystDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.StudentAnalystDto;
import com.example.bloodbankmanagement.repository.report_month_analist.AssignmentAnalystRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentAnalystServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AssignmentAnalystServiceImpl.class);
    private final ResponseCommon responseService;
    private final MessageSource messageSource;
    private final AssignmentAnalystRepository assignmentAnalystRepository;

    public SingleResponseDto<PageAmtListResponseDto<AssignmentAnalystDto.AssignmentAnalystSelectListResponse>> selectAllTotalData(AssignmentAnalystDto.AssignmentAnalystSelectListRequest request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<AssignmentAnalystDto.AssignmentAnalystSelectListResponse> pageAmtObject = new PageAmtListResponseDto<>();
        pageAmtObject = assignmentAnalystRepository.findAllAssignment(request);
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }



    public ExcelFile exportExcelDynamicObject(AssignmentAnalystDto.AssignmentAnalystExportExcelRequest request) {
        //Write data in sheet(set name and value cell by object)
        String userId = CommonUtil.getUsernameByToken();
        List<AssignmentListExcel> dtoObject = assignmentAnalystRepository.findAllAssignmentExportExcel(request);;//studentExportMapper.toEntity(listAllUser)
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("Danh sách đồ án sinh viên", "");
        map.put(CommonUtil.HeaderExcel.CREATE_USER_VI.toString(), userId);
        map.put(CommonUtil.HeaderExcel.DATE_VI.toString(), DateUtil.nowToTimestampStr());
        logger.info("Z033 Export excel End");
        return new OneSheetExcelFile(dtoObject, AssignmentListExcel.class, messageSource, map, "vi");

    }
}
