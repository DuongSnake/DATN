package com.example.bloodbankmanagement.common.excel.execute.multiplesheet;

import com.example.bloodbankmanagement.common.excel.execute.SXSSFExcelFile;
import com.example.bloodbankmanagement.common.excel.resource.DataFormatDecider;
import org.apache.commons.compress.archivers.zip.Zip64Mode;

import java.util.HashMap;
import java.util.List;

public class MultiSheetExcelFile <T> extends SXSSFExcelFile<T> {

	private static final int maxRowCanBeRendered = supplyExcelVersion.getMaxRows() - 1;
	private static final int ROW_START_INDEX = 0;
	private static final int COLUMN_START_INDEX = 0;
	private int currentRowIndex = ROW_START_INDEX;

	public MultiSheetExcelFile(Class<T> type) {
		super(type);
		wb.setZip64Mode(Zip64Mode.Always);
	}

	/*
	 * If you use SXSSF with hug data, you need to set zip mode
	 * see http://apache-poi.1045710.n5.nabble.com/Bug-62872-New-Writing-large-files-with-800k-rows-gives-java-io-IOException-This-archive-contains-unc-td5732006.html
	 */
	public MultiSheetExcelFile(List<T> data, Class<T> type) {
		super(data, type);
		wb.setZip64Mode(Zip64Mode.Always);
	}

	public MultiSheetExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider) {
		super(data, type, dataFormatDecider);
		wb.setZip64Mode(Zip64Mode.Always);
	}

	@Override
	protected void renderExcel(List<T> data) {
		// 1. Create header and return if data is empty
		if (data.isEmpty()) {
			createNewSheetWithHeader();
			return ;
		}

		// 2. Render body
		createNewSheetWithHeader();
		addRows(data);
	}

	@Override
	protected void renderExcel(List<T> data, String lang) {
		// 1. Create header and return if data is empty
		if (data.isEmpty()) {
			createNewSheetWithHeader(lang);
			return ;
		}

		// 2. Render body
		createNewSheetWithHeader(lang);
		addRows(data, lang);
	}

	@Override
	protected void renderExcel(List<T> data, HashMap<String, String> headerString, String language) {
		//Add your initialization logic here.
	}

	@Override
	protected void renderExcel(List<T> data, HashMap<String, String> headerString) {
		//add code
	}

	@Override
	public void addRows(List<T> data) {
		for (Object renderedData : data) {
			renderBody(renderedData, currentRowIndex++, COLUMN_START_INDEX);
			if (currentRowIndex == maxRowCanBeRendered) {
				currentRowIndex = 1;
				createNewSheetWithHeader();
			}
		}
	}

	public void addRows(List<T> data, String lang) {
		for (Object renderedData : data) {
			renderBody(renderedData, currentRowIndex++, COLUMN_START_INDEX);
			if (currentRowIndex == maxRowCanBeRendered) {
				currentRowIndex = 1;
				createNewSheetWithHeader(lang);
			}
		}
	}

	private void createNewSheetWithHeader() {
		sheet = wb.createSheet();
		renderHeadersWithNewSheet(sheet, ROW_START_INDEX, COLUMN_START_INDEX);
		currentRowIndex++;
	}

	private void createNewSheetWithHeader(String lang) {
		sheet = wb.createSheet();
		renderHeadersWithNewSheet(sheet, ROW_START_INDEX, COLUMN_START_INDEX, lang);
		currentRowIndex++;
	}
}
