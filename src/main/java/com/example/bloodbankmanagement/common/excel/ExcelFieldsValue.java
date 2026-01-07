package com.example.bloodbankmanagement.common.excel;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelFieldsValue {
    private Integer rowIndex;
    private Integer columnIndex;
    private Object value;
}
