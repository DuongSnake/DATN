package com.example.bloodbankmanagement.controller.report_analist;

import com.example.bloodbankmanagement.common.excel.execute.ExcelFile;
import com.example.bloodbankmanagement.common.untils.DateUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.StudentMappingDto;
import com.example.bloodbankmanagement.dto.service.report_month_analist.StudentAnalystDto;
import com.example.bloodbankmanagement.service.report_analyst.ReportStudentMajorServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reportStudentByMajor")
@RequiredArgsConstructor
public class ReportStudentByMajorController {

    private final ReportStudentMajorServiceImpl reportStudentByMajorService;

    @PostMapping("/selectAllTotalRecrod")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<StudentMappingDto>>> selectAllTotalRecrod(@RequestBody StudentAnalystDto.StudentAnalystSelectListRequest request) {
        return new ResponseEntity<>(
                reportStudentByMajorService.selectAllTotalData(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/exportExcel2")
    public void exportExcelDynamic(@RequestBody StudentAnalystDto.StudentAnalystExportExcelRequest request, HttpServletResponse response) throws Exception {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"Danh_sach_sinh_vien"+ DateUtil.strNowDate()+DateUtil.strNowTime()+".xlsx\""));
            ExcelFile excelFile = reportStudentByMajorService.exportExcelDynamicObject(request);
            excelFile.write(response.getOutputStream());
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
    }
}
