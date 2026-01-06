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

        // BeneficiaryAccount
        BENEFICIARY_ACCOUNT_INVALID_FORMAT("PosExcelColumn.BeneficiaryAccount.InvalidFormat"),
        BENEFICIARY_ACCOUNT_LENGTH("PosExcelColumn.BeneficiaryAccount.Length"),
        BENEFICIARY_ACCOUNT_NOT_BLANK("PosExcelColumn.BeneficiaryAccount.NotBlank"),
        BENEFICIARY_ACCOUNT_NOT_FOUND("PosExcelColumn.BeneficiaryAccount.NotFound"),

        // CustomerId
        CUSTOMER_ID_INVALID_CONTRACT("PosExcelColumn.CustomerId.Contract"),
        CUSTOMER_ID_INVALID_FORMAT("FAIL"),
        CUSTOMER_ID_LENGTH("OldPasswordMatched"),
        CUSTOMER_ID_NOT_BLANK("NotFoundDataUser"),
        CUSTOMER_ID_NOT_FOUND("NotNullValueInput"),
        CUSTOMER_ID_POS("PosExcelColumn.CustomerId.Pos"),

        // PosId
        POS_ID_DUPLICATE("PosExcelColumn.PosId.Duplicate"),
        POS_ID_INVALID_FORMAT("PosExcelColumn.PosId.InvalidFormat"),
        POS_ID_LENGTH("PosExcelColumn.PosId.Length"),
        POS_ID_NOT_BLANK("PosExcelColumn.PosId.NotBlank"),

        // PosName
        POS_NAME_INVALID_FORMAT("PosExcelColumn.PosName.InvalidFormat"),
        POS_NAME_LENGTH("PosExcelColumn.PosName.Length"),
        POS_NAME_NOT_BLANK("PosExcelColumn.PosName.NotBlank"),

        // ProviderId
        PROVIDER_ID_INVALID_FORMAT("PosExcelColumn.ProviderId.InvalidFormat"),
        PROVIDER_ID_LENGTH("PosExcelColumn.ProviderId.Length"),
        PROVIDER_ID_NOT_BLANK("PosExcelColumn.ProviderId.NotBlank"),
        PROVIDER_ID_NOT_FOUND("PosExcelColumn.ProviderId.NotFound");


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
