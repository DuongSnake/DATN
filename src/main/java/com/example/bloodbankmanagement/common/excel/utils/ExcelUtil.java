package com.example.bloodbankmanagement.common.excel.utils;

import com.example.bloodbankmanagement.common.excel.ExcelFieldsValue;
import com.example.bloodbankmanagement.common.excel.ExcelRowMap;
import com.example.bloodbankmanagement.common.exception.CustomException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {
	public static final String INTERNAL_SERVER_ERROR = "InternalServerError";

	public ExcelRowMap ExcelReader(MultipartFile file, int rowIndexPram) {

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		ExcelRowMap excelRowMap;

		if (!Objects.requireNonNull(extension).equals("xlsx") && !extension.equals("xls")) {
			throw new CustomException("Excel format does not match", "ExcelFormatNotMatch");
		}

		if (extension.equalsIgnoreCase("xlsx")) {
			excelRowMap = ExcelReaderXlsx(file, rowIndexPram);
		} else {
			excelRowMap = ExcelReaderXls(file, rowIndexPram);
		}

		return excelRowMap;
	}

	protected ExcelRowMap ExcelReaderXls(MultipartFile file, int rowIndexPram) {

		ExcelRowMap excelRowMap = new ExcelRowMap();
		HashMap<String, Object> rowMap = null;
		HashMap<Integer, Object> colLoc = new HashMap<>();

		initColLoc(colLoc);
		ZipSecureFile.setMinInflateRatio(0L);
		try (HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream())) {

			HSSFSheet curSheet;
			HSSFRow curRow;
			HSSFCell curCell;

			for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
				curSheet = workbook.getSheetAt(sheetIndex);
				rowMap = new HashMap<>();
				int rowNum = 0;

				for (int rowIndex = rowIndexPram; rowIndex <= curSheet.getLastRowNum(); rowIndex++) {
					curRow = curSheet.getRow(rowIndex);
					String value;

					if (curRow == null)
						continue;
					for (int cellIndex = 0; cellIndex <= curRow.getLastCellNum(); cellIndex++) {
						curCell = curRow.getCell(cellIndex);

						value = "";

						if (curCell != null) {

							switch (curCell.getCellType()) {
								case FORMULA:
									value = curCell.getCellFormula().trim();
									break;
								case NUMERIC:
									if (DateUtil.isCellDateFormatted(curCell)) {
										Date date = curCell.getDateCellValue();
										value = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_YYYYMMDD).format(date);
									} else {
										curCell.setCellType(CellType.STRING);
										value = curCell.getStringCellValue().trim();
									}
									break;
								case STRING:
									value = curCell.getStringCellValue().trim();

									if (com.example.bloodbankmanagement.common.untils.DateUtil.isDatePattern(value, com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_DD_SOURCE_MM_SOURCE_YYYY)) {
										try {
											SimpleDateFormat transFormat = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_DD_SOURCE_MM_SOURCE_YYYY);
											Date date = transFormat.parse(value);

											value = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_YYYYMMDD).format(date);
										} catch (ParseException e) {
											throw new CustomException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
										}
									}
									else if (com.example.bloodbankmanagement.common.untils.DateUtil.isDatePattern(value, com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_DD_MM_YYYY)) {
										try {
											SimpleDateFormat transFormat = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_DD_MM_YYYY);
											Date date = transFormat.parse(value);

											value = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_YYYYMMDD).format(date);
										} catch (ParseException e) {
											throw new CustomException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
										}
									}
									break;
								case BLANK:
									value = "";
									break;
								case ERROR:
									value = curCell.getErrorCellValue() + "";
									break;
								default:
									value = "";
									break;
							}
						}

						rowNum = rowIndex + 1;
						rowMap.put((String) colLoc.get(cellIndex), value);

					}

					excelRowMap.put(rowNum + "", rowMap);
					rowMap = new HashMap<>();
				}
			}
		} catch (IOException e) {
			throw new CustomException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
		}
		return excelRowMap;
	}

	protected ExcelRowMap ExcelReaderXlsx(MultipartFile file, int rowIndexPram) {

		ExcelRowMap excelRowMap = new ExcelRowMap();
		HashMap<String, Object> rowMap = null;
		HashMap<Integer, Object> colLoc = new HashMap<>();

		initColLoc(colLoc);

		ZipSecureFile.setMinInflateRatio(0L);
		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {

			int rowindex = 0;
			int columnindex = 0;
			int rows = 0;

			String value = "";

			XSSFSheet sheet;
			XSSFRow row;
			XSSFCell cell;

			for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
				sheet = workbook.getSheetAt(sheetIndex);
				rowMap = new HashMap<>();
				int rowNum = 0;
				rows = sheet.getLastRowNum();
				for (rowindex = rowIndexPram; rowindex <= rows; rowindex++) {
					row = sheet.getRow(rowindex);

					if (row == null)
						continue;
					value = "";
					for (columnindex = 0; columnindex <= row.getLastCellNum(); columnindex++) {
						cell = row.getCell(columnindex);

						if (cell == null) {
							continue;
						} else {
							switch (cell.getCellType()) {
								case FORMULA:
									value = cell.getCellFormula().trim();
									break;
								case NUMERIC:
									if (DateUtil.isCellDateFormatted(cell)) {
										Date date = cell.getDateCellValue();
										value = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_YYYYMMDD).format(date);
									} else {
										cell.setCellType(CellType.STRING);
										value = cell.getStringCellValue().trim();
									}
									break;
								case STRING:
									value = cell.getStringCellValue().trim();

									if (com.example.bloodbankmanagement.common.untils.DateUtil.isDatePattern(value, com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_DD_SOURCE_MM_SOURCE_YYYY)) {
										try {
											SimpleDateFormat transFormat = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_DD_SOURCE_MM_SOURCE_YYYY);
											Date date = transFormat.parse(value);

											value = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_YYYYMMDD).format(date);
										} catch (ParseException e) {
											throw new CustomException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
										}
									}
									else if (com.example.bloodbankmanagement.common.untils.DateUtil.isDatePattern(value, com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_DD_MM_YYYY)) {
										try {
											SimpleDateFormat transFormat = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_DD_MM_YYYY);
											Date date = transFormat.parse(value);

											value = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_YYYYMMDD).format(date);
										} catch (ParseException e) {
											throw new CustomException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
										}
									}

									break;
								case BLANK:
									value = "";
									break;
								case ERROR:
									value = cell.getErrorCellValue() + "";
									break;
								default:
									value = "";
									break;
							}
						}

						rowNum = rowindex + 1;
						rowMap.put((String) colLoc.get(columnindex), value);
					}

					excelRowMap.put(rowNum + "", rowMap);
					rowMap = new HashMap<>();
				}
			}

		} catch (IOException e) {
			throw new CustomException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
		}
		return excelRowMap;
	}

	public ExcelRowMap ExcelReaderXlsx(MultipartFile file, int rowIndexPram, int sheetIndex) {

		ExcelRowMap excelRowMap = new ExcelRowMap();
		HashMap<String, Object> rowMap = null;
		HashMap<Integer, Object> colLoc = new HashMap<>();

		initColLoc(colLoc);

		ZipSecureFile.setMinInflateRatio(0L);
		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {

			int rowindex = 0;
			int columnindex = 0;
			int rows = 0;

			String value = "";

			XSSFSheet sheet;
			XSSFRow row;
			XSSFCell cell;

			sheet = workbook.getSheetAt(sheetIndex);
			rowMap = new HashMap<>();
			int rowNum = 0;
			rows = sheet.getLastRowNum();
			for (rowindex = rowIndexPram; rowindex <= rows; rowindex++) {
				row = sheet.getRow(rowindex);

				if (row == null)
					continue;
				value = "";
				for (columnindex = 0; columnindex <= row.getLastCellNum(); columnindex++) {
					cell = row.getCell(columnindex);

					if (cell == null) {
						continue;
					} else {
						switch (cell.getCellType()) {
							case FORMULA:
								value = cell.getCellFormula().trim();
								break;
							case NUMERIC:
								if (DateUtil.isCellDateFormatted(cell)) {
									Date date = cell.getDateCellValue();
									value = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_YYYYMMDD).format(date);
								} else {
									cell.setCellType(CellType.STRING);
									value = cell.getStringCellValue().trim();
								}
								break;
							case STRING:
								value = cell.getStringCellValue().trim();

								if (com.example.bloodbankmanagement.common.untils.DateUtil.isDatePattern(value, com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_DD_SOURCE_MM_SOURCE_YYYY)) {
									try {
										SimpleDateFormat transFormat = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_DD_SOURCE_MM_SOURCE_YYYY);
										Date date = transFormat.parse(value);

										value = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_YYYYMMDD).format(date);
									} catch (ParseException e) {
										throw new CustomException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
									}
								} else if (com.example.bloodbankmanagement.common.untils.DateUtil.isDatePattern(value, com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_DD_MM_YYYY)) {
									try {
										SimpleDateFormat transFormat = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_DD_MM_YYYY);
										Date date = transFormat.parse(value);

										value = new SimpleDateFormat(com.example.bloodbankmanagement.common.untils.DateUtil.DATE_FORMAT_YYYYMMDD).format(date);
									} catch (ParseException e) {
										throw new CustomException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
									}
								}

								break;
							case BLANK:
								value = "";
								break;
							case ERROR:
								value = cell.getErrorCellValue() + "";
								break;
							default:
								value = "";
								break;
						}
					}

					rowNum = rowindex + 1;
					rowMap.put((String) colLoc.get(columnindex), value);
				}

				excelRowMap.put(rowNum + "", rowMap);
				rowMap = new HashMap<>();
			}
		} catch (IOException e) {
			throw new CustomException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
		}
		return excelRowMap;
	}

	public static boolean validateMaxRow(MultipartFile file, int maxRow, int startIndexRow) {
		boolean result = false;
		XSSFWorkbook workbook;
		try {
			int startRowCheck = maxRow+startIndexRow;
			ZipSecureFile.setMinInflateRatio(0L);
			workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(0);

			int rows = sheet.getLastRowNum();
			for (int rowIndex = startRowCheck; rowIndex <= rows; rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex);
				if (!checkIfRowIsEmpty(row)) {
					result = true;
					break;
				}
			}
		} catch (IOException e) {
			throw new CustomException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
		}
		return result;
	}

	private static boolean checkIfRowIsEmpty(Row row) {
		if (row == null) {
			return true;
		}
		if (row.getLastCellNum() <= 0) {
			return true;
		}
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
				return false;
			}
		}
		return true;
	}

	public static void fillDataIntoTemplateCopy(File file, int rowStart, Map<String, Object[]> collect, HttpServletResponse response,
												List<ExcelFieldsValue> data) throws IOException {
		fillDataIntoTemplateCopy(file, rowStart, 0, collect, response, data);
	}

	public static void fillDataIntoTemplateCopy(File file, int rowStart, int colStart, Map<String, Object[]> collect, HttpServletResponse response,
												List<ExcelFieldsValue> data) throws IOException {
		ServletOutputStream outputStream = response.getOutputStream();
		XSSFWorkbook workbook = null;
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			data.forEach(item -> {
				Row row = sheet.getRow(item.getRowIndex());
				Cell cell = row.getCell(item.getColumnIndex());
				genericCellSetField(item.getValue(), cell);
			});
			int stt = 0;
			for (String key : collect.keySet()) {
				Row row = sheet.createRow(++rowStart);
				Object [] objArr = collect.get(key);
				int cellnum = colStart+1;
				Cell first = row.createCell(colStart);
				first.setCellValue(++stt);
				setCellBorder(workbook, first);
				for (Object obj : objArr) {
					Cell cell = row.createCell(cellnum++);
					genericCellSetField(obj, cell);
					setCellBorder(workbook, cell);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	public static void genericCellSetField(Object obj, Cell cell) {
		if(obj instanceof String)
			cell.setCellValue((String)obj);
		else if(obj instanceof Integer)
			cell.setCellValue((Integer)obj);
		else if(obj instanceof Double)
			cell.setCellValue((Double)obj);
		else if(obj instanceof BigInteger)
			cell.setCellValue(new BigInteger(String.valueOf(obj)).intValue());
		else if(obj instanceof BigDecimal)
			cell.setCellValue(new BigDecimal(String.valueOf(obj)).doubleValue());
	}

	protected void initColLoc(HashMap<Integer, Object> colLoc) {

		colLoc.put(0, "A");
		colLoc.put(1, "B");
		colLoc.put(2, "C");
		colLoc.put(3, "D");
		colLoc.put(4, "E");
		colLoc.put(5, "F");
		colLoc.put(6, "G");
		colLoc.put(7, "H");
		colLoc.put(8, "I");
		colLoc.put(9, "J");
		colLoc.put(10, "K");
		colLoc.put(11, "L");
		colLoc.put(12, "M");
		colLoc.put(13, "N");
		colLoc.put(14, "O");
		colLoc.put(15, "P");
		colLoc.put(16, "Q");
		colLoc.put(17, "R");
		colLoc.put(18, "S");
		colLoc.put(19, "T");
		colLoc.put(20, "U");
		colLoc.put(21, "V");
		colLoc.put(22, "W");
		colLoc.put(23, "X");
		colLoc.put(24, "Y");
		colLoc.put(25, "Z");
		colLoc.put(26, "AA");
		colLoc.put(27, "AB");
		colLoc.put(28, "AC");
		colLoc.put(29, "AD");
		colLoc.put(30, "AE");

	}

	protected static void setCellBorder(XSSFWorkbook wb, Cell cell) {
		CellStyle style = wb.createCellStyle();
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		cell.setCellStyle(style);
	}
}
