package com.example.bloodbankmanagement.dto.excelObject;

import com.example.bloodbankmanagement.common.excel.DefaultHeaderStyle;
import com.example.bloodbankmanagement.common.excel.ExcelColumn;
import com.example.bloodbankmanagement.common.excel.ExcelColumnStyle;
import com.example.bloodbankmanagement.common.excel.style.BlueHeaderStyle;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Getter
@ToString
@Data
@NoArgsConstructor
@DefaultHeaderStyle(style = @ExcelColumnStyle(excelCellStyleClass = BlueHeaderStyle.class))
public class AssignmentListExcel {
    @ExcelColumn(headerName = "Mã đồ án")
    private Long assignmentId;
    @ExcelColumn(headerName = "Mã sinh viên")
    private Long studentId;
    @ExcelColumn(headerName = "Tên đồ án")
    private String assignmentName;
    @ExcelColumn(headerName = "Tên sinh viên")
    private String studentName;
    @ExcelColumn(headerName = "Email sinh viên")
    private String studentEmail;
    @ExcelColumn(headerName = "Tên giảng viên hướng dẫn")
    private String instructorName;
    @ExcelColumn(headerName = "Tên giảng viên phản biện")
    private String criticalName;
    @ExcelColumn(headerName = "Tên chuyên ngành")
    private String majorName;
    @ExcelColumn(headerName = "Kỳ học")
    private String admissionPeriodName;
    @ExcelColumn(headerName = "Trạng thái đồ án")
    private String valueStatusAssignmentDisplayName;
    @ExcelColumn(headerName = "Ngày tạo")
    private Date createAt;

    public AssignmentListExcel(Long assignmentId, Long studentId, String assignmentName, String studentName, String studentEmail, String instructorName, String criticalName, String majorName, String admissionPeriodName, String valueStatusAssignmentDisplayName, Date createAt) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.assignmentName = assignmentName;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.instructorName = instructorName;
        this.criticalName = criticalName;
        this.majorName = majorName;
        this.admissionPeriodName = admissionPeriodName;
        this.valueStatusAssignmentDisplayName = valueStatusAssignmentDisplayName;
        this.createAt = createAt;
    }
}
