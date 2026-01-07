package com.example.bloodbankmanagement.dto.excelObject;

import com.example.bloodbankmanagement.common.excel.DefaultHeaderStyle;
import com.example.bloodbankmanagement.common.excel.ExcelColumn;
import com.example.bloodbankmanagement.common.excel.ExcelColumnStyle;
import com.example.bloodbankmanagement.common.excel.style.BlueHeaderStyle;
import com.example.bloodbankmanagement.entity.AdmissionPeriod;
import com.example.bloodbankmanagement.entity.Major;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@DefaultHeaderStyle(style = @ExcelColumnStyle(excelCellStyleClass = BlueHeaderStyle.class))
public class StudentExcel {
    @ExcelColumn(headerName = "ExcelColumn.username")
    private String username;
    @ExcelColumn(headerName = "ExcelColumn.email")
    private String email;

    @ExcelColumn(headerName = "ExcelColumn.phone")
    private String phone;
    @ExcelColumn(headerName = "ExcelColumn.fullName")
    private String fullName;

    @ExcelColumn(headerName = "ExcelColumn.identityCard")
    private String identityCard;
    @ExcelColumn(headerName = "ExcelColumn.address")
    private String address;

    @ExcelColumn(headerName = "ExcelColumn.note")
    private String note;

    @ExcelColumn(headerName = "ExcelColumn.status")
    private String status;

    @ExcelColumn(headerName = "ExcelColumn.majorName")
    private String majorName;

    @ExcelColumn(headerName = "ExcelColumn.admissionPeriodName")
    private String admissionPeriodName;
    ///Add for line code convert to entity to object export excel at line List<StudentExcel> dtoObject = studentExportMapper.toEntity(listAllUser);
    @Builder
    public StudentExcel(String username, String email, String phone, String fullName, String identityCard, String address, String note, String status, String majorName, String admissionPeriodName) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.fullName = fullName;
        this.identityCard = identityCard;
        this.address = address;
        this.note = note;
        this.status = status;
        this.majorName = majorName;
        this.admissionPeriodName = admissionPeriodName;
    }
}
