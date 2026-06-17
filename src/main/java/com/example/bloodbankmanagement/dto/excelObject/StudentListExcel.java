package com.example.bloodbankmanagement.dto.excelObject;

import com.example.bloodbankmanagement.common.excel.DefaultHeaderStyle;
import com.example.bloodbankmanagement.common.excel.ExcelColumn;
import com.example.bloodbankmanagement.common.excel.ExcelColumnStyle;
import com.example.bloodbankmanagement.common.excel.style.BlueHeaderStyle;
import lombok.*;

import java.sql.Date;

@Getter
@ToString
@Data
@NoArgsConstructor
@DefaultHeaderStyle(style = @ExcelColumnStyle(excelCellStyleClass = BlueHeaderStyle.class))
public class StudentListExcel {
    @ExcelColumn(headerName = "Mã sinh viên")
    private Long studentId;
    @ExcelColumn(headerName = "Mã giảng viên hướng dẫn")
    private Long instructorId;
    @ExcelColumn(headerName = "Mã giảng viên phản biện")
    private Long criticalId;
    @ExcelColumn(headerName = "Tên sinh viên")
    private String studentName;
    @ExcelColumn(headerName = "Email sinh viên")
    private String studentEmail;
    @ExcelColumn(headerName = "Tên giảng viên hướng dẫn")
    private String instructorName;
    @ExcelColumn(headerName = "Email giảng viên phản biện")
    private String instructorEmail;
    @ExcelColumn(headerName = "Tên giảng viên phản biện")
    private String criticalName;
    @ExcelColumn(headerName = "Email giảng viên phản biện")
    private String criticalEmail;
    @ExcelColumn(headerName = "Ngày tạo")
    private Date createAt;
    // Create a constructor matching the exact selection order in your SQL query
    @Builder
    public StudentListExcel(Long studentId, Long criticalId, Long instructorId,
                             String studentName, String studentEmail,
                            String instructorName, String instructorEmail,
                             String criticalName, String criticalEmail
            , Date createAt) {
        this.studentId = studentId;
        this.criticalId = criticalId;
        this.instructorId = instructorId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.criticalName = criticalName;
        this.criticalEmail = criticalEmail;
        this.createAt = createAt;
    }
}
