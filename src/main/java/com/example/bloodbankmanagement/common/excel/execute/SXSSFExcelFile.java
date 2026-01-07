package com.example.bloodbankmanagement.common.excel.execute;


import com.example.bloodbankmanagement.common.excel.resource.*;
import com.example.bloodbankmanagement.common.exception.ExcelInternalException;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


import static com.example.bloodbankmanagement.common.excel.utils.SuperClassReflectionUtils.getField;

public abstract class SXSSFExcelFile<T> implements ExcelFile<T> {
	
	protected static final SpreadsheetVersion supplyExcelVersion = SpreadsheetVersion.EXCEL2007;
	private MessageSource messageSource;
	protected SXSSFWorkbook wb;
	protected Sheet sheet;
	protected ExcelRenderResource resource;

	/**
	 *SXSSFExcelFile
	 * @param type Class type to be rendered
	 */
	protected SXSSFExcelFile(Class<T> type) {
		this(Collections.emptyList(), type, new DefaultDataFormatDecider(), "vi");
	}

	/**
	 *SXSSFExcelFile
	 * @param type Class type to be rendered
	 */
	protected SXSSFExcelFile(Class<T> type, MessageSource messageSource) {
		this(Collections.emptyList(), type, new DefaultDataFormatDecider(), messageSource);
	}

	/**
	 * SXSSFExcelFile
	 * @param data List Data to render excel file. data should have at least one @ExcelColumn on fields
	 * @param type Class type to be rendered
	 */
	protected SXSSFExcelFile(List<T> data, Class<T> type) {
		this(data, type, new DefaultDataFormatDecider(), "vi");
	}

	/**
	 * SXSSFExcelFile
	 * @param data List Data to render excel file. data should have at least one @ExcelColumn on fields
	 * @param type Class type to be rendered
	 */
	protected SXSSFExcelFile(List<T> data, Class<T> type, MessageSource messageSource) {
		this(data, type, new DefaultDataFormatDecider(), messageSource);
	}

	protected SXSSFExcelFile(List<T> data, Class<T> type, MessageSource messageSource, String lang) {
		this(data, type, new DefaultDataFormatDecider(), messageSource, lang);
	}
	
	/**
	 * SXSSFExcelFile
	 * @param data List Data to render excel file. data should have at least one @ExcelColumn on fields
	 * @param type Class type to be rendered
	 * @param dataFormatDecider Custom DataFormatDecider
	 */
	protected SXSSFExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider) {
		this(data, type, dataFormatDecider, "vi");
	}

	public SXSSFExcelFile(List<T> data, Class<T> type, String language, HashMap<String, String> headerString) {
		this(data, type, new DefaultDataFormatDecider(), language, headerString);
	}

	protected SXSSFExcelFile(List<T> data, Class<T> type, MessageSource messageSource, HashMap<String, String> headerString, String language) {
		this(data, type, new DefaultDataFormatDecider(), messageSource, headerString, language);
	}
	
	/**
	 * SXSSFExcelFile
	 * @param data List Data to render excel file. data should have at least one @ExcelColumn on fields
	 * @param type Class type to be rendered
	 * @param language multi language
	 */
	protected SXSSFExcelFile(List<T> data, Class<T> type, String language) {
		this(data, type, new DefaultDataFormatDecider(), language);
	}
	
	/**
	 * SXSSFExcelFile
	 * @param data List Data to render excel file. data should have at least one @ExcelColumn on fields
	 * @param type Class type to be rendered
	 * @param dataFormatDecider Custom DataFormatDecider
	 */
	protected SXSSFExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider, String language) {
		validateData(data);
		this.wb = new SXSSFWorkbook();
		this.resource = ExcelRenderResourceFactory.prepareRenderResource(type, wb, dataFormatDecider, language);
		renderExcel(data);
	}

	protected SXSSFExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider, MessageSource messageSource) {
		this.messageSource = messageSource;
		validateData(data);
		this.wb = new SXSSFWorkbook();
		this.resource = ExcelRenderResourceFactory.prepareRenderResource(type, wb, dataFormatDecider);
		renderExcel(data);
	}

	protected SXSSFExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider, MessageSource messageSource, String lang) {
		this.messageSource = messageSource;
		validateData(data, lang);
		this.wb = new SXSSFWorkbook();
		this.resource = ExcelRenderResourceFactory.prepareRenderResource(type, wb, dataFormatDecider);
		renderExcel(data, lang);
	}
	protected SXSSFExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider, MessageSource messageSource,
							 HashMap<String, String> headerString, String language) {
		this.messageSource = messageSource;
		validateData(data);
		this.wb = new SXSSFWorkbook();
		this.resource = ExcelRenderResourceFactory.prepareRenderResource(type, wb, dataFormatDecider);
		renderExcel(data, headerString, language);
	}

	public SXSSFExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider, String language,  HashMap<String, String> headerString) {
		validateData(data);
		this.wb = new SXSSFWorkbook();
		this.resource = ExcelRenderResourceFactory.prepareRenderResource(type, wb, dataFormatDecider, language);
		renderExcel(data, headerString);
	}
	
	protected void validateData(List<T> data) { }

	protected void validateData(List<T> data, String lang) { }
	
	protected abstract void renderExcel(List<T> data);

	protected abstract void renderExcel(List<T> data, String lang);

	protected abstract void renderExcel(List<T> data, HashMap<String, String> headerString, String language);

	protected abstract void renderExcel(List<T> data, HashMap<String, String> headerString);

	protected void renderHeadersWithNewSheet(Sheet sheet, int rowIndex, int columnStartIndex) {
		Row row = sheet.createRow(rowIndex);
		int columnIndex = columnStartIndex;
		for (String dataFieldName : resource.getDataFieldNames()) {
			Cell cell = row.createCell(columnIndex++);
			cell.setCellStyle(resource.getCellStyle(dataFieldName, ExcelRenderLocation.HEADER));
			cell.setCellValue(resource.getExcelHeaderName(dataFieldName));
		}
	}

	protected void renderHeadersWithNewSheet(Sheet sheet, int rowIndex, int columnStartIndex, String lang) {
		Row row = sheet.createRow(rowIndex);
		int columnIndex = columnStartIndex;
		for (String dataFieldName : resource.getDataFieldNames()) {
			Cell cell = row.createCell(columnIndex++);
			cell.setCellStyle(resource.getCellStyle(dataFieldName, ExcelRenderLocation.HEADER));
			String columnNm = CommonUtil.getMessage(messageSource, resource.getExcelHeaderName(dataFieldName), lang);
			cell.setCellValue(columnNm);
		}
	}

	protected void renderHeadersWithNewSheet(Sheet sheet, int rowIndex, int columnStartIndex, HashMap<String, String> headerString, String language) {

		Set<String> set = headerString.keySet();
		int startRow = rowIndex;
		for (String key : set) {
			Row header = sheet.createRow(startRow++);
			Cell cellHeaderNm = header.createCell(columnStartIndex);
			cellHeaderNm.setCellValue(key);

			//set Bold for header
			if (headerString.get(key).isEmpty() ) {
				Font font = wb.createFont();
				font.setFontHeightInPoints((short) 15);
				font.setBold(true);

				CellStyle cellStyle = wb.createCellStyle();
				cellStyle.setFont(font);
				cellStyle.setAlignment(HorizontalAlignment.CENTER);

				cellHeaderNm.setCellStyle(cellStyle);
			}

			Cell cellHeaderVal = header.createCell(columnStartIndex + 1);
			cellHeaderVal.setCellValue(headerString.get(key));

		}
		//merge cell title
		CellRangeAddress mergeRange = new CellRangeAddress(0, 0, 0, resource.getDataFieldNames().size() - 1);
		sheet.addMergedRegion(mergeRange);

		Row row = sheet.createRow(rowIndex + set.size() + 1);
		int columnIndex = columnStartIndex;
		for (String dataFieldName : resource.getDataFieldNames()) {
			Cell cell = row.createCell(columnIndex++);
			cell.setCellStyle(resource.getCellStyle(dataFieldName, ExcelRenderLocation.HEADER));
			String columnNm = CommonUtil.getMessage(messageSource, resource.getExcelHeaderName(dataFieldName), language);
			cell.setCellValue(columnNm);
		}
	}

	protected void renderHeadersWithNewSheet(Sheet sheet, int rowIndex, int columnStartIndex, HashMap<String, String> headerString) {
		Set<String> set = headerString.keySet();
		int startRow = rowIndex;
		for (String key : set) {
			Row header = sheet.createRow(startRow++);
			Cell cellHeaderNm = header.createCell(columnStartIndex);
			cellHeaderNm.setCellValue(key);

			//set Bold for header
			if (headerString.get(key).isEmpty() ) {
				Font font = wb.createFont();
				font.setFontHeightInPoints((short) 15);
				font.setBold(true);

				CellStyle cellStyle = wb.createCellStyle();
				cellStyle.setFont(font);
				cellStyle.setAlignment(HorizontalAlignment.CENTER);

				cellHeaderNm.setCellStyle(cellStyle);
			}

			Cell cellHeaderVal = header.createCell(columnStartIndex + 1);
			cellHeaderVal.setCellValue(headerString.get(key));

		}
		//merge cell title
		CellRangeAddress mergeRange = new CellRangeAddress(0, 0, 0, resource.getDataFieldNames().size() - 1);
		sheet.addMergedRegion(mergeRange);

		Row row = sheet.createRow(rowIndex + set.size() + 1);
		int columnIndex = columnStartIndex;
		for (String dataFieldName : resource.getDataFieldNames()) {
			Cell cell = row.createCell(columnIndex++);
			cell.setCellStyle(resource.getCellStyle(dataFieldName, ExcelRenderLocation.HEADER));
			cell.setCellValue(resource.getExcelHeaderName(dataFieldName));
		}
	}
	
	protected void renderBody(Object data, int rowIndex, int columnStartIndex) {
		Row row = sheet.createRow(rowIndex);
		int columnIndex = columnStartIndex;
		for (String dataFieldName : resource.getDataFieldNames()) {
			Cell cell = row.createCell(columnIndex++);
			try {
				Field field = getField(data.getClass(), (dataFieldName));
				field.setAccessible(true);
				cell.setCellStyle(resource.getCellStyle(dataFieldName, ExcelRenderLocation.BODY));
				Object cellValue = field.get(data);
				renderCellValue(cell, cellValue);
			} catch (Exception e) {
				throw new ExcelInternalException(e.getMessage(), e);
			}
		}
	}
	
	private void renderCellValue(Cell cell, Object cellValue) {
		if (cellValue instanceof Number) {
			Number numberValue = (Number) cellValue;
			cell.setCellValue(numberValue.doubleValue());
			return;
		}
		cell.setCellValue(cellValue == null ? "" : cellValue.toString());
	}

	public void write(OutputStream stream) throws IOException {
		wb.write(stream);
		wb.close();
		wb.dispose();
		stream.close();
	}
	
}
