package com.example.bloodbankmanagement.common.untils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class ExcelUtils {

    public static boolean isValid(String val) {
        return StringUtils.isNotBlank(val);
    }

    public static String getCellValue(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    public static boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK && !getCellValue(cell).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public enum UserExcelCode {

        // AdmissionPeriodMap
        USER_NAME_DUPLICATE("UserExcelColumn.UserName.Duplicate"),

        // AdmissionPeriodMap
        ADMISSION_PERIOD_MAP_INVALID_FORMAT("UserExcelColumn.AdmissionPeriodMap.InvalidFormat"),
        ADMISSION_PERIOD_MAP_LENGTH("UserExcelColumn.AdmissionPeriodMap.Length"),
        ADMISSION_PERIOD_MAP_NOT_BLANK("UserExcelColumn.AdmissionPeriodMap.NotBlank"),
        ADMISSION_PERIOD_MAP_NOT_FOUND("UserExcelColumn.AdmissionPeriodMap.NotFound"),

        // RoleId
        ROLE_ID_DUPLICATE("UserExcelColumn.RoleId.Duplicate"),
        ROLE_ID_INVALID_FORMAT("UserExcelColumn.RoleId.InvalidFormat"),
        ROLE_ID_LENGTH("UserExcelColumn.RoleId.Length"),
        ROLE_ID_NOT_BLANK("UserExcelColumn.RoleId.NotBlank"),
        ROLE_ID_NOT_FOUND("UserExcelColumn.RoleId.NotFound"),

        // MajorId
        MAJOR_ID_INVALID_FORMAT("UserExcelColumn.MajorId.InvalidFormat"),
        MAJOR_ID_LENGTH("UserExcelColumn.MajorId.Length"),
        MAJOR_ID_NOT_BLANK("UserExcelColumn.MajorId.NotBlank"),
        MAJOR_ID_NOT_FOUND("UserExcelColumn.MajorId.NotFound");


        private final String id;

        UserExcelCode(final String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
