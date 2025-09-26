package com.example.bloodbankmanagement.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageAmtListResponseDto<T>{
    private int totalRecord;

    private List<T> data;
}
