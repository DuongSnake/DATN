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
public class ScoreAssignmentListExcel {
    @ExcelColumn(headerName = "Mã đồ án")
    private Long assignmentId;
    @ExcelColumn(headerName = "Tên đồ án")
    private String assignmentName;
    @ExcelColumn(headerName = "Tên sinh viên")
    private String studentName;
    @ExcelColumn(headerName = "Điểm quá trình")
    private Double scoreInstructor;
    @ExcelColumn(headerName = "Điểm bảo vệ")
    private Double scoreExaminer;
    @ExcelColumn(headerName = "Điểm trung bình")
    private Double scoreAverage;
    @ExcelColumn(headerName = "Tên giảng viên hướng dẫn")
    private String instructorName;
    @ExcelColumn(headerName = "Tên giảng viên phản biện")
    private String criticalName;
    @ExcelColumn(headerName = "Tên chuyên ngành")
    private String majorName;
    @ExcelColumn(headerName = "Kỳ học")
    private String admissionPeriodName;
    @ExcelColumn(headerName = "Ngày tạo")
    private Date createAt;

    public ScoreAssignmentListExcel(Long assignmentId, String assignmentName, String studentName, Double scoreInstructor, Double scoreExaminer, Double scoreAverage, String instructorName, String criticalName, String majorName, String admissionPeriodName, Date createAt) {
        this.assignmentId = assignmentId;
        this.assignmentName = assignmentName;
        this.studentName = studentName;
        this.scoreInstructor = scoreInstructor;
        this.scoreExaminer = scoreExaminer;
        this.scoreAverage = scoreAverage;
        this.instructorName = instructorName;
        this.criticalName = criticalName;
        this.majorName = majorName;
        this.admissionPeriodName = admissionPeriodName;
        this.createAt = createAt;
    }
}
