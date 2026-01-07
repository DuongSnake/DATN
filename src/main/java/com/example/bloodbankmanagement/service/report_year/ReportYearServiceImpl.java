package com.example.bloodbankmanagement.service.report_year;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.excel.execute.ExcelFile;
import com.example.bloodbankmanagement.common.excel.execute.onesheet.OneSheetExcelFile;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.common.untils.DateUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.excelObject.StudentExcel;
import com.example.bloodbankmanagement.dto.mapper.StudentExportMapper;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.AdmissionPeriodDto;
import com.example.bloodbankmanagement.dto.service.student.StudentDto;
import com.example.bloodbankmanagement.entity.AssignmentStudentRegister;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.UserRepository;
import com.example.bloodbankmanagement.repository.student.AssignmentRegisterRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportYearServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final AssignmentRegisterRepository assignmentRegisterRepository;
    private final UserRepository userRepository;
    private final StudentExportMapper studentExportMapper;
    private final ResponseCommon responseService;
    private final MessageSource messageSource;

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

    public void exportExcel(HttpServletResponse response) throws IOException {
        //Write data in sheet(hard name and value for each cell)
        final List<User> listAllUser = userRepository.findAll();
        final Workbook workbook = new XSSFWorkbook();
        final Sheet sheet = workbook.createSheet("Students");
        final Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Id");
        header.createCell(1).setCellValue("user name");
        header.createCell(2).setCellValue("email");
        header.createCell(3).setCellValue("identity card");
        header.createCell(4).setCellValue("note");
        int rowNum =1;
        for (final  User userValue: listAllUser) {
            final Row contentRow = sheet.createRow(rowNum++);
            contentRow.createCell(0).setCellValue(userValue.getId());
            contentRow.createCell(1).setCellValue(userValue.getUsername());
            contentRow.createCell(2).setCellValue(userValue.getEmail());
            contentRow.createCell(3).setCellValue(userValue.getIdentityCard());
            contentRow.createCell(4).setCellValue(userValue.getNote());
        }
        final ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public ExcelFile exportExcelDynamicObject() {
        //Write data in sheet(set name and value cell by object)
        String userId = CommonUtil.getUsernameByToken();
        List<User> listAllUser = userRepository.findAll();
        List<StudentDto.StudentForExportExcelResponse> listDataConvert = convertToObjectExportExcel(listAllUser);
        List<StudentExcel> dtoObject = studentExportMapper.toEntity(listDataConvert);//studentExportMapper.toEntity(listAllUser)
        HashMap<String, String> map = new LinkedHashMap<>();
            map.put("Danh sach thong tin sinh vien dang ky nam nay", "");
            map.put(CommonUtil.HeaderExcel.CREATE_USER_VI.toString(), userId);
            map.put(CommonUtil.HeaderExcel.DATE_VI.toString(), DateUtil.nowToTimestampStr());
        logger.info("Z033 Export excel End");
//        //Write all data in one sheet
        return new OneSheetExcelFile(dtoObject, StudentExcel.class, messageSource, map, "vi");

    }

    public List<StudentDto.StudentForExportExcelResponse> convertToObjectExportExcel(List<User> listDataUSer){
        List<StudentDto.StudentForExportExcelResponse> listDataResponse = new ArrayList<>();
        for(int i=0;i< listDataUSer.size();i++){
            StudentDto.StudentForExportExcelResponse objectData = new StudentDto.StudentForExportExcelResponse();
            objectData.setUsername(listDataUSer.get(i).getUsername());
            objectData.setEmail(listDataUSer.get(i).getEmail());
            objectData.setPhone(listDataUSer.get(i).getPhone());
            objectData.setFullName(listDataUSer.get(i).getFullName());
            objectData.setIdentityCard(listDataUSer.get(i).getIdentityCard());
            objectData.setAddress(listDataUSer.get(i).getAddress());
            objectData.setNote(listDataUSer.get(i).getNote());
            objectData.setStatus(listDataUSer.get(i).getStatus());
            objectData.setMajorName(null != listDataUSer.get(i).getMajorInfo() ? listDataUSer.get(i).getMajorInfo().getMajorName() : "");
            objectData.setAdmissionPeriodName(null != listDataUSer.get(i).getPeriodTime() ? listDataUSer.get(i).getPeriodTime().getAdmissionPeriodName() : "");
            listDataResponse.add(objectData);
        }
        return listDataResponse;
    }
}
