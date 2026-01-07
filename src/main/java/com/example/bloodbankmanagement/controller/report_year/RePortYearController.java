package com.example.bloodbankmanagement.controller.report_year;

import com.example.bloodbankmanagement.common.excel.execute.ExcelFile;
import com.example.bloodbankmanagement.common.exception.ExcelException;
import com.example.bloodbankmanagement.common.untils.DateUtil;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.AdmissionPeriodDto;
import com.example.bloodbankmanagement.service.report_year.ReportYearServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/reportYear")
@RequiredArgsConstructor
public class RePortYearController {

    private final ReportYearServiceImpl reportYearService;
    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<AdmissionPeriodDto.AdmissionPeriodListInfo>>> selectListAdmissionPeriod(@RequestBody AdmissionPeriodDto.AdmissionPeriodSelectListInfo request) {
        return new ResponseEntity<>(
                reportYearService.selectListAdmissionPeriod(request),
                HttpStatus.OK
        );
    }
    @PostMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"Danh_sach_toan_bo_sinh_vien"+ DateUtil.strNowDate()+DateUtil.strNowTime()+".xlsx\""));
        this.reportYearService.exportExcel(response);
    }
    @PostMapping("/exportExcel2")
    public void exportExcelDynamic(HttpServletResponse response) throws Exception {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"E-Collection-Management_Report_"+ DateUtil.strNowDate()+DateUtil.strNowTime()+".xlsx\""));
            ExcelFile excelFile = reportYearService.exportExcelDynamicObject();
            excelFile.write(response.getOutputStream());
        }catch (Exception e){
            throw new Exception("Could not save file:");
        }
    }
}
