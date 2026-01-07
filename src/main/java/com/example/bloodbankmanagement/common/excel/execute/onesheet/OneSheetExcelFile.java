package com.example.bloodbankmanagement.common.excel.execute.onesheet;


import com.example.bloodbankmanagement.common.excel.execute.SXSSFExcelFile;
import com.example.bloodbankmanagement.common.excel.resource.DataFormatDecider;
import org.springframework.context.MessageSource;

import java.util.HashMap;
import java.util.List;

public class OneSheetExcelFile<T> extends SXSSFExcelFile<T> {

	private static final int ROW_START_INDEX = 0;
	private static final int COLUMN_START_INDEX = 0;
	private int currentRowIndex = ROW_START_INDEX;
	
	public OneSheetExcelFile(Class<T> type) {
		super(type);
	}

	public OneSheetExcelFile(Class<T> type, MessageSource messageSource) {
		super(type, messageSource);
	}

	public OneSheetExcelFile(List<T> data, Class<T> type) {
		super(data, type);
	}

	public OneSheetExcelFile(List<T> data, Class<T> type, MessageSource messageSource) {
		super(data, type, messageSource);
		for (int i =0; i < type.getDeclaredFields().length; i ++){
			wb.getSheet("Sheet0").trackColumnForAutoSizing(i);
			int origColWidth = sheet.getColumnWidth(i);
			sheet.autoSizeColumn(i);
			if(origColWidth +1000 > sheet.getColumnWidth(i)){
				sheet.setColumnWidth(i, origColWidth * 3);
			}
		}
	}
	public OneSheetExcelFile(List<T> data, Class<T> type, MessageSource messageSource, String lang) {
		super(data, type, messageSource, lang);
		for (int i =0; i < type.getDeclaredFields().length; i ++){
			wb.getSheet("Sheet0").trackColumnForAutoSizing(i);
			int origColWidth = sheet.getColumnWidth(i);
			sheet.autoSizeColumn(i);
			if(origColWidth +1000 > sheet.getColumnWidth(i)){
				sheet.setColumnWidth(i, origColWidth * 3);
			}
		}
	}

	public OneSheetExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider) {
		super(data, type, dataFormatDecider);
	}

	public OneSheetExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider, MessageSource messageSource) {
		super(data, type, dataFormatDecider, messageSource);
	}
	
	public OneSheetExcelFile(List<T> data, Class<T> type, String language) {
		super(data, type, language);
	}

	public OneSheetExcelFile(List<T> data, Class<T> type, String language, HashMap<String, String> headerString) {
		super(data, type, language, headerString);

		for (int i =0; i < type.getDeclaredFields().length; i ++){
			wb.getSheet("Sheet0").trackColumnForAutoSizing(i);
			int origColWidth = sheet.getColumnWidth(i);
			sheet.autoSizeColumn(i);
			if(origColWidth * 2 > sheet.getColumnWidth(i)){
				sheet.setColumnWidth(i, origColWidth * 5/2);
			}
		}
	}

	public OneSheetExcelFile(List<T> data, Class<T> type, MessageSource messageSource, HashMap<String, String> headerString, String language) {
		super(data, type, messageSource, headerString, language);

		for (int i =0; i < type.getDeclaredFields().length; i ++){
			wb.getSheet("Sheet0").trackColumnForAutoSizing(i);
			int origColWidth = sheet.getColumnWidth(i);
			sheet.autoSizeColumn(i);
			if(origColWidth + 1000 > sheet.getColumnWidth(i)){
				sheet.setColumnWidth(i, origColWidth * 3);
			}
		}
	}

	@Override
	protected void validateData(List<T> data) {
		int maxRows = supplyExcelVersion.getMaxRows();
		if (data.size() > maxRows) {
			throw new IllegalArgumentException(
					String.format("This concrete ExcelFile does not support over %s rows", maxRows));
		}
	}

	@Override
	protected void validateData(List<T> data, String lang) {
		int maxRows = supplyExcelVersion.getMaxRows();
		if (data.size() > maxRows) {
			throw new IllegalArgumentException(
					String.format("This concrete ExcelFile does not support over %s rows", maxRows));
		}
		if(null == lang || lang.trim().isEmpty()){
			throw new IllegalArgumentException("Value language request in OneSheetExcelFile not null");
		}
	}

	@Override
	public void renderExcel(List<T> data) {
		// 1. Create sheet and renderHeader
		sheet = wb.createSheet();
		renderHeadersWithNewSheet(sheet, currentRowIndex++, COLUMN_START_INDEX);

		if (data.isEmpty()) {
			return;
		}

		// 2. Render Body
		for (Object renderedData : data) {
			renderBody(renderedData, currentRowIndex++, COLUMN_START_INDEX);
		}
	}

	@Override
	protected void renderExcel(List<T> data, String lang) {
		// 1. Create sheet and renderHeader
		sheet = wb.createSheet();
		renderHeadersWithNewSheet(sheet, currentRowIndex++, COLUMN_START_INDEX, lang);

		if (data.isEmpty() || (lang.trim().isEmpty() || null == lang)) {
			return;
		}

		// 2. Render Body
		for (Object renderedData : data) {
			renderBody(renderedData, currentRowIndex++, COLUMN_START_INDEX);
		}
	}

	@Override
	public void renderExcel(List<T> data, HashMap<String, String> headerString, String language) {
		// 1. Create sheet and renderHeader
		sheet = wb.createSheet();
		renderHeadersWithNewSheet(sheet, currentRowIndex++, COLUMN_START_INDEX, headerString, language);

		if (data.isEmpty()) {
			return;
		}

		int startBodyRow = currentRowIndex + headerString.size() + 1;
		// 2. Render Body
		for (Object renderedData : data) {
			renderBody(renderedData, startBodyRow ++, COLUMN_START_INDEX);
		}
	}

	@Override
	protected void renderExcel(List<T> data, HashMap<String, String> headerString) {
		// 1. Create sheet and renderHeader
		sheet = wb.createSheet();
		renderHeadersWithNewSheet(sheet, currentRowIndex++, COLUMN_START_INDEX, headerString);

		if (data.isEmpty()) {
			return;
		}

		int startBodyRow = currentRowIndex + headerString.size() + 1;
		// 2. Render Body
		for (Object renderedData : data) {
			renderBody(renderedData, startBodyRow++, COLUMN_START_INDEX);
		}
	}

	@Override
	public void addRows(List<T> data) {
		renderBody(data, currentRowIndex++, COLUMN_START_INDEX);
	}

}
