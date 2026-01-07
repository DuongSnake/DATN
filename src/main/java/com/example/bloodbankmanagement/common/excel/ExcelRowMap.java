package com.example.bloodbankmanagement.common.excel;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExcelRowMap extends HashMap<String, Object>  {

	private static final long serialVersionUID = 1L;
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	public String getData(String rowNum, String loc) {
		HashMap<String, Object> rowMap = (HashMap<String, Object>) this.get(rowNum);
		return (String) rowMap.get(loc);
	}
	
	public int getInt(String rowNum, String loc) {
		HashMap<String, Object> rowMap = (HashMap<String, Object>) this.get(rowNum);
		
		int rtv = 0;
		
		try {
			rtv = (int)Float.parseFloat(((String)rowMap.get(loc)));	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return rtv;
	}
	
	public Long getLong(String rowNum, String loc) {
		HashMap<String, Object> rowMap = (HashMap<String, Object>) this.get(rowNum);
		
		Long rtv = null;

		try {
			rtv = Long.parseLong((String)rowMap.get(loc));	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return rtv;
	}
	
	public Double getDouble(String rowNum, String loc) {
		HashMap<String, Object> rowMap = (HashMap<String, Object>) this.get(rowNum);
		
		Double rtv = null;
		try {
			String value = (String)rowMap.get(loc);
			value = value.trim();
			
			if(value == null || "".equals(value) || value.equals("false")){
				return 0.0;
			}
			else if(value.equals("-")){
				rtv = 0.0;
			}
			else{
				rtv = Double.parseDouble((String)rowMap.get(loc));	
			}
			
			rtv = (double) Math.round(rtv * 1000d) / 1000d; 
				
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return rtv;
	}
	
	public Date getDate(String rowNum, String loc) {
		HashMap<String, Object> rowMap = (HashMap<String, Object>) this.get(rowNum);
		
		Date date = null;
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			date = transFormat.parse((String)rowMap.get(loc));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return date;
	}
	
	public Float getFloat(String rowNum, String loc) {
		HashMap<String, Object> rowMap = (HashMap<String, Object>) this.get(rowNum);
		
		Float rtv = null;
		try {
			String value = (String)rowMap.get(loc);
			value = value.trim();
			
			if(value == null || "".equals(value) || value.equals("false")){
				return (float) 0;
			}
			else if(value.equals("-")){
				rtv =(float) 0;
			}
			else{
				rtv = Float.parseFloat((String)rowMap.get(loc));	
			}
			
			rtv = (float) (Math.round(rtv * 1000d) / 1000d); 
				
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return rtv;
	}
	
	public BigInteger getBigInteger(String rowNum, String loc) {
		HashMap<String, Object> rowMap = (HashMap<String, Object>) this.get(rowNum);
		
		BigInteger rtv = null;
		String value = (String)rowMap.get(loc);
		value = value.trim();
		try {
			rtv = BigInteger.valueOf(Long.parseLong((String)rowMap.get(loc)));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return rtv;
	}

	public boolean checkDupData(String rowNum, String loc) {
		boolean result = false;
		String checkData = this.getData(rowNum, loc);
		List listData = new ArrayList();
		for (String key : this.keySet()) {
			HashMap<String, Object> rowMap = (HashMap<String, Object>) this.get(key);
			for (String k : rowMap.keySet()) {
				if ( k.equals(loc)) { listData.add(rowMap.get(k)); }
			}
		}
		if (Collections.frequency(listData, checkData) > 1) result = true;
		return result;
	}

	public boolean validEmptyRow(String rowNum) {
		HashMap<String, Object> rowMap = (HashMap<String, Object>) this.get(rowNum);
		for (String k : rowMap.keySet()) {
			String value = (String)rowMap.get(k);
			value = value.trim();
//			logger.info("[row: {}] col{} : {}", rowNum, k, value);
			if(!value.isEmpty()){
//				logger.info("empty : {}", k);
				return false;
			}
		}
		return true;
	}
}
