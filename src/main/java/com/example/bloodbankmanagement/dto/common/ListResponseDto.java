package com.example.bloodbankmanagement.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResponseDto<T>  extends BasicResponseDto {
    private List<T> list;
}
