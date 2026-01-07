package com.example.bloodbankmanagement.common.excel.style;

import com.example.bloodbankmanagement.common.excel.style.align.DefaultExcelAlign;
import com.example.bloodbankmanagement.common.excel.style.border.DefaultExcelBorders;
import com.example.bloodbankmanagement.common.excel.style.border.ExcelBorderStyle;
import com.example.bloodbankmanagement.common.excel.style.configurer.ExcelCellStyleConfigurer;

public class BlueHeaderStyle extends CustomExcelCellStyle {

    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.foregroundColor(223, 235, 246)
                .excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER);
    }

}
